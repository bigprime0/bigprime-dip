package com.bigprime.vo.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "图片验证码")
public class SysCaptchaVO  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "key")
    private String key;

    @Schema(description = "image base64")
    private String image;
}
