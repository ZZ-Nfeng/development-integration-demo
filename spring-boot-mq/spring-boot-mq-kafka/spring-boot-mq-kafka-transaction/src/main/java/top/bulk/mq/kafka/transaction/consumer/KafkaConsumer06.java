package top.bulk.mq.kafka.transaction.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.transaction.message.KafkaMessage06;

import java.time.LocalDateTime;

/**
 * 消费者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
@Slf4j
public class KafkaConsumer06 {
    /**
     * 注意，对应 的 Topic 的 分区一定要设置多个
     */
    @KafkaListener(topics = KafkaMessage06.TOPIC, groupId = KafkaMessage06.GROUP_ID)
    public void onMessage(KafkaMessage06 message) {
        log.info("[{}][KafkaConsumer06][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
        // 这里执行相应的业务逻辑
    }
}
