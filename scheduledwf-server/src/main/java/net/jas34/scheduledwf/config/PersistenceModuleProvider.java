package net.jas34.scheduledwf.config;


import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.netflix.conductor.core.config.Configuration;
import com.netflix.conductor.core.config.SystemPropertiesConfiguration;

/**
 * @author Jasbir Singh
 */
//public class PersistenceModuleProvider implements Provider<List<AbstractModule>> {
//public class PersistenceModuleProvider implements Provider<AbstractModule> {
public class PersistenceModuleProvider implements Provider<AbstractModule> {
	private final SystemPropertiesConfiguration configuration;

	@Inject
	PersistenceModuleProvider(SystemPropertiesConfiguration configuration) {
		this.configuration = configuration;
	}


//	@Override
//	public List<AbstractModule> get() {
//		List<AbstractModule> modules = new ArrayList<>();
////		modules.add(get1());
////		modules.add(new ScheduledWfServerModule());
//		AbstractModule resolvedModule = (AbstractModule) Modules.combine(get1());//.override(get1()).with(new ScheduledWfServerModule());
//		return singletonList(resolvedModule);
////		return modules;
//	}

	@Override
	public AbstractModule get() {
//	public AbstractModule get1() {
		if(Configuration.DB.MEMORY.equals(configuration.getDB())) {
			return new InMemoryPersistenceModule();
		}

		if(Configuration.DB.MYSQL.equals(configuration.getDB())) {
			return new MySQLPersistenceModule();
		}

		throw new UnsupportedOperationException("Only MEMORY AND MYSQL persistence supported for scheduled workflow module.");
	}
}
