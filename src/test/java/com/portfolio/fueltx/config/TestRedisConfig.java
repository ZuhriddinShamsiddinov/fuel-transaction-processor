package com.portfolio.fueltx.config;

import com.portfolio.fueltx.model.dto.DriverFuelSummary;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.config
 * @since 2026-09-10T12:30:00
 */
@TestConfiguration
public class TestRedisConfig {

    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public ReactiveRedisTemplate<String, DriverFuelSummary> driverSummaryRedisTemplate() {
        Map<String, DriverFuelSummary> store = new ConcurrentHashMap<>();

        ReactiveRedisTemplate<String, DriverFuelSummary> template = Mockito.mock(ReactiveRedisTemplate.class);
        ReactiveValueOperations<String, DriverFuelSummary> valueOps = Mockito.mock(ReactiveValueOperations.class);

        Mockito.when(template.opsForValue()).thenReturn(valueOps);
        Mockito.when(valueOps.get(Mockito.anyString()))
                .thenAnswer(invocation -> Mono.justOrEmpty(store.get(invocation.getArgument(0))));
        Mockito.when(valueOps.set(Mockito.anyString(), Mockito.any(DriverFuelSummary.class), Mockito.any(Duration.class)))
                .thenAnswer(invocation -> {
                    store.put(invocation.getArgument(0), invocation.getArgument(1));
                    return Mono.just(true);
                });
        Mockito.when(valueOps.set(Mockito.anyString(), Mockito.any(DriverFuelSummary.class)))
                .thenAnswer(invocation -> {
                    store.put(invocation.getArgument(0), invocation.getArgument(1));
                    return Mono.just(true);
                });

        return template;
    }
}
