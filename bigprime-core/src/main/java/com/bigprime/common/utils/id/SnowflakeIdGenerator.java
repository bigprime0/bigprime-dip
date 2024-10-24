package com.bigprime.common.utils.id;

public class SnowflakeIdGenerator {
    private final long twepoch = 1627824000000L; // 自定义起始时间戳
    private final long nodeId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private static final long nodeIdBits = 10L;
    private static final long maxNodeId = ~(-1L << nodeIdBits);
    private static final long sequenceBits = 12L;
    private static final long nodeIdShift = sequenceBits;
    private static final long timestampLeftShift = sequenceBits + nodeIdBits;
    private static final long sequenceMask = ~(-1L << sequenceBits);

    public SnowflakeIdGenerator(long nodeId) {
        if (nodeId > maxNodeId || nodeId < 0) {
            throw new IllegalArgumentException(String.format("Node ID must be between 0 and %d", maxNodeId));
        }
        this.nodeId = nodeId;
    }

    public synchronized long get() {
        long timestamp = currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = waitUntilNextMillis(timestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (nodeId << nodeIdShift) | sequence;
    }

    private long waitUntilNextMillis(long currentMillis) {
        long timestamp = currentTimeMillis();
        while (timestamp <= currentMillis) {
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }

    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
