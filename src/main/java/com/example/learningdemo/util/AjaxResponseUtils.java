package com.example.learningdemo.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.learningdemo.common.ResponseData;
import com.example.learningdemo.common.WebExceptionResolver;
import com.google.common.base.Charsets;

import lombok.extern.slf4j.Slf4j;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Slf4j
public class AjaxResponseUtils {

    private static final String REQUEST_ID = "requestId";

    private static final WebExceptionResolver webExceptionResolver = new WebExceptionResolver();

    public static void resetResponse(HttpServletRequest request, HttpServletResponse response, Integer errorCode, Exception ex) throws IOException {
        String errorCodeStr = errorCode == null ? null : String.valueOf(errorCode);
        resetResponse(request, response, errorCodeStr, ex);
    }

    public static void resetResponse(HttpServletRequest request, HttpServletResponse response, String errorCode, Exception ex) throws IOException {
        RequestUtils.RequestHolder holder = RequestUtils.RequestHolder.holder(request);
        WebExceptionResolver.HttpResponse httpResponse = Optional.of(hadesExceptionResolve(ex)).orElse(webExceptionResolver.doResolveException(request, ex));
        String requestId = RequestIdHolder.acquireRequestId();
        resetResponse(response);
        PrintWriter writer = response.getWriter();
        writer.write(Json2.toJson(buildJsonMap(httpResponse.getCode(), httpResponse.getMessage(), requestId)));
        writer.flush();
    }

    private static void resetResponse(HttpServletResponse response) {
        response.reset();
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType("application/json");
    }

    private static ResponseData<?> buildJsonMap(String errorCode, String errorMessage, String requestId) {
        ResponseData<?> responseData = ResponseData.fail(errorCode, errorMessage);
        responseData.setRequestId(requestId);
        return responseData;
    }

    private static WebExceptionResolver.HttpResponse hadesExceptionResolve(Exception ex) {
        if (ex instanceof SQLException) {
            log.error("sqlException ", ex);
            return WebExceptionResolver.HttpResponse.error500(String.valueOf(SC_INTERNAL_SERVER_ERROR), "后端数据查询异常，请联系客服");
        }
        return WebExceptionResolver.HttpResponse.error500(String.valueOf(SC_INTERNAL_SERVER_ERROR), ex.getMessage());
    }

}
