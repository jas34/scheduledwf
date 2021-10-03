package io.github.jas34.scheduledwf.concurrent;

import java.util.concurrent.TimeUnit;

import com.netflix.conductor.core.sync.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.jas34.scheduledwf.metadata.ScheduledTaskDef;
import io.github.jas34.scheduledwf.utils.CommonUtils;

import org.springframework.stereotype.Component;

/**
 * @author Jasbir Singh
 */
@Component
public class LockingService {
    private final Logger logger = LoggerFactory.getLogger(LockingService.class);

    private Lock lock;

    private ExecutionPermitter permitter;

    public LockingService(Lock lock, ExecutionPermitter permitter) {
        this.lock = lock;
        this.permitter = permitter;
    }

    public boolean acquireLock(ScheduledTaskDef taskDef) {
        String lockId = resolveLockId(taskDef);
        if (lock.acquireLock(lockId, 100, 500, TimeUnit.MILLISECONDS)) {
            if (!permitter.issue(taskDef)) {
                logger.debug("lock acquired on node={} against lockId={} but permit not received.",
                        CommonUtils.resolveNodeAddress(), lockId);
                return false;
            }

            logger.debug("lock acquired on node={} against lockId={}", CommonUtils.resolveNodeAddress(),
                    lockId);
            return true;
        }

        logger.debug("Unable to acquire lock for lockId={}", lockId);
        return false;
    }

    public void releaseLock(ScheduledTaskDef taskDef, boolean withDeleteLock) {
        String lockId = resolveLockId(taskDef);
        permitter.giveBack(taskDef);
        lock.releaseLock(lockId);
        logger.debug("lock released on node={} against lockId={}", CommonUtils.resolveNodeAddress(), lockId);

        if (withDeleteLock) {
            lock.deleteLock(lockId);
            logger.debug("lock reference deleted from node={} for lockId={}",
                    CommonUtils.resolveNodeAddress(), lockId);
        }
    }

    private String resolveLockId(ScheduledTaskDef taskDef) {
        return taskDef.getWfName() + "-" + taskDef.getWfVersion();
    }

}
