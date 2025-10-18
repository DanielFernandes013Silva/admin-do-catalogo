package com.admin.catologo.infrastructure;

import com.admin.catologo.infrastructure.config.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
     public static void main(String[] args) {
        System.out.println("Infrastructure module");
        SpringApplication.run(WebServerConfig.class, args);
    }
}
