package top.bulk.mq.rabbit.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.mq.rabbit.SpringBootMqRabbitApplication;
import top.bulk.mq.rabbit.message.Message04;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@Slf4j
@SpringBootTest(classes = SpringBootMqRabbitApplication.class)
class Producer04Test {
    @Resource
    Producer04 producer04;

    @Test
    void syncSendSuccess() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        // 必须要 匹配才可以被投递
        producer04.syncSend(id, Message04.HEADER_VALUE);
        log.info("[test producer04 syncSendSuccess][head value: {}][id: {}] 发送成功", Message04.HEADER_VALUE, id);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void syncSendFail() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        String headValue = "aa";
        producer04.syncSend(id, headValue);
        log.info("[test producer04 syncSendFail][head value: {}][id: {}] 发送成功", headValue, id);

        TimeUnit.SECONDS.sleep(2);
    }
}