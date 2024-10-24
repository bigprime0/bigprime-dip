package com.bigprime.plugin.manager;

import cn.hutool.core.bean.BeanUtil;
import com.bigprime.plugin.manager.aspect.PluginAspect;
import com.bigprime.plugin.manager.constant.PluginType;
import com.bigprime.plugin.manager.internals.AbstractPlugin;
import com.bigprime.plugin.manager.model.PluginModel;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
public class Plugin {

    private static final Map<String, PluginModel> register = new HashMap<>();

    private static String getKey(Object id, String type) {
        return String.format("%s-%s", type, id);
    }

    private static <T extends AbstractPlugin> void registerInstance(PluginModel plugin, Boolean isInstance) {
        if (isInstance) {
            AbstractPlugin instance = plugin.getInstance();
            PluginAspect aspect = new PluginAspect();
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(instance.getClass());
            enhancer.setCallback(aspect);
            T result = (T) enhancer.create();
            BeanUtil.copyProperties(instance, result);
            result.setAspect(aspect);
            result.initPlugin();
            plugin.setInstance(result);
        }
        register.put(getKey(plugin.getId(), plugin.getType().name()), plugin);
    }

    public static void register(PluginModel plugin) {
        if (plugin == null) {
            return;
        }
        registerInstance(plugin, true);
    }

    public static <T extends AbstractPlugin> void unregister(PluginType type, Object id) {
        String key = getKey(id, type.toString());
        if (!register.containsKey(key)) {
            return;
        }
        register.remove(key);
    }

    public static <T extends AbstractPlugin> T getInstance(PluginType type, Object id) {
        String key = getKey(id, type.name());
        if (!register.containsKey(key)) {
            return null;
        }
        return (T) register.get(key).getInstance();
    }

    public static <T extends AbstractPlugin> T getPlugin(PluginType type) {
        String key = getKey(type.name(), type.name());
        if (!register.containsKey(key)) {
            Constructor<?> constructor = null;
            try {
                Class<?> clazz = type.getPluginClass();
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            AbstractPlugin plugin = null;
            try {
                plugin = (AbstractPlugin) constructor.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            registerInstance(new PluginModel(type, type.name(), plugin), false);
        }
        return (T) register.get(key).getInstance();
    }
}
