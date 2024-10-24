package com.bigprime.plugin.manager.model;

import com.bigprime.plugin.manager.constant.PluginType;
import com.bigprime.plugin.manager.internals.AbstractPlugin;
import lombok.*;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
public class PluginModel {
    @NonNull
    private PluginType type;

    @NonNull
    private Object id;

    @NonNull
    private AbstractPlugin instance;
}
