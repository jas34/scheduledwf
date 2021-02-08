package net.jas34.scheduledwf.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.conductor.core.utils.IDGenerator;
import net.jas34.scheduledwf.run.Result;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.run.SchedulingResult;
import net.jas34.scheduledwf.run.ShutdownResult;
import net.jas34.scheduledwf.run.Status;
import net.jas34.scheduledwf.scheduler.ScheduledProcess;
import net.jas34.scheduledwf.scheduler.WorkflowScheduler;
import net.jas34.scheduledwf.scheduler.WorkflowSchedulerFactory;

/**
 * @author Jasbir Singh
 */
@Singleton
public class DefaultWorkflowSchedulingAssistant implements WorkflowSchedulingAssistant {

    private final WorkflowSchedulerFactory<ScheduledProcess> factory;

    @Inject
    public DefaultWorkflowSchedulingAssistant(WorkflowSchedulerFactory<ScheduledProcess> factory) {
        this.factory = factory;
    }

    @Override
    public SchedulingResult scheduleSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow) {
        WorkflowScheduler<ScheduledProcess> workflowScheduler = factory.getWorkflowSchedulerFactory(scheduledWorkFlow);
        SchedulingResult result = new SchedulingResult(IDGenerator.generate());
        ScheduledProcess scheduledProcess = executeAndpopulateResult(result, () -> workflowScheduler.schedule(scheduledWorkFlow));
        result.setProcessReference(scheduledProcess);
        return result;
    }

    @Override
    public ShutdownResult shutdownSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow) {
        WorkflowScheduler<ScheduledProcess> workflowScheduler = factory.getWorkflowSchedulerFactory(scheduledWorkFlow);
        ShutdownResult result = new ShutdownResult(IDGenerator.generate());
        executeAndpopulateResult(result, () -> workflowScheduler.shutdown(scheduledWorkFlow.getScheduledProcess()));
        return result;
    }

    @Override
    public List<ShutdownResult> shutdownAllSchedulersWithFailSafety(List<ScheduledWorkFlow> scheduledWorkFlows) {
        List<ShutdownResult> results = new ArrayList<>();
        scheduledWorkFlows.forEach(scheduledWorkFlow -> {
            results.add(shutdownSchedulerWithFailSafety(scheduledWorkFlow));
        });
        return results;
    }

    private <T> T executeAndpopulateResult(Result result, Supplier<T> task) {
        result.setStatus(Status.SUCCESS);
        try {
            return task.get();

        }catch (Throwable e) {
            result.setStatus(Status.FAILURE);
            result.setException(e);
            return null;
        }
    }
}
