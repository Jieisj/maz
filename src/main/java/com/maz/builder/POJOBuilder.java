package com.maz.builder;

import com.maz.bean.Field;
import com.maz.bean.Table;
import com.maz.util.Constructor;
import com.maz.util.Property;
import com.maz.util.StringConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;

public class POJOBuilder {
    private static final Logger logger = LoggerFactory.getLogger("builder.POJOBuilder");
    public static void buildPOJO(Table table){
        //properties
        String poPath = Property.getPoPath();
        boolean isIgnoreComm = Property.getPOJOIgnoreComment();

        String pojoParamName = table.getPojoParamName();
        File dirs = new File(poPath);
        File javaPo = new File(poPath + "/" + pojoParamName + ".java");

        logger.info("---------------------------------------------------------------");
        if (dirs.mkdirs()){
            logger.info("POJO Directors Created");
        }else {
            logger.info("POJO Directors Have Existed");
        }
        try {
            if (javaPo.createNewFile()){
                logger.info("POJO Java File Created");
            }else {
                logger.info("POJO Java File Have Existed");
            }
            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(javaPo));
                BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
                String importInfo = Constructor.consPoOrQueryImport(table);
                String constructedPOJO = Constructor.construct(table, table.getPojoParamName(), Property.getPoPackage(), importInfo, isIgnoreComm);
                bw.write(constructedPOJO);
                bw.flush();
            }catch (IOException e){
                logger.info("POJO File Writer Failed");
            }
        }catch (IOException e){
            logger.info("POJO Build File Failed");
        }
    }

    public static void buildPojoFromTables(Set<Table> tables){
        logger.info("------------------------------Pojo-----------------------------");
        logger.info("Initializing Building Pojo...");
        logger.info("PathSource: {}", Property.getSourcePath());
        logger.info("PathResources: {}", Property.getResourcePath());
        logger.info("PackageBase: {}", Property.getBasePackage());
        logger.info("PoPackage: {}", Property.getPoPackage());
        logger.info("PathPo: {}", Property.getPoPath());
        logger.info("Start Building...");
        for(Table table : tables){
            buildPOJO(table);
        }
        logger.info("------------------------------Pojo End-------------------------");
    }

}
