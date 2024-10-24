package com.bigprime.source.spi.interfaces.impl;

import com.bigprime.source.spi.interfaces.IAdapter;
import com.bigprime.source.spi.internals.AbstractColumn;
import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.internals.impl.JdbcColumn;
import com.bigprime.source.spi.internals.impl.JdbcConnection;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.Time;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JdbcAdapter
        implements IAdapter {

    protected AbstractConnection connection;

    public JdbcAdapter(AbstractConnection connection) {
        this.connection = connection;
    }

    @Override
    public AbstractColumn handlerColumn(ResultSet resultSet) {
        return new JdbcColumn(resultSet);
    }

    @Override
    public Response handlerExecute(String content) {
        JdbcConnection jdbcConnection = (JdbcConnection) this.connection;
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = jdbcConnection.getResponse();
        Connection connection = (Connection) jdbcConnection.getConnection();
        if (response.getIsConnected()) {
            try (Statement statement = connection.createStatement()) {
                List<String> headers = new ArrayList<>();
                List<Object> columns = new ArrayList<>();
                try (ResultSet resultSet = statement.executeQuery(content)) {
                    AbstractColumn jdbcColumn = handlerColumn(resultSet);
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        headers.add(columnName);
                    }
                    while (resultSet.next()) {
                        List<Object> _columns = new ArrayList<>();
                        for (int i = 1; i <= columnCount; i++) {
                            _columns.add(jdbcColumn.mappingColumnData(metaData.getColumnTypeName(i), i));
                        }
                        columns.add(_columns);
                    }
                } catch (SQLException tryUpdateEx) {
                    if (Objects.equals(tryUpdateEx.getSQLState(), "S1009")) {
                        try {
                            headers.add("result");
                            List<Object> _columns = new ArrayList<>();
                            connection.setAutoCommit(false);
                            String[] parts = content.replaceAll("[\\r\\n|\\r|\\n]+", " ")
                                    .split("(?<=\\);)|(?<=\\r\\n)|(?<=\\r)|(?<=\\n)|(?<=\\n;)|(?<=\\r;)|(?<=\\r\\n;)|(?<=;)");
                            int count = 0;
                            for (String part : parts) {
                                if (!part.trim().isEmpty()) {
                                    count += statement.executeUpdate(part);
                                }
                            }
                            _columns.add(count);
                            connection.commit();
                            columns.add(_columns);
                        } catch (SQLException updateEx) {
                            try {
                                connection.rollback();
                            } catch (SQLException rollbackEx) {
                                log.error("Rollback failed ", rollbackEx);
                                throw new SQLException(rollbackEx);
                            }
                            throw new SQLException(updateEx);
                        }
                    } else {
                        throw new SQLException(tryUpdateEx);
                    }
                } finally {
                    response.setIsSuccessful(Boolean.TRUE);
                    response.setHeaders(headers);
                    response.setColumns(formatJson(headers, columns));
                }
            } catch (Exception ex) {
                log.error("Execute content failed content {} exception ", content, ex);
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        jdbcConnection.destroy();
        return response;
    }
}
