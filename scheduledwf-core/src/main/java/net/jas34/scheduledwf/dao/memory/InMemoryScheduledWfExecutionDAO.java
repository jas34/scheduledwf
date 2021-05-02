package net.jas34.scheduledwf.dao.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Singleton;

import net.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * Currently we do not see any prominent use case to have external persistence of this data layer.
 * Hence, we will go with in memory persistence for fixed number of records. Execution data will be
 * exposed from external persistence store via {@link net.jas34.scheduledwf.dao.IndexScheduledWfDAO}
 *
 * @author Jasbir Singh
 */
@Singleton
public class InMemoryScheduledWfExecutionDAO implements ScheduledWfExecutionDAO {
    private static final CacheLoader<String, ScheduledWorkFlow> LOADER =
            new CacheLoader<String, ScheduledWorkFlow>() {
                @Override
                public ScheduledWorkFlow load(String key) throws Exception {
                    return new ScheduledWorkFlow();
                }
            };

    private static final LoadingCache<String, ScheduledWorkFlow> CACHE =
            CacheBuilder.newBuilder().maximumSize(100).build(LOADER);

    @Override
    public String createScheduledWorkflow(ScheduledWorkFlow scheduledWorkFlow) {
        CACHE.put(scheduledWorkFlow.getName(), scheduledWorkFlow);
        return scheduledWorkFlow.getId();
    }

    @Override
    public Optional<ScheduledWorkFlow> getScheduledWfWithNameAndMgrRefId(String name, String managerRefId) {
        return Optional.ofNullable(CACHE.getIfPresent(name));
    }

    @Override
    public Optional<List<ScheduledWorkFlow>> getScheduledWfWithNamesAndMgrRefId(List<String> names,
            String managerRefId) {
        if (CollectionUtils.isEmpty(names)) {
            return Optional.empty();
        }

        List<ScheduledWorkFlow> scheduledWorkFlows = new ArrayList<>();
        names.forEach(name -> {
            if (Objects.nonNull(CACHE.getIfPresent(name))) {
                scheduledWorkFlows.add(CACHE.getIfPresent(name));
            }
        });
        return Optional.of(scheduledWorkFlows);
    }

    @Override
    public Optional<List<ScheduledWorkFlow>> getAllScheduledWfWithByManagerRefId(String managerRefId) {
        return Optional.of(new ArrayList<>(new ArrayList<>(CACHE.asMap().values())));
    }

    @Override
    public Optional<ScheduledWorkFlow> updateStateById(ScheduledWorkFlow.State state, String id,
            String name) {
        ScheduledWorkFlow scheduledWorkFlow = CACHE.getIfPresent(name);
        if (Objects.isNull(scheduledWorkFlow)) {
            return Optional.empty();
        }
        synchronized (scheduledWorkFlow) {
            scheduledWorkFlow.setState(state);
            CACHE.put(name, scheduledWorkFlow);
            return Optional.of(scheduledWorkFlow);
        }
    }

    @Override
    public void removeScheduledWorkflow(String name, String managerRefId) {
        CACHE.invalidate(name);
    }

    @Override
    public void removeAllScheduledWorkflows(String managerRefId) {
        CACHE.invalidateAll();
    }
}
