package com.bigprime.vo.datahouse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class DataHouseGetLayerVO<T> {
    private String label;

    private String ctg;

    private List<DataHouseGetLayerVO> children = new ArrayList<>();

    private T nodeData;

    private String product;

    private Long databaseId;
}
