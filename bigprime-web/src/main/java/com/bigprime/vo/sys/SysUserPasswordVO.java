package com.bigprime.vo.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "用户修改密码")
public class SysUserPasswordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "原密码", required = true)
    @NotBlank(message = "原密码不能为空")
    private String password;

    @Schema(description = "新密码，密码长度为 4-20 位", required = true)
    @Min(value = 4, message = "新密码长度为 4-20 位")
    @Max(value = 20, message = "新密码长度为 4-20 位")
    private String newPassword;
}
