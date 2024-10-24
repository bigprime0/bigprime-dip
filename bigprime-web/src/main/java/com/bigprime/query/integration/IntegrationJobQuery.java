package com.bigprime.query.integration;

import com.bigprime.handler.integration.model.IntegrationJobModel;
import com.bigprime.query.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "数据集成请求参数")
public class IntegrationJobQuery extends BaseQuery<IntegrationJobModel> {
}