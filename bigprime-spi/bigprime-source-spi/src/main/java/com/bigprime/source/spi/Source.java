package com.bigprime.source.spi;

import com.bigprime.source.spi.constant.ProtocolType;
import com.bigprime.source.spi.interfaces.IAdapter;
import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.interfaces.impl.DefaultSourceCommand;
import com.bigprime.source.spi.interfaces.impl.HttpAdapter;
import com.bigprime.source.spi.interfaces.impl.JdbcAdapter;
import com.bigprime.source.spi.interfaces.impl.NativeAdapter;
import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.internals.impl.DefaultConvertDdlStatement;
import com.bigprime.source.spi.internals.impl.DefaultConvertDmlStatement;
import com.bigprime.source.spi.internals.impl.JdbcConnection;
import com.bigprime.source.spi.model.ConvertDdlConfig;
import com.bigprime.source.spi.model.ConvertDmlConfig;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Source {
    ThreadLocal<AbstractConnection> local = new ThreadLocal<>();
    Logger log = LoggerFactory.getLogger(Source.class);

    default ProtocolType protocol() {
        return ProtocolType.JDBC;
    }

    default String name() {
        return this.getClass().getSimpleName().replace("Source", "");
    }

    default String description() {
        return String.format("Integrate %s data sources", this.name());
    }

    default String versionStatement() {
        return "SELECT version() AS version";
    }

    default ISourceCommand sourceCommand() {
        return new DefaultSourceCommand();
    }

    default String convertStatement(ConvertDdlConfig config) {
        return new DefaultConvertDdlStatement(config).execute();
    }

    default String convertStatement(ConvertDmlConfig config) {
        return new DefaultConvertDmlStatement(config).execute();
    }

    String driver();

    String connectType();

    default String connect(SourceConfig config) {
        Response response = new Response();
        try {
            config.setJdbcDriver(this.driver());
            config.setJdbcType(this.connectType());
            JdbcConnection connection = new JdbcConnection(config, response);
            local.set(connection);
            return connection.getUrl();
        } catch (Exception ex) {
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
            log.error("Error connecting : {}", ex);
        }
        return "";
    }

    default Response execute(String content) {
        AbstractConnection connection = local.get();
        Response response = new Response();
        if (connection != null) {
            IAdapter adapter;
            if (protocol().equals(ProtocolType.JDBC)) {
                adapter = new JdbcAdapter(connection);
            } else if (protocol().equals(ProtocolType.NATIVE)) {
                adapter = new NativeAdapter(connection);
            } else {
                adapter = new HttpAdapter(connection);
            }
            response = adapter.handlerExecute(content);
        }
        return response;
    }

    default void destroy() {
        AbstractConnection connection = local.get();
        if (connection != null) {
            connection.destroy();
            local.remove();
        }
    }
}
