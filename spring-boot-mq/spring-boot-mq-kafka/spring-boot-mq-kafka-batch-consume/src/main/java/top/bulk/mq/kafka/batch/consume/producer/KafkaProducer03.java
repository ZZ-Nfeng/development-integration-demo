package top.bulk.mq.kafka.batch.consume.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import top.bulk.mq.kafka.batch.consume.message.KafkaMessage03;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
public class KafkaProducer03 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public ListenableFuture<SendResult<Object, Object>> asyncSend(String id) {
        KafkaMessage03 message = new KafkaMessage03();
        message.setId(id);
        return kafkaTemplate.send(KafkaMessage03.TOPIC, message);
    }
}
