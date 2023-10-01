package top.bulk.mq.kafka.btach.send.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.btach.send.message.KafkaMessage02;

import java.time.LocalDateTime;

/**
 * 消费者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
@Slf4j
public class KafkaConsumer02 {

    @KafkaListener(topics = KafkaMessage02.TOPIC, groupId = KafkaMessage02.GROUP_ID)
    public void onMessage(KafkaMessage02 message) {
        log.info("[{}][KafkaConsumer02][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
        // 这里执行相应的业务逻辑
    }
}
