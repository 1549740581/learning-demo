package com.example.learningdemo.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

public class RequestIdHolder {

    private static final ThreadLocal<String> REQUEST_ID_THREAD_LOCAL = new ThreadLocal<>();

    public static String acquireRequestId() {
        return REQUEST_ID_THREAD_LOCAL.get();
    }

    public static void bindRequestId(HttpServletRequest httpServletRequest) {
        String requestId = httpServletRequest.getParameter("requestId");
        if (requestId != null) {
            REQUEST_ID_THREAD_LOCAL.set(requestId);
        } else {
            REQUEST_ID_THREAD_LOCAL.set(UUID.randomUUID().toString());
        }
    }

    public static void clear() {
        REQUEST_ID_THREAD_LOCAL.remove();
    }

}
