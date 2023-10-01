package top.bulk.mq.rabbit.concurrency.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.concurrency.message.Message08;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer08 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id, String routingKey) {
        // 创建 Message08 消息
        Message08 message = new Message08();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Message08.EXCHANGE, routingKey, message);
    }
}
