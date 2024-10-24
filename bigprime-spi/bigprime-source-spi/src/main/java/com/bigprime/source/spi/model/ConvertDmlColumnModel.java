package com.bigprime.source.spi.model;

import com.bigprime.source.spi.constant.DmlOperatorType;
import com.bigprime.source.spi.constant.DmlOrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertDmlColumnModel {
    private String column;
    private String value;
    private String expression;
    private String alias;
    private DmlOperatorType operator;
    private DmlOrderType order;
}
