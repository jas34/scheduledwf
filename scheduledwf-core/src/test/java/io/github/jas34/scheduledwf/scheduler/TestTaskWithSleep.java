package io.github.jas34.scheduledwf.scheduler;

import java.util.concurrent.TimeUnit;

import io.github.jas34.scheduledwf.metadata.ScheduledTaskDef;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;

/**
 * @author Jasbir Singh
 */
public class TestTaskWithSleep extends TestTask {
	public TestTaskWithSleep(ScheduledTaskDef taskDef, IndexExecutionDataCallback indexExecutionDataCallback) {
		super(taskDef, indexExecutionDataCallback);
	}

	@Override
	public void run() {
		System.out.println("Going to sleep for 3 sec");
		sleepUninterruptibly(2000, TimeUnit.MILLISECONDS);
		System.out.println("Resuming to work from sleep.");
		super.run();
	}
}
