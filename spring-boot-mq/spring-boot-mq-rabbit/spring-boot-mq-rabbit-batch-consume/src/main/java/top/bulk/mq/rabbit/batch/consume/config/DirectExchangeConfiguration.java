package top.bulk.mq.rabbit.batch.consume.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.batch.consume.message.Message10;

/**
 * Direct Exchange 示例的配置类
 * 对应的消息是 Message10
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Configuration
public class DirectExchangeConfiguration {
    /**
     * 创建一个 Queue
     *
     * @return Queue
     */
    @Bean
    public Queue queue10() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(
                Message10.QUEUE,
                true,
                false,
                false);
    }

    /**
     * 创建 Direct Exchange
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange exchange10() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new DirectExchange(Message10.EXCHANGE,
                true,
                false);
    }

    /**
     * 创建 Binding
     * Exchange：Message10.EXCHANGE
     * Routing key：Message10.ROUTING_KEY
     * Queue：Message10.QUEUE
     *
     * @return Binding
     */
    @Bean
    public Binding binding10() {
        return BindingBuilder
                .bind(queue10()).to(exchange10())
                .with(Message10.ROUTING_KEY);
    }
}
