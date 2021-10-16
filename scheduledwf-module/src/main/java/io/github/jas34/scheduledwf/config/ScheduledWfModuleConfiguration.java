package io.github.jas34.scheduledwf.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Description:<br>
 * Date: 27/09/21-8:47 am
 * 
 * @since
 * @author Jasbir Singh
 */
@Configuration
@Import(value = {CoreConfiguration.class, MySQLPersistenceConfiguration.class})
@ComponentScan(basePackages = "io.github.jas34.scheduledwf")
public class ScheduledWfModuleConfiguration {

}
