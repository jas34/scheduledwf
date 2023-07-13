package io.github.jas34.scheduledwf.service;

import java.util.List;
import java.util.Optional;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import io.github.jas34.scheduledwf.exception.ApplicationException;
import com.netflix.conductor.dao.MetadataDAO;

import io.github.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import io.github.jas34.scheduledwf.metadata.ScheduleWfDef;

/**
 * @author Jasbir Singh Vivian Zheng
 */
public class MetadataServiceImpl implements MetadataService {

    private static final CronParser QUARTZ_CRON_PARSER =
            new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));

    private final MetadataDAO metadataDAO;

    private final ScheduledWfMetadataDAO scheduleWorkflowMetadataDao;

    public MetadataServiceImpl(MetadataDAO metadataDAO, ScheduledWfMetadataDAO scheduleWorkflowMetadataDao) {
        this.metadataDAO = metadataDAO;
        this.scheduleWorkflowMetadataDao = scheduleWorkflowMetadataDao;
    }

    @Override
    public void registerScheduleWorkflowDef(ScheduleWfDef def) {
        Optional<WorkflowDef> workflowDef = metadataDAO.getWorkflowDef(def.getWfName(), def.getWfVersion());
        if (!workflowDef.isPresent()) {
            throw new ApplicationException(ApplicationException.Code.INVALID_INPUT,
                    "Cannot find the workflow definition with name=" + def.getWfName() + " and version="
                            + def.getWfVersion());
        }

        Optional<ScheduleWfDef> scheduleWfDef =
                scheduleWorkflowMetadataDao.getScheduledWorkflowDef(def.getWfName());
        if (scheduleWfDef.isPresent() && ScheduleWfDef.Status.RUN == scheduleWfDef.get().getStatus()) {
            throw new ApplicationException(ApplicationException.Code.INVALID_INPUT,
                    "ScheduleWfDef already running. Cannot accept register. First SHUTDOWN or DELETE scheduler.");
        }
        assertCronExpressionIsValid(def.getCronExpression());
        scheduleWorkflowMetadataDao.saveScheduleWorkflow(def);
    }

    @Override
    public void updateScheduledWorkflowDef(String name, ScheduleWfDef.Status status) {

        Optional<ScheduleWfDef> scheduledWorkflowDef =
                scheduleWorkflowMetadataDao.getScheduledWorkflowDef(name);
        if (!scheduledWorkflowDef.isPresent()) {
            throw new ApplicationException(ApplicationException.Code.INVALID_INPUT,
                    "Cannot find the ScheduleWfDef definition with name=" + name
                            + " . Create ScheduleWfDef first.");
        }
        scheduledWorkflowDef.get().setStatus(status);
        scheduleWorkflowMetadataDao.updateScheduleWorkflow(scheduledWorkflowDef.get());
    }

    @Override
    public ScheduleWfDef getScheduledWorkflowDef(String name) {
        Optional<ScheduleWfDef> scheduledWorkflowDef =
                scheduleWorkflowMetadataDao.getScheduledWorkflowDef(name);
        return scheduledWorkflowDef.orElse(null);
    }

    @Override
    public List<ScheduleWfDef> getScheduleWorkflowDefs() {
        Optional<List<ScheduleWfDef>> allScheduledWorkflowDefs =
                scheduleWorkflowMetadataDao.getAllScheduledWorkflowDefs();
        return allScheduledWorkflowDefs.orElse(null);
    }

    @Override
    public void unregisterScheduleWorkflowDef(String name) {
        boolean isRemoved = scheduleWorkflowMetadataDao.removeScheduleWorkflow(name);
        if (!isRemoved) {
            throw new ApplicationException(ApplicationException.Code.INVALID_INPUT,
                    "Cannot find the ScheduleWfDef definition with name=" + name);
        }
    }

    private void assertCronExpressionIsValid(String cronExpression) {
        QUARTZ_CRON_PARSER.parse(cronExpression);
    }
}
