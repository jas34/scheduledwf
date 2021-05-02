package net.jas34.scheduledwf.concurrent;

import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jasbir Singh
 */
@Singleton
public class ExecutionPermitter {

    private Logger logger = LoggerFactory.getLogger(ExecutionPermitter.class);

    private PermitDAO permitDAO;

    @Inject
    public ExecutionPermitter(PermitDAO permitDAO) {
        this.permitDAO = permitDAO;
    }

    public boolean issue(ScheduledTaskDef taskDef) {
        Optional<Permit> permitOptional = permitDAO.fetchByName(taskDef.getName());
        Permit permit;
        if (!permitOptional.isPresent()) {
            permit = new Permit(taskDef.getName());
            permitDAO.insertOrUpdate(permit);
            logger.debug("Creating and issue new permit: {}", permit);
            return true;
        }

        permit = permitOptional.get();
        if (!permit.isUsed() && System.currentTimeMillis() <= permit.getInUseUpto()) {
            permitDAO.insertOrUpdate(permit);
            logger.debug("Found existing non-used permit: {}", permit);
            return true;
        }

        if (permit.isUsed() && System.currentTimeMillis() > permit.getInUseUpto()) {
            permit = new Permit(taskDef.getName());
            permitDAO.insertOrUpdate(permit);
            logger.debug("Found existing permit which is expired. Creating new permit: {}", permit);
            return true;
        }

        logger.debug("No use able permit found: {}", permit);
        return false;
    }

    public void giveBack(ScheduledTaskDef taskDef) {
        Optional<Permit> permitOptional = permitDAO.fetchByName(taskDef.getName());
        if (permitOptional.isPresent()) {
            Permit permit = permitOptional.get();
            permit.setUsed(true);
            permitDAO.insertOrUpdate(permitOptional.get());
            logger.debug("Got back permit, marking as used: {}", permit);
            return;
        }

        throw new IllegalStateException("Can't give back permit which was never issued.");
    }
}
