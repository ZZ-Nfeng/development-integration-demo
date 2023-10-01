package top.bulk.mq.rabbit.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.message.Message01;

/**
 * direct 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(queues = Message01.QUEUE)
@Slf4j
public class Consumer01 {
    @RabbitHandler
    public void onMessage(Message01 message) {
        log.info("[Consumer01 onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
