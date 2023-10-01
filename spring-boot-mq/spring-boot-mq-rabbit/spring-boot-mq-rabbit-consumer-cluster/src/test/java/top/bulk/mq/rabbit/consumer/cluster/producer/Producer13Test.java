package top.bulk.mq.rabbit.consumer.cluster.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-28
 */
@SpringBootTest
@Slf4j
class Producer13Test {
    @Resource
    Producer13 producer13;

    @Test
    void mock() throws InterruptedException {
        // 先启动这个等待着，模拟多个副本的情况
        new CountDownLatch(1).await();
    }

    @Test
    void syncSend() throws InterruptedException {
        // 多发几个 为了能体现出来 负载的效果
        for (int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString();
            producer13.syncSend(id, null);
            log.info("[test producer13 syncSend][id:{}] 发送成功", id);
        }

        TimeUnit.SECONDS.sleep(10);
    }
}