package net.jas34.scheduledwf.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.conductor.dao.MetadataDAO;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;

import java.util.Optional;

/**
 * @author Jasbir Singh
 */
@Singleton
public class MetadataServiceImpl implements MetadataService {

    private final MetadataDAO metadataDAO;

    private final ScheduledWfMetadataDAO scheduleWorkflowMetadataDao;

    @Inject
    public MetadataServiceImpl(MetadataDAO metadataDAO, ScheduledWfMetadataDAO scheduleWorkflowMetadataDao) {
        this.metadataDAO = metadataDAO;
        this.scheduleWorkflowMetadataDao = scheduleWorkflowMetadataDao;
    }

    public void registerScheduleWorkflowDef(ScheduleWfDef def) {
        scheduleWorkflowMetadataDao.saveScheduleWorkflow(def);
    }

    public void updateScheduledWorkflowDef(ScheduleWfDef def) {
        scheduleWorkflowMetadataDao.updateScheduleWorkflow(def);
    }

    public ScheduleWfDef getScheduledWorkflowDef(String name, int version) {
        Optional<ScheduleWfDef> scheduledWorkflowDef =
                scheduleWorkflowMetadataDao.getScheduledWorkflowDef(name, version);
        return scheduledWorkflowDef.isPresent() ? scheduledWorkflowDef.get() : null;
    }
}
