package dev.elieweb.timeaway.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUrlConfig {
    @Value("${app.api.url}")
    private String apiUrl;

    public String getFileUrl(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        
        return String.format("%s/api/v1/files/%s", apiUrl, fileName);
    }
} 