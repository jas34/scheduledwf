package net.jas34.scheduledwf.metadata;

import java.util.Map;

/**
 * @author Jasbir Singh
 */
public class ScheduledTaskDef {
    private String name;
    private String wfName;
    private int wfVersion;
    private Map<String, Object> input;
    private String schedulerId;

    public ScheduledTaskDef(String name, String wfName, int wfVersion, Map<String, Object> input, String schedulerId) {
        this.name = name;
        this.wfName = wfName;
        this.wfVersion = wfVersion;
        this.input = input;
        this.schedulerId = schedulerId;
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
}
