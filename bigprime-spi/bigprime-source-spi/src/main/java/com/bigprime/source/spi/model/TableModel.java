package com.bigprime.source.spi.model;

import com.bigprime.source.spi.annotation.MetadataConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableModel {
    @MetadataConfig(zh = "名称", seq = 1)
    private String name;

    @MetadataConfig(zh = "类型", seq = 2)
    private String type;

    @MetadataConfig(zh = "目录", seq = 3)
    private String catalog;

    @MetadataConfig(zh = "模式", seq = 4)
    private String schema;

    @MetadataConfig(zh = "引擎", seq = 5)
    private String engine;

    @MetadataConfig(zh = "格式", seq = 6)
    private String format;

    @MetadataConfig(zh = "记录数", seq = 7)
    private Long rows;

    @MetadataConfig(zh = "创建时间", seq = 8)
    private Date createTime;

    @MetadataConfig(zh = "更新时间", seq = 9)
    private Date updateTime;

    @MetadataConfig(zh = "排序规则", seq = 10)
    private String collation;

    @MetadataConfig(zh = "表注释", seq = 11)
    private String comment;

    @MetadataConfig(zh = "平均行长度", seq = 12)
    private Long avgRowLength;

    @MetadataConfig(zh = "数据长度", seq = 13)
    private Long dataLength;

    @MetadataConfig(zh = "索引长度", seq = 14)
    private Long indexLength;

    private Map<Object, Object> property;

    @MetadataConfig(zh = "列信息")
    private List<ColumnModel> columns;

    @MetadataConfig(zh = "列类型", isShow = false)
    private List<String> columnTypes;

    @MetadataConfig(zh = "索引信息")
    private List<IndexModel> indexes;

    @MetadataConfig(zh = "索引类型", isShow = false)
    private List<String> indexTypes;
}
