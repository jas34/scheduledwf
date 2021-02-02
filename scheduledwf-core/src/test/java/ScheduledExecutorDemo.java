import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Jasbir Singh
 */
public class ScheduledExecutorDemo {

//    private static int counter = 0;

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Task(countDownLatch), 500, 500, TimeUnit.MILLISECONDS);
        ScheduledFuture<?> scheduledFuture1 = scheduledExecutorService.scheduleAtFixedRate(new Task(countDownLatch), 500, 500, TimeUnit.MILLISECONDS);

        try {
            System.out.println("waiting for scheduled process to finish");
            countDownLatch.await(4000, TimeUnit.MILLISECONDS);
            System.out.println("Scheduled task completed");
//            scheduledFuture.cancel(false);
//            System.out.println("Scheduled task cancelled");
            scheduledExecutorService.shutdown();
            System.out.println("scheduledExecutorService shutdown done");
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

//        while (true) {
//            if(counter >= 5){
//                scheduledFuture.cancel(true);
//                scheduledExecutorService.shutdown();
//                System.out.println("scheduled task cancelled");
//                break;
//            }else {
//                System.out.println("waiting for cancellation");
//            }
//        }
    }

    private static class Task implements Runnable {
        CountDownLatch latch;

        public Task(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println(
                    Thread.currentThread() + " : " + new Date() + " : I am running in periodic scheduler : " + latch.getCount());
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }
}
