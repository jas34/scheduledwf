package io.github.jas34.scheduledwf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.coreoz.wisp.Scheduler;

import io.github.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import io.github.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import io.github.jas34.scheduledwf.execution.DefaultScheduledProcessRegistry;
import io.github.jas34.scheduledwf.execution.DefaultWorkflowSchedulingAssistant;
import io.github.jas34.scheduledwf.execution.ScheduledProcessRegistry;
import io.github.jas34.scheduledwf.execution.WorkflowSchedulingAssistant;
import io.github.jas34.scheduledwf.scheduler.CronBasedWorkflowScheduler;
import io.github.jas34.scheduledwf.scheduler.DefaultIndexExecutionDataCallback;
import io.github.jas34.scheduledwf.scheduler.DefaultWorkflowSchedulerFactory;
import io.github.jas34.scheduledwf.scheduler.IndexExecutionDataCallback;
import io.github.jas34.scheduledwf.scheduler.ScheduledTaskProvider;
import io.github.jas34.scheduledwf.scheduler.SchedulerStats;
import io.github.jas34.scheduledwf.scheduler.WorkflowSchedulerFactory;

/**
 * Description:<br>
 * Date: 21/09/21-2:46 pm
 * 
 * @since
 * @author Jasbir Singh
 */
@Configuration
public class CommonConfiguration {
    @Bean("wispScheduler")
    public Scheduler scheduler() {
        return new Scheduler();
    }

    @Bean
    public CronBasedWorkflowScheduler cronBasedWorkflowScheduler(Scheduler scheduler,
            ScheduledTaskProvider scheduledTaskProvider) {
        return new CronBasedWorkflowScheduler(scheduler, scheduledTaskProvider);
    }

    @Bean
    public IndexExecutionDataCallback indexExecutionDataCallback(IndexScheduledWfDAO indexScheduledWfDAO,
            @Lazy SchedulerStats schedulerStats) {
        return new DefaultIndexExecutionDataCallback(indexScheduledWfDAO, schedulerStats);
    }

    @Bean
    public WorkflowSchedulerFactory schedulerFactory(CronBasedWorkflowScheduler cronBasedWorkflowScheduler) {
        return new DefaultWorkflowSchedulerFactory(cronBasedWorkflowScheduler);
    }

    @Bean
    public WorkflowSchedulingAssistant schedulingAssistant(
            WorkflowSchedulerFactory workflowSchedulerFactory) {
        return new DefaultWorkflowSchedulingAssistant(workflowSchedulerFactory);
    }

    @Bean
    public ScheduledProcessRegistry processRegistry(ScheduledWfExecutionDAO executionDAO) {
        return new DefaultScheduledProcessRegistry(executionDAO);
    }
}
