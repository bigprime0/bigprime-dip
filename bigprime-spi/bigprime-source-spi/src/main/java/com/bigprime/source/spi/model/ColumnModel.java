package com.bigprime.source.spi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnModel {
    private String tableName;
    private String column;
    private Long position;
    private String defaultValue;
    private Boolean isNullable;
    private String dataType;
    private Long precision;
    private Long scale;
    private Long maximumLength;
    private String collation;
    private Boolean isKey;
    private String privileges;
    private String extra;
    private String comment;
    private Map<Object,Object> property;
}
