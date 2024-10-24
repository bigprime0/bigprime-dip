package com.bigprime.source.spi.interfaces;

import cn.hutool.json.JSONObject;
import com.bigprime.source.spi.internals.AbstractColumn;
import com.bigprime.source.spi.model.Response;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public interface IAdapter {
    Response handlerExecute(String content);

    AbstractColumn handlerColumn(ResultSet resultSet);

    default List<JSONObject> formatJson(List<String> headers, List<Object> columnValues) {
        List<JSONObject> columns = new ArrayList<>();

        for (int i = 0; i < columnValues.size(); i++) {
            Object object = columnValues.get(i);
            JSONObject jsonObject = new JSONObject();
            if (object instanceof List) {
                List<Object> list = (ArrayList<Object>) object;
                for (int j = 0; j < list.size(); j++) {
                    jsonObject.put(headers.get(j), list.get(j));
                }
            } else {
                jsonObject.put(headers.get(i), object);
            }
            columns.add(jsonObject);
        }
        return columns;
    }
}
