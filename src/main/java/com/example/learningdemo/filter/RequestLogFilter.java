package com.example.learningdemo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.learningdemo.util.RequestIdHolder;
import com.example.learningdemo.util.RequestUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(-300)
public class RequestLogFilter extends OncePerRequestFilter {

    private static final String HEALTHZ_URL = "healthz";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestIdHolder.bindRequestId(request);
        MDC.put("requestId", RequestIdHolder.acquireRequestId());
        long start = System.currentTimeMillis();
        String paramString = RequestUtils.getParamString(request);
        try {
            filterChain.doFilter(request, response);
        } finally {
            if (!request.getRequestURI().startsWith(HEALTHZ_URL)) {
                long costTime = System.currentTimeMillis() - start;
                log.info("request log costTime:{}  uri:{} method:{} {}", costTime, request.getRequestURI(),
                        request.getMethod(), paramString);
            }
            MDC.clear();
        }
    }

}
