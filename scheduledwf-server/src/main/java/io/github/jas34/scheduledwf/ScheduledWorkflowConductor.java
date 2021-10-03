package io.github.jas34.scheduledwf;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;

/**
 * Description: Replicating `com.netflix.conductor.Conductor` because conductor-server-boot is a
 * spring boot application packaged inside BOOT-INF. We can't use this as a dependency as per
 * <a href=
 * "https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#howto-create-an-additional-executable-jar">documentation</a>.<br>
 * Date: 01/10/21-8:37 am
 * 
 * @since
 * @author Jasbir Singh
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "com.netflix.conductor")
public class ScheduledWorkflowConductor {
	private static final Logger log = LoggerFactory.getLogger(ScheduledWorkflowConductor.class);

	public static void main(String[] args) throws IOException {
		loadExternalConfig();

		SpringApplication.run(ScheduledWorkflowConductor.class, args);
	}

	/**
	 * Reads properties from the location specified in <code>CONDUCTOR_CONFIG_FILE</code>
	 * and sets them as system properties so they override the default properties.
	 * <p>
	 * Spring Boot property hierarchy is documented here,
	 * https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config
	 *
	 * @throws IOException if file can't be read.
	 */
	private static void loadExternalConfig() throws IOException {
		String configFile = System.getProperty("CONDUCTOR_CONFIG_FILE");
		if (!StringUtils.isEmpty(configFile)) {
			FileSystemResource resource = new FileSystemResource(configFile);
			if (resource.exists()) {
				Properties properties = new Properties();
				properties.load(resource.getInputStream());
				properties.forEach((key, value) -> System.setProperty((String) key, (String) value));
				log.info("Loaded {} properties from {}", properties.size(), configFile);
			}else {
				log.warn("Ignoring {} since it does not exist", configFile);
			}
		}
	}
}
