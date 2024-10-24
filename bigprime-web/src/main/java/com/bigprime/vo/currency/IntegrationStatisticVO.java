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
public class IntegrationStatisticVO {
    private Integer numberOfTasks;
    private Integer instances;
    private Integer inOperation;
    private Integer successNumbers;
    private Integer numberOfFailures;
}
