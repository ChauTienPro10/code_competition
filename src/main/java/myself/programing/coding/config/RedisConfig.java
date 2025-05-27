package myself.programing.coding.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import org.redisson.jcache.JCachingProvider;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        JCachingProvider provider = new JCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();
        cacheManager.createCache("bucket4j-cache", RedissonConfiguration.fromInstance(redissonClient));
        return cacheManager;
    }

    @Bean
    public ProxyManager<String> proxyManager(CacheManager cacheManager) {
        return new JCacheProxyManager<>(cacheManager.getCache("bucket4j-cache"));
    }
}
