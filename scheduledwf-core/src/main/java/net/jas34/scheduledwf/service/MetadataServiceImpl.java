package net.jas34.scheduledwf.service;

import com.netflix.conductor.dao.MetadataDAO;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;

/**
 * @author Jasbir Singh
 */
public class MetadataServiceImpl implements MetadataService {

    private final MetadataDAO metadataDAO;

    private final ScheduledWfMetadataDAO scheduleWorkflowMetadataDao;

    public MetadataServiceImpl(MetadataDAO metadataDAO, ScheduledWfMetadataDAO scheduleWorkflowMetadataDao) {
        this.metadataDAO = metadataDAO;
        this.scheduleWorkflowMetadataDao = scheduleWorkflowMetadataDao;
    }

    public void registerScheduleWorkflowDef(ScheduleWfDef def) {

    }

    public void updateScheduledWorkflowDef(ScheduleWfDef def) {

    }

    public ScheduleWfDef getScheduledWorkflowDef(String name, int version) {
        return null;
    }
}
