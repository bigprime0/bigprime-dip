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
public class IndexModel {
    @MetadataConfig(zh = "表名", seq = 1, isShow = false)
    private String tableName;
    @MetadataConfig(zh = "索引名", seq = 2)
    private String indexName;
    @MetadataConfig(zh = "索引列", seq = 3)
    private String columnName;
    @MetadataConfig(zh = "类型", seq = 4)
    private String indexType;
    @MetadataConfig(zh = "描述", seq = 5)
    private String indexComment;
    @MetadataConfig(zh = "普通索引", seq = 6)
    private Boolean nonUnique;
    @MetadataConfig(zh = "索引序", seq = 7)
    private Long seqIndex = 0L;
    private Map<Object, Object> property;
}
