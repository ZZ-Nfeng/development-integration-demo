package top.bulk.mq.rabbit.sequence.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-03-04
 */
@SpringBootTest
@Slf4j
class Producer15Test {
    @Resource
    Producer15 producer15;

    @Test
    void mock() throws InterruptedException {
        // 先启动这个测试类，模拟多个副本情况下，看如何消费
        new CountDownLatch(1).await();
    }

    @Test
    void syncSend() throws InterruptedException {
        // 模拟每个队列中扔 10 个数据，看看效果
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                producer15.syncSend(j, " 编号：" + j + " 第：" + i + " 条消息");
            }
        }

        TimeUnit.SECONDS.sleep(20);
    }
}