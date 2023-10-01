package top.bulk.mq.kafka.sequence.producer;

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
 * @date 2023-03-27
 */
@SpringBootTest
@Slf4j
class KafkaProducer07Test {

    @Resource
    KafkaProducer07 kafkaProducer07;

    @Test
    void mock() throws InterruptedException, ExecutionException {
        log.info("[{}][mock][开始执行]", LocalDateTime.now());
        // 模拟多个副本竞争
        new CountDownLatch(1).await();
    }

    @Test
    void asyncSend() throws InterruptedException, ExecutionException {
        log.info("[{}][testASyncSend][开始执行]", LocalDateTime.now());
        String id = UUID.randomUUID().toString();
        // 模拟每个 key 中扔 10 个数据，看看效果
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                kafkaProducer07.asyncSend(String.valueOf(j), " 编号：" + j + " 第：" + i + " 条消息");
            }
        }

        // 主要观察消费者的打印,是多个线程并发去处理的
        TimeUnit.SECONDS.sleep(2);
    }
}