package io.github.jas34.scheduledwf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.jas34.scheduledwf.execution.WorkflowSchedulingAssistant;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.run.SchedulingResult;
import io.github.jas34.scheduledwf.run.ShutdownResult;

/**
 * This schedulingAssistant just meant for testing purpose
 *
 * @author Jasbir Singh
 */
public class TestSchedulingAssistant implements WorkflowSchedulingAssistant {

    private Map<String, SchedulingResult> schedulingResultMap = new HashMap<>();

    private Map<String, ShutdownResult> shutdownResultMap = new HashMap<>();

    public void setSchedulingResultMap(Map<String, SchedulingResult> schedulingResultMap) {
        this.schedulingResultMap = schedulingResultMap;
    }

    public void setShutdownResultMap(Map<String, ShutdownResult> shutdownResultMap) {
        this.shutdownResultMap = shutdownResultMap;
    }

    @Override
    public SchedulingResult scheduleSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow) {
        return schedulingResultMap.get(scheduledWorkFlow.getName());
    }

    @Override
    public ShutdownResult shutdownSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow) {
        return shutdownResultMap.get(scheduledWorkFlow.getName());
    }

    @Override
    public List<ShutdownResult> shutdownAllSchedulersWithFailSafety(
            List<ScheduledWorkFlow> scheduledWorkFlows) {
        List<ShutdownResult> results = new ArrayList<>();
        scheduledWorkFlows.forEach(scheduledWorkFlow -> {
            results.add(shutdownSchedulerWithFailSafety(scheduledWorkFlow));
        });

        return results;
    }
}
