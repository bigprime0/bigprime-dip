package com.bigprime.source.spi.model;

import com.bigprime.source.spi.constant.DdlStatementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertDdlConfig extends ConvertConfig {
    private DdlStatementType type;
    private String tableComment;
    private List<ColumnModel> columns;
}
