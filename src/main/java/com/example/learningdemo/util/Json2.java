package com.example.learningdemo.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.example.learningdemo.exception.AssertionException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Json2 {
    private final String ISO_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private static ObjectMapper o = new ObjectMapper();

    static {
        SimpleDateFormat format = new SimpleDateFormat(ISO_DATE_TIME);
        o.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(format)
                .registerModule(javaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    private static SimpleModule javaTimeModule() {
        SimpleModule javaTimeModule = new SimpleModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(Date2.DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(Date2.DATE_TIME_FORMATTER));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(Date2.DATE_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(Date2.DATE_FORMATTER));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(Date2.TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(Date2.TIME_FORMATTER));
        return javaTimeModule;
    }

    private static ObjectMapper objectMapper() {
        return o;
    }

    public static String toJson(Object o) {
        try {
            return o == null ? "" : objectMapper().writeValueAsString(o);
        } catch (IOException e) {
            throw new AssertionException(e);
        }
    }

    public static <T> List<T> toList(String jsonStr, Class<T> c) {
        try {
            return objectMapper().readValue(jsonStr, getCollectionType(c));
        } catch (IOException e) {
            throw new AssertionException(e);
        }
    }

    public static <T> List<T> toList(byte[] src, Class<T> c) {
        try {
            return objectMapper().readValue(src, getCollectionType(c));
        } catch (IOException e) {
            throw new AssertionException(e);
        }
    }

    public static <T> T toObject(String jsonStr, Class<T> c) {
        try {
            return StringUtils.isBlank(jsonStr) ? null : objectMapper().readValue(jsonStr, c);
        } catch (IOException e) {
            throw new AssertionException(e);
        }
    }

    public static <T> T toObject(byte[] src, Class<T> c) {
        try {
            return objectMapper().readValue(src, c);
        } catch (IOException e) {
            throw new AssertionException(e);
        }
    }

    public static <T> T toObject(String jsonStr, JavaType javaType) {
        try {
            return StringUtils.isBlank(jsonStr) ? null : objectMapper().readValue(jsonStr, javaType);
        } catch (IOException e) {
            throw new AssertionException(e);
        }
    }

    private static JavaType getCollectionType(Class<?>... elementClasses) {
        return o.getTypeFactory().constructParametricType(ArrayList.class, elementClasses);
    }

    public static JavaType constructType(Type type) {
        return objectMapper().getTypeFactory().constructType(type);
    }
}
