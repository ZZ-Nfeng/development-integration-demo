package top.bulk.mq.kafka.batch.consume.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-03-21
 */
@SpringBootTest
@Slf4j
class KafkaProducer03Test {
    @Resource
    KafkaProducer03 kafkaProducer03;

    @Test
    void asyncSend() throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString();
            kafkaProducer03.asyncSend(id).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

                @Override
                public void onFailure(Throwable e) {
                    log.info("[{}][testASyncSend][发送编号：[{}] 发送异常]]", LocalDateTime.now(), id, e);
                }

                @Override
                public void onSuccess(SendResult<Object, Object> result) {
                    log.info("[{}][testASyncSend][发送编号：[{}] 发送成功，结果为：[{}]]", LocalDateTime.now(), id, result);
                }

            });
        }
        // 最终我们可以看到，测试类中一次性发送了三条，消费者端一次性消费了三条，证明可以批量消费
        TimeUnit.SECONDS.sleep(15);
    }
}