package com.diego.task_manager_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${app.public-url:}") String publicUrl
    ) {
        OpenAPI openAPI = new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .schemaRequirement(
                        "basicAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                );

        if (!publicUrl.isBlank()) {
            openAPI.addServersItem(new Server().url(publicUrl));
        }

        return openAPI;
    }
}