package top.bulk.mq.rabbit.concurrency.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.concurrency.message.Message08;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


/**
 * direct 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Component
// 开启并发消费
@RabbitListener(queues = Message08.QUEUE, concurrency = "2")
@Slf4j
public class Consumer08 {

    @RabbitHandler
    public void onMessage(Message08 message) throws InterruptedException {
        log.info("[{}][Consumer08 onMessage][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
        // 模拟消费耗时，为了让并发消费效果更好的展示
        TimeUnit.SECONDS.sleep(1);
    }
}
