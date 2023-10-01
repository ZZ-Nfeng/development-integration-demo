package top.bulk.mq.rabbit.delay.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.delay.message.Message12;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer12 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id, int delay) {
        Message12 message = new Message12();
        message.setId(id);

        MessagePostProcessor postProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置过期时间
                message.getMessageProperties().setHeader("x-delay", delay);
                return message;
            }
        };

        rabbitTemplate.convertAndSend(Message12.EXCHANGE, Message12.ROUTING_KEY, message, postProcessor);
    }
}
