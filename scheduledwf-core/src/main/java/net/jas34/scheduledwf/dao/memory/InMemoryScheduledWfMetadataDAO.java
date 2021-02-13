package net.jas34.scheduledwf.dao.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.inject.Singleton;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class InMemoryScheduledWfMetadataDAO implements ScheduledWfMetadataDAO {

    private final Map<String, ScheduleWfDef> scheduleWfDefStore = new ConcurrentHashMap<>();

    @Override
    public void saveScheduleWorkflow(ScheduleWfDef def) {
        scheduleWfDefStore.put(def.getWfName(), def);
    }

    @Override
    public void updateScheduleWorkflow(ScheduleWfDef def) {
        scheduleWfDefStore.put(def.getWfName(), def);
    }

//    @Override
//    public void removeScheduleWorkflow(String name) {
//        scheduleWfDefStore.remove(name);
//    }

    @Override
    public Optional<ScheduleWfDef> getScheduledWorkflowDef(String name, int version) {
        return Optional.ofNullable(scheduleWfDefStore.get(name));
    }

    @Override
    public Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status status) {
        List<ScheduleWfDef> collect = scheduleWfDefStore.values().stream()
                .filter(scheduleWfDef -> status.equals(scheduleWfDef.getStatus()))
                .collect(Collectors.toList());
        return Optional.of(collect);
    }
}
