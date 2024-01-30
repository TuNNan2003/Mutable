package com.unloadhome.common;

import java.util.Properties;

public class PropertyFactory {
    private static final Properties prop = new Properties();
    static {
        try {
            prop.load(PropertyFactory.class.getClassLoader().getResourceAsStream("snowflake.properties"));
        }catch (Exception e){
            //TODO
        }
    }
    public static Properties getProperties(){
        return prop;
    }
}
