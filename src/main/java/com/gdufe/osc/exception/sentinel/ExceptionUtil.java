package com.gdufe.osc.exception.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.entity.request.ListSpiderImgRequest;
import com.gdufe.osc.enums.OscResultEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ExceptionUtil {

	/**
	 * 三点注意事项
	 * 1：返回值类型必须与原函数返回值类型一致；
	 * 2：方法参数列表需要和原函数一致，或者可以额外多一个 Throwable 类型的参数用于接收对应的异常。
	 * 3：必须为public static方法
	 * @param request
	 * @param e
	 */
	public static OscResult<List<String>> listSpiderImgV2Exception(ListSpiderImgRequest request, BlockException e) {
		log.info("e = {}", e.getMessage(), e);
		return new OscResult<List<String>>().fail(OscResultEnum.LIMIT_EXCEPTION);
	}
}
