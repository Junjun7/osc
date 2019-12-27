package com.gdufe.osc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试机制
 * 比如调HTTP，超时重试。
 * 调RPC，失败重试等等
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Retry {

	int maxRetry() default 1;

	Class<? extends RuntimeException> ex() default RuntimeException.class;
}
