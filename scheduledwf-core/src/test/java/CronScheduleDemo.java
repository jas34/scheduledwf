import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.coreoz.wisp.Job;
import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedule;
import com.coreoz.wisp.schedule.cron.CronSchedule;
import com.coreoz.wisp.stats.SchedulerStats;

/**
 * @author Jasbir Singh
 */
public class CronScheduleDemo {

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(10);
        Schedule schedule = CronSchedule.parseQuartzCron("0/1 1/1 * 1/1 * ? *");
        Scheduler scheduler = new Scheduler();
        Job job1 = scheduler.schedule(new MyTask("This is my job1", latch), schedule);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            Collection<Job> jobs = scheduler.jobStatus();
            // System.out.println("Job status");
            jobs.forEach(System.out::println);
            SchedulerStats stats = scheduler.stats();
            // System.out.println("Stats:" + stats);
        }, 0, 500, TimeUnit.MILLISECONDS);

        System.out.println("*********** nextExecution time=" + job1.schedule().nextExecutionInMillis(
                System.currentTimeMillis(), job1.executionsCount(), job1.lastExecutionEndedTimeInMillis()));
        latch.await(2000, TimeUnit.MILLISECONDS);
        scheduler.cancel(job1.name());
        System.out.println("job1 canceled......");
        System.out.println("*********** nextExecution time=" + job1.schedule().nextExecutionInMillis(
                System.currentTimeMillis(), job1.executionsCount(), job1.lastExecutionEndedTimeInMillis()));

        System.out.println("going to schedule job1 again......");
        job1 = scheduler.schedule(new MyTask("This is my job1", latch), schedule);
        System.out.println("*********** nextExecution time=" + job1.schedule().nextExecutionInMillis(
                System.currentTimeMillis(), job1.executionsCount(), job1.lastExecutionEndedTimeInMillis()));
        latch.await(5000, TimeUnit.MILLISECONDS);
        scheduler.gracefullyShutdown(Duration.ofMillis(10000));
        service.shutdown();
    }

    private static class MyTask implements Runnable {
        String message;
        CountDownLatch latch;

        public MyTask(String message, CountDownLatch latch) {
            this.message = message;
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread() + " : " + formattedDate() + " : " + message + " : "
                    + latch.getCount());
            latch.countDown();
        }
    }

    private static String formattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        return dateFormat.format(new Date());
    }
}
