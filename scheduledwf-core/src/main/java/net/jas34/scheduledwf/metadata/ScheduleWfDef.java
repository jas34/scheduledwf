package net.jas34.scheduledwf.metadata;

import com.netflix.conductor.common.metadata.Auditable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jasbir Singh
 */
public class ScheduleWfDef extends Auditable {

    private String wfName;

    private int wfVersion;

    private Map<String, Object> wfInput = new HashMap<>();

    private String cronExpression;

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

    @Override
    public String toString() {
        return "ScheduleWfDef{" +
                "wfName='" + wfName + '\'' +
                ", wfVersion='" + wfVersion + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                "} " + super.toString();
    }
}
