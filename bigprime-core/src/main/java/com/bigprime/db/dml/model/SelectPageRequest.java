package com.bigprime.db.dml.model;

import lombok.Data;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class SelectPageRequest {
    private int page;

    private int limit;

    private List<SelectColumnParam> params;

    private List<String> columnNames;

    private String tableName;
}
