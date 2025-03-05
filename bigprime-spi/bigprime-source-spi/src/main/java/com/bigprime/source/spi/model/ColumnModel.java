package com.bigprime.source.spi.model;

import com.bigprime.source.spi.annotation.MetadataConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnModel {
    @MetadataConfig(zh = "表名",seq = 1,isShow = false)
    private String tableName;
    @MetadataConfig(zh = "列名",seq = 2)
    private String column;
    @MetadataConfig(zh = "描述",seq = 4)
    private String comment;
    @MetadataConfig(zh = "位置",seq = 3)
    private Long position;
    @MetadataConfig(zh = "默认值",seq = 5)
    private String defaultValue;
    @MetadataConfig(zh = "是否为空",seq = 6)
    private Boolean isNullable;
    @MetadataConfig(zh = "类型",seq = 7)
    private String dataType;
    @MetadataConfig(zh = "精度",seq = 8)
    private Long precision;
    @MetadataConfig(zh = "小数位数",seq = 9)
    private Long scale;
    @MetadataConfig(zh = "长度",seq = 10)
    private Long maximumLength;
    @MetadataConfig(zh = "排列规则",seq = 11)
    private String collation;
    @MetadataConfig(zh = "是否主键",seq = 12)
    private Boolean isKey;
    @MetadataConfig(zh = "权限",seq = 13)
    private String privileges;
    @MetadataConfig(zh = "生成规则",seq = 14)
    private String extra;
    private Map<Object,Object> property;
}
