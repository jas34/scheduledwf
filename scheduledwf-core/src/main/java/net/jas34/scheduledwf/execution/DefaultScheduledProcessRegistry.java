package net.jas34.scheduledwf.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.jas34.scheduledwf.dao.ScheduledWfExecutionDAO;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.scheduler.ScheduledProcess;

/**
 * CAUTION: Not thread safe. Should be operated by a single thread at a time.
 *
 * @author Jasbir Singh
 */
@Singleton
public class DefaultScheduledProcessRegistry implements ScheduledProcessRegistry {

    private final Logger logger = LoggerFactory.getLogger(DefaultSchedulerManager.class);
    // TODO: need to go through Refrecnce class of java.lang to check whether this map requires
    // attention or not?
    private final Map<String, ScheduledProcess> processReferenceMap;
    private final ScheduledWfExecutionDAO wfExecutionDAO;

    @Inject
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
        updateProcessRefMapIfApplicable(scheduledWorkFlow);
    }

    @Override
    public boolean updateProcessById(ScheduledProcess processReference,
                                     ScheduledWorkFlow.State state, String id, String name) throws IllegalStateException {
        // TODO: IllegalStateException still to be validated for
        if (!isProcessPresentInRegistry(id)) {
            // TODO: lets revisit for mitigation of this scenario.
        }
        ScheduledWorkFlow scheduledWorkFlow = wfExecutionDAO.updateStateById(state, id, name);
        updateProcessRefMapIfApplicable(scheduledWorkFlow);
        return Objects.nonNull(scheduledWorkFlow);
    }

    @Override
    public boolean isProcessTobeScheduled(String name, String managerRefId) {
        Optional<ScheduledWorkFlow> scheduledWorkFlowOptional =
                wfExecutionDAO.getScheduledWfWithNameAndMgrRefId(name, managerRefId);
        logger.debug("Check whether workflow with name={}, managerRefId={} is to be scheduled or not", name,
                managerRefId);
        if (!scheduledWorkFlowOptional.isPresent()) {
            logger.debug("No scheduled workflow found. This is to be scheduled.");
            return true;
        }

        ScheduledWorkFlow scheduledWorkFlow = scheduledWorkFlowOptional.get();
        if (!isProcessPresentInRegistry(scheduledWorkFlow.getId())
                || ScheduledWorkFlow.State.SHUTDOWN == scheduledWorkFlow.getState()
                || ScheduledWorkFlow.State.SHUTDOWN_FAILED == scheduledWorkFlow.getState()) {
            logger.debug(
                    "Scheduled workflow not present in process registry or ScheduledWorkFlow is having state={}. This is to be scheduled.",
                    scheduledWorkFlow.getState());
            return true;
        }

        logger.debug("workflow with name={} and managerRefId={} is already scheduled.", name, managerRefId);
        return false;
    }

    @Override
    public List<ScheduledWorkFlow> getTobeShutDownProcesses(String managerRefId) {
        Optional<List<ScheduledWorkFlow>> optionalScheduledWorkFlow =
                wfExecutionDAO.getAllScheduledWfWithStates(managerRefId, ScheduledWorkFlow.State.SHUTDOWN);
        if (!optionalScheduledWorkFlow.isPresent()) {
            logger.debug("No scheduled workflows applicable for shutdown found.");
            return null;
        }

        updateProcessReferences(optionalScheduledWorkFlow.get());
        return optionalScheduledWorkFlow.get();
    }

    @Override
    public void removeProcess(ScheduledWorkFlow scheduledWorkFlow) {
        wfExecutionDAO.removeAllScheduledWorkflows(scheduledWorkFlow.getId());
        processReferenceMap.remove(scheduledWorkFlow.getId());
        logger.debug(
                "scheduled workflow removed from execution data persistence store and processReferenceMap.");
    }

    @Override
    public List<ScheduledWorkFlow> getAllRunningProcesses(String managerRefId) {
        Optional<List<ScheduledWorkFlow>> allRunningScheduledWorkflows =
                wfExecutionDAO.getAllScheduledWfWithByManagerRefId(managerRefId);
        if (!allRunningScheduledWorkflows.isPresent()) {
            logger.debug("No scheduled workflow applicable found.");
            return null;
        }
        updateProcessReferences(allRunningScheduledWorkflows.get());
        return allRunningScheduledWorkflows.get();
    }

    @Override
    public void shutDownRegistry(String managerRefId, long waitTimeInMillis) {
        wfExecutionDAO.removeAllScheduledWorkflows(managerRefId);
        processReferenceMap.clear();
        logger.debug("registry shutdown complete");
    }

    private boolean isProcessPresentInRegistry(String id) {
        return processReferenceMap.containsKey(id);
    }

    private void updateProcessReferences(List<ScheduledWorkFlow> scheduledWorkFlows) {
        scheduledWorkFlows.forEach(scheduledWorkFlow -> {
            ScheduledProcess processReference =
                    processReferenceMap.get(scheduledWorkFlow.getId());
            if (Objects.nonNull(processReference)) {
                scheduledWorkFlow.setScheduledProcess(processReference);

            } else {
                logger.warn("This is strange!! processReference not found in processReferenceMap for id={}, state={}",
                        scheduledWorkFlow.getId(), scheduledWorkFlow.getState());
            }
        });
    }

    private void updateProcessRefMapIfApplicable(ScheduledWorkFlow scheduledWorkFlow) {
        if (Objects.nonNull(scheduledWorkFlow.getScheduledProcess())) {
            processReferenceMap.put(scheduledWorkFlow.getId(), scheduledWorkFlow.getScheduledProcess());
        }
    }
}
