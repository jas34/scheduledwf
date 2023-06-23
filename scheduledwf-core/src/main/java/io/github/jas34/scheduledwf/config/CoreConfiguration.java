package io.github.jas34.scheduledwf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.service.WorkflowService;

import io.github.jas34.scheduledwf.concurrent.LockingService;
import io.github.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import io.github.jas34.scheduledwf.scheduler.DefaultScheduledTaskProvider;
import io.github.jas34.scheduledwf.scheduler.IndexExecutionDataCallback;
import io.github.jas34.scheduledwf.scheduler.ScheduledTaskProvider;
import io.github.jas34.scheduledwf.service.MetadataService;
import io.github.jas34.scheduledwf.service.MetadataServiceImpl;

/**
 * Description:<br>
 * Date: 26/09/21-12:09 pm
 * 
 * @since v2.0.0
 * @author Jasbir Singh
 */
@Configuration
@Import(value = {LockConfiguration.class, InMemoryPersistenceConfiguration.class, CommonConfiguration.class})
public class CoreConfiguration {

    @Bean
    public MetadataService metadataService(MetadataDAO metadataDAO,
            ScheduledWfMetadataDAO scheduledWfMetadataDAO) {
        return new MetadataServiceImpl(metadataDAO, scheduledWfMetadataDAO);
    }

    @Bean
    public ScheduledTaskProvider scheduledTaskProvider(IndexExecutionDataCallback callback,
            WorkflowService workflowService, LockingService lockingService) {
        return new DefaultScheduledTaskProvider(callback, workflowService, lockingService);
    }
}
