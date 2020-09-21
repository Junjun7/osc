package com.gdufe.osc.entity.request;

import com.gdufe.osc.common.CommonRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author changwenbo
 * @date 2020/9/21 15:51
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListSpiderImgRequest extends CommonRequest {

	@Min(1)
	@NotNull(message = "offset不能为空")
	private int offset;

	@Min(1)
	@NotNull(message = "limit不能为空")
	private int limit;

	// 会继承父类的toString
	// 父类的toString因为是ReflectionToStringBuilder 反射
	// 所以会把子类的字段输出出来
}
