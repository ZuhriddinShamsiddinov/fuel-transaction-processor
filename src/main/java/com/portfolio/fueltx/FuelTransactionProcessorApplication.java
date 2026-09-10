package com.portfolio.fueltx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx
 * @since 2026-09-10T12:15:00
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class FuelTransactionProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuelTransactionProcessorApplication.class, args);
    }
}
