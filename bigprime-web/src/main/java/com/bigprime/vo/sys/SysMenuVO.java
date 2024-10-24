package com.bigprime.vo.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.bigprime.common.base.TreeNode;
import com.bigprime.common.utils.DateUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "菜单管理返回参数")
public class SysMenuVO extends TreeNode<SysMenuVO> {

    @Schema(description = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @Schema(description = "菜单URL")
    private String url;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)")
    private String authority;

    @Schema(description = "排序")
    @Min(value = 0, message = "排序值不能小于0")
    private Integer sort;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createTime;

    @Schema(description = "上级菜单名称")
    private String parentName;

}
