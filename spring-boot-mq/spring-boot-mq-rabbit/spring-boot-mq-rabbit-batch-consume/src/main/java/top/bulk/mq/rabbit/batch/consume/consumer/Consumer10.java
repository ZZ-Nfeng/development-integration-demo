package top.bulk.mq.rabbit.batch.consume.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.batch.consume.message.Message10;

import java.time.LocalDateTime;
import java.util.List;


/**
 * direct 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@RabbitListener(queues = Message10.QUEUE, containerFactory = "consumer10BatchContainerFactory")
@Component
@Slf4j
public class Consumer10 {
    /**
     * 批量消费
     *
     * @param message 一批消息
     */
    @RabbitHandler
    public void onMessage(List<Message10> message) {
        log.info("[{}][Consumer10 批量][线程编号:{}][消息个数：{}][消息内容：{}]"
                , LocalDateTime.now()
                , Thread.currentThread().getId()
                , message.size()
                , message);
    }

    /**
     * 单个消费
     *
     * @param message 一个消息
     */
    @RabbitHandler
    public void onMessage(Message10 message) {
        log.info("[{}][Consumer10 单个][线程编号:{}][消息内容：{}]"
                , LocalDateTime.now()
                , Thread.currentThread().getId()
                , message);
    }
}
