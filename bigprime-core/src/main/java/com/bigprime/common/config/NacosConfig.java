package com.bigprime.common.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bigprime.handler.database.model.DataDatabaseModel;
import com.bigprime.source.spi.model.SourceConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lyw
 * @version 1.0
 */
@Component
@Data
public class NacosConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public DataDatabaseModel getDataBaseModel() {
        DataDatabaseModel model = new DataDatabaseModel();
        model.setId(-1L);
        model.setName("bigPrime");
        model.setProduct("MySQL");
        model.setConfig(JSONUtil.toJsonStr(createJdbcParam()));
        return model;
    }

    private SourceConfig createJdbcParam() {
        SourceConfig param = new SourceConfig();
        param.setUrl(appendUseOldAliasMetadataBehavior(url));
        param.setPassword(password);
        param.setUsername(username);
        param.setType("MySQL");
        param.setProtocol("JDBC");
        String dataBase = extractDatabaseName();
        param.setDatabase(dataBase);
        param.setSchema(dataBase);
        return param;
    }

    private String appendUseOldAliasMetadataBehavior(String url) {
        if (url.contains("useOldAliasMetadataBehavior")) {
            return url;
        }
        return url + "&useOldAliasMetadataBehavior=true";
    }

    private String extractDatabaseName() {
        String partAfterProtocol = StrUtil.subAfter(url, "://", true);
        if (partAfterProtocol != null) {
            int queryParamIndex = partAfterProtocol.indexOf('?');
            if (queryParamIndex != -1) {
                partAfterProtocol = partAfterProtocol.substring(0, queryParamIndex);
            }
            String[] parts = partAfterProtocol.split("/");
            if (parts.length > 0) {
                return parts[parts.length - 1];
            }
        }
        return "bigprime_data";
    }
}
