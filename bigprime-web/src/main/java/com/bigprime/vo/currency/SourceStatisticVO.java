package com.bigprime.vo.currency;

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
@AllArgsConstructor
@NoArgsConstructor
public class SourceStatisticVO {
    private String name;
    private String sourceType;
    private Double volume = 0.00;
    private Integer tableQuantity = 0;
    private Long rows = 0L;
}
