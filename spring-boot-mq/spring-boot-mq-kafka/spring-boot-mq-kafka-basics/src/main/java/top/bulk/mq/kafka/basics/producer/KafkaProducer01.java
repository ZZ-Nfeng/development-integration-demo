package top.bulk.mq.kafka.basics.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import top.bulk.mq.kafka.basics.message.KafkaMessage01;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
public class KafkaProducer01 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 发送同步消息
     * <p>
     * ps：消息是如何序列化的 （可以在 ui 后台上看到）
     * 在序列化时，我们使用了 JsonSerializer 序列化 Message 消息对象，它会在 Kafka 消息 Headers 的 __TypeId__ 上，值为 Message 消息对应的类全名。
     * 在反序列化时，我们使用了 JsonDeserializer 序列化出 Message 消息对象，它会根据 Kafka 消息 Headers 的 __TypeId__ 的值，反序列化消息内容成该 Message 对象。
     */
    public SendResult<Object, Object> syncSend(String id) throws ExecutionException, InterruptedException {
        KafkaMessage01 message = new KafkaMessage01();
        message.setId(id);
        // 同步发送消息，使用 ListenableFuture 对象的 get() 方法，阻塞等待发送结果，从而实现同步的效果
        return kafkaTemplate.send(KafkaMessage01.TOPIC, message).get();
    }

    /**
     * 发送异步消息
     */
    public ListenableFuture<SendResult<Object, Object>> asyncSend(String id) {
        KafkaMessage01 message = new KafkaMessage01();
        message.setId(id);
        // 异步发送消息
        return kafkaTemplate.send(KafkaMessage01.TOPIC, message);
    }
}
