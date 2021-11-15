package com.example.learningdemo.common.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class StringToStringSetConverter implements Converter<String, Set<String>> {

    @Override
    public Set<String> convert(String source) {
        if (StringUtils.isBlank(source)) {
            return Collections.emptySet();
        }
        return Arrays.stream(source.split(",")).collect(Collectors.toSet());
    }
}
