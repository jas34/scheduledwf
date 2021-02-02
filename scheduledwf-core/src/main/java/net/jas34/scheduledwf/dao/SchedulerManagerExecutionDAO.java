package net.jas34.scheduledwf.dao;

import net.jas34.scheduledwf.run.ManagerInfo;

/**
 * @author Jasbir Singh
 */
public interface SchedulerManagerExecutionDAO {

    void registerManager(ManagerInfo managerInfo);
}
