package net.jas34.scheduledwf.dao.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.inject.Singleton;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;

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
    public boolean removeScheduleWorkflow(String name) {
        if (Objects.isNull(scheduleWfDefStore.get(name))) {
            return false;
        }

        scheduleWfDefStore.remove(name);
        return true;
    }

    @Override
    public boolean removeScheduleWorkflows(List<String> names) {
        AtomicBoolean isRemoved = new AtomicBoolean(false);
        names.forEach(name -> {
            isRemoved.set(removeScheduleWorkflows(names));
        });
        return isRemoved.get();
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
