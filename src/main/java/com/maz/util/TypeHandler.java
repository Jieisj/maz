package com.maz.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TypeHandler {
    private static final String[] String = {
            "varchar", "char", "text", "mediumtext", "longtext"
    };
    private static final String[] Integer ={
            "int", "smallint", "mediumint", "tinyint"
    };
    private static final String[] Long ={
            "bigint"
    };
    private static final String[] BigDecimal = {
            "decimal"
    };
    private static final String[] Double = {
            "double", "float"
    };

    public static final String[] Date ={
            "date"
    };

    public static final String[] DateTime={
            "time", "timestamp"
    };
    private static Map<String, String[]> typeMap = new LinkedHashMap<>();

    static{
        mapType();
    }
    public static String typeConversion(String sqlType){
        for (Map.Entry<String, String[]> entry : typeMap.entrySet()){
            String[] value = entry.getValue();
            for(String type : value){
                 if (type.equals(sqlType)){
                     return type;
                 }
            }
        }
        return "";
    }
    private static void mapType(){
        typeMap.put("String", String);
        typeMap.put("Integer", Integer);
        typeMap.put("Long", Long);
        typeMap.put("BigDecimal", BigDecimal);
        typeMap.put("Double", Double);
        typeMap.put("Date", Date);
        typeMap.put("DateTime", DateTime);
    }
}
