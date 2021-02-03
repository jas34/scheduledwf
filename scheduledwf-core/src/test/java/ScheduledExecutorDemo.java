import java.text.SimpleDateFormat;
import java.time.Instant;
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
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        CountDownLatch countDownLatch1 = new CountDownLatch(10);
        CountDownLatch countDownLatch2 = new CountDownLatch(100);
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Task(countDownLatch1, "I am running in periodic scheduler1"), 0, 5000, TimeUnit.MILLISECONDS);
        ScheduledFuture<?> scheduledFuture1 = scheduledExecutorService.scheduleAtFixedRate(new Task(countDownLatch2, "I am running in periodic scheduler2"), 0, 1000, TimeUnit.MILLISECONDS);

        try {
            System.out.println("waiting for scheduled process to finish");
            countDownLatch1.await(60000, TimeUnit.MILLISECONDS);
            scheduledFuture.cancel(true);
            scheduledFuture.cancel(true);
            System.out.println("Scheduled task1 cancelled");

            System.out.println("======> Taking pause.........");
            Thread.sleep(2000);
            countDownLatch1.await(2000, TimeUnit.MILLISECONDS);
            scheduledFuture1.cancel(true);
            System.out.println("Scheduled task2 cancelled");
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
        String message;

        public Task(CountDownLatch latch, String message) {
            this.latch = latch;
            this.message  = message;
        }

        @Override
        public void run() {
            System.out.println(
                    Thread.currentThread() + " : " + formattedDate() + " : " + message +" : " + latch.getCount());
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }

    private static String formattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        return dateFormat.format(new Date());
    }
}
