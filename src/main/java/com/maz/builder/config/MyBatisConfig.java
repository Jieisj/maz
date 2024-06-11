package com.maz.builder.config;

import com.maz.bean.Table;
import com.maz.util.Constructor;
import com.maz.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;

public class MyBatisConfig {
    private static final Logger logger = LoggerFactory.getLogger("builder.MybatisConfig");
    private static void buildMybatisConfig(String template){
        //properties
        String mybatisConfigPath = Property.getMybatisConfigPath();

        File mybatisConfigDirs = new File(mybatisConfigPath);
        File mybatisConfigJava = new File(mybatisConfigPath + "/MybatisConfig.java");

        logger.info("---------------------------------------------------------------");
        if (mybatisConfigDirs.mkdirs()){
            logger.info("MybatisConfig Directors Created");
        }else {
            logger.info("MybatisConfig Directors Have Existed");
        }
        try {
            if (mybatisConfigJava.createNewFile()){
                logger.info("MybatisConfig Java File Created");
            }else {
                logger.info("MybatisConfig Java File Have Existed");
            }
            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(mybatisConfigJava));
                BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
                bw.write(template);
                bw.flush();
            }catch (IOException e){
                logger.info("MybatisConfig File Writer Failed");
            }
        }catch (IOException e){
            logger.info("MybatisConfig Build File Failed");
        }
    }

    public static void buildMybatisConfig(){
        logger.info("------------------------------Mybatis Configuration-----------------------------");
        logger.info("Initializing Building Mybatis Configuration...");
        logger.info("SourcePath: {}", Property.getSourcePath());
        logger.info("BasePackage: {}", Property.getBasePackage());
        logger.info("MybatisPath: {}", Property.getMybatisConfigPath());
        logger.info("Start Building...");
        String template = constructMybatisConfig();
        buildMybatisConfig(template);
        logger.info("------------------------------Mybatis Configuration End-------------------------");
    }

    private static String constructMybatisConfig(){
        String mapperPackage = Property.getMapperPackage();
        String packageInfo = Constructor.consPackage(Property.getMybatisConfigPackage());
        String importInfo = "\n\n" + Constructor.consImport("org.mybatis.spring.annotation.MapperScan");
        importInfo = importInfo + "\n" +Constructor.consImport("org.springframework.context.annotation.Configuration");
        String classBody = String.format("\n\n@Configuration\n@MapperScan(\"%s\")\npublic class MybatisConfig{\n}", mapperPackage);
        return  packageInfo + importInfo +  classBody;
    }
}
