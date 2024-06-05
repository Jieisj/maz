package com.maz.builder;

import com.maz.bean.Table;
import com.maz.util.Property;
import com.maz.util.StringConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Set;

public class TableBuilder {
    private static final Logger logger = LoggerFactory.getLogger("builder.TableBuilder");
    private static final String SHOW_TABLE_STATUS = "SHOW TABLE STATUS";
    public static final String SHOW_FIELD_FROM ="SHOW FULL FIELDS FROM";
    private static Connection conn = null;

    static {
        String dbUrl = Property.getDbUrl();
        String dbPassword = Property.getDbPassWord();
        String dbUsername = Property.getDbUsername();
        String connUrl = String.format("%s?user=%s&password=%s", dbUrl, dbUsername, dbPassword);
        try {
            conn = DriverManager.getConnection(connUrl);
        } catch (SQLException e) {
            logger.error("Database Connection Failed");
        }
    }

    public static Set<Table> getTable() {
        String sqlType = StringConvertor.upperCaseFirstLetter(Property.getSqlType());
        logger.info("Start Building Tables From {} Connection...", sqlType);
        try (PreparedStatement ps = conn.prepareStatement(SHOW_TABLE_STATUS);
                ResultSet rs = ps.executeQuery())
        {
            while (rs.next()){
                //properties
                boolean isIgnorePrefix = Property.getIsIgnorePrefix();
                String poSuffix = Property.getPoSuffix();
                String querySuffix = Property.getQuerySuffix();

                String tableName = rs.getString("Name");
                String tableComment = rs.getString("Comment");
                logger.info("Table: {} Starting Retrieving Data...", tableName);
                Table t = new Table();
                t.setName(tableName);
                t.setComment(tableComment);
                t.setHaveDateTime(false);
                t.setHaveDate(false);
                t.setHaveBigDecimal(false);
                t.setBeanName(procBeanName(tableName, isIgnorePrefix));
                t.setPojoParamName(t.getBeanName() + poSuffix);
                t.setQueryParamName(t.getBeanName() + querySuffix);
                logger.info("Table Data : {}", t);
                getTableField(t);
            }
        } catch (Exception e) {
            logger.info("Retrieving Table Data Failed");
        }
        return null;
    }

    private static String getTableField(Table table){
        try(
                PreparedStatement ps = conn.prepareStatement(SHOW_FIELD_FROM + table.getName());
                ResultSet rs = ps.executeQuery();
            )
        {
         while (rs.next()){
             String field = rs.getString("Field");
             String type = rs.getString(procVarType("Type"));
             String def = rs.getString("Default");
             String comment = rs.getString("Comment");
             boolean canNull  = procFieldNull(rs.getString("Null"));
             String key = rs.getString("Key");
             String extra =  rs.getString("Extra");
             String javaType = type;
         }
        }
        catch (Exception e){
            logger.info("");
        }
        return null;
    }
    private static String procBeanName(String str, boolean ignorePrefix){
        String regex = "_";
        if (ignorePrefix){
            StringConvertor.ignorePrefix(str,regex);
            return StringConvertor.removeAndConcat(str,regex, true);
        }else {
            return  StringConvertor.removeAndConcat(str,regex, true);
        }
    }

    private static String procVarType(String str){
        if (str.contains("(")) {
            return str.substring(0, str.indexOf("("));
        }
        return str;
    }

    private static boolean procFieldNull(String str){
        if (str.equals("Yes")){
            return true;
        }
        return false;
    }
}
