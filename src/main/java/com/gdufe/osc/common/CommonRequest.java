package com.gdufe.osc.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.NotBlank;

/**
 * @author changwenbo
 * @date 2020/9/21 15:48
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonRequest {

	@NotBlank(message = "type不能为空")
	private String type;

	@Override
	public String toString() {
		// 反射，子类可以不用重写toString即可
		return ReflectionToStringBuilder.toString(this);
	}
}
