package com.bigprime.plugin.source.jdbc.oracle;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.bigprime.source.spi.internals.impl.JdbcConnection;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OracleConnection
        extends JdbcConnection {
    public OracleConnection(SourceConfig config, Response response) {
        super(config, response);
    }

    @Override
    protected String formatJdbcUrl() {
        SourceConfig sourceConfig = this.getSourceConfig();
        StringBuffer buffer = new StringBuffer();
        buffer.append("jdbc:");
        buffer.append(sourceConfig.getJdbcType());
        buffer.append(":@");
        buffer.append(sourceConfig.getHost());
        buffer.append(":");
        buffer.append(sourceConfig.getPort());
        if (StrUtil.isNotBlank(sourceConfig.getDatabase())) {
            buffer.append("/");
            buffer.append(sourceConfig.getDatabase());
        }
        if (BeanUtil.isNotEmpty(sourceConfig.getConfigures())) {
            Map<String, Object> env = sourceConfig.getConfigures();
            List<String> flatEnv = env.entrySet()
                    .stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
            if (env.size() > 1) {
                buffer.append("?");
            }
            buffer.append(String.join("&", flatEnv));
        }
        return buffer.toString();
    }
}
