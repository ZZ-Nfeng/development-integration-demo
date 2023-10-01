package top.bulk.mq.rabbit.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.mq.rabbit.SpringBootMqRabbitApplication;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@Slf4j
@SpringBootTest(classes = SpringBootMqRabbitApplication.class)
class Producer02Test {
    @Resource
    Producer02 producer02;

    @Test
    void syncSendSuccess1() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        // 必须要满足  routingKey 规则才能够成功投递
        String routingKey = "aa.key2.key3";
        producer02.syncSend(id, routingKey);
        log.info("[test producer02 syncSendSuccess1][routingKey: {}][id: {}] 发送成功", routingKey, id);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void syncSendSuccess2() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        String routingKey = "aa.bb.key2.key3";
        producer02.syncSend(id, routingKey);
        log.info("[test producer02 syncSendSuccess2][routingKey: {}][id: {}] 发送成功", routingKey, id);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void syncSendFail() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        String routingKey = "aa.key2.zz";
        producer02.syncSend(id, routingKey);
        log.info("[test producer02 syncSendFail][routingKey:{}][id:{}] 发送成功", routingKey, id);

        TimeUnit.SECONDS.sleep(2);
    }
}