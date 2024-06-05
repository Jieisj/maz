package com.maz.util;

public class StringConvertor {
    public static String upperCaseFirstLetter(String s) {
        if (!s.isEmpty()) {
            char c = Character.toUpperCase(s.charAt(0));
            return s.replace(c,s.charAt(0));
        }
        return "";
    }

    public static String ignorePrefix(String str, String regex){
        String prefix = str.split(regex)[0];
        return str.replace(prefix, "");
    }

    public static String removeAndConcat(String str, String regex, boolean upperEach){
        if (upperEach){
            String[] strings = str.split(regex);
            StringBuilder sb = new StringBuilder();
            for (String word : strings) {
                String upperWord = upperCaseFirstLetter(word);
                sb.append(upperWord);
            }
            return sb.toString();
        }else {
            String[] strings = str.split(regex);
            StringBuilder sb = new StringBuilder();
            for (String word : strings) {
                sb.append(word);
            }
            return sb.toString();
        }
    }

}
