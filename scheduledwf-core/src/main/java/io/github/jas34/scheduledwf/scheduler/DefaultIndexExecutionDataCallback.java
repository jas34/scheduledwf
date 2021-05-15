package io.github.jas34.scheduledwf.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import io.github.jas34.scheduledwf.metadata.ScheduledTaskDef;
import io.github.jas34.scheduledwf.run.ScheduledWfExecData;
import io.github.jas34.scheduledwf.run.TriggerResult;
import io.github.jas34.scheduledwf.utils.CommonUtils;

/**
 * @author Jasbir Singh
 */
@Singleton
public class DefaultIndexExecutionDataCallback implements IndexExecutionDataCallback {

    private final IndexScheduledWfDAO indexDAO;
    private final SchedulerStats schedule;

    @Inject
    public DefaultIndexExecutionDataCallback(IndexScheduledWfDAO indexDAO, SchedulerStats schedule) {
        this.indexDAO = indexDAO;
        this.schedule = schedule;
    }

    @Override
    public void indexData(ScheduledTaskDef scheduledTaskDef, TriggerResult result) {
        String scheduledWfName = scheduledTaskDef.getName();
        ScheduledWfExecData scheduledWfExecData = new ScheduledWfExecData();
        scheduledWfExecData.setSchedulerId(scheduledTaskDef.getSchedulerId());
        scheduledWfExecData.setName(scheduledWfName);
        scheduledWfExecData.setWfName(scheduledTaskDef.getWfName());
        scheduledWfExecData.setWfVersion(scheduledTaskDef.getWfVersion());
        scheduledWfExecData.setExecutedAt(
                CommonUtils.toFormattedDate(schedule.lastExecutionStartedTimeInMillis(scheduledWfName)));
        scheduledWfExecData.setLastExecutionEndedAt(
                CommonUtils.toFormattedDate(schedule.lastExecutionEndedTimeInMillis(scheduledWfName)));
        scheduledWfExecData.setNextRunAt(
                CommonUtils.toFormattedDate(schedule.nextExecutionTimeInMillis(scheduledWfName)));
        scheduledWfExecData.setExecutionFailureReason(result.getFailureReason());
        scheduledWfExecData.setManagerRefId(scheduledTaskDef.getManagerRefId());
        scheduledWfExecData.setNodeAddress(scheduledTaskDef.getNodeAddress());
        scheduledWfExecData.setTriggerId(result.getId());
        scheduledWfExecData.setExecutionStatus(result.getStatus().name());
        scheduledWfExecData.setCreateTime(result.getCreateTime());
        scheduledWfExecData.setCreatedBy(result.getCreatedBy());
        scheduledWfExecData.setUpdateTime(result.getUpdateTime());
        scheduledWfExecData.setUpdatedBy(result.getUpdatedBy());
        scheduledWfExecData.setInput(scheduledTaskDef.getInput());
        indexDAO.indexExecutedScheduledWorkflow(scheduledWfExecData);
    }
}
