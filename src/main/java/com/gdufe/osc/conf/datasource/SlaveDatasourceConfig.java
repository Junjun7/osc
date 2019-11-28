package com.gdufe.osc.conf.datasource;

import com.gdufe.osc.entity.DataSourceDO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class SlaveDatasourceConfig extends BaseDatasourceConfig {

	@Bean
	public DataSource slaveDataSource() {
		Properties prop = buildProperties(slaveDatasourceDO());
		System.out.println("Properties = " + prop);
		HikariConfig hikariConfig = new HikariConfig(prop);
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.slave")
	public DataSourceDO slaveDatasourceDO() {
		return new DataSourceDO();
	}
}
