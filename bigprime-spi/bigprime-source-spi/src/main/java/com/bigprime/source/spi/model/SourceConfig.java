package com.bigprime.source.spi.model;

import lombok.*;

import java.util.Map;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SourceConfig
{
    private String protocol;
    private String type;
    private String host;
    private Integer port;
    private String jdbcDriver;
    private String jdbcType;
    private String url;
    private String schema;
    private String username;
    private String password;
    private String database;
    private String version;
    private Map<String, Object> configures;
    private Boolean ssl;
}
