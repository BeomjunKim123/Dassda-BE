package com.dassda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DassdaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DassdaApplication.class, args);
	}

}
