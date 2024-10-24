package com.bigprime.driver.redis.core;

import java.sql.SQLException;

public interface RedisClient
{
    String[] sendCommand(String sql)
            throws SQLException;

    void select(int dbIndex)
            throws SQLException;

    void close();
}
