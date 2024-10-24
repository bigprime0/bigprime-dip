package com.bigprime.common.utils.id;

import java.util.UUID;

public class UUIDGenerator {
    public synchronized String get(int digit){
        UUID uuid = UUID.randomUUID();
        if(digit == 32){
            return uuid.toString().replace("-","");
        }
        return uuid.toString();
    }
}
