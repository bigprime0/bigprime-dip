package com.bigprime.source.spi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface MetadataConfig {
    /**
     * 显示中文
     *
     * @return
     */
    String zh() default "";

    String en() default "";

    /**
     * 显示顺序
     *
     * @return
     */
    int seq() default 200;

    boolean isShow() default true;
}
