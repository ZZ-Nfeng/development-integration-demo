package top.bulk.mq.kafka.basics.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.kafka.basics.message.KafkaMessage01;

/**
 * 消费者
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Component
@Slf4j
public class KafkaConsumer01 {
    /**
     * topics topic名称
     * groupId 是设置消费组，
     * 默认情况下 项目启动后，没有这个 topic 的话，程序就会自动创建
     * <p>
     * KafkaListener注解 常用的几个属性说明
     * <ul>
     * <li>topics 监听的 Topic 数组 (支持 Spel)</li>
     * <li>topicPattern 监听的 Topic 表达式 (支持 Spel)</li>
     * <li>groupId 消费者分组</li>
     * <li>errorHandler 使用消费异常处理器 KafkaListenerErrorHandler 的 Bean 名字</li>
     * <li>concurrency 自定义消费者监听器的并发数</li>
     * <li>autoStartup 是否自动启动监听器。默认情况下，为 true 自动启动。</li>
     * <li>properties Kafka Consumer 拓展属性</li>
     * </ul>
     */
    @KafkaListener(topics = KafkaMessage01.TOPIC, groupId = "consumer-group-01-" + KafkaMessage01.TOPIC)
    public void onMessage(KafkaMessage01 message) {
        log.info("[KafkaConsumer01 consumer-group-01][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    /**
     * 这个也是一个消费者，和上面那个不同的是，我们新给他执行了一个消费者组 01A ，上面那个是 01
     * 这样子，我们可以测试 Kafka 的集群消费特性，即这两个消费者，都会消费到消息，互不影响
     */
    @KafkaListener(topics = KafkaMessage01.TOPIC, groupId = "consumer-group-01A-" + KafkaMessage01.TOPIC)
    public void onMessageA(KafkaMessage01 message) {
        log.info("[KafkaConsumer01 consumer-group-01A1][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    /**
     * 这个消费者和上面形成对比，演示的是，同一个组内的消息，只会被其中的一个消费者所消费
     * ConsumerRecord 是 Kafka 的内置对象，我们可以获取到消费的消息的更多信息，例如说消息的所属队列、创建时间等等属性，
     * 不过消息的内容(value)就需要自己去反序列化。当然，一般情况下，我们不会使用 ConsumerRecord 类。
     */
    @KafkaListener(topics = KafkaMessage01.TOPIC, groupId = "consumer-group-01A-" + KafkaMessage01.TOPIC)
    public void onMessageA2(ConsumerRecord<Integer, String> record) {
        log.info("[KafkaConsumer01 consumer-group-01A2][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), record);
    }
}
