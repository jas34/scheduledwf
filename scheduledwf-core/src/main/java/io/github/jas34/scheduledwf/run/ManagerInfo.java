package io.github.jas34.scheduledwf.run;

import com.netflix.conductor.common.metadata.Auditable;

/**
 * @author Jasbir Singh
 */
public class ManagerInfo extends Auditable {

    private String id;

    private String name;

    private String nodeAddress;

    private Status status;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        RUNNING, SHUTDOWN;
    }

    @Override
    public String toString() {
        return "ManagerInfo{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", nodeAddress='"
                + nodeAddress + '\'' + ", status=" + status + "} " + super.toString();
    }
}
