package com.maz.builder;

import com.maz.bean.Field;
import com.maz.util.Property;
import com.maz.bean.Table;

import com.maz.util.StringConvertor;
import com.maz.util.TypeHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

public class XML {
    private static Logger logger = LoggerFactory.getLogger("builder.BuilderBase");
    private static final String REF_NON_AUTO_SET_PARAMS = "non_auto_set_params";
    private static final String REF_FULL_COLUMNS = "full_columns";
    private static final String REF_NON_AUTO_COLUMNS = "non_auto_columns";
    private static final String REF_QUERY_CONDITIONS = "query_conditions";
    private static final String REF_QUERY_FULL_CONDITIONS = "full_query_conditions";
    private static final String REF_INSERT_COLUMNS = "insert_columns";
    private static final String REF_INSERT_VALUES = "insert_values";
    private static final String REF_IN_OR_UP_COLUMNS = "inOrUp_columns";
    private static final String REF_IN_OR_UP_VALUES = "inOrUp_values";
    private static final String REF_IN_OR_UP_UPDATE_VALUES = "inOrUp_update_values";
    private static final String REF_IN_OR_UP_LIST_COLUMNS = "inOrUpList_columns";
    private static final String REF_IN_OR_UP_LIST_UPDATE_VALUES = "inOrUpList_update_values";

    public static void buildMapperXML(Table table) {
        String mapperXMLPath = Property.getXMLPath();
        File mapperXMLDir = new File(mapperXMLPath);
        System.out.println(mapperXMLPath);
        File mapperXMLFile = new File(mapperXMLPath + "/" + table.getPojoParamName() + "Mapper.xml");
        System.out.println(mapperXMLFile);
        if (mapperXMLDir.mkdirs()){
            logger.info("Mapper XML Director Created");
        }else {
            logger.info("Mapper XML Director Have Existed");
        }
        try{
            if (mapperXMLFile.createNewFile()){
                logger.info("Mapper XML File Created");
            }else {
                logger.info("Mapper XML File Have Existed");
            }
        }catch (IOException e){
            logger.info("Mapper XML File Creation Failed");
        }
        try (OutputStreamWriter ow = new OutputStreamWriter(Files.newOutputStream(mapperXMLFile.toPath()), StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(ow))
        {
            StringBuilder contentSb = new StringBuilder();
            String resultMapID = String.format("%sResultMap", table.getBeanName());
            String resultType = Property.getPoPackage() + "." + table.getPojoParamName();
            String content = buildMapperContent(table, resultMapID, resultType);
            contentSb.append(content);
            String fullPackage = String.format("%s.%s", Property.getMapperPackage(), table.getPojoParamName() + "Mapper");
            String template = buildMapperTemplate(fullPackage, contentSb);
            bw.write(template);
            bw.flush();
        } catch (IOException e) {
            logger.error("Mapper XML File Input Failed");
        }
    }

    public static void buildMapperXMLFromTables(Set<Table> tables) {
        logger.info("----------------------------MapperXML-------------------------------");
        logger.info("Initializing Building MapperXML...");
        logger.info("pathResource: {}", Property.getResourcePath());
        logger.info("MapperPackage: {}", Property.getMapperPackage());
        logger.info("PathMapperXML: {}", Property.getXMLPath());
        logger.info("Start Building...");
        for (Table table : tables) {
            buildMapperXML(table);
        }
        logger.info("Building Mapper XML File Finished :D ~!!!!");
    }

    private static String buildMapperTemplate(String fullPackageName, StringBuilder contentSb) {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n <!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"https://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n";
        String start = String.format("<mapper namespace=\"%s\">\n%s", fullPackageName, contentSb.toString());
        String closing = "\n</mapper>";
        return header + start + closing;
    }

    private static String buildMapperContent(Table table, String resultMapID, String type) {
        String resultMap = resultMapTag(table, resultMapID, type);

        StringBuilder tagSb = new StringBuilder();
        // generate select method
        tagSb.append(genSelMethod(table, resultMapID));
        // generate update method
        tagSb.append(genUpdateMethod(table));
        // generate delete method
        tagSb.append(genDelMethod(table));

        //pojo dynamic helper
        tagSb.append("\n\n\t<!--  Pojo Dynamic Update Sql Helper\t-->\n");
        String nonAutoSetParamsTag = createSqlTag(REF_NON_AUTO_SET_PARAMS, buildNonAutoSetParamsTag(table));
        tagSb.append(nonAutoSetParamsTag);
        tagSb.append("\n\t<!--  Generic Sql Tag  -->");

        //generic mapper
        String selectList = genericSelectList(table);
        String insert = genericInsert(table);
        String insertList = genericInsertList(table);
        String insertOrUpdate = genericInsertOrUpdate(table);
        String insertOrUpdateList = genericInsertOrUpdateList(table);
        String selectCount = genericSelectCount(table);
        tagSb.append(selectList).append(insert).append(insertOrUpdate).append(insertList).append(insertOrUpdateList).append(selectCount);


        tagSb.append("\n\n\t<!-- Generic Mapper Dynamic Sql Tag Helper\t-->\n");

        //generic dynamic sql helper
        String fullColumnTag = createSqlTag(REF_FULL_COLUMNS, buildFullColumnTag(table));
        tagSb.append(fullColumnTag);
        String nonAutoColumnTag = createSqlTag(REF_NON_AUTO_COLUMNS, buildNonAutoColumnTag(table));
        tagSb.append(nonAutoColumnTag);
        String queryConditionsTag = createSqlTag(REF_QUERY_CONDITIONS, buildQueryConditions(table));
        tagSb.append(queryConditionsTag);

        return resultMap + tagSb;
    }

    private static String genSelMethod(Table table, String resultMapID){
        List<String> selectMethodList = Mapper.selectMethodNamesMap.get(table);
        StringBuilder selMethodSb = new StringBuilder();
        String selectMethod = "";
        for (String methodName : selectMethodList) {
            if (methodName.contains("And")){
                String[] properties = methodName.substring(8).split("And");
                StringBuilder paramsSb = new StringBuilder();
                int index = 0;
                for(String property : properties){
                    index++;
                    property = StringConvertor.lowerCaseFirstLetter(property);
                    String params = genSelectSqlParam(table, property);
                    paramsSb.append(params);
                    if(index < properties.length){
                        paramsSb.append(", ");
                    }
                }
                String selectSql = genSelectSql(table, paramsSb.toString());
                selectMethod = selectTag(methodName, resultMapID, selectSql);
            }else {
                String property = StringConvertor.lowerCaseFirstLetter(methodName.substring(8)); //"selectBy" the end index is 7
                String params = genSelectSqlParam(table, property);
                String selectSql = genSelectSql(table, params);
                selectMethod = selectTag(methodName, resultMapID, selectSql);
            }
            selMethodSb.append(selectMethod);
        }
        return selMethodSb.toString();
    }
    private static String genUpdateMethod(Table table){
        List<String> updateMethodList = Mapper.updateMethodNamesMap.get(table);
        StringBuilder updateMethodSb = new StringBuilder();
        String updateMethod = "";
        for (String updateMethodName : updateMethodList) {
            if (updateMethodName.contains("And")){
                String[] properties = updateMethodName.substring(8).split("And");
                StringBuilder paramsSb = new StringBuilder();
                for(String property : properties){
                    property = StringConvertor.lowerCaseFirstLetter(property);
                    String params = genSelectSqlParam(table, property);
                    paramsSb.append(params);
                    paramsSb.append(", ");
                }
                String params = paramsSb.substring(0, paramsSb.lastIndexOf(","));
                String updateSql = genUpdateSql(table, genUpdateSetParamRef(), params);
                updateMethod = updateTag(updateMethodName, updateSql);
            }else {
                String property = StringConvertor.lowerCaseFirstLetter(updateMethodName.substring(8)); //"update" the end index is 7
                String params = genUpdateParam(table, property);
                String updateSql = genUpdateSql(table, genUpdateSetParamRef(), params);
                updateMethod = updateTag(updateMethodName, updateSql);
            }
            updateMethodSb.append(updateMethod);
        }
        return updateMethodSb.toString();
    }
    private static String genDelMethod(Table table){
        List<String> deleteMethodList = Mapper.deleteMethodNamesMap.get(table);
        StringBuilder delMethodSb = new StringBuilder();
        String deleteMethod = "";
        for (String deleteMethodName : deleteMethodList) {
            if (deleteMethodName.contains("And")){
                String[] properties = deleteMethodName.substring(8).split("And");
                StringBuilder paramsSb = new StringBuilder();
                int index = 0;
                for(String property : properties){
                    index++;
                    property = StringConvertor.lowerCaseFirstLetter(property);
                    String params = genDeleteSqlParam(table, property);
                    paramsSb.append(params);
                    if(index < properties.length){
                        paramsSb.append(", ");
                    }
                }
                String deleteSql = genDeleteSql(table, paramsSb.toString());
                deleteMethod = deleteTag(deleteMethodName, deleteSql);
            }else {
                String property = StringConvertor.lowerCaseFirstLetter(deleteMethodName.substring(8)); //"selectBy" the end index is 7
                String params = genDeleteSqlParam(table, property);
                String deleteSql = genDeleteSql(table, params);
                deleteMethod = deleteTag(deleteMethodName, deleteSql);
            }

            delMethodSb.append(deleteMethod);
        }
        return delMethodSb.toString();
    }
    private static String resultMapTag(Table table, String resultId, String resultType){
        String start = String.format("\t<resultMap id=\"%s\" type=\"%s\">", resultId, resultType);
        StringBuilder resultSb = new StringBuilder();
        for (Field field : table.getFields()) {
            String idTag = String.format("\n\t\t<id column=\"%s\" property=\"%s\" />", field.getName(), field.getPropertyName());
            resultSb.append(idTag);
        }
        String resultMapClosing = "\n\t</resultMap>";
        return start + resultSb + resultMapClosing;
    }

    private static String selectTag(String methodName, String resultMapID, String sql) {
        return String.format("\n\n\t<select id=\"%s\" resultMap=\"%s\"> %s \n\t</select>", methodName, resultMapID, sql);
    }

    private static String updateTag(String methodName, String sql) {
        return String.format("\n\n\t<update id=\"%s\"> %s \n\t</update>", methodName, sql);
    }

    private static String deleteTag(String methodName, String sql) {
        return String.format("\n\n\t<delete id=\"%s\"> %s\n\t</delete>", methodName, sql);
    }

    private static String genSelectSql(Table table, String params) {
        return String.format("\n\t\tSelect * from %s where %s", table.getName(), params);
    }
    private static String genSelectSqlParam(Table table, String property) {
        String fieldName = null;
        for (Field f : table.getFields()) {
            if (f.getPropertyName().equals(property)) {
                fieldName = f.getName();
                break;
            }
        }
        return  String.format("%s = #{%s}", fieldName, property);
    }

    private static String genUpdateSql(Table table, String setParam, String param) {
        return String.format("\n\t\tUpdate %s %s \n\t\twhere %s", table.getName(), setParam, param);
    }
    private static String genUpdateParam(Table table, String property){
        String fieldName = null;
        List<Field> fields = table.getFields();
        for (Field f : fields){
            if (f.getPropertyName().equals(property)) {
                fieldName = f.getName();
            }
        }
        return String.format("%s = #{%s}", fieldName, property);
    }
    private static String genUpdateSetParamRef(){
        return createIncludeTag(XML.REF_NON_AUTO_SET_PARAMS);
    }
    private static String genUpdateSetParam(Table table){
        StringBuilder params = new StringBuilder();
        List<Field> fields = table.getFields();
        int index = 0;
        for (Field f : fields){
            index++;
            if (!f.isAutoIncrement()){
                String propertyName = f.getPropertyName();
                params.append(String.format("%s = #{%s}", f.getName(), propertyName));
                if (!(index > fields.size())){
                    params.append(", ");
                }
            }
        }
        return params.toString();
    }

    private static String genDeleteSql(Table table, String params) {
        return String.format("\n\t\tDelete from %s where %s", table.getName(), params);
    }
    private static String genDeleteSqlParam(Table table, String property){
        String fieldName = null;
        for (Field f : table.getFields()) {
            if (f.getPropertyName().equals(property)) {
                fieldName = f.getName();
                break;
            }
        }
        return String.format("%s = #{%s}", fieldName, property);
    }

    private static String createIncludeTag(String refId){
        return  String.format("\n\t\t<include refid=\"%s\"/>", refId);
    }
    private static String createSqlTag(String id, String content){
        return String.format("\n\t<sql id=\"%s\"> %s \n\t</sql>\n", id, content);
    }

    private static String buildFullColumnTag(Table table){
        StringBuilder sb = new StringBuilder();
        for (Field field: table.getFields()) {
            sb.append(field.getName()).append(",");
        }
        return "\n\t\t" + sb.substring(0, sb.lastIndexOf(","));
    }

    private static String buildNonAutoColumnTag(Table table){
        StringBuilder sb = new StringBuilder();
        for (Field field: table.getFields()) {
            if (!field.isAutoIncrement()){
                sb.append(field.getName()).append(",");
            }
        }
        return "\n\t\t" + sb.substring(0, sb.lastIndexOf(","));
    }
    private static String buildNonAutoSetParamsTag(Table table){
        StringBuilder beanSetParamsBuilder = new StringBuilder();
        String beanName = table.getBeanName();
        int size = table.getFields().size() - 1;
        int index = 0;
        for (Field field: table.getFields()) {
            if (!field.isAutoIncrement()){
                String param = StringConvertor.lowerCaseFirstLetter(beanName);
                String tagContent = String.format("%s = #{%s}", field.getName(), param + "." + field.getPropertyName());
                String ifTag = "";
                if (index == size){
                    ifTag = String.format("\n\t\t\t<if test=\"%s.%s != null\">%s</if>", param, field.getPropertyName(),tagContent);
                }else {
                    ifTag = String.format("\n\t\t\t<if test=\"%s.%s != null\">%s,</if>", param, field.getPropertyName(),tagContent);
                }
                beanSetParamsBuilder.append(ifTag);
            }
            index++;
        }
        return String.format("\n\t\t<set>%s\n\t\t</set>",beanSetParamsBuilder);
    }

    private static String buildQueryConditions(Table table){
        StringBuilder baseConditionBuilder = new StringBuilder();
        for (Field field: table.getFields()) {
            String query = "";
            if (ArrayUtils.contains(TypeHandler.String, field.getSqlType())){
                query = " and query." + field.getPropertyName() + "!=''";
            }
            String ifContent = String.format("\n\t\t\tand %s = #{query.%s}", field.getName(),field.getPropertyName());
            String ifSql = String.format("\n\t\t<if test=\"query.%s != null%s\">%s\n\t\t</if>", field.getPropertyName(), query, ifContent);
            baseConditionBuilder.append(ifSql);
        }
        return baseConditionBuilder.toString();
    }

    private static String genericSelectList(Table table){
        String queryPackage = Property.getQueryPackage();
        String queryParamName = table.getQueryParamName();
        String fullQualifiedName = queryPackage + "." + queryParamName;
        String includeTag = createIncludeTag(REF_QUERY_FULL_CONDITIONS);
        String sql = String.format("\n\t\tselect * from %s %s", table.getName(), includeTag);
        return String.format("\n\n\t<select id=\"selectList\" resultType=\"%s\"> %s \n\t</select>", fullQualifiedName, sql);
    }

    private static String genericInsert(Table table){
        String poPackage = Property.getPoPackage();
        String pojoParamName = table.getPojoParamName();
        String fullQualifiedName = poPackage + "." + pojoParamName;
        String tableName = table.getName();
        String includeTag1 = createIncludeTag(REF_INSERT_COLUMNS);
        String includeTag2 = createIncludeTag(REF_INSERT_VALUES);
        return String.format
                ("\n\n\t<insert id=\"insert\" parameterType=\"%s\">\n\t\tinsert into %s %s \n\t\tvalues %s \n\t</insert>",
                        fullQualifiedName, tableName, includeTag1, includeTag2);
    }

    private static String genericInsertOrUpdate(Table table){
        String poPackage = Property.getPoPackage();
        String pojoParamName = table.getPojoParamName();
        String fullQualifiedName = poPackage + "." + pojoParamName;
        String tableName = table.getName();
        String includeTag1 = createIncludeTag(REF_IN_OR_UP_COLUMNS);
        String includeTag2 = createIncludeTag(REF_IN_OR_UP_VALUES);
        String includeTag3 = createIncludeTag(REF_IN_OR_UP_UPDATE_VALUES);
        return String.format
                ("\n\n\t<insert id=\"insertOrUpdate\" parameterType=\"%s\">\n\t\tInsert into %s %s \n\t\tvalues %s \n\t\tOn Duplicate Key Update %s \n\t</insert>",
                        fullQualifiedName, tableName, includeTag1, includeTag2, includeTag3);
    }

    private static String genericInsertList(Table table){
        String includeTag1 = createIncludeTag(REF_NON_AUTO_COLUMNS);
        StringBuilder ifTagSb = new StringBuilder();
        List<Field> fields = table.getFields();
        String beanName = table.getBeanName();
        for (Field field : fields){
            if (!field.isAutoIncrement()){
                String param = beanName + "." + field.getName();
                ifTagSb.append(String.format("\n\t\t\t\t<if test=\"%s != null\">#{%s},</if>", param, param));
                ifTagSb.append(String.format("\n\t\t\t\t<if test=\"%s == null\">Null,</if>", param));
            }
        }
        String ifTag = ifTagSb.toString();
        String trimTag = String.format("\n\t\t\t<trim suffix=\")\" prefix=\"(\" suffixOverrides=\",\"> %s \n\t\t\t</trim>", ifTag);
        String forEachTag = String.format("\n\t\t<foreach item=\"%s\" collection=\"insertList\" separator=\",\"> %s \n\t\t</foreach>", beanName, trimTag);
        return String.format
                ("\n\n\t<insert id=\"insertList\" parameterType=\"java.util.List\"> \n\t\tinsert into table %s %s \n\t\tvalues %s\n\t</insert>",
                        table.getName(), includeTag1, forEachTag
                        );
    }

    private static String genericInsertOrUpdateList(Table table){
        String beanName = table.getBeanName();
        StringBuilder forEachParamsSb = new StringBuilder();
        List<Field> fields = table.getFields();
        for(Field field : fields){
            forEachParamsSb.append(String.format("#{%s},",beanName + "." + field.getName()));
        }
        String forEachParams = "\n\t\t\t" + forEachParamsSb.substring(0, forEachParamsSb.lastIndexOf(","));
        String forEach = String.format("\n\t\t<foreach item=\"%s\" collection=\"inOrUpList\" open=\"(\" close=\")\" separator=\",\">%s\n\t\t</foreach>",beanName, forEachParams);
        String includeTag1 = createIncludeTag(REF_IN_OR_UP_LIST_COLUMNS);
        String includeTag2 = createIncludeTag(REF_IN_OR_UP_LIST_UPDATE_VALUES);
        return String.format
                ("\n\n\t<insert id=\"insertOrUpdateList\" parameterType=\"java.util.List\">\n\t\tinsert into %s %s values %s \n\t\tas %s \n\t\ton duplicate key update %s\n\t</insert>",
                       table.getName(), includeTag1, forEach, beanName, includeTag2);
    }

    private static String genericSelectCount(Table table){
        String tableName = table.getName();
        String queryPackage = Property.getQueryPackage();
        String queryParamName = table.getQueryParamName();
        String includeTag = String.format("\n\t\t\t<include refid=\"%s\"/>", REF_QUERY_CONDITIONS);
        String fullQualifiedName = queryPackage + "." + queryParamName;
        String whereTag = String.format("\n\t\t<where> %s \n\t\t</where>",  includeTag);
        return String.format("\n\n\t<select id=\"selectCount\" parameterType=\"%s\">\n\t\tselect count(*) from %s %s\n\t</select>",
                fullQualifiedName, tableName, whereTag);
    }
}
