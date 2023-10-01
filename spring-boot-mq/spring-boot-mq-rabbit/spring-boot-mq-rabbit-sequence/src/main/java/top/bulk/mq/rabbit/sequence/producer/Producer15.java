package top.bulk.mq.rabbit.sequence.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.sequence.message.Message15;

import javax.annotation.Resource;

/**
 * 想要保证消息的顺序性，要确保发送时候的顺序性，即不要并发发送
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@Slf4j
public class Producer15 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 这里的发送是 拟投递到多个队列中
     *
     * @param id  业务id
     * @param msg 业务信息
     */
    public void syncSend(int id, String msg) {
        Message15 message = new Message15(id, msg);
        rabbitTemplate.convertAndSend(Message15.EXCHANGE, this.getRoutingKey(id), message);
    }

    /**
     * 根据 id 取余来决定丢到那个队列中去
     *
     * @param id id
     * @return routingKey
     */
    private String getRoutingKey(int id) {
        return String.valueOf(id % Message15.QUEUE_COUNT);
    }
}
