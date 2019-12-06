package com.rbac.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class JsonData {
    private int code;
    private String msg;
    private Object data;

    public JsonData(int code) {
        this.code = code;
    }

    public static JsonData success(Object object, String msg) {
        JsonData jsonData = new JsonData(200);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData success(Object object) {
        JsonData jsonData = new JsonData(200);
        jsonData.data = object;
        return jsonData;
    }

    public static JsonData success() {
        JsonData jsonData = new JsonData(200);
        jsonData.data = "成功";
        return jsonData;
    }

    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(500);
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData fail(int code) {
        JsonData jsonData = new JsonData(code);
        return jsonData;
    }

    public static JsonData fail(int code, String msg) {
        JsonData jsonData = new JsonData(code);
        jsonData.msg = msg;
        return jsonData;
    }

    //为了本方法的model,ModelAndView(String viewName, Map<String, ?> model)
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }

}
