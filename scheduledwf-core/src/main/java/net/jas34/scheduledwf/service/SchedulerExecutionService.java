package net.jas34.scheduledwf.service;

import java.util.List;
import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.conductor.core.execution.ApplicationException;
import net.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import net.jas34.scheduledwf.run.ManagerInfo;
import net.jas34.scheduledwf.run.ScheduledWfExecData;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class SchedulerExecutionService {

    private IndexScheduledWfDAO indexScheduledWfDAO;

    @Inject
    public SchedulerExecutionService(IndexScheduledWfDAO indexScheduledWfDAO) {
        this.indexScheduledWfDAO = indexScheduledWfDAO;
    }

    public List<ScheduledWorkFlow> searchScheduledWorkflow(String name, String managerId, String nodeAddress,
            String schedulerId) {
        if (Objects.nonNull(schedulerId)) {
            return indexScheduledWfDAO.getScheduledWorkflow(schedulerId);
        }
        if (Objects.isNull(name) && Objects.isNull(managerId) && Objects.isNull(nodeAddress)) {
            throw new ApplicationException(ApplicationException.Code.INVALID_INPUT,
                    "Either scheduler name or managerId or nodeAddress or schedulerId should be provided.");
        }
        return indexScheduledWfDAO.getScheduledWorkflow(name, managerId, nodeAddress);
    }

    public List<ScheduledWfExecData> searchScheduledWfExecData(String name, String managerId,
            String nodeAddress, String schedulerId) {
        if (Objects.nonNull(schedulerId)) {
            return indexScheduledWfDAO.getScheduledWfExecData(schedulerId);
        }
        if (Objects.isNull(name) && Objects.isNull(managerId) && Objects.isNull(nodeAddress)) {
            throw new ApplicationException(ApplicationException.Code.INVALID_INPUT,
                    "Either scheduler name or managerId or nodeAddress or schedulerId should be provided.");
        }
        return indexScheduledWfDAO.getScheduledWfExecData(name, managerId, nodeAddress);
    }

    public List<ManagerInfo> getManagerInfo(String nodeAddress) {
        if (Objects.nonNull(nodeAddress)) {
            return indexScheduledWfDAO.getManagerInfo(nodeAddress);
        }

        return indexScheduledWfDAO.getManagerInfo();
    }
}
