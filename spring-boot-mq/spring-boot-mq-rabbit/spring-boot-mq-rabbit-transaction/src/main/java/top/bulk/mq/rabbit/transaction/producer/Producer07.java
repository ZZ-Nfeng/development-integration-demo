package top.bulk.mq.rabbit.transaction.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.bulk.mq.rabbit.transaction.message.Message07;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
@Slf4j
public class Producer07 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 在发送消息方法上，我们添加了 @Transactional 注解，声明事务。
     * 因为我们创建了 RabbitTransactionManager 事务管理器，所以这里会创建 RabbitMQ 事务
     * <p>
     * 当然也可以使用编程式事务
     * channel.txSelect();
     * channel.basicPublish();
     * channel.txCommit();
     * channel.txRollback(); // 回滚事务
     *
     * @param id         id
     * @param routingKey routingKey
     * @throws InterruptedException 异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncSend(String id, String routingKey) throws InterruptedException {
        // 创建 Message07 消息
        Message07 message = new Message07();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Message07.EXCHANGE, routingKey, message);
        log.info("[{}][Producer07 syncSend][此时已经发送][id:{}]", LocalDateTime.now(), id);
        /*
            睡上 10s 方便看效果
            如果同步发送消息成功后，Consumer 立即消费到该消息，说明未生效
            如果 Consumer 是 10 秒之后，才消费到该消息，说明已生效
         */
        TimeUnit.SECONDS.sleep(10);
    }
}
