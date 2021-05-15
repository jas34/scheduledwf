package io.github.jas34.scheduledwf.config;


import com.google.inject.AbstractModule;

import io.github.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import io.github.jas34.scheduledwf.dao.memory.InMemoryIndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.memory.InMemoryScheduledWfMetadataDAO;

/**
 * @author Jasbir Singh
 */
public class InMemoryPersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduledWfMetadataDAO.class).to(InMemoryScheduledWfMetadataDAO.class);
        bind(IndexScheduledWfDAO.class).to(InMemoryIndexScheduledWfDAO.class);
    }
}
