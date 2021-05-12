package io.github.jas34.scheduledwf.metadata;

import java.util.Map;

import io.github.jas34.scheduledwf.utils.CommonUtils;

/**
 * @author Jasbir Singh
 */
public class ScheduledTaskDef {
    private String name;
    private String wfName;
    private int wfVersion;
    private Map<String, Object> input;
    private String schedulerId;
    private String nodeAddress;
    private String managerRefId;


    public ScheduledTaskDef(String name, String wfName, int wfVersion, Map<String, Object> input,
            String schedulerId, String managerRefId) {
        this.name = name;
        this.wfName = wfName;
        this.wfVersion = wfVersion;
        this.input = input;
        this.schedulerId = schedulerId;
        this.nodeAddress = CommonUtils.resolveNodeAddress();
        this.managerRefId = managerRefId;
    }

    public String getName() {
        return name;
    }

    public String getWfName() {
        return wfName;
    }

    public int getWfVersion() {
        return wfVersion;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public String getSchedulerId() {
        return schedulerId;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public String getManagerRefId() {
        return managerRefId;
    }

    @Override
    public String toString() {
        return "ScheduledTaskDef{" + "name='" + name + '\'' + ", wfName='" + wfName + '\'' + ", wfVersion="
                + wfVersion + ", input=" + input + ", schedulerId='" + schedulerId + '\'' + ", nodeAddress='"
                + nodeAddress + '\'' + ", managerRefId='" + managerRefId + '\'' + "} " + super.toString();
    }
}
