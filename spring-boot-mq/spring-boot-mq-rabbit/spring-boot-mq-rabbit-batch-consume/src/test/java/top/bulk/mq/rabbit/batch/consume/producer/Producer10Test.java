package top.bulk.mq.rabbit.batch.consume.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.mq.rabbit.batch.consume.message.Message10;

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
class Producer10Test {
    @Resource
    Producer10 producer10;

    @Test
    void sendSingle() throws InterruptedException {
        // 假设 一秒一个，发送 15 个，观察消费者的情况
        for (int i = 0; i < 15; i++) {
            TimeUnit.SECONDS.sleep(1);
            String id = UUID.randomUUID().toString();
            producer10.sendSingle(id, Message10.ROUTING_KEY);
            if (i == 9) {
                log.info("[{}][test producer10 sendSingle] 发送成功10个", LocalDateTime.now());
            }
        }
        log.info("[{}][test producer10 sendSingle] 发送成功", LocalDateTime.now());

        TimeUnit.SECONDS.sleep(20);
    }
}