package net.jas34.scheduledwf.execution;

import net.jas34.scheduledwf.utils.CommonUtils;

/**
 * @author Jasbir Singh
 */
public interface SchedulerManager {

    void registerManager();

    void manageProcesses();

    default String getNodeAddress() {
        return CommonUtils.resolveNodeAddress();
    }
}
