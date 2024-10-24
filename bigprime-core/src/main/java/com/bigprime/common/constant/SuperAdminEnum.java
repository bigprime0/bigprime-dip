package com.bigprime.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lyw
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum SuperAdminEnum {
    YES(1),
    NO(0);

    private final Integer value;
}
