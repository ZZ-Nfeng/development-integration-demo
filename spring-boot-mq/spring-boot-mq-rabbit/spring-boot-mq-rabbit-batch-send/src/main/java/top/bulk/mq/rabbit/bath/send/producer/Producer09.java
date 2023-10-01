package top.bulk.mq.rabbit.bath.send.producer;

import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.bath.send.message.Message09;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer09 {
    @Resource
    private BatchingRabbitTemplate batchingRabbitTemplate;

    public void syncSend(String id, String routingKey) {
        Message09 message = new Message09();
        message.setId(id);
        batchingRabbitTemplate.convertAndSend(Message09.EXCHANGE, routingKey, message);
    }
}
