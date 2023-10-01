package top.bulk.mq.kafka.sequence.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.sequence.message.KafkaMessage07;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
@Slf4j
public class KafkaProducer07 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult<Object, Object> asyncSend(String id, String msg) throws ExecutionException, InterruptedException {
        KafkaMessage07 message = new KafkaMessage07();
        message.setId(id);
        message.setMsg(msg);
        // 以 id 作为第二个参数 消息的key， 指定了消息的 key ，Producer 则会根据 key 的哈希值取模来获取到其在 Topic 下对应的 Partition
        return kafkaTemplate.send(KafkaMessage07.TOPIC, id, message).get();
    }

}
