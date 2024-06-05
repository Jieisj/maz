package com.maz.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger("app.Application");
    public static void main(String[] args) {
        long time = System.nanoTime();


        long timeSpend  = time - System.nanoTime();
        logger.info("Application Finished in {} ns", timeSpend);
    }
}