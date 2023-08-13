package io.github.jas34.scheduledwf.config;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.mysql.config.MySQLConfiguration;
import io.github.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import io.github.jas34.scheduledwf.dao.mysql.MySQLIndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.mysql.MySQLScheduledWfMetaDataDao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.backoff.NoBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Description:<br>
 * Date: 26/09/21-5:13 pm
 * @since v2.0.0
 * @author Jasbir Singh
 * @since v3.0.0
 * @author Vivian Zheng
 */
@Configuration
@Import(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(name = "conductor.db.type", havingValue = "mysql")
public class MySQLPersistenceConfiguration {
	@Bean
	@DependsOn({"flyway", "flywayInitializer"})
	public ScheduledWfMetadataDAO scheduledWfMetadataDAO(@Qualifier("mySQLRetryTemplate") RetryTemplate retryTemplate, ObjectMapper objectMapper, DataSource dataSource) {
		return new MySQLScheduledWfMetaDataDao(retryTemplate, objectMapper, dataSource);
	}

	@Bean
	@DependsOn({"flyway", "flywayInitializer"})
	public IndexScheduledWfDAO indexScheduledWfDAO(@Qualifier("mySQLRetryTemplate") RetryTemplate retryTemplate, ObjectMapper objectMapper, DataSource dataSource) {
		return new MySQLIndexScheduledWfDAO(retryTemplate, objectMapper, dataSource);
	}

}
