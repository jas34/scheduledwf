package io.github.jas34.scheduledwf.run;

import com.netflix.conductor.common.metadata.Auditable;

import java.util.Map;

/**
 * @author Jasbir Singh
 */
public class ScheduledWfExecData extends Auditable {

    private String schedulerId;

    private String name;

    private String wfName;

    private int wfVersion;

    private String nodeAddress;

    private String managerRefId;

    private String executedAt;

    private String nextRunAt;

    private String lastExecutionEndedAt;

    private String triggerId;

    private String executionStatus;

    private String executionFailureReason;

    private Map<String, Object> input;

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(String executedAt) {
        this.executedAt = executedAt;
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

    public Map<String, Object> getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "ScheduledWfExecData{" +
                "schedulerId='" + schedulerId + '\'' +
                ", name='" + name + '\'' +
                ", wfName='" + wfName + '\'' +
                ", wfVersion=" + wfVersion +
                ", nodeAddress='" + nodeAddress + '\'' +
                ", managerRefId='" + managerRefId + '\'' +
                ", executedAt='" + executedAt + '\'' +
                ", nextRunAt='" + nextRunAt + '\'' +
                ", lastExecutionEndedAt='" + lastExecutionEndedAt + '\'' +
                ", triggerId='" + triggerId + '\'' +
                ", executionStatus='" + executionStatus + '\'' +
                ", executionFailureReason='" + executionFailureReason + '\'' +
                ", input=" + input +
                "} " + super.toString();
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public String getManagerRefId() {
        return managerRefId;
    }

    public void setManagerRefId(String managerRefId) {
        this.managerRefId = managerRefId;
    }

    public String getNextRunAt() {
        return nextRunAt;
    }

    public void setNextRunAt(String nextRunAt) {
        this.nextRunAt = nextRunAt;
    }

    public String getLastExecutionEndedAt() {
        return lastExecutionEndedAt;
    }

    public void setLastExecutionEndedAt(String lastExecutionEndedAt) {
        this.lastExecutionEndedAt = lastExecutionEndedAt;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(String executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getExecutionFailureReason() {
        return executionFailureReason;
    }

    public void setExecutionFailureReason(String executionFailureReason) {
        this.executionFailureReason = executionFailureReason;
    }

}
