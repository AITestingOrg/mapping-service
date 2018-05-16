package org.aist.aide.mappingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MappingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MappingServiceApplication.class, args);
    }
}
