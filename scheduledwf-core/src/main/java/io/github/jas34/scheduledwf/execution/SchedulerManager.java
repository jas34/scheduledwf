package io.github.jas34.scheduledwf.execution;

import io.github.jas34.scheduledwf.utils.CommonUtils;

/**
 * @author Jasbir Singh
 */
public interface SchedulerManager {

    void registerManager();

    void manageProcesses();

    void shutdownProcesses();

    default String getNodeAddress() {
        return CommonUtils.resolveNodeAddress();
    }
}
