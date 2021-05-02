package net.jas34.scheduledwf.scheduler;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jas34.scheduledwf.concurrent.LockingService;
import net.jas34.scheduledwf.metadata.ScheduledTaskDef;

/**
 * @author Jasbir Singh
 */
public class TestLockableTask implements Runnable {
    private Logger log = LoggerFactory.getLogger(TestLockableTask.class);

    private LockingService lockingService;

    private ScheduledTaskDef taskDef;

    private CountDownLatch latch;

    private static int executionCount = 0;

    TestLockableTask(LockingService lockingService, ScheduledTaskDef taskDef, CountDownLatch latch) {
        this.lockingService = lockingService;
        this.taskDef = taskDef;
        this.latch = latch;
    }

    @Override
    public void run() {
        if (!lockingService.acquireLock(taskDef)) {
            log.info("Unable to acquire lock for task: {}", taskDef.getName());
            return;
        }
        log.info("==> I am running task {} for {} time", taskDef.getName(), latch.getCount());
        latch.countDown();
        executionCount++;
        lockingService.releaseLock(taskDef, true);
    }

    static int getExecutionCount() {
        return executionCount;
    }
}
