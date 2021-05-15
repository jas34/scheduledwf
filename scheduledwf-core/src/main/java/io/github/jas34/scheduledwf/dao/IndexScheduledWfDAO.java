package io.github.jas34.scheduledwf.dao;

import java.util.List;

import com.netflix.conductor.common.run.SearchResult;
import io.github.jas34.scheduledwf.run.ManagerInfo;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.run.ScheduledWfExecData;


/**
 * @author Jasbir Singh
 */
public interface IndexScheduledWfDAO {

    void indexManagerInfo(ManagerInfo managerInfo);

    void indexScheduledWorkFlow(ScheduledWorkFlow scheduledWorkFlow);

    void indexExecutedScheduledWorkflow(ScheduledWfExecData execData);

    SearchResult<ScheduledWorkFlow> getScheduledWorkflow(String name, String managerId,
                                      String nodeAddress, int start, int size);

    SearchResult<ScheduledWorkFlow> getScheduledWorkflow(String schedulerId, int start, int size);

    SearchResult<ScheduledWfExecData> getScheduledWfExecData(String name, String managerId,
            String nodeAddress, int start, int size);

    SearchResult<ScheduledWfExecData> getScheduledWfExecData(String schedulerId, int start, int size);

    List<ManagerInfo> getManagerInfo(String nodeAddress);

    List<ManagerInfo> getManagerInfo();
}
