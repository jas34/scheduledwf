package net.jas34.scheduledwf.concurrent;

import java.util.Optional;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * It makes sense to use this DAO only for testing.
 * For distributed env always use {@link RedisPermitDAO} or {@link ZookeeperPermitDAO}
 *
 * @author Jasbir Singh
 */
public class LocalOnlyPermitDAO implements PermitDAO {

    private static CacheLoader<String, Permit> LOADER = new CacheLoader<String, Permit>() {
        @Override
        public Permit load(String key) throws Exception {
            return new Permit(key);
        }
    };
    private static final LoadingCache<String, Permit> CACHE =
            CacheBuilder.newBuilder().maximumSize(100).build(LOADER);

    @Override
    public void insertOrUpdate(Permit permit) {
        CACHE.put(permit.getName(), permit);
    }

    @Override
    public Optional<Permit> fetchByName(String name) {
        return Optional.ofNullable(CACHE.getIfPresent(name));
    }
}
