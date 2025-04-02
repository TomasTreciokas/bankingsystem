package com.danskebank.banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankSystemOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:8081")
                .description("Local Development Server");

        Contact contact = new Contact()
                .name("Danske Bank")
                .email("support@danskebank.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Bank System API")
                .version("1.0")
                .description("REST API for a mini banking system that allows managing customer accounts")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
} 