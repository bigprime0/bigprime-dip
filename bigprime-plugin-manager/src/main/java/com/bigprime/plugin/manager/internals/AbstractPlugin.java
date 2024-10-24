package com.bigprime.plugin.manager.internals;

import com.bigprime.plugin.manager.aspect.PluginAspect;
import com.bigprime.plugin.manager.constant.AspectType;
import lombok.NonNull;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author lyw
 * @version 1.0
 */
public abstract class AbstractPlugin<T extends AbstractPlugin, Plugin> {
    protected Plugin plugin;
    protected PluginAspect aspect;

    public void setAspect(PluginAspect aspect) {
        this.aspect = aspect;
    }

    public void initPlugin() {
        this.plugin = plugin();
    }

    public abstract Plugin plugin();

    public final T aspectType(@NonNull AspectType type) {
        aspect.setType(type);
        return (T) this;
    }

    public final T before(Function before) {
        aspect.setBefore(before);
        return (T) this;
    }

    public final T after(Consumer after) {
        aspect.setAfter(after);
        return (T) this;
    }

    public final void destroy() {
        aspect.localClear();
    }
}

