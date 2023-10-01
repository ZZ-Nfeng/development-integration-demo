package top.bulk.mq.rabbit.ack.producer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.mq.rabbit.ack.SpringBootMqRabbitAckApplication;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@SpringBootTest(classes = SpringBootMqRabbitAckApplication.class)
@Slf4j
class Producer05Test {
    @Resource
    Producer05 producer05;

    @SneakyThrows
    @Test
    void syncSend() {
        String id = UUID.randomUUID().toString();
        producer05.syncSend(id);
        log.info("[test producer05 syncSend][id:{}] 发送成功", id);

        TimeUnit.SECONDS.sleep(2);
    }

}