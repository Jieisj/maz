package com.maz.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
@Setter
@Getter
@ToString
public class Table {
    private String name;
    private String comment;
    private List<TableField> fields;
    private Map<String, List<TableField>> indexMap;
    private String beanName;
    private String pojoParamName;
    private String queryParamName;
    private boolean haveDate;
    private boolean haveDateTime;
    private boolean haveBigDecimal;
}
