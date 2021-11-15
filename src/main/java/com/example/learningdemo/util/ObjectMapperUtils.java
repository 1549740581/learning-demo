package com.example.learningdemo.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
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

import static java.time.format.DateTimeFormatter.ofPattern;

public class ObjectMapperUtils {

    private static final ObjectMapper o = new ObjectMapper();
    private static final DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        o.setSerializationInclusion(JsonInclude.Include.ALWAYS)
                .setDateFormat(format)
                .registerModule(javaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
    }

    private static SimpleModule javaTimeModule() {
        SimpleModule javaTimeModule = new SimpleModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(LOCAL_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(LOCAL_DATE_TIME));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(ofPattern("HH:mm:ss")));
        return javaTimeModule;
    }

    public static ObjectMapper getNormalOB() {
        return o;
    }

    public static String writeAsString(Object o) {
        try {
            return o == null ? "" : getNormalOB().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> List<T> readListValue(String jsonStr, Class<T> c) {
        try {
            return getNormalOB().readValue(jsonStr, getCollectionType(ArrayList.class, c));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> List<T> readListValue(byte[] src, Class<T> c) {
        try {
            return getNormalOB().readValue(src, getCollectionType(ArrayList.class, c));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> T readValue(String jsonStr, Class<T> c) {
        try {
            return StringUtils.isBlank(jsonStr) ? null : (T) getNormalOB().readValue(jsonStr, c);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) {
        try {
            return StringUtils.isBlank(jsonStr) ? null : getNormalOB().readValue(jsonStr, valueTypeRef);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> T readValue(byte[] src, Class<T> c) {
        try {
            return (T) getNormalOB().readValue(src, c);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return o.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static Map<String, Object> convertToMap(Object obj) {
        return getNormalOB().convertValue(obj, getNormalOB().getTypeFactory().constructMapType(HashMap.class, String.class, Object.class));
    }

    public static Map<String, Object> readValueAsMap(String jsonStr) {
        return readValueAsMap(jsonStr, Object.class);
    }

    public static <T> Map<String, T> readValueAsMap(String jsonStr, Class<T> valueClass) {
        try {
            return StringUtils.isBlank(jsonStr) ? null : getNormalOB().readValue(jsonStr, getNormalOB().getTypeFactory().constructMapType(HashMap.class, String.class, valueClass));
        } catch (JsonProcessingException e) {
            throw new AssertionError(e);
        }
    }

}
