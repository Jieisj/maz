package com.maz.builder.service;

import com.maz.bean.Field;
import com.maz.bean.Table;
import com.maz.util.Constructor;
import com.maz.util.Property;
import com.maz.util.StringConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger("com.maz.builder.service.ServiceImpl");
    private static void buildServiceImpl(Table table) {
        String serviceImplPath = Property.getServiceImplPath();

        File serviceImplDirs = new File(serviceImplPath);
        File serviceImpl =  new File(serviceImplPath + "/" + table.getBeanName() + "ServiceImpl.java");

        if (serviceImplDirs.mkdirs()){
            logger.info("Service Impl Directors Created");
        }else {
            logger.info("Service Impl Directors Have Existed");
        }

        try {
            if (serviceImpl.createNewFile()){
                logger.info("Service Impl Java Class Created");
            }else {
                logger.info("Service Impl Java Class Have Existed");
            }
            try(OutputStream outputStream = new FileOutputStream(serviceImpl);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            )
            {
                String globalMapperVar = StringConvertor.lowerCaseFirstLetter(table.getBeanName()) + "Mapper";
                String genericMethodCon1 = String.format("\t\treturn %s.insert(bean);", globalMapperVar);
                String genericMethodCon2 = String.format("\t\treturn %s.insertOrUpdate(bean);", globalMapperVar);
                String genericMethodCon3 = String.format("\t\treturn %s.insertBatch(list);", globalMapperVar);
                String genericMethodCon4 = String.format("\t\treturn %s.insertOrUpdateBatch(list);", globalMapperVar);
                String genericMethodCon5 = String.format("\t\treturn %s.selectList(queryBean);", globalMapperVar);
                String genericMethodCon6 = String.format("\t\treturn %s.selectCount(queryBean);", globalMapperVar);
                String content = buildServiceImplContent(table, globalMapperVar);
                content = content + String.format("\n\t@Override\n\tpublic Integer insert(%s bean){\n%s\n\t}", table.getPojoParamName(),genericMethodCon1);
                content = content + String.format("\n\t@Override\n\n\tpublic Integer insertOrUpdate(%s bean){\n%s\n\t}", table.getPojoParamName(),genericMethodCon2);
                content = content + String.format("\n\t@Override\n\n\tpublic Integer insertBatch(List<%s> list){\n%s\n\t}", table.getPojoParamName(),genericMethodCon3);
                content = content + String.format("\n\t@Override\n\n\tpublic Integer insertOrUpdateBatch(List<%s> list){\n%s\n\t}", table.getPojoParamName(),genericMethodCon4);
                content = content + String.format("\n\t@Override\n\n\tpublic List<%s> selectList(%s queryBean){\n%s\n\t}",table.getPojoParamName(), table.getQueryParamName(),genericMethodCon5);
                content = content + String.format("\n\t@Override\n\n\tpublic Integer selectCount(%s queryBean){\n%s\n\t}", table.getQueryParamName(),genericMethodCon6);
                String packageInfo = Constructor.consPackage(Property.getServiceImplPackage());
                String importInfo  = "\n" + Constructor.consImport(Property.getPoPackage() + "." + table.getPojoParamName());
                importInfo = importInfo + "\n" + Constructor.consImport(Property.getQueryPackage() + "." + table.getQueryParamName());
                importInfo = importInfo + "\n" + Constructor.consImport(Property.getMapperPackage() + "." + table.getBeanName() + "Mapper");
                importInfo = importInfo + "\n" + Constructor.consImport(Property.getServicePackage() + "." + table.getBeanName() + "Service");
                importInfo = importInfo + "\n" + Constructor.consImport("java.util.List");
                String serviceTemplate = buildServiceImplTemplate(table, packageInfo, importInfo, content);
                bw.write(serviceTemplate);
            }catch (IOException e){
                logger.info("Service Impl Java File Write Failed");
            }
        }catch (IOException e){
            logger.info("Service Impl Java File Build Failed");
        }
    }

    public static void buildServiceImplFromTables(Set<Table> tables){
        logger.info("------------------------------Service Impl-----------------------------");
        logger.info("Initializing Building Service...");
        logger.info("SourcePath: {}", Property.getSourcePath());
        logger.info("BasePackage: {}", Property.getBasePackage());
        logger.info("ServiceImplPackage: {}", Property.getServiceImplPackage());
        logger.info("ServiceImplPath: {}", Property.getServiceImplPath());
        logger.info("Start Building...");
        for(Table table : tables){
            buildServiceImpl(table);
        }
        logger.info("------------------------------Service Impl End-------------------------");
    }

    private static String buildServiceImplContent(Table table, String mapperVar){
        StringBuilder methodSb = new StringBuilder();
        Map<String, List<Field>> indexMap = table.getIndexMap();
        for (Map.Entry<String, List<Field>> entry : indexMap.entrySet()) {
            List<Field> fields = entry.getValue();
            int index = 0;
            StringBuilder properties = new StringBuilder();
            StringBuilder propertyNames = new StringBuilder();
            StringBuilder params = new StringBuilder();
            for (Field field : fields) {
                index++;
                String propertyName = field.getPropertyName();
                String param = String.format("%s %s", field.getJavaType(), propertyName);

                propertyNames.append(propertyName);
                propertyName = StringConvertor.upperCaseFirstLetter(propertyName);
                properties.append(propertyName);
                params.append(param);

                if (index < fields.size()) {
                    propertyNames.append(",");
                    properties.append("And");
                    params.append(", ");
                }
            }
            String propertyLine = properties.toString();
            String paramLine = params.toString();
            String paramLineNoMod = propertyNames.toString();

            String selectMethodName = String.format("selectBy%s", propertyLine);
            String updateMethodName = String.format("updateBy%s", propertyLine);
            String deleteMethodName = String.format("deleteBy%s", propertyLine);

            String selectMethodBody = String.format("\n\t\treturn %s.%s(%s);", mapperVar, selectMethodName, paramLineNoMod);
            String updateMethodBody = String.format("\n\t\treturn %s.%s(%s);", mapperVar, updateMethodName, paramLineNoMod + ", bean");
            String deleteMethodBody = String.format("\n\t\treturn %s.%s(%s);", mapperVar, deleteMethodName, paramLineNoMod);

            String select = String.format("\n\t@Override\n\tpublic %s %s(%s){%s\n\t}\n", table.getPojoParamName(), selectMethodName, paramLine, selectMethodBody);
            String update = String.format("\n\t@Override\n\tpublic Integer %s(%s){%s\n\t}\n", updateMethodName, paramLine + ", " + table.getPojoParamName() + " bean", updateMethodBody);
            String delete = String.format("\n\t@Override\n\tpublic Integer %s(%s){%s\n\t}\n", deleteMethodName, paramLine, deleteMethodBody);
            methodSb.append(select); // generate interface select method;
            methodSb.append(update); // generate interface update method;
            methodSb.append(delete); // generate interface delete method;
        }
        return methodSb.toString();
    }

    public static String buildServiceImplTemplate(Table table, String packageInfo, String importInfo, String content){
        String implClassName = table.getBeanName() + "Service";
        String mapper = table.getBeanName() +"Mapper";
        String generic1 = table.getPojoParamName();
        String generic2 = table.getQueryParamName();
        String globalVar = StringConvertor.lowerCaseFirstLetter(table.getBeanName()) + "Mapper";
        String injectMapper = String.format("\t@Resource\n\tprivate %s<%s, %s> %s;",mapper,generic1,generic2, globalVar);
        String body = String.format("\n@Service(\"%s\")\npublic class %s implements %s{\n%s\n%s\n}",
                table.getBeanName() + "Service", table.getBeanName() + "ServiceImpl", implClassName, injectMapper, content);
        importInfo = importInfo + "\n" + Constructor.consImport("org.springframework.stereotype.Service");
        importInfo = importInfo + "\n" + Constructor.consImport("jakarta.annotation.Resource");
        if (packageInfo == null || packageInfo.isEmpty()){
            return importInfo + "\n" + body;
        }else {
            return packageInfo + "\n" + importInfo + "\n" +  body;
        }
    }
}
