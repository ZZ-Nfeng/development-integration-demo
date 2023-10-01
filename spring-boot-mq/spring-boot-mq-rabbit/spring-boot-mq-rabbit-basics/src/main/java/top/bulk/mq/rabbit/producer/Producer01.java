package top.bulk.mq.rabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import top.bulk.mq.rabbit.message.Message01;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
public class Producer01 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id) {
        // 创建 Message01 消息
        Message01 message = new Message01();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Message01.EXCHANGE, Message01.ROUTING_KEY, message);
    }

    public void syncSendDefault(String id) {
        // 创建 Message01 消息
        Message01 message = new Message01();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Message01.QUEUE, message);
    }

    @Async
    public ListenableFuture<Void> asyncSend(String id) {
        try {
            // 发送消息
            this.syncSend(id);
            // 返回成功的 Future
            return AsyncResult.forValue(null);
        } catch (Throwable ex) {
            // 返回异常的 Future
            return AsyncResult.forExecutionException(ex);
        }
    }
}
