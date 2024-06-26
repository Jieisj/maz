package com.maz.builder.entity;

import com.maz.bean.Table;
import com.maz.builder.Template;
import com.maz.util.Constructor;
import com.maz.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;

public class Query {
    private static final Logger logger = LoggerFactory.getLogger("builder.Query");

    static {
        // create page query template
        String outputPath = Property.getQueryPath();
        String queryPackage = Property.getQueryPackage();
        String packageInfo = Constructor.consPackage(queryPackage);
        if (Property.getUseLombok()){
            Template.buildFromTxt("PaginationQuery(lombok).txt", "PaginationQuery.java",outputPath, "java_template", packageInfo);
        }else {
            Template.buildFromTxt("PaginationQuery.txt", "PaginationQuery.java",outputPath, "java_template", packageInfo);
        }
    }
    private static void buildQuery(Table table){
        //properties
        String queryPath = Property.getQueryPath();
        boolean useLombok = Property.getUseLombok();
        boolean isIgnoreComm = Property.getQueryIgnoreComment();

        File dirs = new File(queryPath);
        String queryParamName = table.getQueryParamName();
        File javaQuery = new File(queryPath + "/" + queryParamName + ".java");
        logger.info("---------------------------------------------------------------");

        //create file
        if (dirs.mkdirs()){
            logger.info("Query Directors Created");
        }else {
            logger.info("Query Directors Have Existed");
        }
        try {
            if (javaQuery.createNewFile()){
                logger.info("Query Java File Created");
            }else {
                logger.info("Query Java File Have Existed");
            }
            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(javaQuery));
                BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
                String importInfo = Constructor.consPoOrQueryImport(table);
                String extend = " extends PaginationQuery";
                String constructedQuery = Constructor.constructPoQueryEntity
                        (table, table.getQueryParamName(), extend, Property.getQueryPackage(), importInfo, isIgnoreComm,useLombok);
                bw.write(constructedQuery);
                bw.flush();
            }catch (IOException e){
                logger.info("Query File Writer Failed");
            }
        }catch (IOException e){
            logger.info("Query Build File Failed");
        }
    }

    public static void buildQueryFromTables(Set<Table> tables){
        logger.info("------------------------------Query-----------------------------");
        logger.info("Initializing Building Query...");
        logger.info("SourcePath: {}", Property.getSourcePath());
        logger.info("JavaPath: {}", Property.getJavaPath());
        logger.info("PackageBase: {}", Property.getBasePackage());
        logger.info("QueryPackage: {}", Property.getQueryPackage());
        logger.info("QueryPath: {}", Property.getQueryPath());
        logger.info("Start Building...");
        for(Table table : tables){
            buildQuery(table);
        }
        logger.info("------------------------------Query End-------------------------");
    }
}
