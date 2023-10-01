package top.bulk.mq.kafka.btach.send.producer;

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
class KafkaProducer02Test {
    @Resource
    KafkaProducer02 kafkaProducer02;

    @Test
    void asyncSend() throws InterruptedException {
        log.info("[{}][testASyncSend][开始执行]", LocalDateTime.now());

        for (int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString();
            kafkaProducer02.asyncSend(id).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

                @Override
                public void onFailure(Throwable e) {
                    log.info("[{}][testASyncSend][发送编号：[{}] 发送异常]]", LocalDateTime.now(), id, e);
                }

                @Override
                public void onSuccess(SendResult<Object, Object> result) {
                    log.info("[{}][testASyncSend][发送编号：[{}] 发送成功，结果为：[{}]]", LocalDateTime.now(), id, result);
                }

            });

            // 每条消息间隔两秒，注意观察消费者是何时消费消息的
            Thread.sleep(2 * 1000L);
        }
        // 最终我们可以看到，其实没有达到发送要求之前，并没有将消息发出去，也不会有消息回调，最终在达到配置 linger.ms 的时间之后，才会有发送的回执
        TimeUnit.SECONDS.sleep(15);
    }
}