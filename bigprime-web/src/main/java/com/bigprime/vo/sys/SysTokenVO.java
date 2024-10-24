package com.bigprime.vo.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@AllArgsConstructor
@Schema(description = "用户登录")
public class SysTokenVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "token")
    private String token;

    @Schema(description = "用户信息")
    private SysUserVO userInfo;
}
