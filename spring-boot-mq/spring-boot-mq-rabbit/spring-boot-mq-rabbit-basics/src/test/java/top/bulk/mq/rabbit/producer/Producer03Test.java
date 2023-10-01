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
class Producer03Test {
    @Resource
    Producer03 producer03;

    @Test
    void syncSend() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        producer03.syncSend(id);
        log.info("[test producer03 syncSend][id: {}] 发送成功", id);

        TimeUnit.SECONDS.sleep(2);
    }
}