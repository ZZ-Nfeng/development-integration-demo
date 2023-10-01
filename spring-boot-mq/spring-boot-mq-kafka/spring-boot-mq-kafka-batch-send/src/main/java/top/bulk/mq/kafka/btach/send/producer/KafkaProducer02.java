package top.bulk.mq.kafka.btach.send.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import top.bulk.mq.kafka.btach.send.message.KafkaMessage02;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
public class KafkaProducer02 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 我们实际用起来，和普通的发送并没有区别
     * <p>
     * 因为我们发送的消息 Topic 是自动创建的，所以其 Partition 分区大小是 1 。
     * 这样，就能保证我每次调用这个方法，满足批量发送消息的一个前提，相同 Topic 的相同 Partition 分区的消息们。
     */
    public ListenableFuture<SendResult<Object, Object>> asyncSend(String id) {
        KafkaMessage02 message = new KafkaMessage02();
        message.setId(id);
        return kafkaTemplate.send(KafkaMessage02.TOPIC, message);
    }
}
