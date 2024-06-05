package com.maz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Property {
    private static final Properties props = new Properties();
    private static Map<String, String> PROPS_MAP = new ConcurrentHashMap<>();
    static{
        Logger logger = LoggerFactory.getLogger("utils.PropertiesUtils");
        logger.info("---------------Read Properties---------------");
        logger.info("Start Reading Application Properties");
        try(InputStream is = Property.class.getClassLoader().getResourceAsStream("application.properties")){
            props.load(is);
            for(Object o : props.keySet()){
                String key = o.toString();
                PROPS_MAP.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            logger.info("Read Application Properties Finished");
        }
    }

    public static String getPropertiesKey(String key){
        return PROPS_MAP.get(key);
    }

    public static String getDbUrl(){
        return getPropertiesKey("db.url");
    }

    public static String getDbPassWord(){
        return getPropertiesKey("db.password");
    }

    public static String getDbUsername(){
        return getPropertiesKey("db.username");
    }

    public static String getDbDriver(){
        return getPropertiesKey("db.driver");
    }

    public static String getSqlType(){
        return getPropertiesKey("db.url").split(":")[1];
    }

    public static boolean getIsIgnorePrefix(){
        if (getPropertiesKey("entity.ignore.comment") == null){
            return true;
        }
        String ignoreComm = getPropertiesKey("entity.ignore.comment");
        return Boolean.parseBoolean(ignoreComm);
    }

    public static String getPoSuffix(){
        return getPropertiesKey("entity.po.suffix") == null ? "" : getPropertiesKey("entity.po.suffix");
    }

    public static String getQuerySuffix(){
        return getPropertiesKey("entity.query.suffix") == null ? "" : getPropertiesKey("entity.query.suffix");
    }
}
