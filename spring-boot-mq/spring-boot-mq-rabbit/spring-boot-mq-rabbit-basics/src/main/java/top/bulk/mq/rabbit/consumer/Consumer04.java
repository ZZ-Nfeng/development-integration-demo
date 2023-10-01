package top.bulk.mq.rabbit.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.message.Message04;

/**
 * Headers Exchange 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(queues = Message04.QUEUE)
@Slf4j
public class Consumer04 {
    @RabbitHandler
    public void onMessage(Message04 message) {
        log.info("[Consumer03 onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}