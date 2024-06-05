package com.maz.app;

import com.maz.bean.Table;
import com.maz.builder.POJOBuilder;
import com.maz.builder.QueryBuilder;
import com.maz.builder.TableBuilder;
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
        Set<Table> tables = TableBuilder.getTable();
        POJOBuilder.buildPojoFromTables(tables);
        QueryBuilder.buildQueryFromTables(tables);
        long timeSpend  = System.nanoTime() - time;
        logger.info("Application Finished in {} ns", timeSpend);
    }
}