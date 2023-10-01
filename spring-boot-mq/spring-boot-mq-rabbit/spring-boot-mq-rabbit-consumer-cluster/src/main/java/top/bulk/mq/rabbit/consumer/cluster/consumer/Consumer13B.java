package top.bulk.mq.rabbit.consumer.cluster.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.consumer.cluster.message.Message13;

import java.time.LocalDateTime;


/**
 * 模拟多个应用，你也可以新写一个应用
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(
                        name = Message13.QUEUE + "_GROUP_B"
                ),
                exchange = @Exchange(
                        name = Message13.EXCHANGE,
                        type = ExchangeTypes.TOPIC,
                        declare = "false"
                ),
                key = "#"
        )
)
@Slf4j
public class Consumer13B {

    @RabbitHandler
    public void onMessage(Message13 message) throws InterruptedException {
        log.info("[{}][Consumer13B onMessage][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
    }
}
