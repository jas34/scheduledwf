package net.jas34.scheduledwf.run;

/**
 * @author Jasbir Singh
 */
public class ScheduledWfExecData {

    private String schedulerId;

    private String name;

    private String executedAt;

    private String wfName;

    private int wfVersion;

    private String nextRunAt;

    private String lastExecutionEndedAt;

    private String executionStatus;

    private String executionFailureReason;

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

    @Override
    public String toString() {
        return "ScheduledWfExecData{" +
                "schedulerId='" + schedulerId + '\'' +
                ", name='" + name + '\'' +
                ", executedAt='" + executedAt + '\'' +
                ", wfName='" + wfName + '\'' +
                ", wfVersion=" + wfVersion +
                ", nextRunAt='" + nextRunAt + '\'' +
                ", lastExecutionEndedAt='" + lastExecutionEndedAt + '\'' +
                ", executionStatus='" + executionStatus + '\'' +
                ", executionFailureReason='" + executionFailureReason + '\'' +
                '}';
    }
}
