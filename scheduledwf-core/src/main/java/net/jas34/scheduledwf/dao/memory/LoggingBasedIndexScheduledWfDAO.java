package net.jas34.scheduledwf.dao.memory;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import net.jas34.scheduledwf.run.ScheduledWfExecData;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class LoggingBasedIndexScheduledWfDAO implements IndexScheduledWfDAO {

    private final Logger logger = LoggerFactory.getLogger(LoggingBasedIndexScheduledWfDAO.class);

    @Override
    public void indexCreatedScheduledWorkFlow(ScheduledWorkFlow scheduledWorkFlow) {
        logger.info("Fake indexing done for scheduled workflow created with name={}", scheduledWorkFlow.getName());
    }

    @Override
    public void indexShutdownScheduledWorkFlow(ScheduledWorkFlow scheduledWorkFlow) {
        logger.info("Fake indexing done for shutdown of scheduled workflow with name={}", scheduledWorkFlow.getName());
    }

    @Override
    public void indexExecutedScheduledWorkflow(ScheduledWfExecData execData) {
        logger.info("Fake indexing done for executed work workflow. Data captured={}", execData);
    }

}
