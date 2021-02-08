package net.jas34.scheduledwf.dao.memory;

import com.google.inject.Singleton;
import net.jas34.scheduledwf.dao.SchedulerManagerExecutionDAO;
import net.jas34.scheduledwf.run.ManagerInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jasbir Singh
 */
@Singleton
public class InMemorySchedulerManagerExecutionDAO implements SchedulerManagerExecutionDAO {

    private Map<String, ManagerInfo> managerInfoStore = new ConcurrentHashMap<>();

    @Override
    public void registerManager(ManagerInfo managerInfo) {
        managerInfoStore.put(managerInfo.getId(), managerInfo);
    }
}
