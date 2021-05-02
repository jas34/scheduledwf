package net.jas34.scheduledwf.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.IDGenerator;
import com.netflix.conductor.dao.MetadataDAO;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.dao.memory.InMemoryIndexScheduledWfDAO;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;
import net.jas34.scheduledwf.run.ManagerInfo;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.run.SchedulingResult;
import net.jas34.scheduledwf.run.ShutdownResult;
import net.jas34.scheduledwf.run.Status;
import net.jas34.scheduledwf.utils.TestSchedulingAssistant;

/**
 * @author Jasbir Singh
 */
public class TestDefaultSchedulerManager extends TestBase {

    private DefaultSchedulerManager schedulerManager;

    @Mock
    private ScheduledWfMetadataDAO scheduledWfMetadataDAO;

    @Mock
    private ScheduledProcessRegistry processRegistry;

    @Mock
    private MetadataDAO metadataDAO;

    private TestSchedulingAssistant schedulingAssistant;

    private ManagerInfo managerInfo;

    @Before
    public void init() {
        scheduledWfMetadataDAO = Mockito.mock(ScheduledWfMetadataDAO.class);
        processRegistry = Mockito.mock(ScheduledProcessRegistry.class);
        metadataDAO = Mockito.mock(MetadataDAO.class);
        schedulingAssistant = new TestSchedulingAssistant();
        schedulerManager =
                new DefaultSchedulerManager(scheduledWfMetadataDAO, processRegistry,
                        metadataDAO, new InMemoryIndexScheduledWfDAO(), schedulingAssistant, true);
        managerInfo = createManagerInfo();
    }

    @Test
    public void test_manageProcesses_when_no_wfDefIsPresent() {
        Optional<List<ScheduleWfDef>> scheduledWorkflowOptional = Optional.empty();
        when(scheduledWfMetadataDAO.getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status.RUN)).thenReturn(scheduledWorkflowOptional);
        schedulerManager.manageProcesses();
    }

    @Test
    public void test_scheduleApplicableWorkflows_when_wfMetadata_not_present_for_all() {
        schedulerManager.setManagerInfo(managerInfo);
        Optional<List<ScheduleWfDef>> scheduledWorkflowOptional =
                Optional.of(createdDefs(TEST_WF_NAME + "-1", TEST_WF_NAME + "-2", TEST_WF_NAME + "-3"));
        when(scheduledWfMetadataDAO.getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status.RUN)).thenReturn(scheduledWorkflowOptional);

        List<ScheduleWfDef> wfDefs = scheduledWorkflowOptional.get();
        when(processRegistry.isProcessTobeScheduled(wfDefs.get(0).getWfName(), managerInfo.getId()))
                .thenReturn(true);
        when(processRegistry.isProcessTobeScheduled(wfDefs.get(1).getWfName(), managerInfo.getId()))
                .thenReturn(true);

        Optional<WorkflowDef> workflowDef1 = Optional.of(createWorkflowDef(TEST_WF_NAME + "-1"));
        Optional<WorkflowDef> workflowDef2 = Optional.of(createWorkflowDef(TEST_WF_NAME + "-2"));
        when(metadataDAO.getWorkflowDef(wfDefs.get(0).getWfName(), wfDefs.get(0).getWfVersion()))
                .thenReturn(workflowDef1);
        when(metadataDAO.getWorkflowDef(wfDefs.get(1).getWfName(), wfDefs.get(1).getWfVersion()))
                .thenReturn(workflowDef2);
        when(metadataDAO.getWorkflowDef(TEST_WF_NAME + "-3", 1)).thenReturn(Optional.empty());

        ScheduledWorkFlow scheduledWorkFlow1 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-1",
                ScheduledWorkFlow.State.INITIALIZED);
        scheduledWorkFlow1.setScheduledProcess(null);
        ScheduledWorkFlow scheduledWorkFlow2 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-2",
                ScheduledWorkFlow.State.INITIALIZED);
        scheduledWorkFlow2.setScheduledProcess(null);

        SchedulingResult result1 = createSchedulingResult(scheduledWorkFlow1.getName(), Status.SUCCESS);
        SchedulingResult result2 = createSchedulingResult(scheduledWorkFlow2.getName(), Status.FAILURE);
        Map<String, SchedulingResult> resultMap = new HashMap<>();
        resultMap.put(scheduledWorkFlow1.getName(), result1);
        resultMap.put(scheduledWorkFlow2.getName(), result2);
        schedulingAssistant.setSchedulingResultMap(resultMap);

        List<SchedulingResult> results =
                schedulerManager.scheduleApplicableWorkflows();
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(Status.SUCCESS, results.get(0).getStatus());
        assertEquals(Status.FAILURE, results.get(1).getStatus());
    }

    @Test
    public void test_manageShutDownProcesses() {
        schedulerManager.setManagerInfo(managerInfo);
        Optional<List<ScheduleWfDef>> scheduledWorkflowOptional =
                Optional.of(createdDefs(TEST_WF_NAME + "-1", TEST_WF_NAME + "-2"));
        scheduledWorkflowOptional.get().get(0).setStatus(ScheduleWfDef.Status.SHUTDOWN);
        scheduledWorkflowOptional.get().get(1).setStatus(ScheduleWfDef.Status.SHUTDOWN);
        when(scheduledWfMetadataDAO.getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status.SHUTDOWN, ScheduleWfDef.Status.DELETE)).thenReturn(scheduledWorkflowOptional);

        ScheduledWorkFlow scheduledWorkFlow1 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-1",
                ScheduledWorkFlow.State.SHUTDOWN);
        ScheduledWorkFlow scheduledWorkFlow2 = createScheduledWorkFlow(managerInfo, TEST_WF_NAME + "-2",
                ScheduledWorkFlow.State.SHUTDOWN);

        List<ScheduledWorkFlow> tobeShutDownProcesses = Arrays.asList(scheduledWorkFlow1, scheduledWorkFlow2);
        List<String> names = scheduledWorkflowOptional.get().stream().map(ScheduleWfDef::getWfName).collect(Collectors.toList());
        when(processRegistry.getTobeShutDownProcesses(managerInfo.getId(), names)).thenReturn(tobeShutDownProcesses);

        ShutdownResult result1 = createShutdownResult(Status.SUCCESS);
        ShutdownResult result2 = createShutdownResult(Status.FAILURE);
        Map<String, ShutdownResult> shutdownResultMap = new HashMap<>();
        shutdownResultMap.put(scheduledWorkFlow1.getName(), result1);
        shutdownResultMap.put(scheduledWorkFlow2.getName(), result2);
        schedulingAssistant.setShutdownResultMap(shutdownResultMap);
        List<ShutdownResult> shutdownResults = schedulerManager.manageShutDownProcesses();
        assertNotNull(shutdownResults);
        assertEquals(2, shutdownResults.size());
        assertEquals(Status.SUCCESS, shutdownResults.get(0).getStatus());
        assertEquals(Status.FAILURE, shutdownResults.get(1).getStatus());
        assertNotNull(shutdownResults.get(1).getException());
    }

    private List<ScheduleWfDef> createdDefs(String... wfNames) {
        List<ScheduleWfDef> defs = new LinkedList<>();
        for (String wfName : wfNames) {
            defs.add(createdDef(wfName));
        }
        return defs;
    }

    private ScheduleWfDef createdDef(String wfName) {
        ScheduleWfDef def = new ScheduleWfDef();
        def.setWfName(wfName);
        def.setWfVersion(1);
        def.setCronExpression("0/1 1/1 * 1/1 * ? *");
        return def;
    }

    private WorkflowDef createWorkflowDef(String wfName) {
        WorkflowDef def = new WorkflowDef();
        def.setName(wfName);
        def.setVersion(1);
        return def;
    }

    private SchedulingResult createSchedulingResult(String wfName, Status status) {
        SchedulingResult result = new SchedulingResult(IDGenerator.generate());
        result.setStatus(status);
        result.setProcessReference(createScheduledProcess(wfName));
        return result;
    }

    private ShutdownResult createShutdownResult(Status status) {
        ShutdownResult result = new ShutdownResult(IDGenerator.generate());
        result.setStatus(status);
        if(Status.FAILURE == status) {
            result.setException(new RuntimeException("this exception is to simulate shutdow failure"));
        }
        return result;
    }
}
