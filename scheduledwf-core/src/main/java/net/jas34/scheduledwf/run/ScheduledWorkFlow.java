package net.jas34.scheduledwf.run;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.netflix.conductor.common.metadata.Auditable;
import net.jas34.scheduledwf.scheduler.ScheduledProcess;
import net.minidev.json.annotate.JsonIgnore;

/**
 * @author Jasbir Singh
 */
public class ScheduledWorkFlow extends Auditable implements Serializable {

    private String id;

    private String name;

    private String nodeAddress;

    private String wfName;

    private int wfVersion;

    private Map<String, Object> wfInput = new HashMap<>();

    private String cronExpression;

    private State state;

    @JsonIgnore
    private ScheduledProcess scheduledProcess;

    private String managerRefId;

    private Throwable schedulingException;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public String getWfName() {
        return wfName;
    }

    public void setWfName(String wfName) {
        this.wfName = wfName;
    }

    public int getWfVersion() {
        return wfVersion;
    }

    public void setWfVersion(int wfVersion) {
        this.wfVersion = wfVersion;
    }

    public Map<String, Object> getWfInput() {
        return wfInput;
    }

    public void setWfInput(Map<String, Object> wfInput) {
        this.wfInput = wfInput;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ScheduledProcess getScheduledProcess() {
        return scheduledProcess;
    }

    public void setScheduledProcess(ScheduledProcess scheduledProcess) {
        this.scheduledProcess = scheduledProcess;
    }

    public String getManagerRefId() {
        return managerRefId;
    }

    public void setManagerRefId(String managerRefId) {
        this.managerRefId = managerRefId;
    }

    public Throwable getSchedulingException() {
        return schedulingException;
    }

    public void setSchedulingException(Throwable schedulingException) {
        this.schedulingException = schedulingException;
    }

    public enum State {
        INITIALIZED, SCHEDULING_FAILED, RUNNING, SHUTDOWN, SHUTDOWN_FAILED;
    }

    public ScheduledWorkFlow cloneWithoutProcessRef() {
        ScheduledWorkFlow newScheduledWorkFlow = new ScheduledWorkFlow();
        newScheduledWorkFlow.setId(this.getId());
        newScheduledWorkFlow.setName(this.getName());
        newScheduledWorkFlow.setNodeAddress(this.getNodeAddress());
        newScheduledWorkFlow.setWfName(this.getWfName());
        newScheduledWorkFlow.setWfVersion(this.getWfVersion());
        newScheduledWorkFlow.setWfInput(this.getWfInput());
        newScheduledWorkFlow.setState(this.getState());
        newScheduledWorkFlow.setCronExpression(this.getCronExpression());
        newScheduledWorkFlow.setManagerRefId(this.getId());
        newScheduledWorkFlow.setSchedulingException(this.getSchedulingException());
        newScheduledWorkFlow.setCreateTime(System.currentTimeMillis());
        newScheduledWorkFlow.setCreatedBy(this.getCreatedBy());
        newScheduledWorkFlow.setUpdateTime(this.getUpdateTime());
        newScheduledWorkFlow.setUpdatedBy(this.getUpdatedBy());
        return newScheduledWorkFlow;
    }

    @Override
    public String toString() {
        return "ScheduledWorkFlow{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nodeAddress='" + nodeAddress + '\'' +
                ", wfName='" + wfName + '\'' +
                ", wfVersion=" + wfVersion +
                ", wfInput=" + wfInput +
                ", cronExpression='" + cronExpression + '\'' +
                ", state=" + state +
                ", scheduledProcess=" + scheduledProcess +
                ", managerRefId='" + managerRefId + '\'' +
                ", schedulingException=" + schedulingException +
                "} " + super.toString();
    }
}
