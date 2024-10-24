package com.bigprime.common.utils;

import com.bigprime.common.utils.id.SeqIdGenerator;
import com.bigprime.common.utils.id.SnowflakeIdGenerator;
import com.bigprime.common.utils.id.TimestampIdGenerator;
import com.bigprime.common.utils.id.UUIDGenerator;

public class IdUtils {
    public static String getUUId(boolean Is32) {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        return Is32 ? uuidGenerator.get(32) : uuidGenerator.get(64);
    }

    public static String getTSId() {
        TimestampIdGenerator timestampIdGenerator = new TimestampIdGenerator();
        return timestampIdGenerator.get();
    }

    public static long getSnowId() {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1);
        long id = snowflakeIdGenerator.get();
        return id;
    }

    public static int getSeqId() {
        return SeqIdGenerator.get();
    }
}
