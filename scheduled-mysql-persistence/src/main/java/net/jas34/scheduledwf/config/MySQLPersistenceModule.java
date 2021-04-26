package net.jas34.scheduledwf.config;

import javax.sql.DataSource;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.netflix.conductor.mysql.MySQLConfiguration;
import com.netflix.conductor.mysql.MySQLDataSourceProvider;
import com.netflix.conductor.mysql.SystemPropertiesMySQLConfiguration;

import net.jas34.scheduledwf.dao.IndexScheduledWfDAO;
import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.dao.mysql.MySQLIndexScheduledWfDAO;
import net.jas34.scheduledwf.dao.mysql.MySQLScheduledWfMetaDataDao;

/**
 * @author Jasbir Singh
 */
public class MySQLPersistenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MySQLConfiguration.class).to(SystemPropertiesMySQLConfiguration.class);
		bind(DataSource.class).toProvider(MySQLDataSourceProvider.class).in(Scopes.SINGLETON);
		bind(ScheduledWfMetadataDAO.class).to(MySQLScheduledWfMetaDataDao.class);
		bind(IndexScheduledWfDAO.class).to(MySQLIndexScheduledWfDAO.class);
	}
}
