package top.bulk.mq.rabbit.consumer.cluster.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.consumer.cluster.message.Message13;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer13 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id, String routingKey) {
        Message13 message = new Message13();
        message.setId(id);
        rabbitTemplate.convertAndSend(Message13.EXCHANGE, routingKey, message);
    }
}
