package top.bulk.mq.rabbit.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.message.Message04;

import javax.annotation.Resource;

/**
 * Fanout Exchange 类型
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer04 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @param id         消息内容
     * @param routingKey key
     */
    public void syncSend(String id, String headerValue) {
        // 创建 MessageProperties 属性
        MessageProperties messageProperties = new MessageProperties();
        // 设置 header
        messageProperties.setHeader(Message04.HEADER_KEY, headerValue);
        // 创建 Message 消息
        Message04 msg = new Message04();
        msg.setId(id);
        Message message = rabbitTemplate.getMessageConverter().toMessage(msg, messageProperties);
        // 同步发送消息
        rabbitTemplate.send(Message04.EXCHANGE, null, message);
    }
}
