package net.jas34.scheduledwf.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import net.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import net.jas34.scheduledwf.run.ScheduledProcessReference;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public class DefaultScheduledProcessRegistry implements ScheduledProcessRegistry {

    private final Map<String, ScheduledProcessReference> processReferenceMap;
    private final ScheduledWfExecutionDAO wfExecutionDAO;

    public DefaultScheduledProcessRegistry(ScheduledWfExecutionDAO wfExecutionDAO) {
        this.processReferenceMap = new HashMap<>();
        this.wfExecutionDAO = wfExecutionDAO;
    }

    @Override
    public void addProcess(ScheduledWorkFlow scheduledWorkFlow) {
        if (isProcessPresentInRegistry(scheduledWorkFlow.getId())) {
            // TODO: Need to think how this scenario is possible? Manager needs to take process out of this
            // scenario
            // state.
        }
        wfExecutionDAO.createScheduledWorkflow(scheduledWorkFlow);
        processReferenceMap.put(scheduledWorkFlow.getId(),
                processReferenceMap.get(scheduledWorkFlow.getId()));
    }

    @Override
    public boolean updateProcessStateById(ScheduledWorkFlow.State state, String id)
            throws IllegalStateException {
        // TODO: IllegalStateException still to be validated for
        if (!isProcessPresentInRegistry(id)) {
            // TODO: lets revisit for mitigation of this scenario.
        }
        ScheduledWorkFlow scheduledWorkFlow = wfExecutionDAO.updateStateById(state, id);
        return Objects.nonNull(scheduledWorkFlow);
    }

    @Override
    public boolean isProcessTobeScheduled(String name, String managerRefId) {
        Optional<ScheduledWorkFlow> scheduledWorkFlow =
                wfExecutionDAO.getScheduledWfWithNameAndMgrRefId(name, managerRefId);
        if (!scheduledWorkFlow.isPresent() || !isProcessPresentInRegistry(scheduledWorkFlow.get().getId())) {
            return true;
        }

        scheduledWorkFlow = wfExecutionDAO.getScheduledWfNotWithState(name, managerRefId,
                ScheduledWorkFlow.State.SHUTDOWN);
        return scheduledWorkFlow.isPresent();
    }

    @Override
    public List<ScheduledWorkFlow> getTobeShutDownProcesses() {
        Optional<ScheduledWorkFlow> optionalScheduledWorkFlow = wfExecutionDAO.getAllTobeShutDownScheduledWf();
        return null;
    }

    @Override
    public void shutDownRegistry() {

    }

    private boolean isProcessPresentInRegistry(String id) {
        return processReferenceMap.containsKey(id);
    }

    // private void assertIsValidStateForUpdate(ScheduledWorkFlow.State state) {
    //
    // if(state != ScheduledWorkFlow.State.INITIALIZED || state !=
    // ScheduledWorkFlow.State.SCHEDULING_FAILED || state != ScheduledWorkFlow.State.RUNNING) {
    // throw new IllegalStateException();
    // }
    // }
}
