package com.maz.builder;

import com.maz.bean.Field;
import com.maz.bean.Table;
import com.maz.util.Constructor;
import com.maz.util.Property;
import com.maz.util.StringConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Mapper {
    private static final Logger logger = LoggerFactory.getLogger("builder.Mapper");
    public static Map<Table, List<String>> selectMethodNamesMap = new LinkedHashMap<>();
    public static Map<Table, List<String>> updateMethodNamesMap = new LinkedHashMap<>();
    public static Map<Table, List<String>> deleteMethodNamesMap = new LinkedHashMap<>();

    static {
        String outputPath = Property.getMapperPath();
        String mapperPackage = Property.getMapperPackage();
        String packageInfo = Constructor.consPackage(mapperPackage);
        if (Property.getUseLombok()){
            Template.buildFromTxt("GenericMapper.txt", "GenericMapper.java",outputPath, "java_template", packageInfo);
        }else {
            Template.buildFromTxt("GenericMapper.txt", "GenericMapper.java",outputPath,"java_template", packageInfo);
        }
    }

    private static void buildMapper(Table table) {
        //properties
        String mapperPath = Property.getMapperPath();

        String className = table.getBeanName();
        File dirs = new File(mapperPath);
        File javaMapper = new File(mapperPath + "/" + className + "Mapper.java");

        logger.info("---------------------------------------------------------------");
        if (dirs.mkdirs()) {
            logger.info("Mapper Directors Created");
        } else {
            logger.info("Mapper Directors Have Existed");
        }
        try {
            if (javaMapper.createNewFile()) {
                logger.info("Mapper Java File Created");
            } else {
                logger.info("Mapper Java File Have Existed");
            }
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(javaMapper));
                 BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
                String mapperContent = consContent(table);
                String mapper = consMapper(table, mapperContent);
                bw.write(mapper);
                bw.flush();
            } catch (IOException e) {
                logger.info("Mapper File Writer Failed");
            }
        } catch (Exception e) {
            logger.info("Mapper Build File Failed");
        }
    }

    public static void buildMapperFromTables(Set<Table> tables) {
        logger.info("------------------------------Mapper-----------------------------");
        logger.info("Initializing Building Mapper...");
        logger.info("PathSource: {}", Property.getSourcePath());
        logger.info("BasePackage: {}", Property.getBasePackage());
        logger.info("MapperPackage: {}", Property.getMapperPackage());
        logger.info("MapperPath: {}", Property.getMapperPath());
        logger.info("Start Building...");
        for (Table table : tables) {
            buildMapper(table);
        }
        logger.info("------------------------------Mapper End-------------------------");
    }

    private static String consContent(Table table) {
        ArrayList<String> selectMethodNamesList = new ArrayList<>();
        ArrayList<String> updateMethodNamesList = new ArrayList<>();
        ArrayList<String> deleteMethodNamesList = new ArrayList<>();
        StringBuilder sb = new StringBuilder(); // content store in StringBuilder;
        Map<String, List<Field>> indexMap = table.getIndexMap();
        for (Map.Entry<String, List<Field>> entry : indexMap.entrySet()) {
            List<Field> fields = entry.getValue();
            int index = 0;
            StringBuilder properties = new StringBuilder();
            StringBuilder params = new StringBuilder();
            for (Field field : fields) {
                index++;
                String propertyName = field.getPropertyName();
                String param = String.format("@Param(\"%s\") %s %s", propertyName, field.getJavaType(), propertyName);

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

            String select = String.format("\tT %s(%s);\n", selectMethodName, paramLine);
            String update = String.format("\tInteger %s(%s);\n", updateMethodName, paramLine + String.format(", @Param(\"%s\") T t", table.getPojoParamName()));
            String delete = String.format("\tInteger %s(%s);\n", deleteMethodName, paramLine);
            sb.append(select); // generate interface select method;
            sb.append(update); // generate interface update method;
            sb.append(delete); // generate interface delete method;

            selectMethodNamesList.add(selectMethodName);
            updateMethodNamesList.add(updateMethodName);
            deleteMethodNamesList.add(deleteMethodName);
        }

        selectMethodNamesMap.put(table, selectMethodNamesList);
        updateMethodNamesMap.put(table, updateMethodNamesList);
        deleteMethodNamesMap.put(table, deleteMethodNamesList);
        return sb.toString();
    }

    private static String consMapper(Table table, String content) {
        String packageInfo = Property.getMapperPackage();
        String importInfo = "import org.apache.ibatis.annotations.Param;\n";
        String start = String.format("\npublic interface %s extends %s{\n%s", table.getBeanName() + "Mapper<T,P>", "GenericMapper<T,P>", content);
        String close = "}";
        if (packageInfo == null || packageInfo.isEmpty()) {
            return start + importInfo + close;
        }
        packageInfo = Constructor.consPackage(packageInfo) + "\n";
        return packageInfo + importInfo + start + close;
    }

    private static void consGenericMapper() {
        URL resource = Mapper.class.getClassLoader().getResource("java_template/GenericMapper.txt");
        String resourceUrl = "";
        if (resource != null) {
            resourceUrl = resource.getPath();
        }
        try (OutputStream outputStream = new FileOutputStream(Property.getMapperPath() + "/GenericMapper.java");
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

             InputStream inputStream = new FileInputStream(resourceUrl);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ) {
            String classPackage = Constructor.consPackage(Property.getMapperPackage()) + "\n";
            bufferedWriter.write(classPackage);
            bufferedWriter.newLine();
            String lineContent;
            while ((lineContent = bufferedReader.readLine()) != null) {
                bufferedWriter.write(lineContent);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.flush();
        } catch (Exception e) {
            logger.error("GenericMapper.txt Java File Construct Failed");
        }
    }
}
