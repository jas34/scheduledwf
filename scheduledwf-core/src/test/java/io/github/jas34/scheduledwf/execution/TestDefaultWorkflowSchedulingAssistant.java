package io.github.jas34.scheduledwf.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.coreoz.wisp.Scheduler;

import io.github.jas34.scheduledwf.run.ManagerInfo;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.run.SchedulingResult;
import io.github.jas34.scheduledwf.run.ShutdownResult;
import io.github.jas34.scheduledwf.run.Status;
import io.github.jas34.scheduledwf.scheduler.WorkflowScheduler;
import io.github.jas34.scheduledwf.scheduler.WorkflowSchedulerFactory;

/**
 * @author Jasbir Singh
 */
public class TestDefaultWorkflowSchedulingAssistant extends TestBase {

    private DefaultWorkflowSchedulingAssistant schedulingAssistant;

    private Scheduler scheduler;

    private ManagerInfo managerInfo;

    @Mock
    private WorkflowSchedulerFactory factory;

    @Mock
    private WorkflowScheduler workflowScheduler;

    @Before
    public void init() {
        managerInfo = createManagerInfo();
        this.factory = Mockito.mock(WorkflowSchedulerFactory.class);
        this.workflowScheduler = Mockito.mock(WorkflowScheduler.class);
        this.schedulingAssistant = new DefaultWorkflowSchedulingAssistant(factory);
    }

    @Test
    public void test_successful_scheduling_and_shutdown() {
        ScheduledWorkFlow scheduledWorkFlow1 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-1",
                ScheduledWorkFlow.State.INITIALIZED);
        when(factory.getWorkflowSchedulerFactory(scheduledWorkFlow1)).thenReturn(workflowScheduler);
        when(workflowScheduler.schedule(scheduledWorkFlow1))
                .thenReturn(scheduledWorkFlow1.getScheduledProcess());

        SchedulingResult result1 = schedulingAssistant.scheduleSchedulerWithFailSafety(scheduledWorkFlow1);
        assertNotNull(result1);
        Assert.assertEquals(Status.SUCCESS, result1.getStatus());
        assertNotNull(result1.getProcessReference());

        ScheduledWorkFlow scheduledWorkFlow2 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-2",
                ScheduledWorkFlow.State.SCHEDULING_FAILED);
        when(factory.getWorkflowSchedulerFactory(scheduledWorkFlow2)).thenReturn(workflowScheduler);

        ScheduledWorkFlow scheduledWorkFlow3 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-3",
                ScheduledWorkFlow.State.INITIALIZED);
        when(factory.getWorkflowSchedulerFactory(scheduledWorkFlow3)).thenReturn(workflowScheduler);

        List<ShutdownResult> shutdownResults = schedulingAssistant
                .shutdownAllSchedulersWithFailSafety(Arrays.asList(scheduledWorkFlow2, scheduledWorkFlow3));
        assertEquals(shutdownResults.size(), 2);
        assertEquals(Status.SUCCESS, shutdownResults.get(0).getStatus());
        assertEquals(Status.SUCCESS, shutdownResults.get(1).getStatus());
    }

    @Test
    public void test_successful_schedulingFailed_and_shutdownFailed() {
        ScheduledWorkFlow scheduledWorkFlow1 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-1",
                ScheduledWorkFlow.State.INITIALIZED);
        RuntimeException runtimeException =
                new RuntimeException("Runtime failure while scheduling " + TEST_WF_NAME + "-1");
        when(factory.getWorkflowSchedulerFactory(scheduledWorkFlow1)).thenReturn(workflowScheduler);
        when(workflowScheduler.schedule(scheduledWorkFlow1)).thenThrow(runtimeException);

        SchedulingResult result1 = schedulingAssistant.scheduleSchedulerWithFailSafety(scheduledWorkFlow1);
        assertNotNull(result1);
        assertEquals(Status.FAILURE, result1.getStatus());
        assertEquals(runtimeException, result1.getException());

        when(workflowScheduler.shutdown(scheduledWorkFlow1.getScheduledProcess()))
                .thenThrow(runtimeException);
        List<ShutdownResult> shutdownResults = schedulingAssistant
                .shutdownAllSchedulersWithFailSafety(Collections.singletonList(scheduledWorkFlow1));
        assertEquals(shutdownResults.size(), 1);
        assertEquals(Status.FAILURE, shutdownResults.get(0).getStatus());
        assertEquals(runtimeException, result1.getException());
    }
}
