package com.bigprime.plugin.source.natived.redis;

import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.constant.ProtocolType;
import com.bigprime.source.spi.interfaces.IAdapter;
import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceConfig;
import org.apache.commons.lang3.ObjectUtils;

public class RedisSource
        implements Source {
    private RedisConnection connection;
    private Response response;

    @Override
    public String versionStatement() {
        return "PING Version";
    }

    @Override
    public ProtocolType protocol() {
        return ProtocolType.NATIVE;
    }

    @Override
    public ISourceCommand sourceCommand() {
        return new RedisSourceCommand();
    }

    @Override
    public String driver() {
        return "";
    }

    @Override
    public String connectType() {
        return "";
    }

    @Override
    public String connect(SourceConfig config) {
        try {
            this.response = new Response();
            this.connection = new RedisConnection(config, this.response);
            return this.connection.getUrl();
        } catch (Exception ex) {
            this.response.setIsConnected(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
            return "";
        }
    }

    @Override
    public Response execute(String content) {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            this.response = this.connection.getResponse();
            IAdapter processor = new RedisAdapter(this.connection);
            this.response = processor.handlerExecute(content);
        }
        return this.response;
    }

    @Override
    public void destroy() {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            this.connection.destroy();
        }
    }
}