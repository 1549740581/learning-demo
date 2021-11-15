package com.example.learningdemo.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.learningdemo.common.ResponseData;
import com.example.learningdemo.util.RequestIdHolder;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class JsonResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (body instanceof ResponseData) {
            ((ResponseData<?>) body).setRequestId(RequestIdHolder.acquireRequestId());
            return body;
        }
        ResponseData<Object> responseData = ResponseData.success(body);
        responseData.setRequestId(RequestIdHolder.acquireRequestId());
        return responseData;
    }
}
