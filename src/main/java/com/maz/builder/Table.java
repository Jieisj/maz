package com.maz.builder;

import com.maz.bean.Field;
import com.maz.util.Property;
import com.maz.util.StringConvertor;
import com.maz.util.TypeHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class Table {
    private static final Logger logger = LoggerFactory.getLogger("builder.TableBuilder");
    private static final String SHOW_TABLE_STATUS = "SHOW TABLE STATUS";
    public static final String SHOW_FIELD_FROM = "SHOW FULL FIELDS FROM ";
    public static final String SHOW_INDEX_FROM= "SHOW INDEX FROM ";
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

    public static Set<com.maz.bean.Table> getTable() {
        Set<com.maz.bean.Table> tableSet = new HashSet<>();
        String sqlType = StringConvertor.upperCaseFirstLetter(Property.getSqlType());
        logger.info("Start Building Tables From {} Connection...", sqlType);
        logger.info("^^^^^^^^^^^^^^^^^^^ Table ^^^^^^^^^^^^^^^^^^^");
        try (PreparedStatement ps = conn.prepareStatement(SHOW_TABLE_STATUS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                //properties
                boolean isIgnorePrefix = Property.getIsIgnorePrefix();
                String poSuffix = Property.getPoSuffix();
                String querySuffix = Property.getQuerySuffix();

                String tableName = rs.getString("Name");
                String tableComment = rs.getString("Comment");
                logger.info("------------------Table------------------");
                logger.info("Table: {} Starting Retrieving Data...", tableName);
                com.maz.bean.Table t = new com.maz.bean.Table();
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
                getTableIndex(t);
                tableSet.add(t);
            }
            logger.info("------------------ Table Build End --------------------");
        } catch (Exception e) {
            logger.error("Retrieving Table Data Failed");
        }
        return tableSet;
    }

    private static void getTableIndex(com.maz.bean.Table table) {
        try(PreparedStatement ps = conn.prepareStatement(SHOW_INDEX_FROM + table.getName());
            ResultSet rs = ps.executeQuery())
        {
            logger.info("--------------- Index ---------------");
            Map<String, List<Field>> indexMap= new LinkedHashMap<>();
            table.setIndexMap(indexMap);
            while (rs.next()){
                String fieldName = rs.getString("Column_Name");
                String keyName = rs.getString("Key_name");
                String indexComment = rs.getString("Index_comment");
                int nonUnique = rs.getInt("Non_unique");
                logger.info("Indexes: KeyName: {}, ColumnName: {}, Non_unique: {}, IndexComment:{}",
                        keyName, fieldName, nonUnique, indexComment);
                if (nonUnique != 1){
                    List<Field> fields = indexMap.computeIfAbsent(keyName, k -> new ArrayList<>());
                    for (Field f : table.getFields()){
                        if (f.getName().equals(fieldName)){
                            fields.add(f);
                            break;
                        }
                    }
                }
            }
            logger.info("Table: {} Index: {}", table.getName(), table.getIndexMap().keySet());
        }
        catch (Exception e){
            logger.error("Index Build Failed");
        }
    }

    private static void getTableField(com.maz.bean.Table table) {
        try (
                PreparedStatement ps = conn.prepareStatement(SHOW_FIELD_FROM + table.getName());
                ResultSet rs = ps.executeQuery();
        ) {
            logger.info("--------------- Field ---------------");
            List<Field> fieldList = new ArrayList<>();
            while (rs.next()) {
                Field field = new Field();

                String name = rs.getString("Field");
                String type = procVarType(rs.getString("Type"));
                String defValue = rs.getString("Default");
                String comment = rs.getString("Comment");
                boolean canNull = procFieldNull(rs.getString("Null"));
                String key = rs.getString("Key");
                String extra = rs.getString("Extra");
                String javaType = TypeHandler.typeConversion(type);
                boolean autoIncrement = isExtraAutoIncrement(extra);

                field.setName(name);
                field.setSqlType(type);
                field.setDefaultValue(defValue);
                field.setComment(comment);
                field.setCanNull(canNull);
                field.setKey(key);
                field.setExtra(extra);
                field.setJavaType(javaType);
                field.setAutoIncrement(autoIncrement);
                field.setPropertyName(StringConvertor.removeAndConcatCamel(name, "_"));

                if (ArrayUtils.contains(TypeHandler.DateTime, type)) {
                    table.setHaveDateTime(true);
                }
                if (ArrayUtils.contains(TypeHandler.BigDecimal, type)) {
                    table.setHaveBigDecimal(true);
                }
                if (ArrayUtils.contains(TypeHandler.Date, type)) {
                    table.setHaveDate(true);
                }
                fieldList.add(field);
                logger.info("Field: {}", field);
            }
            table.setFields(fieldList);
            logger.info("Table Field Build Success");
        } catch (Exception e) {
            logger.error("Field Table Build Failed");
        }
    }

    private static String procBeanName(String str, boolean ignorePrefix) {
        if (str.contains("_")){
            String regex = "_";
            if (ignorePrefix) {
                String words = StringConvertor.ignorePrefix(str, regex);
                return StringConvertor.removeAndConcat(words, regex, true);
            } else {
                return StringConvertor.removeAndConcat(str, regex, true);
            }
        }else {
            return StringConvertor.upperCaseFirstLetter(str);
        }
    }

    private static String procVarType(String str) {
        if (str.contains("(")) {
            return str.substring(0, str.indexOf("("));
        }
        return str;
    }

    private static boolean procFieldNull(String str) {
        return str.equals("Yes");
    }

    public static boolean isExtraAutoIncrement(String extra) {
        return extra.equals("auto_increment");
    }
}
