package com.bigprime.source.spi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableModel {
    private String name;
    private String type;
    private String catalog;
    private String schema;
    private String engine;
    private String format;
    private Long rows;
    private Date createTime;
    private Date updateTime;
    private String collation;
    private String comment;
    private Long avgRowLength;
    private Long dataLength;
    private Long indexLength;
    private Map<Object,Object> property;

    private List<ColumnModel> columns;
}
