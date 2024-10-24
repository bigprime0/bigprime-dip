package com.bigprime.plugin.source.http.clickhouse;

import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.constant.ProtocolType;
import com.bigprime.source.spi.interfaces.IAdapter;
import com.bigprime.source.spi.internals.impl.HttpConnection;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceConfig;
import com.bigprime.source.spi.model.SourceHttpConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
public class ClickHouseSource
        implements Source {
    private SourceHttpConfig sourceHttpConfig;
    private HttpConnection connection;
    private Response response;

    @Override
    public ProtocolType protocol() {
        return ProtocolType.HTTP;
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
            this.sourceHttpConfig = new SourceHttpConfig();
            BeanUtils.copyProperties(this.sourceHttpConfig, config);
            this.connection = new ClickHouseConnection(this.sourceHttpConfig, this.response);
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
            IAdapter processor = new ClickHouseAdapter(this.connection);
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
