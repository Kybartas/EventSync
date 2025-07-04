package org.kybartas.eventsync;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventSyncOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("EventSync")
                .description("Organize events, manage feedback"));
    }
}