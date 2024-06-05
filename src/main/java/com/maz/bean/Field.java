package com.maz.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class Field {
    private String name;
    private String sqlType;
    private boolean canNull;
    private String key;
    private String defaultValue;
    private String extra;
    private String comment;
    private boolean autoIncrement;
    private String javaType;
    private String propertyName;
}
