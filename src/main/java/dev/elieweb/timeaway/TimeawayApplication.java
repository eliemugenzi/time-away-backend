package dev.elieweb.timeaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "dev.elieweb.timeaway")
@EnableJpaRepositories(basePackages = "dev.elieweb.timeaway")
public class TimeawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeawayApplication.class, args);
    }

} 