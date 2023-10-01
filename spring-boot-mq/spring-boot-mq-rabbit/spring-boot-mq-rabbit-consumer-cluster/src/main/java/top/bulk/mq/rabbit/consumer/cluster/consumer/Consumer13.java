package top.bulk.mq.rabbit.consumer.cluster.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.consumer.cluster.message.Message13;

import java.time.LocalDateTime;


/**
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(
                        name = Message13.QUEUE + "_GROUP_A"
                ),
                exchange = @Exchange(
                        name = Message13.EXCHANGE,
                        type = ExchangeTypes.TOPIC,
                        // 因为这个交换机已经创建了，所以这里不用在创建了 就是 false
                        declare = "false"
                ),
                // 设置使用的 RoutingKey 为 “#” 匹配所有
                key = "#"
        )
)
@Slf4j
public class Consumer13 {

    @RabbitHandler
    public void onMessage(Message13 message) throws InterruptedException {
        log.info("[{}][Consumer13 onMessage][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
    }

}
