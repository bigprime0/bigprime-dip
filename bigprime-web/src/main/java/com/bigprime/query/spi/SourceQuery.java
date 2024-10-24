package com.bigprime.query.spi;

import com.bigprime.source.spi.model.ConvertDmlConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "源管理请求参数")
public class SourceQuery {
    private Long databaseId;
    private List<String> cryptoColumns = new ArrayList<>();
    private Long cryptoId;
    private ConvertDmlConfig dmlConfig;
    private String sql;
}
