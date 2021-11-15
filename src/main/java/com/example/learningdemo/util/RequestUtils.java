package com.example.learningdemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;

public class RequestUtils {

    public static String getParamString(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.size() > 0) {
            stringBuilder.append("url param:");
            for (String pName : parameterMap.keySet()) {
                stringBuilder.append("[").append(pName).append(":").append(Joiner.on(",").join(request.getParameterValues(pName))).append("]");
            }
        }
        if (isNotMultipart(request)) {
            String body = getRequestBody(request);
            if (body.length() > 0) {
                stringBuilder.append("json:");
                stringBuilder.append(body);
            }
        }
        return stringBuilder.toString();
    }

    public static String getRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return stringBuilder.toString();
    }

    public static boolean isNotMultipart(ServletRequest request) {
        return !StringUtils.startsWithIgnoreCase(request.getContentType(), "multipart/");
    }

    public static class RequestHolder {

        Map<String, String[]> parameterMap;

        String body;

        public static RequestHolder holder(HttpServletRequest request) {
            return new RequestHolder(request);
        }

        private RequestHolder(HttpServletRequest request) {
            if (isNotMultipart(request)) {
                body = getRequestBody(request);
            }
            parameterMap = request.getParameterMap();
        }

        public String[] getParameter(String name) {
            return parameterMap.get(name);
        }

        public String getBody() {
            return body;
        }
    }

    public static String genRequestId() {
        return UUID.randomUUID().toString();
    }

}
