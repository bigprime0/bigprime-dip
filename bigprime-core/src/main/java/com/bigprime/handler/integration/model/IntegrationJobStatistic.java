package com.bigprime.handler.integration.model;

import com.bigprime.handler.integration.model.proxy.IntegrationJobStatisticProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

@Data
@EntityProxy
public class IntegrationJobStatistic implements ProxyEntityAvailable<IntegrationJobStatistic , IntegrationJobStatisticProxy> {
    private Integer type;
    private String status;
    private Integer count;
}
