package io.github.jas34.scheduledwf.execution;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.coreoz.wisp.Job;
import com.coreoz.wisp.Scheduler;
import com.google.inject.Inject;

import io.github.jas34.scheduledwf.run.ManagerInfo;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.scheduler.CronBasedScheduledProcess;
import io.github.jas34.scheduledwf.scheduler.CronBasedWorkflowScheduler;
import io.github.jas34.scheduledwf.utils.TestRunner;

/**
 * @author Jasbir Singh
 */
@RunWith(TestRunner.class)
public class TestCronBasedWorkflowScheduler extends TestBase {

    @Inject
    private CronBasedWorkflowScheduler cronBasedWorkflowScheduler;

    @Inject
    private Scheduler scheduler;

    private ManagerInfo managerInfo;

    @Before
    public void init() {
        managerInfo = createManagerInfo();
    }

    @Test
    public void test_scheduler() {
        ScheduledWorkFlow scheduledWorkFlow =
                createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-1", ScheduledWorkFlow.State.INITIALIZED);
        CronBasedScheduledProcess scheduledProcess =
                cronBasedWorkflowScheduler.schedule(scheduledWorkFlow);
        Job job = scheduledProcess.getJobReference();

        assertNotNull(scheduler.findJob(scheduledWorkFlow.getName()));
        assertEquals(scheduledWorkFlow.getName(), job.name());

        // lets wait for job to run and then check executionsCount. It should be greater than 0
        sleepUninterruptibly(2000, TimeUnit.MILLISECONDS);
        assertTrue(0 < job.executionsCount());

        // test shutdown
        cronBasedWorkflowScheduler.shutdown(scheduledProcess);
        assertEquals(resolveNextExecutionTime(scheduledProcess.getJobReference()), -1L);
    }

    @Test
    public void test_reScheduling() {
        ScheduledWorkFlow scheduledWorkFlow =
                createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-2", ScheduledWorkFlow.State.INITIALIZED);
        scheduledWorkFlow.setReSchedulingEnabled(true);
        scheduledWorkFlow.setScheduledProcess(null);
        scheduledWorkFlow.setWfName(TEST_WF_NAME + "-2");
        CronBasedScheduledProcess scheduledProcess =
                cronBasedWorkflowScheduler.schedule(scheduledWorkFlow);
        Job job = scheduledProcess.getJobReference();

        assertNotNull(scheduler.findJob(scheduledWorkFlow.getName()));
        assertEquals(scheduledWorkFlow.getName(), job.name());

        // lets wait for job to run and then check executionsCount. It should be greater than 0
        sleepUninterruptibly(3000, TimeUnit.MILLISECONDS);
        assertTrue(0 < job.executionsCount());

        //override definition for rescheduling.
        scheduledWorkFlow.setScheduledProcess(scheduledProcess);
        scheduledWorkFlow.setCronExpression("0/2 1/1 * 1/1 * ? *");
        System.out.println("Going to schedule updated scheduler definition");

        scheduledProcess = cronBasedWorkflowScheduler.schedule(scheduledWorkFlow);
        sleepUninterruptibly(5000, TimeUnit.MILLISECONDS);
        assertTrue(0 < scheduledProcess.getJobReference().executionsCount());
    }

    @Test
    public void test_Job_Cacel_Without_Wait_To_Complete() {
        ScheduledWorkFlow scheduledWorkFlow =
                createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-3-sleep", ScheduledWorkFlow.State.INITIALIZED);
        scheduledWorkFlow.setReSchedulingEnabled(true);
        scheduledWorkFlow.setScheduledProcess(null);
        scheduledWorkFlow.setWfName(TEST_WF_NAME + "-3-sleep");
        CronBasedScheduledProcess scheduledProcess =
                cronBasedWorkflowScheduler.schedule(scheduledWorkFlow);
        sleepUninterruptibly(5000, TimeUnit.MILLISECONDS);
        cronBasedWorkflowScheduler.shutdown(scheduledProcess);
        assertEquals(resolveNextExecutionTime(scheduledProcess.getJobReference()), -1L);
    }
}
