package com.bigprime.plugin.source.jdbc.redis;

import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.interfaces.impl.JdbcAdapter;
import com.bigprime.source.spi.internals.impl.JdbcConnection;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
public class RedisSource
        implements Source {
    private JdbcConnection jdbcConnection;
    private Response response;

    @Override
    public String versionStatement() {
        return "INFO Server";
    }

    @Override
    public ISourceCommand sourceCommand() {
        return new RedisSourceCommand();
    }

    @Override
    public String driver() {
        return "com.bigprime.driver.redis.driver.redis.RedisDriver";
    }

    @Override
    public String connectType() {
        return "redis";
    }

    @Override
    public String connect(SourceConfig config) {
        try {
            config.setJdbcDriver(this.driver());
            config.setJdbcType(this.connectType());
            this.response = new Response();
            this.jdbcConnection = new JdbcConnection(config, this.response);
            return this.jdbcConnection.getUrl();
        } catch (Exception ex) {
            this.response.setIsConnected(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
            return "";
        }
    }

    @Override
    public Response execute(String content) {
        if (ObjectUtils.isNotEmpty(this.jdbcConnection)) {
            this.response = this.jdbcConnection.getResponse();
            JdbcAdapter processor = new RedisAdapter(this.jdbcConnection);
            this.response = processor.handlerExecute(content);
        }
        return this.response;
    }

    @Override
    public void destroy() {
        if (ObjectUtils.isNotEmpty(this.jdbcConnection)) {
            this.jdbcConnection.destroy();
        }
    }
}
