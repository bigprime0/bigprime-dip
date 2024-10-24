package com.bigprime.source.spi.model;

import com.bigprime.source.spi.constant.HttpMethodType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import okhttp3.MediaType;

import java.util.Map;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class SourceHttpConfig
        extends SourceConfig
{
    private String path;
    private HttpMethodType method;
    private Integer retry = 0;
    private Boolean autoConnected = Boolean.FALSE;
    private Map<String, String> params;
    private String jsonBody;
    private boolean isDecoded = false;
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
}
