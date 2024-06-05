package com.maz.util;

import ch.qos.logback.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Property {
    private static final Properties props = new Properties();
    private static final Map<String, String> PROPS_MAP = new ConcurrentHashMap<>();
    static{
        Logger logger = LoggerFactory.getLogger("utils.PropertiesUtils");
        logger.info("---------------Read Properties---------------");
        logger.info("Start Reading Application Properties");
        try(InputStream is = Property.class.getClassLoader().getResourceAsStream("application.properties")){
            props.load(is);
            for(Object o : props.keySet()){
                String key = o.toString();
                PROPS_MAP.put(key, props.getProperty(key).trim());
            }
        } catch (IOException e) {
            logger.info("Property Read Failed");
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

    public static boolean getQueryIgnoreComment(){
        if (getPropertiesKey("entity.query.ignore.comment") == null){
            return false;
        }
        return Boolean.parseBoolean(getPropertiesKey("entity.query.ignore.comment"));
    }

    public static boolean getPOJOIgnoreComment(){
        if (getPropertiesKey("entity.pojo.ignore.comment") == null){
            return false;
        }
        return Boolean.parseBoolean(getPropertiesKey("entity.pojo.ignore.comment"));
    }
    public static String getPoSuffix(){
        return getPropertiesKey("entity.po.suffix") == null ? "" : getPropertiesKey("entity.po.suffix");
    }

    public static String getQuerySuffix(){
        return getPropertiesKey("entity.query.suffix") == null ? "" : getPropertiesKey("entity.query.suffix");
    }

    public static String getSourcePath(){
        return getPropertiesKey("source.path");
    }
    public static String getResourcePath(){
        return getPropertiesKey("resources.path");
    }
    public static String getQueryPath(){
        if (getPropertiesKey("entity.query.path") == null){
            if (getBasePackage().isEmpty()){
                return getSourcePath() + "/" + packageToPath(getQueryPackage());
            }else {
                return getSourcePath() + "/" + packageToPath(getBasePackage()) + "/" + packageToPath(getQueryPackage());
            }
        }
        return getPropertiesKey("entity.query.path");
    }
    public static String getPoPath(){
        if (getPropertiesKey("entity.po.path") == null){
            if (getBasePackage().isEmpty()){
                return getSourcePath() + "/" + packageToPath(getPoPackage());
            }else {
                return getSourcePath() + "/" + packageToPath(getBasePackage()) + "/" + packageToPath(getPoPackage());
            }
        }
        return getPropertiesKey("entity.po.path");
    }
    public static String getBasePackage(){
        return getPropertiesKey("base.package") == null ? "" : getPropertiesKey("base.package");
    }
    public static String getPoPackage(){
        if (getPropertiesKey("entity.po.package") == null){
            return getBasePackage().isEmpty() ? "po" : getBasePackage();
        }else {
            return getBasePackage().isEmpty() ? getPropertiesKey("entity.po.package") :
                    getBasePackage() + "." + getPropertiesKey("entity.po.package");
        }
    }
    public static String getQueryPackage(){
        if (getPropertiesKey("entity.query.package") == null){
            return getBasePackage().isEmpty() ? "query" : getBasePackage();
        }else {
            return getBasePackage().isEmpty() ? getPropertiesKey("entity.query.package") :
                    getBasePackage() + "." + getPropertiesKey("entity.query.package");
        }
    }
    public static String packageToPath(String packName){
        return packName.replaceAll("\\.", "/");
    }
}
