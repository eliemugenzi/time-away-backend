package dev.elieweb.timeaway;

import dev.elieweb.timeaway.leave.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "dev.elieweb.timeaway")
@EnableJpaRepositories(basePackages = "dev.elieweb.timeaway")
@EnableScheduling
@RequiredArgsConstructor
public class TimeawayApplication {
    private final LeaveBalanceService leaveBalanceService;

    public static void main(String[] args) {
        SpringApplication.run(TimeawayApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeLeaveBalances() {
        return args -> {
            leaveBalanceService.initializeLeaveBalancesForExistingUsers();
        };
    }
} 