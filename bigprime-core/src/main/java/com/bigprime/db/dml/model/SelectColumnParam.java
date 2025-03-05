package com.bigprime.db.dml.model;

import lombok.Data;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class SelectColumnParam {
    private String columnName;

    private Object columnValue;
}
