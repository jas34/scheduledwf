package io.github.jas34.scheduledwf.dao.postgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.run.SearchResult;
import io.github.jas34.scheduledwf.config.PostgreSQLTestConfiguration;
import io.github.jas34.scheduledwf.run.ManagerInfo;
import io.github.jas34.scheduledwf.run.ScheduledWfExecData;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.run.Status;
import io.github.jas34.scheduledwf.utils.CommonUtils;
import io.github.jas34.scheduledwf.utils.IDGenerator_;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.utility.DockerImageName;

import org.testcontainers.containers.PostgreSQLContainer;


import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Vivian Zheng
 */
@Ignore
@Import(PostgreSQLTestConfiguration.class)
@RunWith(SpringRunner.class)
public class PostgreSQLIndexScheduledWfDAOTest {
    private PostgreSQLDAOTestUtil testUtil;
    private PostgreSQLIndexScheduledWfDAO dao;

    @Autowired
    private ObjectMapper objectMapper;

    @Rule
    public TestName name = new TestName();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        PostgreSQLContainer<?> postgreSQLContainer =
                new PostgreSQLContainer<>(DockerImageName.parse("postgres")).withDatabaseName(name.getMethodName());
        postgreSQLContainer.start();
        testUtil = new PostgreSQLDAOTestUtil(postgreSQLContainer, objectMapper);
        dao = new PostgreSQLIndexScheduledWfDAO(new RetryTemplate(), testUtil.getObjectMapper(), testUtil.getDataSource());
    }

    @After
    public void teardown() {
        if (testUtil != null) {
            testUtil.getDataSource().close();
        }
    }


    @Test
    public void testIndexManagerInfo() {
        ManagerInfo managerInfo1 = createManagerInfo("testIndexManagerInfo1");
        dao.indexManagerInfo(managerInfo1);
        List<ManagerInfo> managerInfos = dao.getManagerInfo(CommonUtils.resolveNodeAddress());
        assertNotNull(managerInfos);
        assertEquals(1, managerInfos.size());

        ManagerInfo managerInfo2 = createManagerInfo("testIndexManagerInfo2");
        dao.indexManagerInfo(managerInfo2);
        List<ManagerInfo> allManagers = dao.getManagerInfo();
        assertNotNull(allManagers);
        assertEquals(2, allManagers.size());
    }

    @Test
    public void testIndexScheduledWorkFlow() {
        ScheduledWorkFlow scheduledWorkFlow1 =
                createScheduledWorkflow("testIndexScheduledWorkFlow1", ScheduledWorkFlow.State.RUNNING);
        dao.indexScheduledWorkFlow(scheduledWorkFlow1);

        SearchResult<ScheduledWorkFlow> result1 = dao.getScheduledWorkflow(scheduledWorkFlow1.getName(),
                scheduledWorkFlow1.getManagerRefId(), scheduledWorkFlow1.getNodeAddress(), 0, 0);
        assertNotNull(result1);
        assertEquals(1, result1.getTotalHits());

        result1 = dao.getScheduledWorkflow(scheduledWorkFlow1.getId(), 0, 0);
        assertNotNull(result1);
        assertEquals(1, result1.getTotalHits());
    }

    @Test
    public void testIndexExecutedScheduledWorkflow() {
        ScheduledWfExecData execData1 =
                createScheduledWfExecData("testIndexExecutedScheduledWorkflow1", Status.SUCCESS.name(), null);
        dao.indexExecutedScheduledWorkflow(execData1);

        ScheduledWfExecData execData2 = createScheduledWfExecData("testIndexExecutedScheduledWorkflow2",
                Status.FAILURE.name(), "Test Failure");
        dao.indexExecutedScheduledWorkflow(execData2);

        SearchResult<ScheduledWfExecData> result1 = dao.getScheduledWfExecData(execData1.getName(),
                execData1.getManagerRefId(), execData1.getNodeAddress(), 0, 0);
        assertNotNull(result1);
        assertEquals(1, result1.getTotalHits());

        SearchResult<ScheduledWfExecData> result2 =
                dao.getScheduledWfExecData(execData2.getSchedulerId(), 0, 0);
        assertNotNull(result2);
        assertEquals(1, result2.getTotalHits());
    }

    private ManagerInfo createManagerInfo(String name) {
        ManagerInfo managerInfo = new ManagerInfo();
        managerInfo.setName(name);
        managerInfo.setNodeAddress(CommonUtils.resolveNodeAddress());
        managerInfo.setId(IDGenerator_.generate());
        managerInfo.setStatus(ManagerInfo.Status.RUNNING);
        return managerInfo;
    }

    private ScheduledWorkFlow createScheduledWorkflow(String name, ScheduledWorkFlow.State state) {
        ScheduledWorkFlow scheduledWorkFlow = new ScheduledWorkFlow();
        scheduledWorkFlow.setId(IDGenerator_.generate());
        scheduledWorkFlow.setName(name);
        scheduledWorkFlow.setNodeAddress(CommonUtils.resolveNodeAddress());
        scheduledWorkFlow.setWfName(name);
        scheduledWorkFlow.setWfVersion(1);
        scheduledWorkFlow.setState(state);
        scheduledWorkFlow.setCronExpression("0/1 1/1 * 1/1 * ? *");
        scheduledWorkFlow.setManagerRefId(IDGenerator_.generate());
        return scheduledWorkFlow;
    }

    private ScheduledWfExecData createScheduledWfExecData(String name, String status, String failureReason) {
        ScheduledWfExecData execData = new ScheduledWfExecData();
        execData.setSchedulerId(IDGenerator_.generate());
        execData.setName(name);
        execData.setWfName(name);
        execData.setWfVersion(1);
        execData.setNodeAddress(CommonUtils.resolveNodeAddress());
        execData.setManagerRefId(IDGenerator_.generate());
        execData.setExecutedAt(CommonUtils.toFormattedDate(System.currentTimeMillis()));
        execData.setNextRunAt(CommonUtils.toFormattedDate(System.currentTimeMillis() + 120000));
        execData.setLastExecutionEndedAt(CommonUtils.toFormattedDate(System.currentTimeMillis() + 4000));
        execData.setTriggerId(IDGenerator_.generate());
        execData.setExecutionStatus(status);
        execData.setExecutionFailureReason(failureReason);
        return execData;
    }
}
