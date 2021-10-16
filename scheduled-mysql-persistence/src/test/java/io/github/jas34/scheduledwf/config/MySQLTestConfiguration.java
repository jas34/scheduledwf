package io.github.jas34.scheduledwf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.config.ObjectMapperProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:<br>
 * Date: 26/09/21-5:45 pm
 * @since
 * @author Jasbir Singh
 */
@Configuration
public class MySQLTestConfiguration {

	@Bean
	public ObjectMapper testObjectMapper() {
		return new ObjectMapperProvider().getObjectMapper();
	}
}
