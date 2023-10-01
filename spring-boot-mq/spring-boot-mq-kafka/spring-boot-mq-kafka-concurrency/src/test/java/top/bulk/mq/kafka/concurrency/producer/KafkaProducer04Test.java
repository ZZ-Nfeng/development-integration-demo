package top.bulk.mq.kafka.concurrency.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-03-21
 */
@SpringBootTest
@Slf4j
class KafkaProducer04Test {
    @Resource
    KafkaProducer04 kafkaProducer04;

    @Test
    void asyncSend() throws InterruptedException, ExecutionException {
        log.info("[{}][testASyncSend][开始执行]", LocalDateTime.now());
        // 发送十条消息测试
        for (int i = 0; i < 10; i++) {
            String id = UUID.randomUUID().toString();
            kafkaProducer04.asyncSend(id);
        }
        // 主要观察消费者的打印,是多个线程并发去处理的
        TimeUnit.SECONDS.sleep(150);
    }
}