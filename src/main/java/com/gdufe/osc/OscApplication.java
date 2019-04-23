package com.gdufe.osc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@MapperScan(value = OscApplication.PACKAGE)
public class OscApplication {

	public static final String PACKAGE = "com.gdufe.osc.dao.mapper";

	public static void main(String[] args) {

		SpringApplication.run(OscApplication.class, args);
	}
}
