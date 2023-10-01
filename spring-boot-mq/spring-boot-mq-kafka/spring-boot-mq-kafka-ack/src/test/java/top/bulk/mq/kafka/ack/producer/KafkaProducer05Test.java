package top.bulk.mq.kafka.ack.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * @author 散装java
 * @date 2023-03-22
 */
@SpringBootTest
@Slf4j
class KafkaProducer05Test {

    @Resource
    KafkaProducer05 kafkaProducer05;

    @Test
    void mock() throws InterruptedException, ExecutionException {
        // 充当多副本
        new CountDownLatch(1).await();
    }

    @Test
    void syncSend() throws InterruptedException, ExecutionException {
        log.info("[{}][testASyncSend][开始执行]", LocalDateTime.now());
        // 发送十条消息测试
        for (int i = 1; i <= 4; i++) {
            kafkaProducer05.syncSend(i);
        }
        // 注意观察Kafka-ui 的 Current offset 、	End offset
        // 其实这里的确认指的是 当前消息（及之前的消息）offset均已被消费完成
        // 可以参考 README.md 文件中的图
        new CountDownLatch(1).await();
    }
}