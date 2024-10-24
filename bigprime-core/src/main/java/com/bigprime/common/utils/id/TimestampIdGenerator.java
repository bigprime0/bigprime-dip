package com.bigprime.common.utils.id;

public class TimestampIdGenerator {
    private long lastTimestamp = -1L;
    private int counter = 0;
    private static final int MAX_COUNTER = 9999;

    public synchronized String get() {
        long timestamp = System.currentTimeMillis();

        if (timestamp == lastTimestamp) {
            counter++;
            if (counter > MAX_COUNTER) {
                // 等待下一个毫秒
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis();
                }
                counter = 0;
            }
        } else {
            counter = 0;
            lastTimestamp = timestamp;
        }

        return timestamp + String.format("%04d", counter);
    }
}
