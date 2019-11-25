package com.gdufe.osc.conf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author changwenbo
 * @date 2019/11/25 20:51
 */
@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource();
	}
}
