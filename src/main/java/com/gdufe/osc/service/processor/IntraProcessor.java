package com.gdufe.osc.service.processor;

import com.gdufe.osc.common.OscResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author changwenbo
 * @date 2021/2/8 16:11
 */
@Slf4j
public class IntraProcessor {

	public static <T> OscResult<T> process(ProcessExecutor<T> executor) throws Exception {

		T execute = null;
		try {
			execute = executor.execute();
		} catch (Exception e) {
			log.error("e = {}", e);
			throw e;
		}
		return new OscResult<T>().success(execute);
	}

}
