package com.gdufe.osc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口花费多少时间的注解
 * 具体实现在：com.gdufe.osc.interceptor.TimeWatchAspect
 * @author: yizhen
 * @date: 2019/1/26 15:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeWatch {
}
