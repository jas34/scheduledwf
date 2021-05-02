package net.jas34.scheduledwf.config;

import com.google.inject.AbstractModule;
import com.netflix.conductor.core.config.Configuration;

import net.jas34.scheduledwf.concurrent.LocalOnlyPermitDAO;
import net.jas34.scheduledwf.concurrent.LockingService;
import net.jas34.scheduledwf.concurrent.NoOpPermitDAO;
import net.jas34.scheduledwf.concurrent.PermitDAO;
import net.jas34.scheduledwf.concurrent.RedisPermitDAO;

/**
 * @author Jasbir Singh
 */
public class LockingServiceConfigurationModule extends AbstractModule {

    Configuration.LOCKING_SERVER lockingServer;

    public LockingServiceConfigurationModule(Configuration.LOCKING_SERVER lockingServer) {
        this.lockingServer = lockingServer;
    }

    @Override
    protected void configure() {
        switch (lockingServer) {
            case NOOP_LOCK:
                bind(PermitDAO.class).to(NoOpPermitDAO.class);
                break;

            case REDIS:
                bind(PermitDAO.class).to(RedisPermitDAO.class);
                break;

            case LOCAL_ONLY:
                bind(PermitDAO.class).to(LocalOnlyPermitDAO.class);
                break;

            case ZOOKEEPER:
                throw new UnsupportedOperationException(
                        LockingService.class.getCanonicalName() + " with zookeeper not supported.");
        }

        bind(LockingService.class);
    }
}
