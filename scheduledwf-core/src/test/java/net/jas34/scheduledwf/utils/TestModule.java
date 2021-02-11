package net.jas34.scheduledwf.utils;

import com.coreoz.wisp.Scheduler;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import net.jas34.scheduledwf.config.WispSchedulerProvider;
import net.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import net.jas34.scheduledwf.dao.memory.LoggingBasedIndexScheduledWfDAO;
import net.jas34.scheduledwf.execution.DefaultWorkflowSchedulingAssistant;
import net.jas34.scheduledwf.execution.WorkflowSchedulingAssistant;
import net.jas34.scheduledwf.scheduler.CronBasedWorkflowScheduler;
import net.jas34.scheduledwf.scheduler.DefaultIndexExecutionDataCallback;
import net.jas34.scheduledwf.scheduler.DefaultWorkflowSchedulerFactory;
import net.jas34.scheduledwf.scheduler.IndexExecutionDataCallback;
import net.jas34.scheduledwf.scheduler.ScheduledProcess;
import net.jas34.scheduledwf.scheduler.ScheduledTaskProvider;
import net.jas34.scheduledwf.scheduler.TestScheduledTaskProvider;
import net.jas34.scheduledwf.scheduler.WorkflowJob;
import net.jas34.scheduledwf.scheduler.WorkflowScheduler;
import net.jas34.scheduledwf.scheduler.WorkflowSchedulerFactory;

/**
 * @author Jasbir Singh
 */
public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IndexScheduledWfDAO.class).to(LoggingBasedIndexScheduledWfDAO.class);
        bind(Scheduler.class).toProvider(WispSchedulerProvider.class).in(Scopes.SINGLETON);
        bind(IndexExecutionDataCallback.class).to(DefaultIndexExecutionDataCallback.class);
        bind(ScheduledTaskProvider.class).to(TestScheduledTaskProvider.class);
        bind(WorkflowScheduler.class).to(CronBasedWorkflowScheduler.class);
        bind(WorkflowJob.class).to(CronBasedWorkflowScheduler.class);
        bind(new TypeLiteral<WorkflowSchedulerFactory<ScheduledProcess>>() {})
                .to(new TypeLiteral<DefaultWorkflowSchedulerFactory>() {});

        bind(WorkflowSchedulingAssistant.class).to(DefaultWorkflowSchedulingAssistant.class);
    }
}
