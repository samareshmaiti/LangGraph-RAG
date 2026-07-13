package com.chatbot.util;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI chatbotOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("ChatBot REST API")
                        .description("REST API Documentation for ChatBot Application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Samaresh")
                                .email("samaresh1maiti@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .schemaRequirement(
                        securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation"));
    }
}