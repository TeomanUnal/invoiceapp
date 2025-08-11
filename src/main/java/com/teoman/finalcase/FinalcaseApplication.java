package com.teoman.finalcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.teoman"})
@EntityScan(basePackages = {"com.teoman"})
@EnableJpaRepositories(basePackages = {"com.teoman"})
@SpringBootApplication
public class FinalcaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalcaseApplication.class, args);
    }

}
