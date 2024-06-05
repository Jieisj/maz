package com.maz.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class TypeHandler {
    public static final String[] String = {
            "varchar", "char", "text", "mediumtext", "longtext"
    };
    public static final String[] Integer ={
            "int", "smallint", "mediumint", "tinyint"
    };
    public static final String[] Long ={
            "bigint"
    };
    public static final String[] BigDecimal = {
            "decimal"
    };
    public static final String[] Double = {
            "double", "float"
    };

    public static final String[] Date ={
            "date"
    };

    public static final String[] DateTime={
            "time", "timestamp"
    };
    public static final Map<String, String[]> typeMap = new LinkedHashMap<>();
    private static void mapType(){
        typeMap.put("String", String);
        typeMap.put("Integer", Integer);
        typeMap.put("Long", Long);
        typeMap.put("BigDecimal", BigDecimal);
        typeMap.put("Double", Double);
        typeMap.put("Date", Date);
        typeMap.put("DateTime", DateTime);
    }
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
}
