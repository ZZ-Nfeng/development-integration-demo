package top.bulk.mq.rabbit.delay.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.delay.message.Message11;


/**
 * Direct Exchange 示例的配置类
 * 对应的消息是 Message11
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Configuration
public class DirectExchangeConfiguration {
    /**
     * 延迟队列
     *
     * @return Queue
     */
    @Bean
    public Queue queueDelay11() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(
                Message11.QUEUE_DELAY,
                true,
                false,
                false);
    }

    /**
     * 队列，绑定过期时间等
     *
     * @return Queue
     */
    @Bean
    public Queue queue11() {
        return QueueBuilder
                // durable: 是否持久化
                .durable(Message11.QUEUE)
                // exclusive: 是否排它
                .exclusive()
                // autoDelete: 是否自动删除
                .autoDelete()
                // TTL 设置队列里的默认过期时间为 10 秒
                .ttl(10 * 1000)
                // DLX
                .deadLetterExchange(Message11.EXCHANGE)
                .deadLetterRoutingKey(Message11.ROUTING_KEY_DELAY)
                .build();
    }

    @Bean
    public DirectExchange exchange11() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new DirectExchange(Message11.EXCHANGE,
                true,
                false);
    }

    /**
     * 创建 Binding
     * Exchange：Message11.EXCHANGE
     * Routing key：Message11.ROUTING_KEY
     * Queue：Message11.QUEUE
     *
     * @return Binding
     */
    @Bean
    public Binding binding11() {
        return BindingBuilder
                .bind(queue11()).to(exchange11())
                .with(Message11.ROUTING_KEY);
    }

    /**
     * 绑定延迟队列
     *
     * @return Binding
     */
    @Bean
    public Binding bindingDelay11() {
        return BindingBuilder
                .bind(queueDelay11()).to(exchange11())
                .with(Message11.ROUTING_KEY_DELAY);
    }
}
