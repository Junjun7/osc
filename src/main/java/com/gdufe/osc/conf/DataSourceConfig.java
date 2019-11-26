package com.gdufe.osc.conf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author changwenbo
 * @date 2019/11/25 20:51
 */
@Configuration
public class DataSourceConfig {

	/**
	 * https://blog.csdn.net/hadues/article/details/102567458
	 * 文章链接
	 *
	 * @param dataSourceProperties
	 * @return
	 */
	@Bean
	public DataSource dataSource(DataSourceProperties dataSourceProperties) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(dataSourceProperties.getUrl());
		hikariConfig.setUsername(dataSourceProperties.getUsername());
		hikariConfig.setPassword(dataSourceProperties.getPassword());
		hikariConfig.setDriverClassName(dataSourceProperties.getDriverClassName());
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}
}
