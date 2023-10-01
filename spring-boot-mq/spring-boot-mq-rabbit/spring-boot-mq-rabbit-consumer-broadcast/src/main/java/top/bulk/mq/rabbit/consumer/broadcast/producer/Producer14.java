package top.bulk.mq.rabbit.consumer.broadcast.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.consumer.broadcast.message.Message14;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer14 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id, String routingKey) {
        Message14 message = new Message14();
        message.setId(id);
        rabbitTemplate.convertAndSend(Message14.EXCHANGE, routingKey, message);
    }
}
