package net.jas34.scheduledwf.config;

import com.google.inject.AbstractModule;
import net.jas34.scheduledwf.resources.ScheduleWfResource;
import net.jas34.scheduledwf.resources.SchedulerResource;

/**
 * @author Jasbir Singh
 */
public class ScheduledWfServerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ScheduledWorkdlowsModule());
        bind(ScheduleWfResource.class);
        bind(SchedulerResource.class);
    }
}
