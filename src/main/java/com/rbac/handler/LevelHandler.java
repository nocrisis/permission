package com.rbac.handler;


import org.apache.commons.lang3.StringUtils;

public class LevelHandler {
    public static final String SEPARATOR = ".";
    public static final String ROOT = "0";

    //0
    //0.1
    //0.1.2
    //0.1.3
    //0.4
    //即level是逐级拼接的
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        }else{
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
