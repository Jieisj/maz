package com.maz.builder.controller;

import com.maz.bean.Table;
import com.maz.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Controller {
    private static Logger logger = LoggerFactory.getLogger("builder.Controller");
    private static void buildController(Table table){

    }

    private static void buildControllerFromTables(Set<Table> tables){
        logger.info("------------------------------Controller-----------------------------");
        logger.info("Initializing Building Mapper...");
        logger.info("SourcePath: {}", Property.getSourcePath());
        logger.info("JavaPath: {}", Property.getJavaPath());
        logger.info("BasePackage: {}", Property.getBasePackage());
        logger.info("MapperPackage: {}", Property.getMapperPackage());
        logger.info("MapperPath: {}", Property.getMapperPath());
        logger.info("Start Building...");
        for (Table table : tables) {
            buildController(table);
        }
        logger.info("------------------------------Controller End-------------------------");
    }
}
