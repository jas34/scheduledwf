package net.jas34.scheduledwf.dao.mysql;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.conductor.common.run.SearchResult;
import com.netflix.conductor.dao.mysql.MySQLBaseDAO;

import net.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import net.jas34.scheduledwf.run.ManagerInfo;
import net.jas34.scheduledwf.run.ScheduledWfExecData;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class MySQLIndexScheduledWfDAO extends MySQLBaseDAO implements IndexScheduledWfDAO {

    @Inject
    MySQLIndexScheduledWfDAO(ObjectMapper om, DataSource dataSource) {
        super(om, dataSource);
    }

    @Override
    public void indexManagerInfo(ManagerInfo managerInfo) {
        final String INSERT_MANAGER_INFO_QUERY =
                "INSERT INTO manager_info (nodeAddress, json_input) VALUES (?,?)";
        withTransaction(tx -> {
            execute(tx, INSERT_MANAGER_INFO_QUERY, q -> q.addParameter(managerInfo.getNodeAddress())
                    .addJsonParameter(managerInfo).executeUpdate());
        });
    }

    @Override
    public void indexScheduledWorkFlow(ScheduledWorkFlow scheduledWorkFlow) {
        final String INSERT_SCHEDULED_WF_QUERY =
                "INSERT INTO scheduled_wf (manager_id, scheduler_id, nodeAddress, name, json_input) VALUES (?,?, ?, ?, ?)";
        withTransaction(tx -> {
            execute(tx, INSERT_SCHEDULED_WF_QUERY, q -> q.addParameter(scheduledWorkFlow.getManagerRefId())
                    .addParameters(scheduledWorkFlow.getId()).addParameter(scheduledWorkFlow.getNodeAddress())
                    .addParameter(scheduledWorkFlow.getName()).addJsonParameter(scheduledWorkFlow)
                    .executeUpdate());
        });
    }

    @Override
    public void indexExecutedScheduledWorkflow(ScheduledWfExecData execData) {
        final String INSERT_SCHEDULED_WF_EXEC_DATA_QUERY =
                "INSERT INTO scheduled_wf_execution (manager_id, scheduler_id, nodeAddress, name, json_input) VALUES (?,?, ?, ?, ?)";
        withTransaction(tx -> {
            execute(tx, INSERT_SCHEDULED_WF_EXEC_DATA_QUERY,
                    q -> q.addParameter(execData.getManagerRefId()).addParameters(execData.getSchedulerId())
                            .addParameter(execData.getNodeAddress()).addParameter(execData.getName())
                            .addJsonParameter(execData).executeUpdate());
        });
    }

    /**
     * Pagination not implemented in MVP release
     * 
     * @param name
     * @param managerId
     * @param nodeAddress
     * @param start
     * @param size
     * @return
     */
    @Override
    public SearchResult<ScheduledWorkFlow> getScheduledWorkflow(String name, String managerId,
            String nodeAddress, int start, int size) {
        final String GET_SCHEDULED_WFS_QUERY =
                "SELECT json_input FROM scheduled_wf WHERE name = ? AND manager_id = ? AND nodeAddress = ?";

        Optional<List<ScheduledWorkFlow>> scheduledWorkFlows =
                Optional.ofNullable(queryWithTransaction(GET_SCHEDULED_WFS_QUERY,
                        q -> q.addParameters(name).addParameter(managerId).addParameter(nodeAddress)
                                .executeAndFetch(ScheduledWorkFlow.class)));

        return scheduledWorkFlows.map(workFlows -> new SearchResult<>(workFlows.size(), workFlows))
                .orElse(null);
    }

    /**
     * Pagination not implemented in MVP release
     *
     * @param schedulerId
     * @param start
     * @param size
     * @return
     */
    @Override
    public SearchResult<ScheduledWorkFlow> getScheduledWorkflow(String schedulerId, int start, int size) {
        final String GET_SCHEDULED_WFS_QUERY = "SELECT json_input FROM scheduled_wf WHERE scheduler_id = ?";

        Optional<List<ScheduledWorkFlow>> scheduledWorkFlows =
                Optional.ofNullable(queryWithTransaction(GET_SCHEDULED_WFS_QUERY,
                        q -> q.addParameters(schedulerId).executeAndFetch(ScheduledWorkFlow.class)));

        return scheduledWorkFlows.map(workFlows -> new SearchResult<>(workFlows.size(), workFlows))
                .orElse(null);
    }

    /**
     * Pagination not implemented in MVP release
     * 
     * @param name
     * @param managerId
     * @param nodeAddress
     * @param start
     * @param size
     * @return
     */
    @Override
    public SearchResult<ScheduledWfExecData> getScheduledWfExecData(String name, String managerId,
            String nodeAddress, int start, int size) {
        final String GET_SCHEDULED_WFS_EXEC_DATA_QUERY =
                "SELECT json_input FROM scheduled_wf_execution WHERE name = ? AND manager_id = ? AND nodeAddress = ?";

        Optional<List<ScheduledWfExecData>> scheduledWfExecDatas =
                Optional.ofNullable(queryWithTransaction(GET_SCHEDULED_WFS_EXEC_DATA_QUERY,
                        q -> q.addParameters(name).addParameter(managerId).addParameter(nodeAddress)
                                .executeAndFetch(ScheduledWfExecData.class)));

        return scheduledWfExecDatas.map(workFlows -> new SearchResult<>(workFlows.size(), workFlows))
                .orElse(null);
    }

    /**
     * Pagination not implemented in MVP release
     *
     * @param schedulerId
     * @param start
     * @param size
     * @return
     */
    @Override
    public SearchResult<ScheduledWfExecData> getScheduledWfExecData(String schedulerId, int start, int size) {
        final String GET_SCHEDULED_WFS_EXEC_DATA_QUERY =
                "SELECT json_input FROM scheduled_wf_execution WHERE scheduler_id = ?";

        Optional<List<ScheduledWfExecData>> scheduledWfExecDatas =
                Optional.ofNullable(queryWithTransaction(GET_SCHEDULED_WFS_EXEC_DATA_QUERY,
                        q -> q.addParameters(schedulerId).executeAndFetch(ScheduledWfExecData.class)));

        return scheduledWfExecDatas.map(workFlows -> new SearchResult<>(workFlows.size(), workFlows))
                .orElse(null);
    }

    @Override
    public List<ManagerInfo> getManagerInfo(String nodeAddress) {
        final String GET_MANAGER_INFO_QUERY = "SELECT json_input FROM manager_info WHERE nodeAddress = ?";

        Optional<List<ManagerInfo>> managerInfos =
                Optional.ofNullable(queryWithTransaction(GET_MANAGER_INFO_QUERY,
                        q -> q.addParameters(nodeAddress).executeAndFetch(ManagerInfo.class)));
        return managerInfos.get();
    }

    @Override
    public List<ManagerInfo> getManagerInfo() {
        final String GET_ALL_MANAGER_INFO_QUERY = "SELECT json_input FROM manager_info";

        Optional<List<ManagerInfo>> managerInfos = Optional.ofNullable(
                queryWithTransaction(GET_ALL_MANAGER_INFO_QUERY, q -> q.executeAndFetch(ManagerInfo.class)));
        return managerInfos.get();
    }
}
