package io.github.jas34.scheduledwf.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jas34.scheduledwf.concurrent.Permit;
import io.github.jas34.scheduledwf.concurrent.RedisPermitDAO;

import redis.embedded.RedisServer;

/**
 * @author Jasbir Singh
 */
public class TestRedisPermitDAO {

    private static RedisServer redisServer;
    private static RedissonClient redisson;
    private static ObjectMapper objectMapper;
    private RedisPermitDAO redisPermitDAO;

    @BeforeClass
    public static void setUp() throws Exception {

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.setDefaultPropertyInclusion(
                JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.ALWAYS));

        String testServerAddress = "redis://127.0.0.1:6371";
        redisServer = new RedisServer(6371);
        if (redisServer.isActive()) {
            redisServer.stop();
        }
        redisServer.start();

        Config redissonConfig = new Config();
        redissonConfig.useSingleServer().setAddress(testServerAddress).setTimeout(10000);
        redisson = Redisson.create(redissonConfig);
    }

    @Before
    public void beforeTest() {
        redisPermitDAO = new RedisPermitDAO((Redisson) redisson, "TestRedisPermitDAO", objectMapper);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        redisServer.stop();
    }

    @Test
    public void test_insertOrUpdate_and_fetchByName() {
        Permit permit = new Permit("redis_test_permit");
        redisPermitDAO.insertOrUpdate(permit);
        Optional<Permit> optionalPermit = redisPermitDAO.fetchByName("redis_test_permit");
        assertTrue(optionalPermit.isPresent());
        assertEquals(optionalPermit.get().getId(), permit.getId());
    }
}
