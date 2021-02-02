package net.jas34.scheduledwf.metadata;

import com.netflix.conductor.common.metadata.Auditable;

/**
 * @author Jasbir Singh
 */
public class ScheduleWfDef extends Auditable {

    private String name;

    private int version;

    private String cronExpression;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                "} " + super.toString();
    }
}
