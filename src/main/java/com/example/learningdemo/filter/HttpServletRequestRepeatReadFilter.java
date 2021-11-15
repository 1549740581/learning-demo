package com.example.learningdemo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.example.learningdemo.util.RequestUtils;

@Component
@Order(Integer.MIN_VALUE)
public class HttpServletRequestRepeatReadFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (RequestUtils.isNotMultipart(request)) {
            chain.doFilter(new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request), response);
        } else {
            chain.doFilter(request, response);
        }
    }

}
