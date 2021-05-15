package io.github.jas34.scheduledwf.config;

import javax.inject.Provider;

import com.coreoz.wisp.Scheduler;

/**
 * @author Jasbir Singh
 */
public class WispSchedulerProvider implements Provider<Scheduler> {

    // TODO: as of now provide with default configuration of pool. Will revisit later
    @Override
    public Scheduler get() {
        return new Scheduler();
    }
}
