package com.bigprime.vo;

import com.bigprime.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class BaseVO {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "修改人ID")
    public Long  updater;

    @Schema(description = "修改人")
    private String updaterName;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date updateTime;

    @Schema(description = "创建人ID")
    public Long  creator;

    @Schema(description = "创建人")
    private String creatorName;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createTime;
}
