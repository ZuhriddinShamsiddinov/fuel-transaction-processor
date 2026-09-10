package com.portfolio.fueltx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.fueltx.model.dto.DriverFuelSummary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.config
 * @since 2026-09-10T12:20:00
 */
@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, DriverFuelSummary> driverSummaryRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<DriverFuelSummary> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, DriverFuelSummary.class);

        RedisSerializationContext<String, DriverFuelSummary> context =
                RedisSerializationContext.<String, DriverFuelSummary>newSerializationContext(new StringRedisSerializer())
                        .value(valueSerializer)
                        .hashKey(new StringRedisSerializer())
                        .hashValue(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
