package com.gdufe.osc.utils;

import com.gdufe.osc.OscApplicationTests;
import com.gdufe.osc.exception.NetworkException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author changwenbo
 * @date 2019/11/6 20:31
 */
@Slf4j
public class HttpTest extends OscApplicationTests {

	@Autowired
	private HttpHelper helper;

	/** aop只有在非静态方法才启作用 */
	@Test
	public void testHttpRetry() throws NetworkException {
//		String res = helper.get();
	}
}








