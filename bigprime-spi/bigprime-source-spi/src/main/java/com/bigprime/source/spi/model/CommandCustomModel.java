package com.bigprime.source.spi.model;

import lombok.*;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommandCustomModel {
    private String key;
    private String value;

}
