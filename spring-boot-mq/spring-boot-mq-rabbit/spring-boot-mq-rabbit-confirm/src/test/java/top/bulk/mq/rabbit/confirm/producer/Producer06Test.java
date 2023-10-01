package top.bulk.mq.rabbit.confirm.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.mq.rabbit.confirm.SpringBootMqRabbitConfirmApplication;
import top.bulk.mq.rabbit.confirm.message.Message06;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-21
 */
@SpringBootTest(classes = SpringBootMqRabbitConfirmApplication.class)
@Slf4j
class Producer06Test {
    @Resource
    Producer06 producer06;

    @Test
    void syncSend() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        producer06.syncSend(id, Message06.ROUTING_KEY);
        log.info("[test producer06 syncSend][id:{}] 发送成功", id);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void syncSendFail() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        // 发送一个不存在的 routingKey
        producer06.syncSend(id, "aaa");
        log.info("[test producer06 syncSend][id:{}] 发送成功", id);

        TimeUnit.SECONDS.sleep(2);
    }
}