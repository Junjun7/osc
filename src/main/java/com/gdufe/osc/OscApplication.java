package com.gdufe.osc;

import com.gdufe.osc.utils.ZKUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OscApplication {

	public static void main(String[] args) {

		SpringApplication.run(OscApplication.class, args);
		ZKUtils.t();
	}
}
