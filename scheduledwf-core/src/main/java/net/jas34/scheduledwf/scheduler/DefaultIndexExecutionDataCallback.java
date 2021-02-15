package net.jas34.scheduledwf.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.run.ScheduledWfExecData;
import net.jas34.scheduledwf.run.Status;
import net.jas34.scheduledwf.utils.CommonUtils;

/**
 * @author Jasbir Singh
 */
@Singleton
public class DefaultIndexExecutionDataCallback implements IndexExecutionDataCallback {

    private final IndexScheduledWfDAO indexDAO;
    private final WorkflowJob schedule;

    @Inject
    public DefaultIndexExecutionDataCallback(IndexScheduledWfDAO indexDAO, WorkflowJob schedule) {
        this.indexDAO = indexDAO;
        this.schedule = schedule;
    }

    @Override
    public void indexData(ScheduledTaskDef scheduledTaskDef, String triggerId, Status status, String failureReason) {
        String scheduledWfName = scheduledTaskDef.getName();
        ScheduledWfExecData scheduledWfExecData = new ScheduledWfExecData();
        scheduledWfExecData.setSchedulerId(scheduledTaskDef.getSchedulerId());
        scheduledWfExecData.setName(scheduledWfName);
        scheduledWfExecData.setWfName(scheduledTaskDef.getWfName());
        scheduledWfExecData.setWfVersion(scheduledTaskDef.getWfVersion());
        scheduledWfExecData.setExecutedAt(CommonUtils.toFormattedDate(schedule.lastExecutionStartedTimeInMillis(scheduledWfName)));
        scheduledWfExecData.setLastExecutionEndedAt(CommonUtils.toFormattedDate(schedule.lastExecutionEndedTimeInMillis(scheduledWfName)));
        scheduledWfExecData.setNextRunAt(CommonUtils.toFormattedDate(schedule.nextExecutionTimeInMillis(scheduledWfName)));
        scheduledWfExecData.setTriggerId(triggerId);
        scheduledWfExecData.setExecutionStatus(status.name());
        scheduledWfExecData.setExecutionFailureReason(failureReason);
        indexDAO.indexExecutedScheduledWorkflow(scheduledWfExecData);
    }
}
