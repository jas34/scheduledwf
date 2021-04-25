package net.jas34.scheduledwf.service;

import java.util.List;
import java.util.Optional;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.conductor.annotations.Service;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.execution.ApplicationException;
import com.netflix.conductor.dao.MetadataDAO;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;

/**
 * @author Jasbir Singh
 */
@Singleton
public class MetadataServiceImpl implements MetadataService {

    private static final CronParser QUARTZ_CRON_PARSER =
            new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));

    private final MetadataDAO metadataDAO;

    private final ScheduledWfMetadataDAO scheduleWorkflowMetadataDao;

    @Inject
    public MetadataServiceImpl(MetadataDAO metadataDAO, ScheduledWfMetadataDAO scheduleWorkflowMetadataDao) {
        this.metadataDAO = metadataDAO;
        this.scheduleWorkflowMetadataDao = scheduleWorkflowMetadataDao;
    }

    @Override
    @Service
    public void registerScheduleWorkflowDef(ScheduleWfDef def) {
        Optional<WorkflowDef> workflowDef = metadataDAO.getWorkflowDef(def.getWfName(), def.getWfVersion());
        if (!workflowDef.isPresent()) {
            throw new ApplicationException(ApplicationException.Code.INVALID_INPUT,
                    "Cannot find the workflow definition with name=" + def.getWfName() + " and version="
                            + def.getWfVersion());
        }

        Optional<ScheduleWfDef> scheduleWfDef =
                scheduleWorkflowMetadataDao.getScheduledWorkflowDef(def.getWfName());
        if (scheduleWfDef.isPresent()) {
            throw new ApplicationException(ApplicationException.Code.INVALID_INPUT,
                    "ScheduleWfDef already present. Cannot accept register.");
        }
        assertCronExpressionIsValid(def.getCronExpression());
        scheduleWorkflowMetadataDao.saveScheduleWorkflow(def);
    }

    @Override
    @Service
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
    @Service
    public ScheduleWfDef getScheduledWorkflowDef(String name) {
        Optional<ScheduleWfDef> scheduledWorkflowDef =
                scheduleWorkflowMetadataDao.getScheduledWorkflowDef(name);
        return scheduledWorkflowDef.orElse(null);
    }

    @Override
    @Service
    public List<ScheduleWfDef> getScheduleWorkflowDefs() {
        Optional<List<ScheduleWfDef>> allScheduledWorkflowDefs =
                scheduleWorkflowMetadataDao.getAllScheduledWorkflowDefs();
        return allScheduledWorkflowDefs.orElse(null);
    }

    @Override
    @Service
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
