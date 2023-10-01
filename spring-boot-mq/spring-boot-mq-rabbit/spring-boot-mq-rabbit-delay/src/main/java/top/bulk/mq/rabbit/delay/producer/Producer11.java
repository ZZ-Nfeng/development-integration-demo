package top.bulk.mq.rabbit.delay.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.delay.message.Message11;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer11 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id, int delay) {
        Message11 message = new Message11();
        message.setId(id);

        MessagePostProcessor postProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息的 TTL 过期时间
                if (delay > 0) {
                    message.getMessageProperties().setExpiration(String.valueOf(delay));
                }
                return message;
            }
        };

        rabbitTemplate.convertAndSend(Message11.EXCHANGE, Message11.ROUTING_KEY, message, postProcessor);
    }
}
