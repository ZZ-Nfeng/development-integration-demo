package top.bulk.mq.redis.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import top.bulk.mq.redis.annotation.MessageConsumer;
import top.bulk.mq.redis.annotation.MessageListener;
import top.bulk.mq.redis.pojo.Message;

import javax.annotation.Resource;

/**
 * 消费者
 *
 * @author 散装java
 * @date 2023-02-03
 */
@MessageConsumer
@Slf4j
public class RedisMqConsumer {
    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    @MessageListener(topic = "topic1", mode = MessageListener.Mode.TOPIC)
    public void testTopic1(Message<?> message) {
        log.info("topic1===> " + message);
    }

    @MessageListener(topic = "topic1", mode = MessageListener.Mode.TOPIC)
    public void testTopic11(Message<?> message) {
        log.info("topic11===> " + message);
    }

    @MessageListener(topic = "topic2", mode = MessageListener.Mode.TOPIC)
    public void testTopic2(Message<?> message) {
        log.info("topic2===> " + message);
    }

    @MessageListener(topic = "topic3", mode = MessageListener.Mode.TOPIC)
    public void testTopic3(Message<?> message) {
        log.info("topic3===> " + message);
    }

    @MessageListener(channel = "pubsub", mode = MessageListener.Mode.PUBSUB)
    public void testPubsub1(Message<?> message) {
        log.info("pubsub1===> " + message);
    }

    @MessageListener(channel = "pubsub", mode = MessageListener.Mode.PUBSUB)
    public void testPubsub2(Message<?> message) {
        log.info("pubsub2===> " + message);
    }

    @MessageListener(streamKey = "streamKey", consumerGroup = "consumerGroup", consumerName = "consumerName", mode = MessageListener.Mode.STREAM)
    public void testStream1(ObjectRecord<Object, Object> message) {
        log.info("testStream1 组 consumerGroup 名 consumerName 消费消息 ===> " + message);
        // 手动 ack
        redisTemplate.opsForStream().acknowledge("consumerGroup", message);
    }

    /**
     * 同一个组，不同的 consumerName 都可以进行消息的消费，与上面 testStream() 为竞争关系，消息仅被其中一个消费
     *
     * @param message msg
     */
    @MessageListener(streamKey = "streamKey", consumerGroup = "consumerGroup", consumerName = "consumerName", mode = MessageListener.Mode.STREAM)
    public void testStream2(ObjectRecord<Object, Object> message) {
        log.info("testStream2  组 consumerGroup 名 consumerName2 消费消息 ===> " + message);
//        redisTemplate.opsForStream().acknowledge("consumerGroup", message);
    }

    /**
     * 不同组的消息，可以正常消费
     *
     * @param message message
     */
    @MessageListener(streamKey = "streamKey", consumerGroup = "consumerGroup3", consumerName = "consumerName3", mode = MessageListener.Mode.STREAM)
    public void testStream3(ObjectRecord<Object, Object> message) {
        log.info("testStream3 组 consumerGroup3 名 consumerName3 消费消息 ===> " + message);
    }

    /**
     * 消费 streamKey 中，没有进行 ack 的消息
     *
     * @param message message
     */
    @MessageListener(
            streamKey = "streamKey",
            consumerGroup = "consumerGroup",
            consumerName = "consumerName",
            pending = true,
            mode = MessageListener.Mode.STREAM)
    public void testStreamPending(ObjectRecord<Object, Object> message) {
        log.info("testStreamPending 组 consumerGroup 名 consumerName 消费未 ack 消息 ===> " + message);
        redisTemplate.opsForStream().acknowledge("consumerGroup", message);
    }
}