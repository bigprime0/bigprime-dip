package com.bigprime.common.constant;

/**
 * @author lyw
 * @version 1.0
 */
public enum DataBaseGroupEnum {
    JDBC("jdbc"),
    NOSQL("nosql");

    private String describe;

    public String getDescribe() {
        return describe;
    }

    DataBaseGroupEnum(String describe){
        this.describe = describe;
    }
}
