package com.maz.util;

public class StringConvertor {
    public static String upperCaseFirstLetter(String s) {
        if (!s.isEmpty()) {
            char c = Character.toUpperCase(s.charAt(0));
            return s.replace(s.charAt(0), c);
        }
        return s;
    }

    public static String lowerCaseFirstLetter(String s){
        if (!s.isEmpty()){
            char c = Character.toLowerCase(s.charAt(0));
            return s.replace(s.charAt(0), c);
        }
        return s;
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

    public static String removeAndConcatCamel(String str, String regex){
        if (str.contains(regex)){
            String[] words = str.split(regex);
            StringBuilder sb = new StringBuilder();
            sb.append(words[0]);
            for (int i = 1; i < words.length; i++) {
                String word = words[i];
                String upperWord = upperCaseFirstLetter(word);
                sb.append(upperWord);
            }
            return sb.toString();
        }
        return str;
    }
}
