package top.bulk.mq.rabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.message.Message03;

import javax.annotation.Resource;

/**
 * Fanout Exchange 类型
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer03 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @param id         消息内容
     * @param routingKey key
     */
    public void syncSend(String id) {
        Message03 message = new Message03();
        message.setId(id);
        rabbitTemplate.convertAndSend(Message03.EXCHANGE, null, message);
    }
}
