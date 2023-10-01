package top.bulk.mq.rabbit.delay.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.delay.message.Message11;

import java.time.LocalDateTime;

/**
 * direct 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(queues = Message11.QUEUE_DELAY)
@Slf4j
public class Consumer11 {

    @RabbitHandler
    public void onMessage(Message11 message) {
        log.info("[{}][Consumer11 onMessage][消息内容：{}]", LocalDateTime.now(), message);
    }
}
