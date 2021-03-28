package com.gdufe.osc.exception.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionUtil {

	public static void handleException(BlockException e) {

		System.out.println("e = " + e.getMessage());

	}
}
