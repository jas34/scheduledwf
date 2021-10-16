package io.github.jas34.scheduledwf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.github.jas34.scheduledwf.scheduler.IndexExecutionDataCallback;
import io.github.jas34.scheduledwf.scheduler.ScheduledTaskProvider;
import io.github.jas34.scheduledwf.scheduler.TestScheduledTaskProvider;

/**
 * Description:<br>
 * Date: 26/09/21-10:38 am
 * 
 * @since v2.0.0
 * @author Jasbir Singh
 */
@Configuration
@Import(value = {CommonConfiguration.class, InMemoryPersistenceConfiguration.class})
public class CoreTestConfiguration {
    @Bean
    public ScheduledTaskProvider scheduledTaskProvider(IndexExecutionDataCallback callback) {
        return new TestScheduledTaskProvider(callback);
    }
}
