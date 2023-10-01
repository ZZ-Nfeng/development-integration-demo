package top.bulk.mq.rabbit.delay.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.delay.message.Message12;
import top.bulk.mq.rabbit.delay.message.Message12;

import java.time.LocalDateTime;

/**
 * direct 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(queues = Message12.QUEUE)
@Slf4j
public class Consumer12 {

    @RabbitHandler
    public void onMessage(Message12 message) {
        log.info("[{}][Consumer12 onMessage][消息内容：{}]", LocalDateTime.now(), message);
    }
}
