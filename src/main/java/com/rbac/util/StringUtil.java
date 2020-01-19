package com.rbac.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {
    public static List<Integer> splitToListInt(String rangeWithComma) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(rangeWithComma);
        List<Integer> list = strList.stream().map(Integer::parseInt).collect(Collectors.toList());
        return list;
     }
}
