package net.jas34.scheduledwf.scheduler;


import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coreoz.wisp.Job;
import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedule;
import com.coreoz.wisp.schedule.cron.CronSchedule;
import com.google.inject.Provider;
import com.netflix.conductor.core.utils.IDGenerator;
import com.netflix.conductor.core.utils.LocalOnlyLock;
import com.netflix.conductor.core.utils.Lock;

import net.jas34.scheduledwf.concurrent.ExecutionPermitter;
import net.jas34.scheduledwf.concurrent.LocalOnlyPermitDAO;
import net.jas34.scheduledwf.concurrent.LockingService;
import net.jas34.scheduledwf.metadata.ScheduledTaskDef;

/**
 * @author Jasbir Singh
 */
public class TestLockingService {
    Logger log = LoggerFactory.getLogger(TestLockingService.class);
    private static final String TASK_1 = "task1";
    private static final String TASK_2 = "task2";
    private LockingService lockingService;
    private Scheduler scheduler;

    @Before
    public void setup() {
        lockingService =
                new LockingService(new LockProvider(), new ExecutionPermitter(new LocalOnlyPermitDAO()));
        this.scheduler = new Scheduler();
    }

    @Test
    public void test() throws Exception {
        CountDownLatch latch = new CountDownLatch(10);
        Schedule schedule = CronSchedule.parseQuartzCron("0/1 * * 1/1 * ? *");
        Job scheduledJob1 = scheduler.schedule(TASK_1,
                new TestLockableTask(lockingService, createScheduledTaskDef(TASK_1), latch), schedule);

        Job scheduledJob2 = scheduler.schedule(TASK_2,
                new TestLockableTask(lockingService, createScheduledTaskDef(TASK_1), latch), schedule);

        latch.await();
        scheduler.gracefullyShutdown(Duration.ofMillis(1000));
        log.info("Task Executed for: {} times", TestLockableTask.getExecutionCount());
        assertTrue(5 < scheduledJob1.executionsCount());
        assertTrue(5 < scheduledJob2.executionsCount());
    }

    static class LockProvider implements Provider<Lock> {
        @Override
        public Lock get() {
            return new LocalOnlyLock();
        }
    }

    private ScheduledTaskDef createScheduledTaskDef(String taskName) {
        return new ScheduledTaskDef(taskName, taskName, 1, null, IDGenerator.generate(),
                IDGenerator.generate());
    }
}
