package net.jas34.scheduledwf.scheduler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.service.WorkflowService;

import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.run.Status;
import net.jas34.scheduledwf.run.TriggerResult;
import net.jas34.scheduledwf.service.LockingService;

/**
 * @author Jasbir Singh
 */
public class TriggerScheduledWorkFlowTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(TriggerScheduledWorkFlowTask.class);

    private ScheduledTaskDef taskDef;

    private IndexExecutionDataCallback indexExecutionDataCallback;

    private WorkflowService workflowService;

    private LockingService lockingService;

    public TriggerScheduledWorkFlowTask(ScheduledTaskDef taskDef,
            IndexExecutionDataCallback indexExecutionDataCallback, WorkflowService workflowService,
            LockingService lockingService) {
        this.taskDef = taskDef;
        this.indexExecutionDataCallback = indexExecutionDataCallback;
        this.workflowService = workflowService;
        this.lockingService = lockingService;
    }

    @Override
    public void run() {
        lockingService.acquireLock(resolveLockId());
        TriggerResult result = new TriggerResult();

        try {
            // TODO: we can add throttling here by looking at number of running of workflows .... Let revisit

            logger.debug("Going to start workflow with name={}", taskDef.getName());
            String workflowId = workflowService.startWorkflow(taskDef.getWfName(), taskDef.getWfVersion(),
                    taskDef.getSchedulerId(), taskDef.getInput());
            result.setId(workflowId);
            result.setStatus(Status.SUCCESS);
            logger.debug("Workflow with name={} started and workflowId={}", taskDef.getName(), workflowId);

        } catch (Exception e) {
            result.setException(e);
            result.setStatus(Status.FAILURE);
            logger.error("Unable to start workflow due to error.", e);
        }

        result.setUpdateTime(System.currentTimeMillis());
        indexExecutionDataCallback.indexData(taskDef, result);
        lockingService.releaseLock(resolveLockId(), true);
    }

    private String resolveLockId(){
        return taskDef.getWfName() + "-" + taskDef.getWfVersion();
    }
}
