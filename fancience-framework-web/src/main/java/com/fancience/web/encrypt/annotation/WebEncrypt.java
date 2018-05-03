package com.fancience.web.encrypt.annotation;

import java.lang.annotation.*;

/**
 * web 数据加密注解
 * Created by Leonid on 18/3/27.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebEncrypt {

    String value() default "";

}
