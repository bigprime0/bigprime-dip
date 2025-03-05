package com.bigprime.common.constant;

/**
 * @author lyw
 * @version 1.0
 */
public enum FieldControlEnum {
    STRING("文本类型"),
    NUMBER("数字类型"),
    BOOLEAN("布尔类型"),
    FIELDS("选择列信息"),
    LIST("下拉列表");

    private String message;

    FieldControlEnum(String message){
        this.message = message;
    }
}
