package com.bigprime.plugin.manager.aspect;

import com.bigprime.plugin.manager.annotation.EnableAspect;
import com.bigprime.plugin.manager.constant.AspectType;
import lombok.NonNull;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author lyw
 * @version 1.0
 */
public class PluginAspect implements MethodInterceptor {
    private ThreadLocal<AspectType> type = new ThreadLocal<>();
    private ThreadLocal<Function<Object[], Boolean>> before = new ThreadLocal<>();
    private ThreadLocal<Consumer> after = new ThreadLocal<>();

    public void setBefore(Function<Object[], Boolean> before) {
        this.before.set(before);
    }

    public void setAfter(Consumer after) {
        this.after.set(after);
    }

    public void setType(@NonNull AspectType type) {
        this.type.set(type);
    }

    public void localClear() {
        type.remove();
        before.remove();
        after.remove();
    }

    protected void aspectClear() {
        if(type.get() == null){
            localClear();
            return;
        }
        if (type.get() == AspectType.METHOD) {
            before.remove();
            after.remove();
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        boolean enable = method.isAnnotationPresent(EnableAspect.class);
        boolean execute = true;
        Object result = null;
        if (enable && before.get() != null) {
            execute = before.get().apply(objects);
        }
        if (execute) {
            result = methodProxy.invokeSuper(o, objects);
            if (enable && after.get() != null) {
                after.get().accept(result);
            }
        }
        aspectClear();
        return result;
    }
}
