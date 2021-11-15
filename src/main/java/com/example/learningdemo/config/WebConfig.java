package com.example.learningdemo.config;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.learningdemo.util.Date2;
import com.example.learningdemo.util.JsonUtils2;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, jackson2MessageConverter());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateTimeFormatter(Date2.DATE_TIME_FORMATTER);
        registrar.setDateFormatter(DateTimeFormatter.ISO_DATE);
        registrar.setTimeFormatter(DateTimeFormatter.ISO_TIME);
        registrar.registerFormatters(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "index.html");
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    private MappingJackson2HttpMessageConverter jackson2MessageConverter() {
        return new MappingJackson2HttpMessageConverter(JsonUtils2.createObjectMapper());
    }
}
