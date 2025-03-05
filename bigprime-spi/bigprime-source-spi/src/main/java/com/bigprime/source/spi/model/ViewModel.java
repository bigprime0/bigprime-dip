package com.bigprime.source.spi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewModel {
    private String viewName;

    private String viewDefinition;

    private String checkOption;

    private String isUpdatable;

    private String characterSet;

    private String collation;

    private Map<Object, Object> property;
}
