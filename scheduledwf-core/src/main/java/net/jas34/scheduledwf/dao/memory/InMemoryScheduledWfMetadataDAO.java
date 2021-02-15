package net.jas34.scheduledwf.dao.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Override
    public int removeScheduleWorkflow(String name) {
        if (Objects.isNull(scheduleWfDefStore.get(name))) {
            return 0;
        }

        scheduleWfDefStore.remove(name);
        return 1;
    }

    @Override
    public int removeScheduleWorkflows(List<String> names) {
        return names.stream().mapToInt(this::removeScheduleWorkflow).sum();
    }

    @Override
    public Optional<ScheduleWfDef> getScheduledWorkflowDef(String name) {
        return Optional.ofNullable(scheduleWfDefStore.get(name));
    }

    @Override
    public Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status... status) {
        List<ScheduleWfDef> collect = scheduleWfDefStore
                .values().stream().filter(scheduleWfDef -> Stream.of(status)
                        .collect(Collectors.toCollection(ArrayList::new)).contains(scheduleWfDef.getStatus()))
                .collect(Collectors.toList());
        return Optional.of(collect);
    }

    @Override
    public Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefs() {
        return Optional.of(new ArrayList<>(scheduleWfDefStore.values()));
    }
}
