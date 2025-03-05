package com.bigprime.query.spi;

import com.bigprime.service.spi.SourceDdlType;
import com.bigprime.source.spi.model.ColumnModel;
import com.bigprime.source.spi.model.TableModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "源管理请求参数")
public class SourceDdlQuery {
    private SourceDdlType type;
    private Long databaseId;
    private Boolean isCascade;
    private String name;
    private String columnName;
    private TableModel model;
    private ColumnModel columnModel;
}
