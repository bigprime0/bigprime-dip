package com.bigprime.vo.currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticVO {
    private IntegrationStatisticVO integration;
    private List<SourceStatisticVO> source;
}
