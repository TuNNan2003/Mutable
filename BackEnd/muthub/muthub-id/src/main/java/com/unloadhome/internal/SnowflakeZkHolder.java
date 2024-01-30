package com.unloadhome.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.unloadhome.common.PropertyFactory;
import com.unloadhome.exception.CheckLastTimeException;
import org.apache.commons.io.FileUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


public class SnowflakeZkHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeZkHolder.class);
    // PREFIX_ZK_PATH = /snowflake/muthubID
    private static final String PREFIX_ZK_PATH = "/snowflake/" + PropertyFactory.getProperties().getProperty("leaf.name");
    // PROP_PATH = /tmp/muthubID/leafconf/{port}/workerID.properties/
    private static final String PROP_PATH = System.getProperty("java.io.tmpdir") + File.separator + PropertyFactory.getProperties().getProperty("leaf.name") + "/leafconf/{port}/workerID.properties";
    // PATH_FOREVER = /snowflake/muthubID/forever
    private static final String PATH_FOREVER = PREFIX_ZK_PATH + "/forever";
    private String zk_AddressNode = null;
    private String listenAddress = null;
    private int workerID;
    private String ip;
    private String  port;
    private String connectionString;
    private long lastUpdateTime;
    public SnowflakeZkHolder(String ip, String port, String connectionString){
        this.ip = ip;
        this.port = port;
        this.listenAddress = ip + ":" + port;
        this.connectionString = connectionString;
    }

    public boolean init(){
        try{
            CuratorFramework curator = createWithOptions(connectionString, new RetryUntilElapsed(1000, 4), 10000, 6000);
            curator.start();
            Stat stat = curator.checkExists().forPath(PATH_FOREVER);
            if(stat == null){
                /*root node not exist, first time start
                * should create /snowflake/ip:port-0000 0000 0
                * and upload data
                */
                zk_AddressNode = createNode(curator);
                updateLocalWorkerID(workerID);
                ScheduledUploadData(curator, zk_AddressNode);
                return true;
            }else {
                Map<String, Integer> nodeMap = Maps.newHashMap();
                Map<String, String> realNode = Maps.newHashMap();
                List<String> keys = curator.getChildren().forPath(PATH_FOREVER);
                for(String key : keys){
                    String[] nodeKey = key.split("-");
                    realNode.put(nodeKey[0], key);
                    nodeMap.put(nodeKey[0], Integer.parseInt(nodeKey[1]));
                }
                Integer workerid = nodeMap.get(listenAddress);
                if(workerid != null){
                    zk_AddressNode = PATH_FOREVER + "/" + realNode.get(listenAddress);
                    workerID = workerid;
                    if(!checkInitTimePin(curator, zk_AddressNode)){
                        throw new CheckLastTimeException("init timepin check error");
                    }
                    doService(curator);
                    updateLocalWorkerID(workerID);
                    LOGGER.info("[old node]find forever node have this endpoint ip-{} port-{} workid-{} childnode and start success", ip, port, workerID);
                }else {
                    String newNode = createNode(curator);
                    zk_AddressNode = newNode;
                    String[] nodeKey = newNode.split("-");
                    workerID = Integer.parseInt(nodeKey[1]);
                    doService(curator);
                    updateLocalWorkerID(workerID);
                    LOGGER.info("[new node]create forever node and start success");
                }
            }
        } catch (Exception e){
            LOGGER.info("exception: ", e);
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(new File(PROP_PATH.replace("{port}", port + ""))));
                workerID = Integer.valueOf(properties.getProperty("workerID"));
                LOGGER.warn("[zk error]zookeeper start failed, use local file");
            }catch (Exception e1){
                //LOGGER
                LOGGER.error("[local error]use local file error: ", e1);
                return false;
            }
        }
        return  true;
    }

    private void doService(CuratorFramework curator){
        ScheduledUploadData(curator, zk_AddressNode);
    }

    public int getWorkerID(){
        return workerID;
    }
    /*
    * @description
    * 周期性向zk上报本机的时间
    * 因为snowflake依赖于本机产生的时间戳，为防止时钟回拨发生，将时间戳上传
    * 若发生回拨则会产生时间戳小于zk保存的时间戳从而产生异常
    * */
    private void ScheduledUploadData(final CuratorFramework curator, final String zk_AddressNode){
        Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "schedule-upload-time");
                thread.setDaemon(true);
                return thread;
            }
        }).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run(){
                updataNewData(curator, zk_AddressNode);
            }
        }, 1L, 3L, TimeUnit.SECONDS);
    }

    private void updataNewData(CuratorFramework curator, String path){
        try {
            if(System.currentTimeMillis() < lastUpdateTime){
                LOGGER.warn("[time error]time on machine was reset");
                return;
            }
            curator.setData().forPath(path, buildData().getBytes());
            lastUpdateTime = System.currentTimeMillis();
        }catch (Exception e){
            LOGGER.info("update init data error, path is {}, error is {}", path, e);
        }
    }

    private void updateLocalWorkerID(int workerID){
        File confFile = new File(PROP_PATH.replace("{port}", port));
        boolean exist = confFile.exists();
        LOGGER.info("local file exist status is {}", exist);
        if(exist){
            try {
                FileUtils.writeStringToFile(confFile, "workerID=" + workerID, false);
                LOGGER.info("update local file, caching workerID-{}", workerID);
            }catch (IOException e){
                LOGGER.error("update local file error: ", e);
            }
        }else {
            try {
                boolean mkdirs = confFile.getParentFile().mkdirs();
                LOGGER.info("init local file cache create parent dis status is {}, worker id is {}", mkdirs, workerID);
                if(mkdirs){
                    if(confFile.createNewFile()){
                        FileUtils.writeStringToFile(confFile, "workerID=" + workerID, false);
                        //LOGGER
                        LOGGER.info("init local file, workerID-{}", workerID);
                    }
                }else {
                    LOGGER.warn("create parent file error");
                }
            }catch (IOException e){
                LOGGER.warn("craete workerID conf file error", e);
            }
        }
    }

    private String createNode(CuratorFramework curator) throws  Exception{
        return curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath(PATH_FOREVER + "/" + listenAddress + "-", buildData().getBytes());
    }

    static class Endpoint {
        private String ip;
        private String port;
        private long timepin;

        public Endpoint(){}
        public Endpoint(String ip, String port, long timepin){
            this.ip = ip;
            this.port = port;
            this.timepin = timepin;
        }

        public String getIp() {
            return ip;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public long getTimepin() {
            return timepin;
        }

        public void setTimepin(long timepin) {
            this.timepin = timepin;
        }

        public void setIp(String ip){
            this.ip = ip;
        }
    }

    private CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs){
        return CuratorFrameworkFactory.builder().connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();

    }

    private boolean checkInitTimePin(CuratorFramework curator, String  zk_AddressNode) throws Exception {
        byte[] bytes = curator.getData().forPath(zk_AddressNode);
        Endpoint endpoint = deBuildData(new String(bytes));
        return !(endpoint.getTimepin() > System.currentTimeMillis());
    }
    private String buildData() throws JsonProcessingException{
        Endpoint endpoint = new Endpoint(ip, port, System.currentTimeMillis());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(endpoint);
    }
    private Endpoint deBuildData(String json) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Endpoint endpoint = mapper.readValue(json, Endpoint.class);
        return endpoint;
    }
}
