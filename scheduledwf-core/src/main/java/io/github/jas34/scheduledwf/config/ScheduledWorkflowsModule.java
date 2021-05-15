package io.github.jas34.scheduledwf.config;

import com.coreoz.wisp.Scheduler;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import io.github.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import io.github.jas34.scheduledwf.dao.memory.InMemoryScheduledWfExecutionDAO;
import io.github.jas34.scheduledwf.execution.DefaultScheduledProcessRegistry;
import io.github.jas34.scheduledwf.execution.DefaultSchedulerManager;
import io.github.jas34.scheduledwf.execution.DefaultWorkflowSchedulingAssistant;
import io.github.jas34.scheduledwf.execution.ScheduledProcessRegistry;
import io.github.jas34.scheduledwf.execution.WorkflowSchedulingAssistant;
import io.github.jas34.scheduledwf.scheduler.CronBasedWorkflowScheduler;
import io.github.jas34.scheduledwf.scheduler.DefaultIndexExecutionDataCallback;
import io.github.jas34.scheduledwf.scheduler.DefaultScheduledTaskProvider;
import io.github.jas34.scheduledwf.scheduler.DefaultWorkflowSchedulerFactory;
import io.github.jas34.scheduledwf.scheduler.IndexExecutionDataCallback;
import io.github.jas34.scheduledwf.scheduler.ScheduledProcess;
import io.github.jas34.scheduledwf.scheduler.ScheduledTaskProvider;
import io.github.jas34.scheduledwf.scheduler.SchedulerStats;
import io.github.jas34.scheduledwf.scheduler.WorkflowScheduler;
import io.github.jas34.scheduledwf.scheduler.WorkflowSchedulerFactory;
import io.github.jas34.scheduledwf.service.MetadataService;
import io.github.jas34.scheduledwf.service.MetadataServiceImpl;
import io.github.jas34.scheduledwf.service.SchedulerExecutionService;

/**
 * @author Jasbir Singh
 */
public class ScheduledWorkflowsModule extends AbstractModule {

    @Override
    protected void configure() {
        // dao Binding
        bind(ScheduledWfExecutionDAO.class).to(InMemoryScheduledWfExecutionDAO.class);

        // metadata service
        bind(MetadataService.class).to(MetadataServiceImpl.class);
        bind(SchedulerExecutionService.class);

        // scheduler config....
        bind(Scheduler.class).toProvider(WispSchedulerProvider.class).in(Scopes.SINGLETON);
        bind(IndexExecutionDataCallback.class).to(DefaultIndexExecutionDataCallback.class);
        bind(ScheduledTaskProvider.class).to(DefaultScheduledTaskProvider.class);
        bind(WorkflowScheduler.class).to(CronBasedWorkflowScheduler.class);
        bind(SchedulerStats.class).to(CronBasedWorkflowScheduler.class);
        bind(new TypeLiteral<WorkflowSchedulerFactory<ScheduledProcess>>() {})
                .to(new TypeLiteral<DefaultWorkflowSchedulerFactory>() {});

        // execution config
        bind(WorkflowSchedulingAssistant.class).to(DefaultWorkflowSchedulingAssistant.class);
        bind(ScheduledProcessRegistry.class).to(DefaultScheduledProcessRegistry.class);

        // configure manager
        bind(DefaultSchedulerManager.class).asEagerSingleton();
    }
}
