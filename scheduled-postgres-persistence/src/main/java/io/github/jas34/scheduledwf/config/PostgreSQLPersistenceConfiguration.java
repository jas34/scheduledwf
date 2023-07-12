package io.github.jas34.scheduledwf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import io.github.jas34.scheduledwf.dao.postgres.PostgreSQLIndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.postgres.PostgreSQLScheduledWfMetaDataDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.retry.support.RetryTemplate;

import javax.sql.DataSource;

/**
 * @author Vivian Zheng
 */

@Configuration
@Import(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(name = "conductor.db.type", havingValue = "postgres")
public class PostgreSQLPersistenceConfiguration {
    @Bean
    @DependsOn("flywayForPrimaryDb")
    public ScheduledWfMetadataDAO scheduledWfMetadataDAO(@Qualifier("postgresRetryTemplate") RetryTemplate retryTemplate, ObjectMapper objectMapper, DataSource dataSource) {
        return new PostgreSQLScheduledWfMetaDataDao(retryTemplate, objectMapper, dataSource);
    }

    @Bean
    @DependsOn("flywayForPrimaryDb")
    public IndexScheduledWfDAO indexScheduledWfDAO(@Qualifier("postgresRetryTemplate") RetryTemplate retryTemplate, ObjectMapper objectMapper, DataSource dataSource) {
        return new PostgreSQLIndexScheduledWfDAO(retryTemplate, objectMapper, dataSource);
    }
}
