package net.jas34.scheduledwf.dao.mysql;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import com.netflix.conductor.mysql.MySQLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jasbir Singh
 */
public class TestConfiguration implements MySQLConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(TestConfiguration.class);
	private static final Map<String, String> testProperties = new HashMap<>();

	@Override
	public int getSweepFrequency() {
		return getIntProperty("decider.sweep.frequency.seconds", 30);
	}

	@Override
	public boolean disableSweep() {
		String disable = getProperty("decider.sweep.disable", "false");
		return Boolean.getBoolean(disable);
	}

	@Override
	public boolean disableAsyncWorkers() {
		String disable = getProperty("conductor.disable.async.workers", "false");
		return Boolean.getBoolean(disable);
	}

	@Override
	public boolean isEventMessageIndexingEnabled() {
		return true;
	}

	@Override
	public boolean isEventExecutionIndexingEnabled() {
		return true;
	}

	@Override
	public String getServerId() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "unknown";
		}
	}

	@Override
	public String getEnvironment() {
		return getProperty("environment", "test");
	}

	@Override
	public String getStack() {
		return getProperty("STACK", "test");
	}

	@Override
	public String getAppId() {
		return getProperty("APP_ID", "conductor");
	}

	@Override
	public String getRegion() {
		return getProperty("EC2_REGION", "us-east-1");
	}

	@Override
	public String getAvailabilityZone() {
		return getProperty("EC2_AVAILABILITY_ZONE", "us-east-1c");
	}

	public void setProperty(String key, String value) {
		testProperties.put(key, value);
	}

	@Override
	public int getIntProperty(String key, int defaultValue) {
		String val = getProperty(key, Integer.toString(defaultValue));
		try {
			defaultValue = Integer.parseInt(val);
		} catch (NumberFormatException ignored) {
		}
		return defaultValue;
	}

	@Override
	public long getLongProperty(String key, long defaultValue) {
		String val = getProperty(key, Long.toString(defaultValue));
		try {
			defaultValue = Long.parseLong(val);
		} catch (NumberFormatException e) {
			logger.error("Error parsing the Long value for Key:{} , returning a default value: {}", key, defaultValue);
		}
		return defaultValue;
	}

	@SuppressWarnings("Duplicates")
	@Override
	public String getProperty(String key, String defaultValue) {
		String val;
		if (testProperties.containsKey(key)) {
			return testProperties.get(key);
		}

		val = System.getenv(key.replace('.', '_'));
		if (val == null || val.isEmpty()) {
			val = Optional.ofNullable(System.getProperty(key))
					.orElse(defaultValue);
		}
		return val;
	}

	@Override
	public Map<String, Object> getAll() {
		Map<String, Object> map = new HashMap<>();
		Properties props = System.getProperties();
		props.forEach((key, value) -> map.put(key.toString(), value));
		map.putAll(testProperties);
		return map;
	}

	@Override
	public Long getWorkflowInputPayloadSizeThresholdKB() {
		return 5120L;
	}

	@Override
	public Long getMaxWorkflowInputPayloadSizeThresholdKB() {
		return 10240L;
	}

	@Override
	public Long getWorkflowOutputPayloadSizeThresholdKB() {
		return 5120L;
	}
	@Override
	public boolean getBooleanProperty(String name, boolean defaultValue) {
		return false;
	}

	@Override
	public Long getMaxWorkflowOutputPayloadSizeThresholdKB() {
		return 10240L;
	}

	@Override
	public Long getMaxWorkflowVariablesPayloadSizeThresholdKB() {
		return 256L;
	}

	@Override
	public Long getTaskInputPayloadSizeThresholdKB() {
		return 3072L;
	}

	@Override
	public Long getMaxTaskInputPayloadSizeThresholdKB() {
		return 10240L;
	}

	@Override
	public Long getTaskOutputPayloadSizeThresholdKB() {
		return 3072L;
	}

	@Override
	public Long getMaxTaskOutputPayloadSizeThresholdKB() {
		return 10240L;
	}
}
