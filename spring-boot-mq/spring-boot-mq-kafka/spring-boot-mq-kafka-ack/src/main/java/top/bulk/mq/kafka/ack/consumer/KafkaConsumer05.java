package top.bulk.mq.kafka.ack.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.ack.message.KafkaMessage05;

import java.time.LocalDateTime;

/**
 * 消费者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
@Slf4j
public class KafkaConsumer05 {

    @KafkaListener(topics = KafkaMessage05.TOPIC, groupId = KafkaMessage05.GROUP_ID)
    public void onMessage(KafkaMessage05 message, Acknowledgment acknowledgment) {
        log.info("[{}][KafkaConsumer05][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
        // 这里执行相应的业务逻辑

        // 提交消费进度
        if (message.getId() % 2 == 1) {
            acknowledgment.acknowledge();
            log.info("[{}][KafkaConsumer05]提交ack[id:{}]", LocalDateTime.now(), message.getId());
        } else {
            // 且当前消费线程在阻塞指定sleep（如下3000毫秒）后重新调用poll获取待消费消息（包括之前poll被抛弃的消息）
            // acknowledgment.nack(3000);
        }
        // 注意观察Kafka-ui 的 Current offset 、	End offset
        // 其实这里的确认指的是 当前消息（及之前的消息）offset均已被消费完成
    }
}
