package top.bulk.mq.rabbit.producer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;
import top.bulk.mq.rabbit.SpringBootMqRabbitApplication;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@SpringBootTest(classes = SpringBootMqRabbitApplication.class)
@Slf4j
class Producer01Test {
    @Resource
    Producer01 producer01;

    @SneakyThrows
    @Test
    void syncSend() {
        String id = UUID.randomUUID().toString();
        producer01.syncSend(id);
        log.info("[test producer01 syncSend][id:{}] 发送成功", id);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void syncSendDefault() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        producer01.syncSendDefault(id);
        log.info("[test producer01 syncSendDefault][id:{}] 发送成功", id);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void asyncSend() throws InterruptedException {
        String id = UUID.randomUUID().toString();

        producer01.asyncSend(id).addCallback(new ListenableFutureCallback<Void>() {

            @Override
            public void onFailure(Throwable e) {
                log.info("[testASyncSend][发送编号：[{}] 发送异常]]", id, e);
            }

            @Override
            public void onSuccess(Void aVoid) {
                log.info("[testASyncSend][发送编号：[{}] 发送成功，发送成功]", id);
            }

        });

        log.info("[test producer01 asyncSend][id:{}] 发送成功", id);

        TimeUnit.SECONDS.sleep(2);
    }
}