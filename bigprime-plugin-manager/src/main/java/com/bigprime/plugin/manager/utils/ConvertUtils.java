package com.bigprime.plugin.manager.utils;

import cn.hutool.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
public class ConvertUtils {

    public static <T> List<T> getModels(List<JSONObject> objects, Class<T> clazz) {
        List<T> models = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> fieldNameMap = new HashMap<>();
        Field propertyField = null;

        for (Field field : fields) {
            fieldNameMap.put(field.getName(), field);
            if ("property".equals(field.getName())) {
                propertyField = field;
            }
        }

        for (JSONObject object : objects) {
            T model = null;
            try {
                model = clazz.newInstance();
                Map<String, Object> extraProperties = new HashMap<>();

                for (String key : object.keySet()) {
                    Field field = fieldNameMap.get(key);
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(model, object.get(key, field.getType()));
                    } else {
                        extraProperties.put(key, object.get(key));
                    }
                }

                if (propertyField != null && !extraProperties.isEmpty()) {
                    propertyField.setAccessible(true);
                    propertyField.set(model, extraProperties);
                }

                models.add(model);
            } catch (Exception e) {
                throw new RuntimeException("Error creating model from JSON", e);
            }
        }

        return models;
    }
}
