package top.bulk.mq.rabbit.delay.producer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author 散装java
 * @date 2023-02-27
 */
@SpringBootTest
@Slf4j
class Producer11Test {

    @Resource
    Producer11 producer11;

    @SneakyThrows
    @Test
    void syncSend() {
        String id = UUID.randomUUID().toString();
        int delay = 5000;
        producer11.syncSend(id, delay);
        log.info("[{}][test producer11 syncSend][延迟时间为：{}][id:{}] 发送成功", LocalDateTime.now(), delay, id);

        String id2 = UUID.randomUUID().toString();
        int delay2 = 2000;
        producer11.syncSend(id2, delay2);
        log.info("[{}][test producer11 syncSend][延迟时间为：{}][id:{}] 发送成功", LocalDateTime.now(), delay2, id2);
        // 其实采用 ttl 这种方式会有一个问题，就是当一个队列中有多个不一样的过期时间的消息的时候，会形成阻塞，只有前一个被消费了才会轮到后一个
        // 比如先发送了一个延迟20s的消息，后发送了一个延迟为2s的消息，如果第一个消息未到达则后一个消息会被阻塞
        new CountDownLatch(1).await();
    }
}