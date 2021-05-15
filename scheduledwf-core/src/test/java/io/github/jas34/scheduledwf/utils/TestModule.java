package io.github.jas34.scheduledwf.utils;

import com.coreoz.wisp.Scheduler;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import io.github.jas34.scheduledwf.config.WispSchedulerProvider;
import io.github.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.memory.InMemoryIndexScheduledWfDAO;
import io.github.jas34.scheduledwf.execution.DefaultWorkflowSchedulingAssistant;
import io.github.jas34.scheduledwf.execution.WorkflowSchedulingAssistant;
import io.github.jas34.scheduledwf.scheduler.CronBasedWorkflowScheduler;
import io.github.jas34.scheduledwf.scheduler.DefaultIndexExecutionDataCallback;
import io.github.jas34.scheduledwf.scheduler.DefaultWorkflowSchedulerFactory;
import io.github.jas34.scheduledwf.scheduler.IndexExecutionDataCallback;
import io.github.jas34.scheduledwf.scheduler.ScheduledProcess;
import io.github.jas34.scheduledwf.scheduler.ScheduledTaskProvider;
import io.github.jas34.scheduledwf.scheduler.SchedulerStats;
import io.github.jas34.scheduledwf.scheduler.TestScheduledTaskProvider;
import io.github.jas34.scheduledwf.scheduler.WorkflowScheduler;
import io.github.jas34.scheduledwf.scheduler.WorkflowSchedulerFactory;

/**
 * @author Jasbir Singh
 */
public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IndexScheduledWfDAO.class).to(InMemoryIndexScheduledWfDAO.class);
        bind(Scheduler.class).toProvider(WispSchedulerProvider.class).in(Scopes.SINGLETON);
        bind(IndexExecutionDataCallback.class).to(DefaultIndexExecutionDataCallback.class);
        bind(ScheduledTaskProvider.class).to(TestScheduledTaskProvider.class);
        bind(WorkflowScheduler.class).to(CronBasedWorkflowScheduler.class);
        bind(SchedulerStats.class).to(CronBasedWorkflowScheduler.class);
        bind(new TypeLiteral<WorkflowSchedulerFactory<ScheduledProcess>>() {})
                .to(new TypeLiteral<DefaultWorkflowSchedulerFactory>() {});

        bind(WorkflowSchedulingAssistant.class).to(DefaultWorkflowSchedulingAssistant.class);
    }
}
