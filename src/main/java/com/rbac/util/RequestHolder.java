package com.rbac.util;

import com.rbac.model.SysUser;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {
    private static final ThreadLocal<String> usernameHolder = new ThreadLocal<>();
    private static final ThreadLocal<Integer> userIdHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(String name) {
        usernameHolder.set(name);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static void add(Integer id) {
        userIdHolder.set(id);
    }

    public static String getCurrentUsername() {
        return usernameHolder.get();
    }

    public static Integer getCurrentUserId() {
        return userIdHolder.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        usernameHolder.remove();
        requestHolder.remove();
        userIdHolder.remove();
    }
}
