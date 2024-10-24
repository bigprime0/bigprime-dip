package com.bigprime.plugin.manager.constant;

import com.bigprime.plugin.manager.internals.impl.SourcePlugin;
import com.bigprime.plugin.manager.internals.AbstractPlugin;

/**
 * @author lyw
 * @version 1.0
 */
public enum PluginType {
    SOURCE {
        @Override
        public Class<? extends AbstractPlugin> getPluginClass() {
            return SourcePlugin.class;
        }
    };

    public abstract Class<? extends AbstractPlugin> getPluginClass();
}
