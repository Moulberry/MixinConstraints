package com.moulberry.mixinconstraints.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Repeatable(IfModAbsents.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface IfModAbsent {

    String value();
    String[] aliases() default {};
    String minVersion() default "";
    String maxVersion() default "";
    boolean minInclusive() default true;
    boolean maxInclusive() default true;

}
