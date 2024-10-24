package com.bigprime.source.spi.internals;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractColumn
{
    protected final ResultSet resultSet;

    public AbstractColumn(ResultSet resultSet)
    {
        this.resultSet = resultSet;
    }

    public abstract Object mappingColumnData(String columnType, Integer columnIndex)
            throws SQLException;

    public abstract String mappingColumnType(String columnName)
            throws SQLException;

    public abstract List<String> getColumnTypes();
}
