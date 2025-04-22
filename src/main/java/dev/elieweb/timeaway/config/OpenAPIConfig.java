package dev.elieweb.timeaway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Elie Mugenzi",
            email = "eliemugenzi@gmail.com",
            url = "https://elieweb.dev"
        ),
        description = "OpenApi documentation for TimeAway API",
        title = "OpenApi specification - TimeAway",
        version = "1.0",
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        ),
        termsOfService = "Terms of service"
    ),
    security = {
        @SecurityRequirement(name = "Bearer Authentication")
    },
    servers = {
        @Server(
            description = "Local ENV",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Docker Local ENV",
            url = "http://localhost:8083"
        ),
        @Server(
            description = "PROD ENV",
            url = "https://time-away-backend-production.up.railway.app"
        )
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
} 