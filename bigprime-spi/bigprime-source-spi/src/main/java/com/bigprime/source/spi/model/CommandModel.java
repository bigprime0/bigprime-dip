package com.bigprime.source.spi.model;

import lombok.*;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommandModel {
    private Object key;
    private Boolean required = false;
    private Boolean show = true;
    private Object defaultValue;
    private String description;
    private List<CommandCustomModel> commandCustomModels;
}
