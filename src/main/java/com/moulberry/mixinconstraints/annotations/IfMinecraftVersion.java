package com.moulberry.mixinconstraints.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface IfMinecraftVersion {

    String minVersion() default "";
    String maxVersion() default "";
    boolean negate() default false;

}
