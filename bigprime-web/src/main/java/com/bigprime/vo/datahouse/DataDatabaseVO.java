package com.bigprime.vo.datahouse;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bigprime.source.spi.model.SourceConfig;
import com.bigprime.vo.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "数据源")
public class DataDatabaseVO extends BaseVO {
    @Schema(description = "产品")
    private String product;

    @Schema(description = "产品")
    private String productType;

    @Schema(description = "源名称")
    private String name;

    @Schema(description = "描述")
    private String summary;

    @Schema(description = "产品源配置")
    private String config;

    @Schema(description = "配置信息")
    private SourceConfig source;

    public void setSource(SourceConfig source) {
        this.source = source;
        if (this.source != null) {
            this.config = JSONUtil.toJsonStr(this.source);
        }
    }

    public void setConfig(String config) {
        this.config = config;
        if (!StrUtil.isBlank(config)) {
            if (source == null) {
                this.source = JSONUtil.toBean(config, SourceConfig.class, true);
            } else {
                this.config = null;
            }
        }
    }

    private Boolean active = false;
}
