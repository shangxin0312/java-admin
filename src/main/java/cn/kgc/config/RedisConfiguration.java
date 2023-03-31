package cn.kgc.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.net.UnknownHostException;
import java.util.LinkedHashSet;
import java.util.List;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        //准备序列化器
        RedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        //修改默认的键及值的序列化器
        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);

        return template;
    }

    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //准备序列化器
        RedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        //获取默认配置参数
        org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig();
        //修改值的序列化器
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));


        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
