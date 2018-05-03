package com.fancience.web.logger.annotation;

import com.fancience.web.logger.em.LogLevel;
import com.fancience.web.logger.em.LogType;

import java.lang.annotation.*;

/**
 * web日志纪录
 * Created by Leonid on 18/3/21.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {
    // 日志名称
    String value() default "";
    // 日志类型  默认为"系统类型"
    LogType type() default LogType.User;
    // 日志关注度等级   默认为 "低"
    LogLevel level() default LogLevel.Low;
    // 是否是报错信息
    boolean isError() default false;
}
