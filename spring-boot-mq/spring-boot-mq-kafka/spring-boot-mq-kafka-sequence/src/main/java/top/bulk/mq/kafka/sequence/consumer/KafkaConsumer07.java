package top.bulk.mq.kafka.sequence.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.sequence.message.KafkaMessage07;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 消费者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
@Slf4j
public class KafkaConsumer07 {

    @KafkaListener(topics = KafkaMessage07.TOPIC, groupId = KafkaMessage07.GROUP_ID)
    public void onMessage(KafkaMessage07 message) throws InterruptedException {
        log.info("[{}][KafkaConsumer07][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
        // 这里执行相应的业务逻辑
        // 这里随机睡一会，模拟业务处理时候的耗时
        long l = new Random(1000).nextLong();
        TimeUnit.MILLISECONDS.sleep(l);
    }
}
