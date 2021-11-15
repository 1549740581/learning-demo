package com.example.learningdemo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.learningdemo.util.AjaxResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(-1000)
public class ExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("exception", e);
            AjaxResponseUtils.resetResponse(request, response, "500", e);
        }
    }

}
