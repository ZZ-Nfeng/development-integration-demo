package top.bulk.mq.rabbit.bath.send.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.bath.send.message.Message09;

import java.time.LocalDateTime;


/**
 * direct 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@RabbitListener(queues = Message09.QUEUE)
@Component
@Slf4j
public class Consumer09 {

    @RabbitHandler
    public void onMessage(Message09 message) {
        log.info("[{}][Consumer09 onMessage][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
    }
}
