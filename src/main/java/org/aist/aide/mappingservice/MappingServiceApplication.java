package org.aist.aide.mappingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class MappingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MappingServiceApplication.class, args);
	}
}
