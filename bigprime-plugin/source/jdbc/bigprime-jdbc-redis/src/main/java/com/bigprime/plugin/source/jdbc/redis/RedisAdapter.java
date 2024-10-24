package com.bigprime.plugin.source.jdbc.redis;

import com.bigprime.source.spi.interfaces.impl.JdbcAdapter;
import com.bigprime.source.spi.internals.impl.JdbcColumn;
import com.bigprime.source.spi.internals.impl.JdbcConnection;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.Time;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class RedisAdapter
        extends JdbcAdapter {
    public RedisAdapter(JdbcConnection jdbcConnection) {
        super(jdbcConnection);
    }

    @Override
    public Response handlerExecute(String content) {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.connection.getResponse();
        Connection connection = (Connection) this.connection.getConnection();
        if (response.getIsConnected()) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(content)) {
                List<String> headers = new ArrayList<>();
                List<Object> columns = new ArrayList<>();
                boolean isPresent = true;
                JdbcColumn jdbcColumn = new JdbcColumn(resultSet);
                while (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    List<Object> _columns = new ArrayList<>();
                    if (content.equals("INFO Server")) {
                        headers.add("version");
                        _columns.add(parseRedisVersion(jdbcColumn.mappingColumnData(metaData.getColumnTypeName(1), 1)));
                    } else {
                        int columnCount = metaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            if (isPresent) {
                                String header = metaData.getColumnName(i);
                                headers.add(header);
                            }
                            _columns.add(jdbcColumn.mappingColumnData(metaData.getColumnTypeName(i), i));
                        }
                        isPresent = false;
                    }
                    columns.add(_columns);
                }
                response.setHeaders(headers);
                response.setColumns(formatJson(headers, columns));
                response.setIsSuccessful(Boolean.TRUE);
            } catch (SQLException ex) {
                log.error("Execute content failed content {} exception ", content, ex);
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        this.connection.destroy();
        return response;
    }

    private String parseRedisVersion(Object infoOutput) {
        try {
            String[] lines = infoOutput.toString().split("\n");
            for (String line : lines) {
                if (line.startsWith("redis_version:")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        return parts[1].trim();
                    }
                }
            }
        } catch (Exception ex) {
            log.warn("Convert failed content {} exception {}", infoOutput, ex.getMessage());
        }
        return "-";
    }
}
