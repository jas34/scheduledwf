package net.jas34.scheduledwf.dao.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import net.jas34.scheduledwf.metadata.ScheduleWfDef;

/**
 * @author Jasbir Singh
 */
@RunWith(JUnit4.class)
@Ignore
public class MySQLScheduledWfMetaDataDaoTest {

    private MySQLDAOTestUtil testUtil;
    private MySQLScheduledWfMetaDataDao dao;

    @Rule
    public TestName name = new TestName();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() throws Exception {
        testUtil = new MySQLDAOTestUtil("conductor_unit_test");
        dao = new MySQLScheduledWfMetaDataDao(testUtil.getObjectMapper(), testUtil.getDataSource());
    }

    @After
    public void teardown() throws Exception {
        testUtil.resetAllData();
        testUtil.getDataSource().close();
    }

    @Test
    public void testSaveScheduleWorkflow() {
        ScheduleWfDef def = createScheduleWfDef("testSaveScheduleWorkflow");
        dao.saveScheduleWorkflow(def);
        Optional<ScheduleWfDef> savedDef = dao.getScheduledWorkflowDef("testSaveScheduleWorkflow");
        assertTrue(savedDef.isPresent());
        assertEquals(def.getWfName(), savedDef.get().getWfName());
    }

    /*
     * 
     * boolean removeScheduleWorkflows(List<String> names);
     * 
     */

    @Test
    public void testUpdateScheduleWorkflow() throws Exception {
        ScheduleWfDef def = createScheduleWfDef("testUpdateScheduleWorkflow");
        dao.saveScheduleWorkflow(def);

        Optional<ScheduleWfDef> savedDef = dao.getScheduledWorkflowDef("testUpdateScheduleWorkflow");
        assertTrue(savedDef.isPresent());
        assertEquals(def.getStatus(), savedDef.get().getStatus());
    }

    @Test
    public void testGetAllScheduledWorkflowDefsByStatus() {
        ScheduleWfDef def1 =
                createScheduleWfDef("testGetAllScheduledWorkflowDefsByStatus1", ScheduleWfDef.Status.RUN);
        dao.saveScheduleWorkflow(def1);
        ScheduleWfDef def2 = createScheduleWfDef("testGetAllScheduledWorkflowDefsByStatus2",
                ScheduleWfDef.Status.SHUTDOWN);
        dao.saveScheduleWorkflow(def2);

        Optional<List<ScheduleWfDef>> allDefsByStatus = dao
                .getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status.RUN, ScheduleWfDef.Status.SHUTDOWN);
        assertTrue(allDefsByStatus.isPresent());
        assertEquals(2, allDefsByStatus.get().size());
    }

    @Test
    public void testGetAllScheduledWorkflowDefs() {
        ScheduleWfDef def1 =
                createScheduleWfDef("testGetAllScheduledWorkflowDefs1", ScheduleWfDef.Status.RUN);
        dao.saveScheduleWorkflow(def1);
        ScheduleWfDef def2 =
                createScheduleWfDef("testGetAllScheduledWorkflowDefs2", ScheduleWfDef.Status.SHUTDOWN);
        dao.saveScheduleWorkflow(def2);

        Optional<List<ScheduleWfDef>> allDefs = dao.getAllScheduledWorkflowDefs();
        assertTrue(allDefs.isPresent());
        assertEquals(2, allDefs.get().size());
    }

    @Test
    public void testRemoveScheduleWorkflow() {
        ScheduleWfDef def1 = createScheduleWfDef("testRemoveScheduleWorkflow1", ScheduleWfDef.Status.RUN);
        dao.saveScheduleWorkflow(def1);
        ScheduleWfDef def2 =
                createScheduleWfDef("testRemoveScheduleWorkflow2", ScheduleWfDef.Status.SHUTDOWN);
        dao.saveScheduleWorkflow(def2);

        boolean isDef1Removed = dao.removeScheduleWorkflow("testRemoveScheduleWorkflow2");
        assertTrue(isDef1Removed);

        Optional<List<ScheduleWfDef>> allDefs = dao.getAllScheduledWorkflowDefs();
        assertTrue(allDefs.isPresent());
        assertEquals(1, allDefs.get().size());
    }

    @Test
    public void testRemoveScheduleWorkflows() {
        ScheduleWfDef def1 = createScheduleWfDef("testRemoveScheduleWorkflow1", ScheduleWfDef.Status.RUN);
        dao.saveScheduleWorkflow(def1);
        ScheduleWfDef def2 =
                createScheduleWfDef("testRemoveScheduleWorkflow2", ScheduleWfDef.Status.SHUTDOWN);
        dao.saveScheduleWorkflow(def2);

        boolean isDefsRemoved =
                dao.removeScheduleWorkflows(Arrays.asList(def1.getWfName(), def2.getWfName()));
        assertTrue(isDefsRemoved);

        Optional<List<ScheduleWfDef>> allDefs = dao.getAllScheduledWorkflowDefs();
        assertTrue(allDefs.isPresent());
        assertEquals(0, allDefs.get().size());
    }

    private ScheduleWfDef createScheduleWfDef(String name, ScheduleWfDef.Status status) {
        ScheduleWfDef def = createScheduleWfDef(name);
        def.setStatus(status);
        return def;
    }

    private ScheduleWfDef createScheduleWfDef(String wfName) {
        ScheduleWfDef def = new ScheduleWfDef();
        def.setWfName(wfName);
        def.setWfVersion(1);
        def.setStatus(ScheduleWfDef.Status.RUN);
        def.setCronExpression("0 0/5 * 1/1 * ? *");
        return def;
    }
}
