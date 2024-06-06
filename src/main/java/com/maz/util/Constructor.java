package com.maz.util;

import com.maz.bean.Field;
import com.maz.bean.Table;

import java.util.List;

public class Constructor {
    private static final String DECIMAL_PACKAGE = "java.math.BigDecimal";
    private static final String DATE_PACKAGE = "java.util.Date";
    private static final String DATE_TIME_PACKAGE = "java.time.LocalDateTime";
    public static String consPackage(String pack){
        return String.format("package %s;", pack);
    }

    private static String consImport(String importPack){
        return String.format("import %s;",importPack);
    }

    public static String constructEntity(Table table, String className, String packageInfo, String importInfo, boolean isIgnoreComment){
        StringBuilder contentBuilder = new StringBuilder();
        String packageInfos = Constructor.consPackage(packageInfo) + "\n";
        String fieldsLine = Constructor.consField(table, isIgnoreComment);
        contentBuilder.append(fieldsLine);
        contentBuilder.append(consSetter(table.getFields()));
        contentBuilder.append(consGetter(table.getFields()));
        contentBuilder.append(consToString(table));
        return Constructor.consEntity(className, packageInfos, importInfo, contentBuilder.toString());
    }

    private static String consField(Table table, boolean isIgnoreComm){
        if (isIgnoreComm){
            StringBuilder stringBuilder = new StringBuilder();
            for (Field field : table.getFields()){
                String fieldLine = "\t" + String.format("private %s %s;", field.getJavaType(), field.getPropertyName())+ "\n";
                stringBuilder.append(fieldLine);
            }
            return stringBuilder.toString();
        }else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Field field : table.getFields()){
                String comment = consComment(field);
                String fieldLine = "\t" + String.format("private %s %s;", field.getJavaType(), field.getPropertyName())+ "\n";
                stringBuilder.append(comment).append(fieldLine);
            }
            return stringBuilder.toString();
        }
    }
    private static String consEntity(String className, String packageInfo, String importInfo, String content){
        StringBuilder sb = new StringBuilder();
        String start = String.format("\npublic class %s {\n", className);
        String close = "}";
        if (packageInfo.isEmpty() && importInfo.isEmpty()){
            sb.append(start).append(content).append(close);
            return sb.toString();
        }
        if (!packageInfo.isEmpty() && importInfo.isEmpty()){
            sb.append(packageInfo).append(start).append(content).append(close);
        }else {
            sb.append(packageInfo).append(importInfo).append(start).append(content).append(close);
        }
        return sb.toString();
    }

    public static String consPoOrQueryImport(Table table){
        String importInfo = "";
        if (table.isHaveBigDecimal()){
            importInfo += consImport(DECIMAL_PACKAGE) + "\n";
        }
        if (table.isHaveDate()){
            importInfo += consImport(DATE_PACKAGE) + "\n";
        }
        if (table.isHaveDateTime()){
            importInfo += consImport(DATE_TIME_PACKAGE) + "\n";
        }
        return importInfo;
    }

    private static String consToString(Table table){
        List<Field> fields = table.getFields();
        StringBuilder sb = new StringBuilder();
        String methodBody = String.format("\t\treturn \"%s{\" +\n", table.getPojoParamName());
        for(Field field : fields){
            String propertyName = field.getPropertyName();
            String prop = String.format("\t\t\t\t\"%s=\" + %s + \",\" +\n", propertyName, propertyName);
            sb.append(prop);
        }
        String methodParams = sb.substring(0, sb.lastIndexOf(",")-1);
        String methodClose = "\n\t\t\t\t\"}\";";
        methodBody = methodBody + methodParams + methodClose;
        return String.format("\n\tpublic String toString(){\n%s\n\t}\n", methodBody);
    }

    private static String consComment(Field field){
        return "\t/** " + field.getComment() + " */\n";
    }
    private static String consSetter(List<Field> fields){
        StringBuilder sb = new StringBuilder();
        for (Field field : fields){
            String propertyName = StringConvertor.upperCaseFirstLetter(field.getPropertyName());
            String javaType = field.getJavaType();
            String variable = field.getPropertyName();
            String functionBody = String.format("\tthis.%s = %s;", field.getPropertyName(), variable);
            String setter = String.format("\n\tpublic void set%s(%s %s){\n\t %s \n\t}", propertyName, javaType, variable, functionBody);
            sb.append(setter);
        }
        return sb.toString();
    }

    private static String consGetter(List<Field> fields){
        StringBuilder sb = new StringBuilder();
        for (Field field : fields){
            String propertyName = StringConvertor.upperCaseFirstLetter(field.getPropertyName());
            String javaType = field.getJavaType();
            String functionBody = String.format("\treturn this.%s;", field.getPropertyName());
            String getter = String.format("\n\tpublic %s get%s(){\n\t %s \n\t}", javaType, propertyName, functionBody);
            sb.append(getter);
        }
        return sb.toString();
    }
    private static String consEntity(Table table, String packageInfo, String importInfo, boolean useLombok){
        return null;
    }
}
