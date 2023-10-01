package top.bulk.mq.rabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.message.Message02;

import javax.annotation.Resource;

/**
 * 生产者 topic 类型
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer02 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @param id         消息内容
     * @param routingKey key
     */
    public void syncSend(String id, String routingKey) {
        Message02 message = new Message02();
        message.setId(id);
        rabbitTemplate.convertAndSend(Message02.EXCHANGE, routingKey, message);
    }
}
