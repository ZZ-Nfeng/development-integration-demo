package top.bulk.mq.rabbit.batch.consume.producer;

import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.batch.consume.message.Message10;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer10 {

    @Resource
    RabbitTemplate rabbitTemplate;

    public void sendSingle(String id, String routingKey) {
        Message10 message = new Message10();
        message.setId(id);
        rabbitTemplate.convertAndSend(Message10.EXCHANGE, routingKey, message);
    }
}
