package net.jas34.scheduledwf.dao.memory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import com.google.inject.Singleton;
import net.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import net.jas34.scheduledwf.run.ManagerInfo;
import net.jas34.scheduledwf.run.ScheduledWfExecData;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class InMemoryIndexScheduledWfDAO implements IndexScheduledWfDAO {

    private ManagerInfo managerInfo = null;
    private final ArrayListValuedHashMap<String, ScheduledWorkFlow> scheduledWorkFlowStore =
            new ArrayListValuedHashMap<>();
    private final ArrayListValuedHashMap<String, ScheduledWfExecData> scheduledWfExecDataStore =
            new ArrayListValuedHashMap<>();

    @Override
    public void indexManagerInfo(ManagerInfo managerInfo) {
        this.managerInfo = managerInfo;
    }

    @Override
    public void indexScheduledWorkFlow(ScheduledWorkFlow scheduledWorkFlow) {
        scheduledWorkFlowStore.put(scheduledWorkFlow.getName(), scheduledWorkFlow);
    }

    @Override
    public void indexExecutedScheduledWorkflow(ScheduledWfExecData execData) {
        scheduledWfExecDataStore.put(execData.getName(), execData);
    }

    @Override
    public List<ScheduledWorkFlow> getScheduledWorkflow(String name, String managerId, String nodeAddress) {
        if (Objects.nonNull(name)) {
            return scheduledWorkFlowStore.get(name);
        }

        return (List<ScheduledWorkFlow>) scheduledWorkFlowStore.values();
    }

    @Override
    public List<ScheduledWorkFlow> getScheduledWorkflow(String schedulerId) {
        List<ScheduledWorkFlow> values = (List<ScheduledWorkFlow>) scheduledWorkFlowStore.values();
        if (Objects.isNull(values)) {
            return null;
        }
        return values.stream().filter(value -> schedulerId.equals(value.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduledWfExecData> getScheduledWfExecData(String name, String managerId,
            String nodeAddress) {
        if (Objects.nonNull(name)) {
            return scheduledWfExecDataStore.get(name);
        }

        return (List<ScheduledWfExecData>) scheduledWfExecDataStore.values();
    }

    @Override
    public List<ScheduledWfExecData> getScheduledWfExecData(String schedulerId) {
        List<ScheduledWfExecData> values = (List<ScheduledWfExecData>) scheduledWfExecDataStore.values();
        if (Objects.isNull(values)) {
            return null;
        }
        return values.stream().filter(value -> schedulerId.equals(value.getSchedulerId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManagerInfo> getManagerInfo(String nodeAddress) {
        return Arrays.asList(managerInfo);
    }

    @Override
    public List<ManagerInfo> getManagerInfo() {
        return Arrays.asList(managerInfo);
    }
}
