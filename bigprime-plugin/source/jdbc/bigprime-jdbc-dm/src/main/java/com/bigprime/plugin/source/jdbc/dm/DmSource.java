package com.bigprime.plugin.source.jdbc.dm;

import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.interfaces.impl.JdbcAdapter;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
public class DmSource
        implements Source {
    private DmConnection connection;
    private Response response;

    @Override
    public String name() {
        return "Dameng";
    }

    @Override
    public String driver() {
        return "dm.jdbc.driver.DmDriver";
    }

    @Override
    public String connectType() {
        return "dm";
    }

    @Override
    public String connect(SourceConfig config) {
        try {
            config.setJdbcDriver(this.driver());
            config.setJdbcType(this.connectType());
            this.response = new Response();
            this.connection = new DmConnection(config, this.response);
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
            JdbcAdapter processor = new DmAdapter(this.connection);
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
