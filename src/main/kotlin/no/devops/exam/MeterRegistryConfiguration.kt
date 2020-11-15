package no.devops.exam

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("default")
class MeterRegistryConfiguration {

    @Bean
    fun meterRegistry(): MeterRegistry = SimpleMeterRegistry()
}