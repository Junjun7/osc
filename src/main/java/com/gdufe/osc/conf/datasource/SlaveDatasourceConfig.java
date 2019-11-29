package com.gdufe.osc.conf.datasource;

import com.gdufe.osc.entity.DataSourceDO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = SlaveDatasourceConfig.SLAVE_PACKAGE,
		sqlSessionFactoryRef = SlaveDatasourceConfig.SQL_SESSION_FACTORY_NAME)
public class SlaveDatasourceConfig extends BaseDatasourceConfig {

	private static final String MAPPER_LOCATIONS = "classpath:mybatis/mapper/slave/*.xml";
	public static final String SLAVE_PACKAGE = "com.gdufe.osc.dao.mapper.slave";
	public static final String SQL_SESSION_FACTORY_NAME = "slaveSqlSessionFactory";

	@Bean("slaveDataSource")
	public DataSource slaveDataSource() {
		Properties prop = buildProperties(slaveDatasourceDO());
		HikariConfig hikariConfig = new HikariConfig(prop);
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.slave")
	public DataSourceDO slaveDatasourceDO() {
		return new DataSourceDO();
	}

	@Bean(name = "slaveSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("slaveDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
				.getResources(MAPPER_LOCATIONS));
		return sessionFactoryBean.getObject();
	}
}
