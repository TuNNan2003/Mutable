package com.unloadhome.service;

import com.unloadhome.common.PropertyFactory;
import com.unloadhome.dubbointerface.IdResponse;
import com.unloadhome.internal.IDgen;
import com.unloadhome.internal.SnowflakeIDgenImpl;
import org.springframework.stereotype.Service;
import com.unloadhome.common.Constants;
import com.unloadhome.exception.InitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Service
public class SnowflakeService {
    private IDgen idGen;
    private static Logger logger = LoggerFactory.getLogger(SnowflakeService.class);
    public SnowflakeService() throws InitException {
        Properties properties = PropertyFactory.getProperties();
        boolean flag = Boolean.parseBoolean(properties.getProperty(Constants.LEAF_SNOWFLAKE_ENABLE, "ture"));
        if(flag){
            String zkAddress = properties.getProperty(Constants.LEAF_SNOWFLAKE_ZK_ADDRESS);
            int port = Integer.parseInt(properties.getProperty(Constants.LEAF_SNOWFLAKE_PORT));
            idGen = new SnowflakeIDgenImpl(zkAddress, port);
            if(idGen.init()){
                logger.info("Snowfalke Service Init Success");
            }else {
                logger.info("Snowflake idGen failed");
                throw new InitException("Snowflake init fail");
            }
        }else {
            logger.info("Constants.LEAF_SNOWFLAKE_ENABLE not enabled");
        }
    }

    public IdResponse getId(String key){
        return idGen.get(key);
    }
}
