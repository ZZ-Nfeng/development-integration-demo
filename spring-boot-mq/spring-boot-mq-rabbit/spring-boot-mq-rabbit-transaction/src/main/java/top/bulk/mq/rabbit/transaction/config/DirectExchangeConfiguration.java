package top.bulk.mq.rabbit.transaction.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.transaction.message.Message07;

/**
 * Direct Exchange 示例的配置类
 * 对应的消息是 Message07
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
    public Queue queue07() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(
                Message07.QUEUE,
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
    public DirectExchange exchange07() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new DirectExchange(Message07.EXCHANGE,
                true,
                false);
    }

    /**
     * 创建 Binding
     * Exchange：Message07.EXCHANGE
     * Routing key：Message07.ROUTING_KEY
     * Queue：Message07.QUEUE
     *
     * @return Binding
     */
    @Bean
    public Binding binding07() {
        return BindingBuilder
                .bind(queue07()).to(exchange07())
                .with(Message07.ROUTING_KEY);
    }
}
