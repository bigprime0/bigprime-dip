package com.bigprime.source.spi.model;

import com.bigprime.source.spi.annotation.MetadataConfig;
import lombok.*;

import java.util.Map;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SourceConfig {
    @MetadataConfig(zh = "驱动类", seq = 1)
    private String database;
    @MetadataConfig(zh = "数据库类型", seq = 2)
    private String jdbcType;
    @MetadataConfig(zh = "协议", seq = 3)
    private String protocol;
    @MetadataConfig(zh = "类型", seq = 4)
    private String type;
    @MetadataConfig(zh = "模式", seq = 5)
    private String schema;
    @MetadataConfig(zh = "版本", seq = 6)
    private String version;
    @MetadataConfig(zh = "使用SSL", seq = 7)
    private Boolean ssl;
    @MetadataConfig(zh = "主机", seq = 8)
    private String host;
    @MetadataConfig(zh = "端口", seq = 9)
    private Integer port;
    @MetadataConfig(zh = "驱动类", seq = 10)
    private String jdbcDriver;
    @MetadataConfig(zh = "连接地址", seq = 11)
    private String url;
    @MetadataConfig(zh = "用户名", seq = 12)
    private String username;
    @MetadataConfig(zh = "密码", seq = 13)
    private String password;
    private Map<String, Object> configures;
}
