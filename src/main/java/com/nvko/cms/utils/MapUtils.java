package com.nvko.cms.utils;

import com.nvko.cms.exception.ConfigurationException;

import java.util.Map;

import static io.micrometer.common.util.StringUtils.isBlank;
import static java.util.Objects.isNull;

public class MapUtils {

    public static String getNestedValue(Map<String, Object> payload, String fieldPath) {
        if (isBlank(fieldPath)) {
            throw new ConfigurationException("Field name missing from the configuration!");
        }
        if (isNull(payload)) {
            throw new ConfigurationException("No request body was found");
        }
        final String[] keys = fieldPath.split("\\.");
        Object value = payload;
        for (String key : keys) {
            if (!(value instanceof Map)) {
                throw new ConfigurationException("Wrong data type sent!");
            }
            value = ((Map<?, ?>) value).get(key);
            if (value == null) {
                throw new ConfigurationException("Field is missing from the request body!");
            }
        }
        return (String) value;
    }


}
