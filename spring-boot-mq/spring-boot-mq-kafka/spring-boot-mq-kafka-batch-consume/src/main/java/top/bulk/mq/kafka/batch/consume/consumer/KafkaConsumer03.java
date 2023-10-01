package top.bulk.mq.kafka.batch.consume.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.batch.consume.message.KafkaMessage03;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消费者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
@Slf4j
public class KafkaConsumer03 {
    /**
     * ps：如果配置改成 spring.kafka.listener.type=SINGLE ，就会发现这里只会单条消费了。
     *
     */
    @KafkaListener(topics = KafkaMessage03.TOPIC, groupId = KafkaMessage03.GROUP_ID)
    public void onMessage(List<KafkaMessage03> list) {
        log.info("[{}][KafkaMessage03][消息数量：{} 消息内容：{}]", LocalDateTime.now(), list.size(), list);
        // 这里执行相应的业务逻辑
    }
}
