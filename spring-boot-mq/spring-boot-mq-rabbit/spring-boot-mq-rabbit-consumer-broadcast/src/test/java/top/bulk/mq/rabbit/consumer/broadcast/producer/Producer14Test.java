package top.bulk.mq.rabbit.consumer.broadcast.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-03-01
 */
@SpringBootTest
@Slf4j
class Producer14Test {
    @Resource
    Producer14 producer14;

    @Test
    void mock() throws InterruptedException {
        // 先启动这个等待着，模拟多个副本的情况
        new CountDownLatch(1).await();
    }

    @Test
    void syncSend() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString();
            producer14.syncSend(id, null);
            log.info("[test producer14 syncSend][id:{}] 发送成功", id);
        }
        // 最终的演示效果为，每一个应用的每一个副本都会消费到消息
        TimeUnit.SECONDS.sleep(10);
    }
}