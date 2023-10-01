package top.bulk.mq.redis.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import top.bulk.mq.redis.annotation.MessageHandler;
import top.bulk.mq.redis.annotation.MessageListener;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Stream方式 实现消息队列
 *
 * @author 散装java
 * @date 2023-02-06
 */
@MessageHandler(value = MessageListener.Mode.STREAM)
@Slf4j
public class StreamMessageHandler extends AbstractMessageHandler {
    @Resource
    StreamMessageListenerContainer<String, ObjectRecord<String, String>> redisListenerContainer;

    public StreamMessageHandler(RedisTemplate<Object, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public synchronized void invokeMessage(Method method) {
        // 获取注解中的相关信息
        MessageListener annotation = method.getAnnotation(MessageListener.class);
        String streamKey = annotation.streamKey();
        String consumerGroup = annotation.consumerGroup();
        String consumerName = annotation.consumerName();
        boolean pending = annotation.pending();

        // 检测组是否存在
        this.checkAndCreatGroup(streamKey, consumerGroup);

        // 获取 StreamOffset
        StreamOffset<String> offset = this.getOffset(streamKey, pending);

        // 创建消费者
        Consumer consumer = Consumer.from(consumerGroup, consumerName);

        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest =
                StreamMessageListenerContainer
                        .StreamReadRequest.builder(offset)
                        .errorHandler((error) -> log.error("redis stream 异常 ：{}", error.getMessage()))
                        .cancelOnError(e -> false)
                        .consumer(consumer)
                        //关闭自动ack确认
                        .autoAcknowledge(false)
                        .build();

        // 指定消费者对象注册到容器中去
        redisListenerContainer.register(streamReadRequest, (message) -> {
            Class<?> declaringClass = method.getDeclaringClass();
            Object bean = applicationContext.getBean(declaringClass);
            try {
                method.invoke(bean, message);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private StreamOffset<String> getOffset(String streamKey, boolean pending) {
        if (pending) {
            // 获取尚未 ack 的消息
            return StreamOffset.create(streamKey, ReadOffset.from("0"));
        }
        // 指定消费最新的消息
        return StreamOffset.create(streamKey, ReadOffset.lastConsumed());
    }

    /**
     * 校验消费组是否存在，不存在则创建，否则会出现异常
     * Error in execution; nested exception is io.lettuce.core.RedisCommandExecutionException: NOGROUP No such key 'streamKey' or consumer group 'consumerGroup' in XREADGROUP with GROUP option
     *
     * @param streamKey     streamKey
     * @param consumerGroup consumerGroup
     */
    private void checkAndCreatGroup(String streamKey, String consumerGroup) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(streamKey))) {
            StreamOperations<Object, Object, Object> streamOperations = redisTemplate.opsForStream();
            StreamInfo.XInfoGroups groups = streamOperations.groups(streamKey);
            if (groups.isEmpty() || groups.stream().noneMatch(data -> consumerGroup.equals(data.groupName()))) {
                creatGroup(streamKey, consumerGroup);
            } else {
                groups.stream().forEach(g -> {
                    log.info("XInfoGroups:{}", g);
                    StreamInfo.XInfoConsumers consumers = streamOperations.consumers(streamKey, g.groupName());
                    log.info("XInfoConsumers:{}", consumers);
                });
            }
        } else {
            creatGroup(streamKey, consumerGroup);
        }
    }

    private void creatGroup(String key, String group) {
        StreamOperations<Object, Object, Object> streamOperations = redisTemplate.opsForStream();
        String groupName = streamOperations.createGroup(key, group);
        log.info("创建组成功:{}", groupName);
    }
}
