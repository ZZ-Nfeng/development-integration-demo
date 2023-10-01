package top.bulk.mq.rabbit.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.message.Message03;

/**
 * Fanout Exchange 类型消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@Slf4j
public class Consumer03 {
    @RabbitListener(queues = Message03.QUEUE_A)
    public void onMessage1(Message03 message) {
        log.info("[Consumer03 onMessage1][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    @RabbitListener(queues = Message03.QUEUE_B)
    public void onMessage2(Message03 message) {
        log.info("[Consumer03 onMessage2][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}