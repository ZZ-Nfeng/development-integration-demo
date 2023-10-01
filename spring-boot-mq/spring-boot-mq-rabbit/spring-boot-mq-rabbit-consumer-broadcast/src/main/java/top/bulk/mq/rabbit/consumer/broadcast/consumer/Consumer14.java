package top.bulk.mq.rabbit.consumer.broadcast.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.consumer.broadcast.message.Message14;

import java.time.LocalDateTime;


/**
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(
                        // 广播消费，要确保每一个队列名称都不相同。通过 Spring EL 表达式 生成一个 UUID ，这样就能全部接收到消息了
                        name = Message14.QUEUE + "_A_" + "#{T(java.util.UUID).randomUUID()}",
                        // 自动删除
                        autoDelete = "true"
                ),
                exchange = @Exchange(
                        name = Message14.EXCHANGE,
                        type = ExchangeTypes.TOPIC,
                        declare = "false"
                )
        )
)
@Slf4j
public class Consumer14 {

    @RabbitHandler
    public void onMessage(Message14 message) throws InterruptedException {
        log.info("[{}][Consumer14 onMessage][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
    }

}
