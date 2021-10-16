package io.github.jas34.scheduledwf.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import io.github.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import io.github.jas34.scheduledwf.dao.memory.InMemoryIndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.memory.InMemoryScheduledWfExecutionDAO;
import io.github.jas34.scheduledwf.dao.memory.InMemoryScheduledWfMetadataDAO;

/**
 * Description:<br>
 * Date: 24/09/21-6:02 pm
 * 
 * @since v2.0.0
 * @author Jasbir Singh
 */
@Configuration
public class InMemoryPersistenceConfiguration {

    @Bean
    @ConditionalOnProperty(name = "conductor.db.type", havingValue = "memory", matchIfMissing = true)
    public ScheduledWfMetadataDAO scheduledWfMetadataDAO() {
        return new InMemoryScheduledWfMetadataDAO();
    }

    @Bean
    @ConditionalOnProperty(name = "conductor.db.type", havingValue = "memory", matchIfMissing = true)
    public IndexScheduledWfDAO indexScheduledWfDAO() {
        return new InMemoryIndexScheduledWfDAO();
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduledWfExecutionDAO scheduledWfExecutionDAO() {
        return new InMemoryScheduledWfExecutionDAO();
    }
}
