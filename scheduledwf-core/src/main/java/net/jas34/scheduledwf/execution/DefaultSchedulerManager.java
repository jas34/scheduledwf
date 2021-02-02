package net.jas34.scheduledwf.execution;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import net.jas34.scheduledwf.run.Result;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.IDGenerator;
import com.netflix.conductor.dao.MetadataDAO;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.dao.SchedulerManagerExecutionDAO;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;
import net.jas34.scheduledwf.run.ManagerInfo;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.run.SchedulingResult;
import net.jas34.scheduledwf.run.ShutdownResult;

import javax.annotation.PreDestroy;

/**
 * @author Jasbir Singh
 */
@Singleton
public class DefaultSchedulerManager implements SchedulerManager {

    private final Logger logger = LoggerFactory.getLogger(DefaultSchedulerManager.class);

    private final ScheduledWfMetadataDAO scheduledWfMetadataDAO;
    private final SchedulerManagerExecutionDAO managerExecutionDAO;
    private final ScheduledProcessRegistry processRegistry;
    private final MetadataDAO metadataDAO;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private WorkflowSchedulingAssistant schedulingAssistant;
    private ManagerInfo managerInfo;

    @Inject
    public DefaultSchedulerManager(ScheduledWfMetadataDAO scheduledWfMetadataDAO,
            SchedulerManagerExecutionDAO managerExecutionDAO, ScheduledProcessRegistry processRegistry,
            MetadataDAO metadataDAO, WorkflowSchedulingAssistant schedulingAssistant) {
        this.scheduledWfMetadataDAO = scheduledWfMetadataDAO;
        this.managerExecutionDAO = managerExecutionDAO;
        this.processRegistry = processRegistry;
        this.metadataDAO = metadataDAO;
        this.schedulingAssistant = schedulingAssistant;
        registerManager();
        manageProcesses();
    }

    @Override
    public void registerManager() {
        long currentTimeStamp = System.currentTimeMillis();
        managerInfo = new ManagerInfo();
        managerInfo.setId(IDGenerator.generate());
        managerInfo.setName(DefaultSchedulerManager.class.getSimpleName());
        managerInfo.setNodeAddress(getNodeAddress());
        managerInfo.setCreateTime(currentTimeStamp);
        managerExecutionDAO.registerManager(managerInfo);
    }

    @Override
    public void manageProcesses() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Optional<List<ScheduleWfDef>> scheduledWorkflowOptional =
                    scheduledWfMetadataDAO.getAllScheduledWorkflowDefs();

            if (!scheduledWorkflowOptional.isPresent()
                    || CollectionUtils.isEmpty(scheduledWorkflowOptional.get())) {
                logger.debug("No scheduled workflow definitions available. No process to be scheduled");
                return;
            }

            List<ScheduleWfDef> unScheduledWorkflows = scheduledWorkflowOptional
                    .get().stream().filter(scheduleWfDef -> !processRegistry
                            .isProcessTobeScheduled(scheduleWfDef.getName(), managerInfo.getId()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(unScheduledWorkflows)) {
                logger.info("No workflow left to be scheduled.");
                return;
            }

            scheduleApplicableWorkflows(unScheduledWorkflows);
            manageShutDownProcesses();

        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    @Override
    public void shutdownProcesses() {
        List<ScheduledWorkFlow> runningProcesses = processRegistry.getAllRunningProcesses(managerInfo.getId());
        schedulingAssistant.shutdownAllSchedulersWithFailSafety(runningProcesses);
        processRegistry.shutDownRegistry(managerInfo.getId(),5000);
    }

    private void scheduleApplicableWorkflows(List<ScheduleWfDef> unScheduledWorkflows) {
        unScheduledWorkflows.forEach(unScheduledWorkflow -> {
            Optional<WorkflowDef> workflowDef = metadataDAO.getWorkflowDef(unScheduledWorkflow.getName(),
                    unScheduledWorkflow.getVersion());
            if (!workflowDef.isPresent()) {
                logger.info(
                        "No workflow definition present for workflow.name={}, workflow.version={}. Nothing will be scheduled for this workflow",
                        unScheduledWorkflow.getName(), unScheduledWorkflow.getVersion());
                return;
            }

            logger.info("Going to schedule workflow with name={} on node={}", unScheduledWorkflow.getName(),
                    managerInfo.getName());
            ScheduledWorkFlow scheduledWorkFlow = new ScheduledWorkFlow();
            scheduledWorkFlow.setId(IDGenerator.generate());
            scheduledWorkFlow.setName(unScheduledWorkflow.getName());
            scheduledWorkFlow.setNodeAddress(managerInfo.getNodeAddress());
            scheduledWorkFlow.setWfName(unScheduledWorkflow.getName());
            scheduledWorkFlow.setWfVersion(unScheduledWorkflow.getVersion());
            scheduledWorkFlow.setState(ScheduledWorkFlow.State.INITIALIZED);
            scheduledWorkFlow.setManagerRefId(managerInfo.getId());

            processRegistry.addProcess(scheduledWorkFlow);
            SchedulingResult result = schedulingAssistant.scheduleSchedulerWithFailSafety(scheduledWorkFlow);
            if (SchedulingResult.Status.FAILURE == result.getStatus()) {
                logger.error("Unable to  schedule workflow name={}, version={} with some error",
                        scheduledWorkFlow.getWfName(), scheduledWorkFlow.getWfVersion(),
                        result.getException());
                scheduledWorkFlow.setState(ScheduledWorkFlow.State.SCHEDULING_FAILED);
            } else {
                logger.info("Process submitted for scheduling with the id={}, initialDelay={}, period={}",
                        result.getId(), result.getInitialDelay(), result.getPeriod());
                scheduledWorkFlow.setProcessReference(result.getProcessReference());
                scheduledWorkFlow.setState(ScheduledWorkFlow.State.RUNNING);
            }
            processRegistry.updateProcessStateById(scheduledWorkFlow.getState(), scheduledWorkFlow.getId());
        });
    }

    private void manageShutDownProcesses() {
        List<ScheduledWorkFlow> tobeShutDownProcesses = processRegistry.getTobeShutDownProcesses();
        if (CollectionUtils.isEmpty(tobeShutDownProcesses)) {
            logger.debug("No process applicable for shutdown");
            return;
        }

        tobeShutDownProcesses.forEach(shutdownProcess -> {
            ShutdownResult result =
                    schedulingAssistant.shutdownSchedulerWithFailSafety(shutdownProcess);

            if (Result.Status.FAILURE == result.getStatus()) {
                logger.error("Unable to  shutdown workflow name={}, version={} with some error",
                        shutdownProcess.getWfName(), shutdownProcess.getWfVersion(),
                        result.getException());
                shutdownProcess.setState(ScheduledWorkFlow.State.SHUTDOWN_FAILED);
                processRegistry.updateProcessStateById(shutdownProcess.getState(), shutdownProcess.getId());
            } else {
                logger.info("Process submit signalled ted for shutdown with the id={}", result.getId());
                processRegistry.removeProcess(shutdownProcess);
            }
        });
    }
}
