package com.bigprime.annotation;

import com.bigprime.common.constant.FieldControlEnum;
import com.bigprime.db.ddl.internals.TypeAbstract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lyw
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldDescription {
    String description() default "";
    FieldControlEnum fieldControl() default FieldControlEnum.STRING;
    Class<? extends TypeAbstract> type() default TypeAbstract.class;
    boolean canUpdate() default false;
    boolean isProperties() default false;
    boolean isShow() default true;
    int sort() default 0;
}
