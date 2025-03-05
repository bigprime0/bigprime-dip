package com.bigprime.db.dml.model;

import lombok.Data;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class SelectPageResult<T> {
    private int page = 1;

    private int limit = 10;

    private long total;

    private List<T> list;
}
