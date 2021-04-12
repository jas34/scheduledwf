package net.jas34.scheduledwf.service;

import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.netflix.conductor.core.utils.Lock;
import net.jas34.scheduledwf.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jasbir Singh
 */
@Singleton
public class LockingService {
	private final Logger logger = LoggerFactory.getLogger(LockingService.class);

	private Provider<Lock> lockProvider;

	@Inject
	LockingService(Provider<Lock> lockProvider) {
		this.lockProvider = lockProvider;
	}

	//	acquireLock(String lockId, long timeToTry, long leaseTime, TimeUnit unit);
    public boolean acquireLock(String lockId) {
        Lock lock = lockProvider.get();
        if (lock.acquireLock(lockId, 100, 500, TimeUnit.MILLISECONDS)) {
            logger.debug("lock acquired on node={} against lockId={}", CommonUtils.resolveNodeAddress(), lockId);
            return true;
        }

        logger.error("Unable to acquire lock for lockit={}", lockId);
        return false;
    }

	public void releaseLock(String lockId, boolean withDeleteLock) {
		Lock lock = lockProvider.get();
		lock.releaseLock(lockId);
		logger.debug("lock released on node={} against lockId={}", CommonUtils.resolveNodeAddress(), lockId);

		if(withDeleteLock) {
			lock.deleteLock(lockId);
			logger.debug("lock reference deleted from node={} for lockId={}", CommonUtils.resolveNodeAddress(), lockId);
		}
	}
}
