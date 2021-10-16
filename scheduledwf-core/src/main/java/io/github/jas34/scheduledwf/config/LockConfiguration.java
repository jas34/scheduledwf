package io.github.jas34.scheduledwf.config;

import org.redisson.Redisson;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redislock.config.RedisLockProperties;

import io.github.jas34.scheduledwf.concurrent.LocalOnlyPermitDAO;
import io.github.jas34.scheduledwf.concurrent.NoOpPermitDAO;
import io.github.jas34.scheduledwf.concurrent.PermitDAO;
import io.github.jas34.scheduledwf.concurrent.RedisPermitDAO;

/**
 * Description:<br>
 * Date: 21/09/21-5:24 pm
 * 
 * @since v2.0.0
 * @author Jasbir Singh
 */
@Configuration
public class LockConfiguration {

    @Bean
    @ConditionalOnProperty(name = "conductor.workflow-execution-lock.type", havingValue = "noop_lock",
            matchIfMissing = true)
    public PermitDAO noOpPermitDAO() {
        return new NoOpPermitDAO();
    }

    @Bean
    @ConditionalOnProperty(name = "conductor.workflow-execution-lock.type", havingValue = "local_only")
    public PermitDAO localOnlyPermitDAO() {
        return new LocalOnlyPermitDAO();
    }

    @Bean
    @ConditionalOnProperty(name = "conductor.workflow-execution-lock.type", havingValue = "redis")
    public PermitDAO redisPermitDAO(Redisson redisson, RedisLockProperties redisLockProperties,
            ConductorProperties conductorProperties, ObjectMapper mapper) {
        String mapName =
                redisLockProperties.getNamespace() + "." + conductorProperties.getStack() + ".mapCache";
        return new RedisPermitDAO(redisson, mapName, mapper);
    }
}
