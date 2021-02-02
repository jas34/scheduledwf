package net.jas34.scheduledwf.run;

import com.netflix.conductor.common.metadata.Auditable;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Jasbir Singh
 */
public class ScheduledWorkFlow extends Auditable {

    private String id;

    private String name;

    //this can be threadId
    private String executorId;

    //TODO: need to revisit this parameter... might be something similar already available in conductor
    private String nodeAddress;

    private String wfName;

    private int wfVersion;

    private String nextRunAt;

    private State state;

    private ScheduledProcessReference processReference;

    private String managerRefId;

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

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
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

    public String getNextRunAt() {
        return nextRunAt;
    }

    public void setNextRunAt(String nextRunAt) {
        this.nextRunAt = nextRunAt;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ScheduledProcessReference getProcessReference() {
        return processReference;
    }

    public void setProcessReference(ScheduledProcessReference processReference) {
        this.processReference = processReference;
    }

    public String getManagerRefId() {
        return managerRefId;
    }

    public void setManagerRefId(String managerRefId) {
        this.managerRefId = managerRefId;
    }

    @Override
    public String toString() {
        return "ScheduledWorkFlow{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", executorId='" + executorId + '\'' +
                ", nodeAddress='" + nodeAddress + '\'' +
                ", wfName='" + wfName + '\'' +
                ", wfVersion='" + wfVersion + '\'' +
                ", nextRunAt='" + nextRunAt + '\'' +
                ", state=" + state +
                "} " + super.toString();
    }

    public enum State {
        INITIALIZED,
        SCHEDULING_FAILED,
        RUNNING,
        SHUTDOWN;
    }
}
