package top.bulk.mq.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;

/**
 * Redis配置
 *
 * @author 散装java
 * @date 2023-02-03
 */
@Configuration
public class RedisConfig {
    /**
     * 配置 一个 RedisTemplate bean
     * 为啥要加 @SuppressWarnings("all") :
     * 相传 Spring Boot 2.7 以上的版本 redisConnectionFactory 会爆红提示bean不存在，但其实是正常使用的
     * ps: 不要加 @ConditionalOnSingleCandidate 注解，会导致你自己定义的这个注解不会被注入
     *
     * @param redisConnectionFactory factory
     * @return RedisTemplate
     */
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> redisSerializer = RedisSerializer.string();
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setDefaultSerializer(redisSerializer);
        redisTemplate.setStringSerializer(redisSerializer);
        return redisTemplate;
    }

    /**
     * 获取一个 StreamMessageListenerContainer
     *
     * @param executor      执行线程池
     * @param redisTemplate template
     * @return redisListenerContainer
     */
    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> redisListenerContainer(ThreadPoolTaskExecutor executor, RedisTemplate<Object, Object> redisTemplate) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        // 拉取消息超时时间
                        .pollTimeout(Duration.ofSeconds(5))
                        // 批量抓取消息
                        .batchSize(1)
                        // 传递的数据类型
                        .targetType(String.class)
                        .executor(executor)
                        .build();
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        assert connectionFactory != null;
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer
                .create(connectionFactory, options);
        container.start();
        return container;
    }

}
