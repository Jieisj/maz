package com.maz.builder.service;

import com.maz.bean.Field;
import com.maz.util.Constructor;
import com.maz.util.Property;
import com.maz.bean.Table;
import com.maz.util.StringConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Service {
    private static final Logger logger = LoggerFactory.getLogger("com.maz.builder.service.Service");
    private static void buildService(Table table){
        String servicePath = Property.getServicePath();

        File serviceDirs = new File(servicePath);
        File serviceInterface =  new File(servicePath + "/" + table.getBeanName() + "Service.java");

        if (serviceDirs.mkdirs()){
            logger.info("Service Directors Created");
        }else {
            logger.info("Service Directors Have Existed");
        }

        try {
            if (serviceInterface.createNewFile()){
                logger.info("Service Java Interface Created");
            }else {
                logger.info("Service Java Interface Have Existed");
            }
            try(OutputStream outputStream = new FileOutputStream(serviceInterface);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            )
            {
                String content = buildServiceContent(table);
                content = content + String.format("\tInteger insert(%s bean);", table.getPojoParamName());
                content = content + String.format("\n\tInteger insertOrUpdate(%s bean);", table.getPojoParamName());
                content = content + String.format("\n\tInteger insertBatch(List<%s> list);", table.getPojoParamName());
                content = content + String.format("\n\tInteger insertOrUpdateBatch(List<%s> list);", table.getPojoParamName());
                content = content + String.format("\n\tList<%s> selectList(%s queryBean);  //pagination could apply here", table.getPojoParamName(), table.getQueryParamName());
                content = content + String.format("\n\tInteger selectCount(%s queryBean);", table.getQueryParamName());
                String packageInfo = Constructor.consPackage(Property.getServicePackage());
                String importInfo  = "\n" + Constructor.consImport(Property.getPoPackage() + "." + table.getPojoParamName());
                importInfo = importInfo + "\n" + Constructor.consImport(Property.getQueryPackage() + "." + table.getQueryParamName());
                importInfo = importInfo + "\n" + Constructor.consImport("java.util.List") + "\n";
                String serviceTemplate = buildServiceTemplate(table, packageInfo, importInfo, content);
                bw.write(serviceTemplate);
            }catch (IOException e){
                logger.info("Service File Write Failed");
            }
        }catch (IOException e){
            logger.info("Service Build Failed");
        }
    }

    public static void buildServiceFromTables(Set<Table> tables){
        logger.info("------------------------------Service-----------------------------");
        logger.info("Initializing Building Service...");
        logger.info("SourcePath: {}", Property.getSourcePath());
        logger.info("BasePackage: {}", Property.getBasePackage());
        logger.info("ServicePackage: {}", Property.getServicePackage());
        logger.info("ServicePath: {}", Property.getServicePath());
        logger.info("Start Building...");
        for(Table table : tables){
            buildService(table);
        }
        logger.info("------------------------------Service End-------------------------");
    }

    private static String buildServiceContent(Table table){
        StringBuilder methodSb = new StringBuilder();
        Map<String, List<Field>> indexMap = table.getIndexMap();
        for (Map.Entry<String, List<Field>> entry : indexMap.entrySet()) {
            List<Field> fields = entry.getValue();
            int index = 0;
            StringBuilder properties = new StringBuilder();
            StringBuilder params = new StringBuilder();
            for (Field field : fields) {
                index++;
                String propertyName = field.getPropertyName();
                String param = String.format("%s %s", field.getJavaType(), propertyName);

                propertyName = StringConvertor.upperCaseFirstLetter(propertyName);

                properties.append(propertyName);
                params.append(param);

                if (index < fields.size()) {
                    properties.append("And");
                    params.append(", ");
                }
            }
            String propertyLine = properties.toString();
            String paramLine = params.toString();

            String selectMethodName = String.format("selectBy%s", propertyLine);
            String updateMethodName = String.format("updateBy%s", propertyLine);
            String deleteMethodName = String.format("deleteBy%s", propertyLine);

            String select = String.format("\t%s %s(%s);\n", table.getPojoParamName(), selectMethodName, paramLine);
            String update = String.format("\tInteger %s(%s);\n", updateMethodName, paramLine + String.format(", %s bean", table.getPojoParamName()));
            String delete = String.format("\tInteger %s(%s);\n", deleteMethodName, paramLine);
            methodSb.append(select); // generate interface select method;
            methodSb.append(update); // generate interface update method;
            methodSb.append(delete); // generate interface delete method;
        }
        return methodSb.toString();
    }

    public static String buildServiceTemplate(Table table, String packageInfo, String importInfo, String content){
        String body = String.format("public interface %s{\n%s\n}", table.getBeanName() + "Service", content);
        if (packageInfo == null || packageInfo.isEmpty()){
            return importInfo == null || importInfo.isEmpty() ? body : importInfo + "\n" + body;
        }else {
            return importInfo == null || importInfo.isEmpty() ? packageInfo + "\n" +  body : packageInfo + "\n" + importInfo + "\n" +  body;
        }
    }
}
