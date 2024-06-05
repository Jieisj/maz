package com.maz.app;

import com.maz.bean.Table;
import com.maz.builder.TableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger("app.Application");
    public static void main(String[] args) {
        long time = System.nanoTime();
        try {
            Application.class.getClassLoader().loadClass("utils.Property");
        }catch (Exception e){
            logger.info("Load Property File Failed !");
        }
        Set<Table> table = TableBuilder.getTable();
        System.out.println(table);
        long timeSpend  = time - System.nanoTime();
        logger.info("Application Finished in {} ns", timeSpend);
    }
}