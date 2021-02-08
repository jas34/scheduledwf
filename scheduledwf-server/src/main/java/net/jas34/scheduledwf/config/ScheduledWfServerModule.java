package net.jas34.scheduledwf.config;

import com.google.inject.AbstractModule;
import net.jas34.scheduledwf.resources.ScheduleWfResource;

/**
 * @author Jasbir Singh
 */
public class ScheduledWfServerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ScheduledWorkdlowsModule());
        bind(ScheduleWfResource.class).asEagerSingleton();
    }
}
