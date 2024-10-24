package com.bigprime.plugin.source.jdbc.clickhouse;

import com.bigprime.source.spi.Source;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClickHouseSource
        implements Source {

    @Override
    public String driver() {
        return "com.clickhouse.jdbc.ClickHouseDriver";
    }

    @Override
    public String connectType() {
        return "clickhouse";
    }
}
