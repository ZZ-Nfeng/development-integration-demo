package top.bulk.mq.kafka.transaction.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.bulk.mq.kafka.transaction.message.KafkaMessage06;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

/**
 * 生产者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
@Slf4j
public class KafkaProducer06 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 注意 @Transactional 必须配合 事务管理器使用，即要配置 spring.kafka.producer.transaction-id-prefix
     */
    @Transactional(rollbackFor = Exception.class)
    public SendResult<Object, Object> asyncSend(String id) throws ExecutionException, InterruptedException {
        KafkaMessage06 message = new KafkaMessage06();
        message.setId(id);
        SendResult<Object, Object> result = kafkaTemplate.send(KafkaMessage06.TOPIC, message).get();
        log.info("[{}][KafkaProducer06][send 已调用]", LocalDateTime.now());
        // 模拟异常，看这条消息是否还能成功发送
        double i = 1 / 0;
        return result;
    }

    public SendResult<Object, Object> asyncSend2(String id) {
        // 事务消息， 编程式
        return kafkaTemplate.executeInTransaction(operationsCallBack -> {
            KafkaMessage06 message = new KafkaMessage06();
            message.setId(id);
            SendResult<Object, Object> result;
            try {
                result = kafkaTemplate.send(KafkaMessage06.TOPIC, message).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.info("[{}][KafkaProducer06][send 已调用]", LocalDateTime.now());
            // 模拟异常，看这条消息是否还能成功发送
            double i = 1 / 0;
            return result;
        });

    }
}
