package top.bulk.mq.redis.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import top.bulk.mq.redis.annotation.MessageHandler;
import top.bulk.mq.redis.annotation.MessageListener;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * topic处理方式 实现 订阅消息
 * 基于 list 的 lPush/brPop
 * @author 散装java
 * @date 2023-02-03
 */
@MessageHandler(value = MessageListener.Mode.TOPIC)
public class TopicMessageHandler extends AbstractMessageHandler {

    public TopicMessageHandler(RedisTemplate<Object,Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void invokeMessage(Method method) {
        Set<String> consumers = new HashSet<>();
        MessageListener annotation = method.getAnnotation(MessageListener.class);
        String topic = getTopic(annotation);
        RedisConnection connection = getConnection();
        Class<?> declaringClass = method.getDeclaringClass();
        Object bean = applicationContext.getBean(declaringClass);
        while (true) {
            List<byte[]> bytes = connection.bRPop(1, topic.getBytes());
            if (CollectionUtil.isNotEmpty(bytes)) {
                if (null != bytes.get(1)) {
                    consumer(method, consumers, bean, bytes.get(1));
                }
            }
        }
    }

    private String getTopic(MessageListener annotation) {
        String value = annotation.value();
        String topic = annotation.topic();
        return StrUtil.isBlank(topic) ? value : topic;
    }

}
