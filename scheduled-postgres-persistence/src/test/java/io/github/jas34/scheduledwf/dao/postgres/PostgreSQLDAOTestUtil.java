package io.github.jas34.scheduledwf.dao.postgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.postgres.config.PostgresProperties;
import com.zaxxer.hikari.HikariDataSource;

import static org.mockito.Mockito.mock;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import javax.sql.DataSource;

import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;

import static org.mockito.Mockito.when;

/**
 * @author Vivian Zheng
 */
public class PostgreSQLDAOTestUtil {

    private final HikariDataSource dataSource;
    private final PostgresProperties properties = mock(PostgresProperties.class);
    private final ObjectMapper objectMapper;


    public PostgreSQLDAOTestUtil(PostgreSQLContainer<?> postgreSQLContainer, ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;

        this.dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setUsername(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());
        dataSource.setAutoCommit(false);

        when(properties.getTaskDefCacheRefreshInterval()).thenReturn(Duration.ofSeconds(60));

        // Prevent DB from getting exhausted during rapid testing
        dataSource.setMaximumPoolSize(8);

        flywayMigrate(dataSource);
    }

    private void flywayMigrate(DataSource dataSource) {
        FluentConfiguration fluentConfiguration = Flyway.configure()
                .table("schema_version")
                .dataSource(dataSource)
                .placeholderReplacement(false)
                .locations("classpath:db/migration_postgres"); // Set the migration path

        Flyway flyway = fluentConfiguration.load();
        flyway.migrate();
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public PostgresProperties getTestProperties() {
        return properties;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }


}
