package top.bulk.mq.rabbit.confirm.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.confirm.message.Message06;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer06 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id, String routingKey) {
        // 创建 Message06 消息
        Message06 message = new Message06();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Message06.EXCHANGE, routingKey, message);
    }
}
