package com.gdufe.osc.conf.datasource;

import com.gdufe.osc.entity.DataSourceDO;

import java.util.Properties;

public class BaseDatasourceConfig {

	/**
	 * 构建properties
	 * @param dataSourceDO
	 * @return
	 */
	protected Properties buildProperties(DataSourceDO dataSourceDO) {
		Properties prop = new Properties();
		// HikariConfig必须为jdbcUrl而不是url，注意一下
		prop.put("jdbcUrl", dataSourceDO.getUrl());
		prop.put("username", dataSourceDO.getUsername());
		prop.put("password", dataSourceDO.getPassword());
		prop.put("driverClassName", dataSourceDO.getDriverClassName());
		return prop;
	}
}
