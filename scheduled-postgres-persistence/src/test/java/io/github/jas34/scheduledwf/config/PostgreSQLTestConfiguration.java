package io.github.jas34.scheduledwf.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.config.ObjectMapperProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Vivian Zheng
 */

@Configuration
public class PostgreSQLTestConfiguration {
    @Bean
    public ObjectMapper testObjectMapper() {
        return new ObjectMapperProvider().getObjectMapper();
    }
}
