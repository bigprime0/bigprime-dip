package com.bigprime.plugin.source.jdbc.mongo;

import com.bigprime.source.spi.Source;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MongoSource
        implements Source {
    @Override
    public String name() {
        return "MongoDB";
    }

    @Override
    public String driver() {
        return "com.mongodb.jdbc.MongoDriver";
    }

    @Override
    public String connectType() {
        return "mongodb";
    }
}
