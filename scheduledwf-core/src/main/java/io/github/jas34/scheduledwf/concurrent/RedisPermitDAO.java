package io.github.jas34.scheduledwf.concurrent;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RMapCache;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jasbir Singh
 */
public class RedisPermitDAO implements PermitDAO {

    private RMapCache<String, String> mapCache;

    private ObjectMapper objectMapper;

    private static final String PERMITTER_NAME_SPACE = "scheduled.workflow.permitter.namespace";

    private static final String PERMITTER_NAME_SPACE_DEFAULT_VALUE = "permitter";

    public RedisPermitDAO(Redisson redisson, String mapName, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.mapCache = redisson.getMapCache(mapName);
    }

    @Override
    public void insertOrUpdate(Permit permit) {
        mapCache.put(permit.getName(), toJson(permit), 20, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Permit> fetchByName(String name) {
        return Optional.ofNullable(readValue(mapCache.get(name)));
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Permit readValue(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return objectMapper.readValue(json, Permit.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
