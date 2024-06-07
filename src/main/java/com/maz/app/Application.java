package com.maz.app;

import com.maz.builder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger("app.Application");
    public static void main(String[] args) {
        long time = System.nanoTime();
        try {
            Application.class.getClassLoader().loadClass("com.maz.util.Property");
        }catch (Exception e){
            logger.info("Load Property File Failed!");
        }
        Set<com.maz.bean.Table> tables = Table.getTable();
        POJO.buildPOJOFromTables(tables);
        Query.buildQueryFromTables(tables);
        Mapper.buildMapperFromTables(tables);
        XML.buildMapperXMLFromTables(tables);
        long timeSpend  = System.nanoTime() - time;
        logger.info("Application Finished in {} ns", timeSpend);
    }
}