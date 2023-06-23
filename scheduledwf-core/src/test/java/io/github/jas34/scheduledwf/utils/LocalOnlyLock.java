package io.github.jas34.scheduledwf.utils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.netflix.conductor.core.sync.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

// Same as com.netflix.conductor.contribs.lock.LocalOnlyLock
public class LocalOnlyLock implements Lock {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalOnlyLock.class);
    private static final CacheLoader<String, Semaphore> LOADER = new CacheLoader<String, Semaphore>() {
        public Semaphore load(String key) {
            return new Semaphore(1, true);
        }
    };
    private static final LoadingCache<String, Semaphore> CACHE;
    private static final ThreadGroup THREAD_GROUP;
    private static final ThreadFactory THREAD_FACTORY;
    private static final ScheduledExecutorService SCHEDULER;

    public LocalOnlyLock() {
    }

    public void acquireLock(String lockId) {
        LOGGER.trace("Locking {}", lockId);
        ((Semaphore)CACHE.getUnchecked(lockId)).acquireUninterruptibly();
    }

    public boolean acquireLock(String lockId, long timeToTry, TimeUnit unit) {
        try {
            LOGGER.trace("Locking {} with timeout {} {}", new Object[]{lockId, timeToTry, unit});
            return ((Semaphore)CACHE.getUnchecked(lockId)).tryAcquire(timeToTry, unit);
        } catch (InterruptedException var6) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(var6);
        }
    }

    public boolean acquireLock(String lockId, long timeToTry, long leaseTime, TimeUnit unit) {
        LOGGER.trace("Locking {} with timeout {} {} for {} {}", new Object[]{lockId, timeToTry, unit, leaseTime, unit});
        if (this.acquireLock(lockId, timeToTry, unit)) {
            LOGGER.trace("Releasing {} automatically after {} {}", new Object[]{lockId, leaseTime, unit});
            SCHEDULER.schedule(() -> {
                this.releaseLock(lockId);
            }, leaseTime, unit);
            return true;
        } else {
            return false;
        }
    }

    public void releaseLock(String lockId) {
        synchronized(CACHE) {
            if (((Semaphore)CACHE.getUnchecked(lockId)).availablePermits() == 0) {
                LOGGER.trace("Releasing {}", lockId);
                ((Semaphore)CACHE.getUnchecked(lockId)).release();
            }

        }
    }

    public void deleteLock(String lockId) {
        LOGGER.trace("Deleting {}", lockId);
        CACHE.invalidate(lockId);
    }

    @VisibleForTesting
    LoadingCache<String, Semaphore> cache() {
        return CACHE;
    }

    static {
        CACHE = CacheBuilder.newBuilder().build(LOADER);
        THREAD_GROUP = new ThreadGroup("LocalOnlyLock-scheduler");
        THREAD_FACTORY = (runnable) -> {
            return new Thread(THREAD_GROUP, runnable);
        };
        SCHEDULER = Executors.newScheduledThreadPool(1, THREAD_FACTORY);
    }
}

