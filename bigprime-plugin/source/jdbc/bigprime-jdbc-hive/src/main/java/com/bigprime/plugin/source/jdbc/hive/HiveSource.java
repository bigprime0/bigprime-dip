package com.bigprime.plugin.source.jdbc.hive;

import com.bigprime.source.spi.Source;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HiveSource
        implements Source {
    @Override
    public String driver() {
        return "org.apache.hive.jdbc.HiveDriver";
    }

    @Override
    public String connectType() {
        return "hive2";
    }
}
