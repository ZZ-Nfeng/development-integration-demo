package top.bulk.mq.kafka.concurrency.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.concurrency.message.KafkaMessage04;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
public class KafkaProducer04 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult<Object, Object> asyncSend(String id) throws ExecutionException, InterruptedException {
        KafkaMessage04 message = new KafkaMessage04();
        message.setId(id);
        return kafkaTemplate.send(KafkaMessage04.TOPIC, message).get();
    }
}
