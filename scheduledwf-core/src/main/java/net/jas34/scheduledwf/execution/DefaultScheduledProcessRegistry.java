package net.jas34.scheduledwf.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import net.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import net.jas34.scheduledwf.run.ScheduledProcessReference;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CAUTION: Not thread safe. Should be operated by a single thread at a time.
 *
 * @author Jasbir Singh
 */
public class DefaultScheduledProcessRegistry implements ScheduledProcessRegistry {

    private final Logger logger = LoggerFactory.getLogger(DefaultSchedulerManager.class);
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
            logger.debug(
                    "No scheduled workflow found for name={}, managerRefId={} or workflow not present in process registry",
                    name, managerRefId);
            return true;
        }

        scheduledWorkFlow = wfExecutionDAO.getScheduledWfWithStates(name, managerRefId,
                ScheduledWorkFlow.State.SHUTDOWN, ScheduledWorkFlow.State.SHUTDOWN_FAILED);
        logger.debug("scheduled workflow with name={} and managerRefId={} yet to be scheduled={}", name,
                managerRefId, !scheduledWorkFlow.isPresent());
        return !scheduledWorkFlow.isPresent();
    }

    @Override
    public List<ScheduledWorkFlow> getTobeShutDownProcesses() {
        Optional<List<ScheduledWorkFlow>> optionalScheduledWorkFlow =
                wfExecutionDAO.getAllTobeShutDownScheduledWf();
        if (!optionalScheduledWorkFlow.isPresent()) {
            logger.debug("No scheduled workflow applicable for shutdown found.");
            return null;
        }

        updateProcessReferences(optionalScheduledWorkFlow.get());
        return optionalScheduledWorkFlow.get();
    }

    @Override
    public void removeProcess(ScheduledWorkFlow scheduledWorkFlow) {
        wfExecutionDAO.removeScheduledWorkflow(scheduledWorkFlow.getId());
        processReferenceMap.remove(scheduledWorkFlow.getId());
        logger.debug("scheduled workflow removed from execution store and processReferenceMap.");
    }

    @Override
    public List<ScheduledWorkFlow> getAllRunningProcesses(String managerRefId) {
        Optional<List<ScheduledWorkFlow>> allRunningScheduledWorkflows = wfExecutionDAO.getAllScheduledWfWithStates(managerRefId, ScheduledWorkFlow.State.RUNNING);
        if (!allRunningScheduledWorkflows.isPresent()) {
            logger.debug("No scheduled workflow applicable found.");
            return null;
        }
        updateProcessReferences(allRunningScheduledWorkflows.get());
        return allRunningScheduledWorkflows.get();
    }

    @Override
    public void shutDownRegistry(String managerRefId, long waitTimeInMillis) {
        wfExecutionDAO.removeScheduledWorkflow(managerRefId);
        processReferenceMap.clear();
        logger.debug("registry shutdown complete");
    }

    private boolean isProcessPresentInRegistry(String id) {
        return processReferenceMap.containsKey(id);
    }

    private void updateProcessReferences(List<ScheduledWorkFlow> scheduledWorkFlows) {
        scheduledWorkFlows.forEach(scheduledWorkFlow -> {
            ScheduledProcessReference<?> processReference =
                    processReferenceMap.get(scheduledWorkFlow.getId());
            if (Objects.nonNull(processReference)) {
                scheduledWorkFlow.setProcessReference(processReference);

            } else {
                logger.warn("This is strange!! processReference not found in processReferenceMap for id={}",
                        scheduledWorkFlow.getId());
            }
        });
    }

    // private void assertIsValidStateForUpdate(ScheduledWorkFlow.State state) {
    //
    // if(state != ScheduledWorkFlow.State.INITIALIZED || state !=
    // ScheduledWorkFlow.State.SCHEDULING_FAILED || state != ScheduledWorkFlow.State.RUNNING) {
    // throw new IllegalStateException();
    // }
    // }
}
