package net.jas34.scheduledwf.concurrent;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.netflix.conductor.core.utils.Lock;

import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.utils.CommonUtils;

/**
 * @author Jasbir Singh
 */
@Singleton
public class LockingService {
    private final Logger logger = LoggerFactory.getLogger(LockingService.class);

    private Provider<Lock> lockProvider;

    private ExecutionPermitter permitter;

    @Inject
    public LockingService(Provider<Lock> lockProvider, ExecutionPermitter permitter) {
        this.lockProvider = lockProvider;
        this.permitter = permitter;
    }

    public boolean acquireLock(ScheduledTaskDef taskDef) {
        Lock lock = lockProvider.get();
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
        Lock lock = lockProvider.get();
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
