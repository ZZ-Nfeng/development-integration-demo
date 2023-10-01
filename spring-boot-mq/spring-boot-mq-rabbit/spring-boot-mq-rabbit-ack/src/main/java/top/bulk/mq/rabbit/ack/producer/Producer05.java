package top.bulk.mq.rabbit.ack.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.ack.message.Message05;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer05 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id) {
        // 创建 Message05 消息
        Message05 message = new Message05();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Message05.EXCHANGE, Message05.ROUTING_KEY, message);
    }
}
