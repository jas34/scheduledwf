package io.github.jas34.scheduledwf.metadata;

import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotEmpty;

import com.netflix.conductor.common.constraints.NoSemiColonConstraint;
import com.netflix.conductor.common.metadata.Auditable;

/**
 * @author Jasbir Singh
 */
public class ScheduleWfDef extends Auditable {

    @NotEmpty(message = "WorkflowDef name cannot be null or empty")
    @NoSemiColonConstraint(message = "Workflow name cannot contain the following set of characters: ':'")
    private String wfName;

    private int wfVersion = 1;

    @NotEmpty(message = "status cannot be empty")
    private Status status;

    private Map<String, Object> wfInput = new HashMap<>();

    @NotEmpty(
            message = "cronExpression can not be empty. Should be valid cron expression. Tip: Create definition from http://www.cronmaker.com/")
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public enum Status {
        RUN, SHUTDOWN, DELETE;
    }

    @Override
    public String toString() {
        return "ScheduleWfDef{" + "wfName='" + wfName + '\'' + ", wfVersion='" + wfVersion + '\''
                + ", cronExpression='" + cronExpression + '\'' + "} " + super.toString();
    }
}
