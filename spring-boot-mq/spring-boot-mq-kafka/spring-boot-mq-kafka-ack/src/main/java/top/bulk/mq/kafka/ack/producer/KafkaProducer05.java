package top.bulk.mq.kafka.ack.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.ack.message.KafkaMessage05;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
public class KafkaProducer05 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult<Object, Object> syncSend(Integer id) throws ExecutionException, InterruptedException {
        KafkaMessage05 message = new KafkaMessage05();
        message.setId(id);
        // 同步发送消息，使用 ListenableFuture 对象的 get() 方法，阻塞等待发送结果，从而实现同步的效果
        return kafkaTemplate.send(KafkaMessage05.TOPIC, message).get();
    }
}
