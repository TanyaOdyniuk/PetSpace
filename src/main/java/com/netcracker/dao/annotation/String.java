package com.netcracker.dao.annotation;

/**
 * Created by Odyniuk on 12/11/2017.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface String {
    int attr_id();
}
