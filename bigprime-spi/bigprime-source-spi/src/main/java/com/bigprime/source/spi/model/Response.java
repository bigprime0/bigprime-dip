package com.bigprime.source.spi.model;

import cn.hutool.json.JSONObject;
import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response
{
    private List<String> headers;
    private List<JSONObject> columns;
    private Boolean isConnected = Boolean.FALSE;
    private Boolean isSuccessful = Boolean.FALSE;
    private String message;
    private Time connection;
    private Time processor;
    private String content;
    private Long totalRecords;
}
