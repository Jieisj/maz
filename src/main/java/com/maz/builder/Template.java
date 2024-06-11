package com.maz.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.MissingResourceException;

public class Template {
    private static Logger logger = LoggerFactory.getLogger("builder.Template");

    public static void buildFromTxt(String inputFileName, String outputFileName, String outputPath, String relativePath, String classPackage) {
        File directory = new File(outputPath);
        File file = new File(outputPath, outputFileName);

        logger.info("------------------------------Template-------------------------");
        if (directory.mkdirs()) {
            logger.info("{} Director Created", outputPath);
        } else {
            logger.info("{} Director Have Existed", outputPath);
        }
        try {
            if (file.createNewFile()) {
                logger.info("{} Java File Created", inputFileName.split("\\.")[0]);
            } else {
                logger.info("{} Java File Have Existed", inputFileName.split("\\.")[0]);
            }
            URL resource = Template.class.getClassLoader().getResource(relativePath + "/" + inputFileName);
            if (resource == null) {
                throw new MissingResourceException("Missing Resource ", "Template", String.format("%s", inputFileName));
            }
            String templatePath = resource.getPath();
            try (OutputStream outputStream = new FileOutputStream(file);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                 BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                 InputStream inputStream = new FileInputStream(templatePath);
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
            {
                if (classPackage.isEmpty()) {
                    String lineContent = null;
                    while ((lineContent = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(lineContent);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                } else {
                    bufferedWriter.write(classPackage);
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                    String lineContent = null;
                    while ((lineContent = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(lineContent);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                }
            } catch (Exception e) {
                logger.error("Template Read and Write Failed");
            }
        } catch (IOException e) {
            logger.info("Template : {} Build Failed", inputFileName);
        }
    }
}