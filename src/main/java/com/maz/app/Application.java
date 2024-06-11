package com.maz.app;

import com.maz.builder.*;
import com.maz.builder.config.MyBatisConfig;
import com.maz.builder.dao.Mapper;
import com.maz.builder.entity.POJO;
import com.maz.builder.entity.Query;
import com.maz.builder.entity.Response;
import com.maz.builder.service.Service;
import com.maz.builder.service.ServiceImpl;
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
        Service.buildServiceFromTables(tables);
        ServiceImpl.buildServiceImplFromTables(tables);
        MyBatisConfig.buildMybatisConfig();
        Response.buildResponse();
        long timeSpend  = System.nanoTime() - time;
        logger.info("Application Finished in {} ns", timeSpend);
    }
}