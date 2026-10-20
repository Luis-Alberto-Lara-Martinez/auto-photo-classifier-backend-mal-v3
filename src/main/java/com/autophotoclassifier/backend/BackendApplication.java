package com.autophotoclassifier.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BackendApplication {

    static void main() {
        SpringApplication.run(BackendApplication.class);
    }
}
