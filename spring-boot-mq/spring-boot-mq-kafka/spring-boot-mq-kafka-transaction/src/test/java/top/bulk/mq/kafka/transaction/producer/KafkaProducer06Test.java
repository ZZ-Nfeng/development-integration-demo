package top.bulk.mq.kafka.transaction.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-03-25
 */
@SpringBootTest
@Slf4j
class KafkaProducer06Test {

    @Resource
    KafkaProducer06 kafkaProducer06;

    @Test
    void mock() throws InterruptedException, ExecutionException {
        new CountDownLatch(1).await();
    }

    @Test
    void asyncSend() throws InterruptedException, ExecutionException {
        log.info("[{}][testASyncSend][开始执行]", LocalDateTime.now());
        String id = UUID.randomUUID().toString();
        kafkaProducer06.asyncSend(id);
        // 主要观察消费者的打印,是多个线程并发去处理的
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void asyncSend2() throws InterruptedException, ExecutionException {
        String id = UUID.randomUUID().toString();
        log.info("[{}][testASyncSend][开始执行][{}]", LocalDateTime.now(), id);
        kafkaProducer06.asyncSend2(id);
        // 主要观察消费者的打印,是多个线程并发去处理的
        TimeUnit.SECONDS.sleep(2);
    }
}