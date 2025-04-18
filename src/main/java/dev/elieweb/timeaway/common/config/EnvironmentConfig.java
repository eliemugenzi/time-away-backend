package dev.elieweb.timeaway.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class EnvironmentConfig {
    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    @Value("${sendgrid.from-name}")
    private String fromName;

    public boolean isProduction() {
        return "prod".equals(activeProfile);
    }

    public boolean isStaging() {
        return "staging".equals(activeProfile);
    }

    public boolean isDevelopment() {
        return !isProduction() && !isStaging();
    }
} 