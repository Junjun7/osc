package com.gdufe.osc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: yizhen
 * @Date: 2018/12/17 15:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

	private Integer replyCount;
	private Integer msgCount;
	private Integer fansCount;
	private Integer referCount;
}
