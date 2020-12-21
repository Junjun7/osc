package com.gdufe.osc.service.processor;

/**
 * @author changwenbo
 * @date 2020/9/21 15:32
 */
@FunctionalInterface
public interface ProcessExecutor<T> {

	T execute() throws Exception;
}
