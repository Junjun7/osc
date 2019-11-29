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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = MasterDatasourceConfig.MASTER_PACKAGE,
		sqlSessionFactoryRef = MasterDatasourceConfig.SQL_SESSION_FACTORY_NAME)
public class MasterDatasourceConfig extends BaseDatasourceConfig {

	private static final String MAPPER_LOCATIONS = "classpath:mybatis/mapper/master/*.xml";
	public static final String MASTER_PACKAGE = "com.gdufe.osc.dao.mapper.master";
	public static final String SQL_SESSION_FACTORY_NAME = "masterSqlSessionFactory";

	@Primary
	@Bean(name = "masterDataSource")
	public DataSource masterDataSource() {
		Properties prop = buildProperties(masterDatasourceDO());
		HikariConfig hikariConfig = new HikariConfig(prop);
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.master")
	public DataSourceDO masterDatasourceDO() {
		return new DataSourceDO();
	}

	@Primary
	@Bean(name = "masterSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
				.getResources(MAPPER_LOCATIONS));
		return sessionFactoryBean.getObject();
	}
}
