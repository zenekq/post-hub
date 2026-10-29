package com.posthub.util.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
//@Profile("local")
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.db.insert-test-data-on-start",
        havingValue = "true"
)
public class LocalDataRunner implements CommandLineRunner {

    private final DataSource dataSource;

    @Value("${app.db.schema}")
    private String utilServiceShema;

    @Override
    public void run(String... args) throws Exception {
        var resolver = new PathMatchingResourcePatternResolver();
        Resource[] scripts = resolver.getResources(
                "classpath*:db/local-test-data/*.sql"
        );

        try (Connection connection = dataSource.getConnection()) {

            String schema = connection.getSchema();

            connection.setSchema(utilServiceShema);

            for (Resource script : scripts) {
                log.info("Executing: {}", script.getFilename());
                ScriptUtils.executeSqlScript(connection, script);
            }

            connection.setSchema(schema);
        }
    }
}