package com.bigprime.vo.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "账号登录")
public class SysAccountLoginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "唯一key")
    private String key;

    @Schema(description = "验证码")
    private String captcha;

    @Schema(description = "是否需要验证码")
    private boolean checkCaptcha;
}
