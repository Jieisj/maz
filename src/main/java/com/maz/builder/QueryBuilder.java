package com.maz.builder;

import com.maz.bean.Table;
import com.maz.util.Constructor;
import com.maz.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;

public class QueryBuilder {
    private static final Logger logger = LoggerFactory.getLogger("builder.POJOBuilder");
    private static void buildQuery(Table table){
        //properties
        String queryPath = Property.getQueryPath();
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
                String constructedQuery = Constructor.construct(table, table.getQueryParamName(), Property.getQueryPackage(), importInfo, isIgnoreComm);
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
        logger.info("PathSource: {}", Property.getSourcePath());
        logger.info("PathResources: {}", Property.getResourcePath());
        logger.info("PackageBase: {}", Property.getBasePackage());
        logger.info("QueryPackage: {}", Property.getQueryPackage());
        logger.info("PathQuery: {}", Property.getQueryPath());
        logger.info("Start Building...");
        for(Table table : tables){
            buildQuery(table);
        }
        logger.info("------------------------------Query End-------------------------");
    }
}
