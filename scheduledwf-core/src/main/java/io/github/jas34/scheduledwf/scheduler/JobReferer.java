package io.github.jas34.scheduledwf.scheduler;

/**
 * @author Jasbir Singh
 */
public interface JobReferer<T> {
    T getJobReference();
}
