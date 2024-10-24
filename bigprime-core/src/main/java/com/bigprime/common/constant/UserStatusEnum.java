package com.bigprime.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lyw
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    /**
     * 停用
     */
    DISABLE(0),
    /**
     * 正常
     */
    ENABLED(1);

    private final int value;
}
