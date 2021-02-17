package net.jas34.scheduledwf.dao;

import java.util.List;

import net.jas34.scheduledwf.run.ManagerInfo;
import net.jas34.scheduledwf.run.ScheduledWfExecData;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public interface IndexScheduledWfDAO {

    void indexManagerInfo(ManagerInfo managerInfo);

    void indexScheduledWorkFlow(ScheduledWorkFlow scheduledWorkFlow);

    void indexExecutedScheduledWorkflow(ScheduledWfExecData execData);

    /**
     * Either of the three parameters are mandatory to be present. Based upon the presence of parameters
     * look up will be performed in the following order: 1. name && managerId or 2. name && nodeAddress
     * or 3. name or 4. managerId or 5. nodeAddress
     * 
     * @param name
     * @param managerId
     * @param nodeAddress
     * @return
     */
    List<ScheduledWorkFlow> getScheduledWorkflow(String name, String managerId, String nodeAddress);

    List<ScheduledWorkFlow> getScheduledWorkflow(String schedulerId);

    /**
     * Either of the three parameters are mandatory to be present. Based upon the presence of parameters
     * look up will be performed in the following order: 1. name && managerId or 2. name && nodeAddress
     * or 3. name or 4. managerId or 5. nodeAddress
     * 
     * @param name
     * @param managerId
     * @param nodeAddress
     * @return
     */
    List<ScheduledWfExecData> getScheduledWfExecData(String name, String managerId, String nodeAddress);

    List<ScheduledWfExecData> getScheduledWfExecData(String schedulerId);

    List<ManagerInfo> getManagerInfo(String nodeAddress);

    List<ManagerInfo> getManagerInfo();
}
