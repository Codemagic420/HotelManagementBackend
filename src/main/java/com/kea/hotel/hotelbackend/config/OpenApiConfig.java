package com.kea.hotel.hotelbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI Configuration
 * Enables JWT Bearer token authentication in Swagger UI
 * Provides "Authorize" button in the top-right corner
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Hotel Management Backend API")
                .version("1.0.0")
                .description("REST API for Hotel Management System with MySQL, MongoDB, and Neo4j")
                .contact(new Contact()
                    .name("Hotel Development Team")
                    .url("https://github.com/yourrepo")))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT Bearer token for API authentication\n\n" +
                            "How to use:\n" +
                            "1. Click 'Authorize' button\n" +
                            "2. Get JWT token from POST /api/auth/login\n" +
                            "3. Paste token: Bearer <your_token_here>\n" +
                            "4. All protected endpoints will be authenticated")));
    }
}
