package org.example.spothofra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "org.example.config",
        "org.example.controller",
        "org.example.security",
        "org.example.service",
        "org.example.service.impl",
        "org.example.util"
})
@EntityScan(basePackages = "org.example.entity")
@EnableJpaRepositories(basePackages = "org.example.repository")
public class SpotHofraApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotHofraApplication.class, args);
    }
}
