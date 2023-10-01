package top.bulk.mq.rabbit.transaction.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.transaction.message.Message07;

import java.time.LocalDateTime;


/**
 * direct 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(queues = Message07.QUEUE)
@Slf4j
public class Consumer07 {

    @RabbitHandler
    public void onMessage(Message07 message) {
        log.info("[{}][Consumer07 onMessage][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
    }
}
