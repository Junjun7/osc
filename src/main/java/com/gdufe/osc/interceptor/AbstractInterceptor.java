package com.gdufe.osc.interceptor;

import com.google.common.base.Joiner;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author changwenbo
 * @date 2021/3/31 18:01
 */
public abstract class AbstractInterceptor implements HandlerInterceptor {

	public String join(String spilt, String... vars) {
		return Joiner.on(spilt).join(vars);
	}

	public String getRequestParam(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuilder sb = new StringBuilder("[");
		for (Map.Entry<String, String[]> mp : paramMap.entrySet()) {
			String key = mp.getKey();
			String[] values = mp.getValue();
			String value = Joiner.on(",").join(values);
			sb.append(key + " = " + value);
			sb.append(";");
		}
		sb.append("]");
		return sb.toString();
	}
}
