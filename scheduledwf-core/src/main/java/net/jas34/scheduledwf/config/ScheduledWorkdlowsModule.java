package net.jas34.scheduledwf.config;

import com.coreoz.wisp.Scheduler;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import net.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import net.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.dao.memory.InMemoryScheduledWfExecutionDAO;
import net.jas34.scheduledwf.dao.memory.InMemoryScheduledWfMetadataDAO;
import net.jas34.scheduledwf.dao.memory.InMemoryIndexScheduledWfDAO;
import net.jas34.scheduledwf.execution.DefaultScheduledProcessRegistry;
import net.jas34.scheduledwf.execution.DefaultSchedulerManager;
import net.jas34.scheduledwf.execution.DefaultWorkflowSchedulingAssistant;
import net.jas34.scheduledwf.execution.ScheduledProcessRegistry;
import net.jas34.scheduledwf.execution.WorkflowSchedulingAssistant;
import net.jas34.scheduledwf.scheduler.CronBasedWorkflowScheduler;
import net.jas34.scheduledwf.scheduler.DefaultIndexExecutionDataCallback;
import net.jas34.scheduledwf.scheduler.DefaultScheduledTaskProvider;
import net.jas34.scheduledwf.scheduler.DefaultWorkflowSchedulerFactory;
import net.jas34.scheduledwf.scheduler.IndexExecutionDataCallback;
import net.jas34.scheduledwf.scheduler.ScheduledProcess;
import net.jas34.scheduledwf.scheduler.ScheduledTaskProvider;
import net.jas34.scheduledwf.scheduler.SchedulerStats;
import net.jas34.scheduledwf.scheduler.WorkflowScheduler;
import net.jas34.scheduledwf.scheduler.WorkflowSchedulerFactory;
import net.jas34.scheduledwf.service.LockingService;
import net.jas34.scheduledwf.service.MetadataService;
import net.jas34.scheduledwf.service.MetadataServiceImpl;
import net.jas34.scheduledwf.service.SchedulerExecutionService;

/**
 * @author Jasbir Singh
 */
public class ScheduledWorkdlowsModule extends AbstractModule {
    @Override
    protected void configure() {
        // TODO: this is not final config. Will be revisited later
        // DAO Config...
        bind(IndexScheduledWfDAO.class).to(InMemoryIndexScheduledWfDAO.class);
        bind(ScheduledWfExecutionDAO.class).to(InMemoryScheduledWfExecutionDAO.class);
        bind(ScheduledWfMetadataDAO.class).to(InMemoryScheduledWfMetadataDAO.class);

        // metadata service
        bind(MetadataService.class).to(MetadataServiceImpl.class);
        bind(SchedulerExecutionService.class);
        bind(LockingService.class);

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
