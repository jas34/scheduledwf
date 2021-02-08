package net.jas34.scheduledwf.config;

import com.coreoz.wisp.Scheduler;

import javax.inject.Provider;

/**
 * @author Jasbir Singh
 */
public class WispSchedulerProvider implements Provider<Scheduler> {

    //TODO: as of now provide with default configuration of pool. Will revisit later
    @Override
    public Scheduler get() {
        return new Scheduler();
    }
}
