package com.bigprime.vo.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.bigprime.common.utils.DateUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "用户管理返回参数")
public class SysUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "姓名", required = true)
    @NotBlank(message = "姓名不能为空")
    private String realName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "性别 0：男   1：女   2：未知", required = true)
    private Integer gender;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "机构ID", required = true)
    @NotNull(message = "机构ID不能为空")
    private Long orgId;

    @Schema(description = "状态 0：停用    1：正常", required = true)
    private Integer status;

    @Schema(description = "角色ID列表")
    private List<Long> roleIdList;

    @Schema(description = "超级管理员   0：否   1：是")
    private Integer superAdmin;

    @Schema(description = "组织名称")
    private String orgName;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createTime;

    @Schema(description = "头像url")
    private String avatarUrl;
}
