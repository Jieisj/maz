package com.maz.builder.entity;

import com.maz.builder.Template;
import com.maz.util.Constructor;
import com.maz.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    public static void buildResponse(){
        logger.info("------------------------------Response-----------------------------");
        logger.info("Initializing Building Pojo...");
        logger.info("SourcePath: {}", Property.getSourcePath());
        logger.info("JavaPath: {}", Property.getJavaPath());
        logger.info("BasePackage: {}", Property.getBasePackage());
        logger.info("ResponsePackage: {}", Property.getResponsePackage());
        logger.info("ResponsePath: {}", Property.getResponsePath());
        logger.info("Start Building...");
        String outPath = Property.getResponsePath();
        String responsePackage = Property.getResponsePackage();
        String packageInfo = Constructor.consPackage(responsePackage);
        if (Property.getUseLombok()){
            Template.buildFromTxt("Response(lombok).txt", "Response.java", outPath, "java_template", packageInfo);
        }else {
            Template.buildFromTxt("Response.txt", "Response.java", outPath, "java_template", packageInfo);
        }
        logger.info("------------------------------Response End-------------------------");
    }

}
