package com.ordermanagement.orderapp.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:Online Store API}")
    private String applicationName;

    /**
     * Configures the OpenAPI documentation for the application.
     *
     * @return OpenAPI instance with custom configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName)
                        .version("1.0.0")
                        .description("REST API for managing orders in a MELI store. " +
                                "Provides CRUD operations for customers, products, and orders.")
                        .contact(new Contact()
                                .name("Digital NAO Team"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://plumaverde.site")
                                .description("Production Server"),
                        new Server()
                                .url("https://api.plumaverde.site")
                                .description("Production API Server")
                ));
    }
}
