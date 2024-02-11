package com.unloadhome.internal;

import com.unloadhome.dubbointerface.IdResponse;
import com.unloadhome.common.Status;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.net.Inet6Address;


public class SnowflakeIDgenImpl implements IDgen{
    private static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeIDgenImpl.class);
    private final long seqBits = 12L;
    private final long seqMask = ~(-1L << seqBits);
    private long seq = 0L;
    private final long workerIdBits = 10L;
    private final long workerIdMax = ~(-1L << workerIdBits);
    private final long workerIdShift = seqBits;
    private long workerId;
    private final long timePinBegin;
    private final long timePinShift = seqBits + workerIdBits;
    private long timePinLatest = -1L;
    private static final Random RANDOM = new Random();

    public SnowflakeIDgenImpl(String zkAdd, int port){
        this(zkAdd, port, 1705006422263L);
    }

    public SnowflakeIDgenImpl(String zkadd, int port, long timePinBegin){
        this.timePinBegin = timePinBegin;
        if(getTime() <= this.timePinBegin){
            //TODO:throw a exception
        }else{
            final String ip = getIp();
            SnowflakeZkHolder holder = new SnowflakeZkHolder(ip, String.valueOf(port), zkadd);
            LOGGER.info("startTimePin-{}, ip-{}, zkAddress-{}, port-{}", timePinBegin, ip, zkadd, port);
            boolean flag = holder.init();
            if(flag){
                workerId = holder.getWorkerID();
                LOGGER.info("START SUCCESS use workerID-{}", workerId);
            }else {
                LOGGER.error("snowflakezkholder not init success");
            }
            if(workerId < 0 || workerId > workerIdMax){
                LOGGER.error("error of zk, get invalid id");
            }
        }
    }

    //不知道这个函数的作用是啥
    @Override
    public boolean init() {
        return true;
    }

    @Override
    public synchronized IdResponse get(String key){
        long timepin = timeGen();
        if(timepin < timePinLatest){
            long offset = timePinLatest - timepin;
            if(offset <= 5){
                try {
                    wait(offset << 1);
                    timepin = timeGen();
                    if(timepin < timePinLatest){
                        return new IdResponse(-1, Status.Exception);
                    }
                }catch (InterruptedException e) {
                    LOGGER.error("wait interrupted");
                    return new IdResponse(-2, Status.Exception);
                }
            }else {
                return new IdResponse(-3, Status.Exception);
            }
        }
        if(timepin == timePinLatest){
            seq = (seq + 1) & seqMask;
            if(seq == 0){
                seq = RANDOM.nextInt(100);
                timepin = tilNextMills(timePinLatest);
            }
        } else {
            seq = RANDOM.nextInt(100);
        }
        timePinLatest = timepin;
        long id = ((timepin - timePinBegin) << timePinShift) |
                (workerId << workerIdShift) |
                seq;
        return new IdResponse(id, Status.SUCCESS);
    }

    public long getWorkerId() {
        return workerId;
    }

    private long getTime(){
        return System.currentTimeMillis();
    }

    private String getIp(){
        String ip;
        try{
            List<String> ipList = getHostAddress(null);
            ip = ipList.get(0);
        }catch (Exception ex){
            ip = "";
        }
        return ip;
    }

    private static List<String> getHostAddress(String interfaceName) throws SocketException {
        List<String> ipList = new ArrayList<String>(5);
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> allAddress = ni.getInetAddresses();
            while (allAddress.hasMoreElements()) {
                InetAddress address = allAddress.nextElement();
                if (address.isLoopbackAddress()) {
                    // skip the loopback addr
                    continue;
                }
                if (address instanceof Inet6Address) {
                    // skip the IPv6 addr
                    continue;
                }
                String hostAddress = address.getHostAddress();
                if (null == interfaceName) {
                    ipList.add(hostAddress);
                } else if (interfaceName.equals(ni.getDisplayName())) {
                    ipList.add(hostAddress);
                }
            }
        }
        return ipList;
    }
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    protected long tilNextMills(long timePinLatest){
        long timePin = timeGen();
        while(timePin <= timePinLatest){
            timePin = timeGen();
        }
        return timePin;
    }
}
