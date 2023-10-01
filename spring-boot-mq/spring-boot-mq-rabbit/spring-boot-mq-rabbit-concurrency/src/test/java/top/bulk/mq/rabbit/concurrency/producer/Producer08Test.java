package top.bulk.mq.rabbit.concurrency.producer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.mq.rabbit.concurrency.message.Message08;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-23
 */
@SpringBootTest
@Slf4j
class Producer08Test {

    @Resource
    Producer08 producer08;

    @Test
    void mock() throws InterruptedException {
        TimeUnit.SECONDS.sleep(20);
    }

    @SneakyThrows
    @Test
    void syncSend() {
        // 循环发送十个，观察消费者情况
        for (int i = 0; i < 10; i++) {
            String id = UUID.randomUUID().toString();
            producer08.syncSend(id, Message08.ROUTING_KEY);
        }
        log.info("[{}][test producer08 syncSend] 发送成功", LocalDateTime.now());
        // 这里多睡一会，确保消息全部消费完成
        TimeUnit.SECONDS.sleep(10);
    }
}