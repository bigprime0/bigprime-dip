package com.bigprime.source.spi.model;

import com.bigprime.source.spi.constant.DmlStatementType;
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
public class ConvertDmlConfig extends ConvertConfig {
    private DmlStatementType type;
    private List<ConvertDmlColumnModel> columns;
    private List<ConvertDmlColumnModel> orders;
    private List<ConvertDmlColumnModel> where;
    private Integer limit;
    private Integer offset;
}
