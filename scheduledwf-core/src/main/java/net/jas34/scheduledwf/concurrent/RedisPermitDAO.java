package net.jas34.scheduledwf.concurrent;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RMapCache;

import com.netflix.conductor.core.config.Configuration;

/**
 * @author Jasbir Singh
 */
@Singleton
public class RedisPermitDAO implements PermitDAO {

    private RMapCache<String, String> mapCache;

    private ObjectMapper objectMapper;

    private static final String PERMITTER_NAME_SPACE = "scheduled.workflow.permitter.namespace";

    private static final String PERMITTER_NAME_SPACE_DEFAULT_VALUE = "permitter";

    @Inject
    public RedisPermitDAO(Redisson redisson, Configuration configuration, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.mapCache = redisson.getMapCache(resolveMapName(configuration));
    }

    @Override
    public void insertOrUpdate(Permit permit) {
        mapCache.put(permit.getName(), toJson(permit), 20, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Permit> fetchByName(String name) {
        return Optional.ofNullable(readValue(mapCache.get(name)));
    }

    private String resolveMapName(Configuration configuration) {
        return configuration.getProperty(PERMITTER_NAME_SPACE, PERMITTER_NAME_SPACE_DEFAULT_VALUE) + "."
                + configuration.getStack() + ".mapCache";
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Permit readValue(String json) {
        if(StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return objectMapper.readValue(json, Permit.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
