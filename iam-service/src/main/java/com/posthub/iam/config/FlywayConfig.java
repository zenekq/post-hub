package com.posthub.iam.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(
        name = "app.flyway.clean-on-start",
        havingValue = "true"
)
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {

            log.warn(">>> Calling Flyway.clean()");
            flyway.clean();

            log.warn(">>> Calling Flyway.migrate()");
            flyway.migrate();
        };
    }
}