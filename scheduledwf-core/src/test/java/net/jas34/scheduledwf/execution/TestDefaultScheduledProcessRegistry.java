package net.jas34.scheduledwf.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import net.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import net.jas34.scheduledwf.dao.memory.InMemoryScheduledWfExecutionDAO;
import net.jas34.scheduledwf.run.ManagerInfo;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public class TestDefaultScheduledProcessRegistry extends TestBase {

    private static final String TEST_WF_NAME = "TestDefaultScheduledProcessRegistry_ScheduledWorkFlow";

    private DefaultScheduledProcessRegistry processRegistry;
    private ManagerInfo managerInfo;
    private ScheduledWfExecutionDAO executionDAO;

    @Before
    public void init() {
        managerInfo = createManagerInfo();
        executionDAO = new InMemoryScheduledWfExecutionDAO();
        processRegistry = new DefaultScheduledProcessRegistry(executionDAO);
    }

    @Test
    public void testWf_Eligibility_For_Scheduling() {
        //1. when scheduledWorkFlow in not present in executionDao
        ScheduledWorkFlow scheduledWorkFlow = createScheduledWorkFlow(managerInfo, ScheduledWorkFlow.State.SCHEDULING_FAILED);
        assertTrue(processRegistry.isProcessTobeScheduled(scheduledWorkFlow.getName(), managerInfo.getId()));

        //2. when scheduledWorkFlow is present in execution dao but not in reference map
        executionDAO.createScheduledWorkflow(scheduledWorkFlow);
        assertTrue(processRegistry.isProcessTobeScheduled(scheduledWorkFlow.getName(), managerInfo.getId()));

        //3. when scheduledWorkFlow is present in reference map as well as execution dao but with state eligible for scheduling.
        processRegistry.addProcess(scheduledWorkFlow);
        assertTrue(processRegistry.isProcessTobeScheduled(scheduledWorkFlow.getName(), managerInfo.getId()));

        //4. when scheduledWorkFlow is not eligible for scheduling
        processRegistry.updateProcessById(scheduledWorkFlow.getScheduledProcess(), ScheduledWorkFlow.State.RUNNING, scheduledWorkFlow.getId(), scheduledWorkFlow.getName());
        assertFalse(processRegistry.isProcessTobeScheduled(scheduledWorkFlow.getName(), managerInfo.getId()));
    }

    @Test
    public void test_addProcess() {
        ScheduledWorkFlow scheduledWorkFlow = createScheduledWorkFlow(managerInfo, ScheduledWorkFlow.State.INITIALIZED);
        assertTrue(processRegistry.addProcess(scheduledWorkFlow));
        assertFalse(processRegistry.addProcess(scheduledWorkFlow));
    }

    @Test
    public void test_updateProcessById() {
        ScheduledWorkFlow scheduledWorkFlow = createScheduledWorkFlow(managerInfo, ScheduledWorkFlow.State.INITIALIZED);
        assertFalse(processRegistry.updateProcessById(scheduledWorkFlow.getScheduledProcess(), scheduledWorkFlow.getState(), scheduledWorkFlow.getId(), scheduledWorkFlow.getName()));

        processRegistry.addProcess(scheduledWorkFlow);
        assertTrue(processRegistry.updateProcessById(scheduledWorkFlow.getScheduledProcess(), scheduledWorkFlow.getState(), scheduledWorkFlow.getId(), scheduledWorkFlow.getName()));
    }

    @Test
    public void test_getTobeShutDownProcesses() {
        ScheduledWorkFlow scheduledWorkFlow1 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-1", ScheduledWorkFlow.State.RUNNING);
        ScheduledWorkFlow scheduledWorkFlow2 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-2", ScheduledWorkFlow.State.SHUTDOWN);
        processRegistry.addProcess(scheduledWorkFlow1);
        processRegistry.addProcess(scheduledWorkFlow2);

        List<ScheduledWorkFlow> tobeShutDownProcesses = processRegistry.getTobeShutDownProcesses(managerInfo.getId());
        assertEquals(tobeShutDownProcesses.size(), 1);
        assertEquals(scheduledWorkFlow2.getName(), tobeShutDownProcesses.get(0).getName());
    }

    @Test
    public void test_getAllRunningProcesses() {
        ScheduledWorkFlow scheduledWorkFlow1 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-1", ScheduledWorkFlow.State.RUNNING);
        ScheduledWorkFlow scheduledWorkFlow2 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-2", ScheduledWorkFlow.State.SHUTDOWN);
        processRegistry.addProcess(scheduledWorkFlow1);
        processRegistry.addProcess(scheduledWorkFlow2);

        List<ScheduledWorkFlow> runningProcesses = processRegistry.getAllRunningProcesses(managerInfo.getId());
        assertEquals(runningProcesses.size(), 2);
        assertTrue(runningProcesses.remove(scheduledWorkFlow1));
        assertTrue(runningProcesses.remove(scheduledWorkFlow2));
    }
}
