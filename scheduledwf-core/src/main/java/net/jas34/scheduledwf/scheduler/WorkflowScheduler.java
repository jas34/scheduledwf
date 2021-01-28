package net.jas34.scheduledwf.scheduler;

/**
 * @author Jasbir Singh
 */
public interface WorkflowScheduler {

    String startWorkFlow(String workflowName);

    void shutdown();

    boolean pause();

    boolean resume();
}
