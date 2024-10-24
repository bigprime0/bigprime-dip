package com.bigprime.source.spi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbModel {
    private String catalog;
    private String name;
    private String characterSet;
    private String collation;
}
