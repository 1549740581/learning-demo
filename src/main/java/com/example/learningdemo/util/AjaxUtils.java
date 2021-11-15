package com.example.learningdemo.util;

import javax.servlet.http.HttpServletRequest;

public class AjaxUtils {

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

}
