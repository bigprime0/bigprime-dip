package com.bigprime.common.utils.id;

import java.util.concurrent.atomic.AtomicInteger;

public class SeqIdGenerator {
    private static AtomicInteger counter = new AtomicInteger(0);
    public static int get(){
        return counter.incrementAndGet();
    }
}
