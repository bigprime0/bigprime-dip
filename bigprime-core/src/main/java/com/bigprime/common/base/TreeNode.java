package com.bigprime.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class TreeNode<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Schema(description = "id")
    private Long id;
    /**
     * 上级ID
     */
    @Schema(description = "上级ID")
    @NotNull(message = "上级ID不能为空")
    private Long pid;

    @Schema(description = "上级名称")
    private String pidName;

    /**
     * 子节点列表
     */
    private List<T> children = new ArrayList<>();
}
