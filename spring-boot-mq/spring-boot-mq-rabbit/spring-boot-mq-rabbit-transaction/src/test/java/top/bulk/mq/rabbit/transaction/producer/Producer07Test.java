package top.bulk.mq.rabbit.transaction.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.mq.rabbit.transaction.message.Message07;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-22
 */
@SpringBootTest
@Slf4j
class Producer07Test {
    @Resource
    Producer07 producer07;

    @Test
    void syncSend() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        producer07.syncSend(id, Message07.ROUTING_KEY);
        log.info("[{}][test producer07 syncSend][id:{}] 发送成功", LocalDateTime.now(), id);

        TimeUnit.SECONDS.sleep(2);
    }
}