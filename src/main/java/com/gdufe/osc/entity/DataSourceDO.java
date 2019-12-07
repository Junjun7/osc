package com.gdufe.osc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceDO {
	/**
	 * 数据源链接地址
	 */
	private String url;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 驱动类名，后面通过反射生成驱动对象
	 */
	private String driverClassName;
}
