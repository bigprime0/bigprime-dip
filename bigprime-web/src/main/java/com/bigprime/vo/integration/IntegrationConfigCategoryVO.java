package com.bigprime.vo.integration;

import com.bigprime.common.base.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "数据集成分类配置")
public class IntegrationConfigCategoryVO extends TreeNode<IntegrationConfigCategoryVO> {

    private String label;

    private String route;

    private String category;

    private int isAdd;

    private int isLeaf;
}
