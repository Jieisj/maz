package com.maz.builder.entity;

import com.maz.bean.Table;
import com.maz.util.Constructor;
import com.maz.util.Property;
import com.maz.util.StringConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;

public class POJO {
    private static final Logger logger = LoggerFactory.getLogger("builder.POJO");
    public static void buildPOJO(Table table){
        //properties
        String poPath = Property.getPoPath();
        boolean useLombok = Property.getUseLombok();
        boolean isIgnoreComm = Property.getPOJOIgnoreComment();

        String className = table.getPojoParamName();
        File dirs = new File(poPath);
        File poJava = new File(poPath + "/" + className + ".java");

        logger.info("---------------------------------------------------------------");
        if (dirs.mkdirs()){
            logger.info("POJO Directors Created");
        }else {
            logger.info("POJO Directors Have Existed");
        }
        try {
            if (poJava.createNewFile()){
                logger.info("POJO Java File Created");
            }else {
                logger.info("POJO Java File Have Existed");
            }
            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(poJava));
                BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
                String importInfo = Constructor.consPoOrQueryImport(table);
                String constructedPOJO = Constructor.constructPoQueryEntity
                        (table, table.getPojoParamName(), null, Property.getPoPackage(), importInfo, isIgnoreComm,useLombok);
                bw.write(constructedPOJO);
                bw.flush();
            }catch (IOException e){
                logger.info("POJO File Writer Failed");
            }
        }catch (IOException e){
            logger.info("POJO Build File Failed");
        }
    }

    public static void buildPOJOFromTables(Set<Table> tables){
        logger.info("------------------------------POJO-----------------------------");
        logger.info("Initializing Building Pojo...");
        logger.info("SourcePath: {}", Property.getSourcePath());
        logger.info("JavaPath: {}", Property.getJavaPath());
        logger.info("ResourcesPath: {}", Property.getResourcesPath());
        logger.info("BasePackage: {}", Property.getBasePackage());
        logger.info("PoPackage: {}", Property.getPoPackage());
        logger.info("PoPath: {}", Property.getPoPath());
        logger.info("Start Building...");
        for(Table table : tables){
            buildPOJO(table);
        }
        logger.info("------------------------------POJO End-------------------------");
    }

}
