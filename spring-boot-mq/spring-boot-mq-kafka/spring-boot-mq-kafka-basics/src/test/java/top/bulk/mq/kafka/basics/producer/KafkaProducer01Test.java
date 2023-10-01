package top.bulk.mq.kafka.basics.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @date 2023-03-14
 */
@SpringBootTest
@Slf4j
class KafkaProducer01Test {
    @Resource
    KafkaProducer01 kafkaProducer01;

    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        SendResult<Object, Object> result = kafkaProducer01.syncSend(id);
        log.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);

        // 阻塞等待，保证消费
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void testASyncSend() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        kafkaProducer01.asyncSend(id).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

            @Override
            public void onFailure(Throwable e) {
                log.info("[testASyncSend][发送编号：[{}] 发送异常]]", id, e);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> result) {
                log.info("[testASyncSend][发送编号：[{}] 发送成功，结果为：[{}]]", id, result);
            }

        });

        log.info("[testASyncSend] 发送完毕");

        // 阻塞等待，保证消费
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * 演示发送多条，体验下 同一个组内的消费者 只有一个会消费到消息
     */
    @Test
    public void testSyncSend2() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 40; i++) {
            String id = UUID.randomUUID().toString();
            SendResult<Object, Object> result = kafkaProducer01.syncSend(id);
        }
        // 阻塞等待，保证消费
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void mock() throws ExecutionException, InterruptedException {
        // 阻塞，充当一个消费者
        new CountDownLatch(1).await();
    }
}