package com.portfolio.fueltx.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.config
 * @since 2026-09-10T12:15:00
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fuelTxOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fuel Transaction Processor API")
                        .description("Webhook ingestion and analytics for fuel card transactions")
                        .version("1.0.0")
                        .contact(new Contact().name("Zuhriddin")));
    }
}
