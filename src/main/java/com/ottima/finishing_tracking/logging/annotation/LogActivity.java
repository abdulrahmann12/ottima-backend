package com.ottima.finishing_tracking.logging.annotation;

import com.ottima.finishing_tracking.logging.enums.ActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogActivity {

    ActionType actionType();

    String entityName();

    String details() default "";
}