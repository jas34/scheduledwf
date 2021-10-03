package io.github.jas34.scheduledwf.service;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.exception.ApplicationException;
import com.netflix.conductor.dao.MetadataDAO;

import io.github.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import io.github.jas34.scheduledwf.metadata.ScheduleWfDef;

/**
 * @author Jasbir Singh
 */
public class TestMetadataServiceImpl {

    static final String TEST_WF_NAME = "TestMetadataServiceImpl_ScheduledWorkFlow";

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ScheduledWfMetadataDAO scheduleWorkflowMetadataDao;

    private MetadataServiceImpl metadataService;

    @Before
    public void init() {
        metadataDAO = Mockito.mock(MetadataDAO.class);
        scheduleWorkflowMetadataDao = Mockito.mock(ScheduledWfMetadataDAO.class);
        metadataDAO = Mockito.mock(MetadataDAO.class);
        metadataService = new MetadataServiceImpl(metadataDAO, scheduleWorkflowMetadataDao);
    }

    @Test(expected = ApplicationException.class)
    public void test_registerScheduleWorkflowDef_with_no_workflowDef() {
        ScheduleWfDef scheduleWfDef = createdDef(TEST_WF_NAME + "-1");
        // Optional<WorkflowDef> workflowDefOptional = Optional.of;
        when(metadataDAO.getWorkflowDef(scheduleWfDef.getWfName(), scheduleWfDef.getWfVersion()))
                .thenReturn(Optional.empty());
        metadataService.registerScheduleWorkflowDef(scheduleWfDef);

    }

    @Test(expected = ApplicationException.class)
    public void test_registerScheduleWorkflowDef_when_scheduler_in_RUN_state() {
        WorkflowDef def = createWorkflowDef(TEST_WF_NAME + "-1");
        Optional<WorkflowDef> workflowDefOptional = Optional.of(def);
        when(metadataDAO.getWorkflowDef(def.getName(), def.getVersion())).thenReturn(workflowDefOptional);

        ScheduleWfDef scheduleWfDef = createdDef(TEST_WF_NAME + "-1");
        scheduleWfDef.setStatus(ScheduleWfDef.Status.RUN);
        Optional<ScheduleWfDef> scheduleWfDefOptional = Optional.of(scheduleWfDef);
        when(scheduleWorkflowMetadataDao.getScheduledWorkflowDef(scheduleWfDef.getWfName()))
                .thenReturn(scheduleWfDefOptional);
        metadataService.registerScheduleWorkflowDef(scheduleWfDef);
    }

    @Test
    public void test_registerScheduleWorkflowDef() {
        WorkflowDef def = createWorkflowDef(TEST_WF_NAME + "-1");
        Optional<WorkflowDef> workflowDefOptional = Optional.of(def);
        when(metadataDAO.getWorkflowDef(def.getName(), def.getVersion())).thenReturn(workflowDefOptional);

        ScheduleWfDef scheduleWfDef = createdDef(TEST_WF_NAME + "-1");
        scheduleWfDef.setStatus(ScheduleWfDef.Status.RUN);
        Optional<ScheduleWfDef> scheduleWfDefOptional = Optional.empty();
        when(scheduleWorkflowMetadataDao.getScheduledWorkflowDef(scheduleWfDef.getWfName()))
                .thenReturn(scheduleWfDefOptional);
        metadataService.registerScheduleWorkflowDef(scheduleWfDef);
    }

    @Test(expected = ApplicationException.class)
    public void test_updateScheduledWorkflowDef_when_no_def_present() {
        ScheduleWfDef scheduleWfDef = createdDef(TEST_WF_NAME + "-1");
        Optional<ScheduleWfDef> scheduleWfDefOptional = Optional.empty();
        when(scheduleWorkflowMetadataDao.getScheduledWorkflowDef(scheduleWfDef.getWfName()))
                .thenReturn(scheduleWfDefOptional);
        metadataService.updateScheduledWorkflowDef(scheduleWfDef.getWfName(), ScheduleWfDef.Status.SHUTDOWN);
    }

    @Test
    public void test_updateScheduledWorkflowDef() {
        ScheduleWfDef scheduleWfDef = createdDef(TEST_WF_NAME + "-1");
        scheduleWfDef.setStatus(ScheduleWfDef.Status.RUN);
        Optional<ScheduleWfDef> scheduleWfDefOptional = Optional.of(scheduleWfDef);
        when(scheduleWorkflowMetadataDao.getScheduledWorkflowDef(scheduleWfDef.getWfName()))
                .thenReturn(scheduleWfDefOptional);
        metadataService.updateScheduledWorkflowDef(scheduleWfDef.getWfName(), ScheduleWfDef.Status.SHUTDOWN);
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
}

