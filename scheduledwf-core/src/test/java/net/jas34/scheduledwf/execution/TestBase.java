package net.jas34.scheduledwf.execution;

import com.coreoz.wisp.Job;
import com.netflix.conductor.core.utils.IDGenerator;
import net.jas34.scheduledwf.run.ManagerInfo;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.scheduler.ScheduledProcess;
import net.jas34.scheduledwf.utils.CommonUtils;

/**
 * @author Jasbir Singh
 */
public class TestBase {

    public static final String TEST_WF_NAME = "TestDefaultScheduledProcessRegistry_ScheduledWorkFlow";

    public ScheduledWorkFlow createScheduledWorkFlow(ManagerInfo managerInfo, ScheduledWorkFlow.State state) {
        ScheduledWorkFlow scheduledWorkFlow = new ScheduledWorkFlow();
        scheduledWorkFlow.setId(IDGenerator.generate());
        scheduledWorkFlow.setName(TEST_WF_NAME);
        scheduledWorkFlow.setNodeAddress(managerInfo.getNodeAddress());
        scheduledWorkFlow.setWfName(TEST_WF_NAME);
        scheduledWorkFlow.setWfVersion(1);
        scheduledWorkFlow.setState(state);
        scheduledWorkFlow.setCronExpression("0/1 1/1 * 1/1 * ? *");
        scheduledWorkFlow.setManagerRefId(managerInfo.getId());
        scheduledWorkFlow.setCreateTime(System.currentTimeMillis());
        scheduledWorkFlow.setCreatedBy(managerInfo.getNodeAddress() + ":" + managerInfo.getId());
        scheduledWorkFlow.setScheduledProcess(createScheduledProcess(TEST_WF_NAME));
        return scheduledWorkFlow;
    }

    public ScheduledWorkFlow createScheduledWorkFlow(ManagerInfo managerInfo, String name,
            ScheduledWorkFlow.State state) {
        ScheduledWorkFlow scheduledWorkFlow = createScheduledWorkFlow(managerInfo, state);
        scheduledWorkFlow.setName(name);
        scheduledWorkFlow.setScheduledProcess(createScheduledProcess(name));
        return scheduledWorkFlow;
    }

    public ManagerInfo createManagerInfo() {
        ManagerInfo managerInfo = new ManagerInfo();
        managerInfo.setId(IDGenerator.generate());
        managerInfo.setName(DefaultSchedulerManager.class.getSimpleName());
        managerInfo.setNodeAddress(CommonUtils.resolveNodeAddress());
        managerInfo.setCreateTime(System.currentTimeMillis());
        return managerInfo;
    }

    public long resolveNextExecutionTime(Job job) {
        return job.schedule().nextExecutionInMillis(System.currentTimeMillis(), job.executionsCount(),
                job.lastExecutionEndedTimeInMillis());
    }

    public ScheduledProcess createScheduledProcess(String name) {
        return new ScheduledProcess() {
            @Override
            public Object getJobReference() {
                return name;
            }
        };
    }
}
