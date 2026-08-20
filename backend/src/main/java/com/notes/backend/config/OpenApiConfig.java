package com.notes.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Notes Management API",
                description = "A production-ready REST API for creating, managing, and searching notes. " +
                              "Built with Spring Boot 4, Spring Data JPA, and PostgreSQL.",
                version = "v1.0.0",
                contact = @Contact(
                        name = "Aman Jaiswal",
                        email = "hello@amanjaiswal.cc",
                        url = "https://github.com/exeaman"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "Local Development Environment",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production Environment",
                        url = "https://amanjaiswal.cc"
                )
        }
)
public class OpenApiConfig {
        @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Notes API")
                .pathsToMatch("/api/notes/**")
                .build();
    }
}