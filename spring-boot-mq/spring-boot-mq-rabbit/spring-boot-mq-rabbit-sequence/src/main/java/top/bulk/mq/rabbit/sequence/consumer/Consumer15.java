package top.bulk.mq.rabbit.sequence.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.sequence.message.Message15;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 要想保证消息的顺序，每个队列只能有一个消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(queues = Message15.QUEUE_0)
@RabbitListener(queues = Message15.QUEUE_1)
@RabbitListener(queues = Message15.QUEUE_2)
@RabbitListener(queues = Message15.QUEUE_3)
@Slf4j
public class Consumer15 {

    @RabbitHandler
    public void onMessage(Message15 message) throws InterruptedException {
        log.info("[{}][Consumer15 onMessage][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
        // 这里随机睡一会，模拟业务处理时候的耗时
        long l = new Random(1000).nextLong();
        TimeUnit.MILLISECONDS.sleep(l);
    }
}
