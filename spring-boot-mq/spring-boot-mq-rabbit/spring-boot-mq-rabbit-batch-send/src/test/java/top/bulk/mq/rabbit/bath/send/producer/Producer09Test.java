package top.bulk.mq.rabbit.bath.send.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.mq.rabbit.bath.send.message.Message09;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-24
 */
@SpringBootTest
@Slf4j
class Producer09Test {
    @Resource
    Producer09 producer09;

    @Test
    void syncSend() throws InterruptedException {
        // 循环发送十个，观察消费者情况
        for (int i = 0; i < 10; i++) {
            String id = UUID.randomUUID().toString();
            producer09.syncSend(id, Message09.ROUTING_KEY);
        }
        log.info("[{}][test producer09 syncSend] 发送成功", LocalDateTime.now());
        // 测试结果是，只有等到十秒过后，或者条数达到10条才会 推送 （满足RabbitConfiguration 的配置才会发送）
        TimeUnit.SECONDS.sleep(12);
    }
}