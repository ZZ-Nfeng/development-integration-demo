package top.bulk.mq.rabbit.bath.send.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.bath.send.message.Message09;

/**
 * Direct Exchange 示例的配置类
 * 对应的消息是 Message09
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
    public Queue queue09() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(
                Message09.QUEUE,
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
    public DirectExchange exchange09() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new DirectExchange(Message09.EXCHANGE,
                true,
                false);
    }

    /**
     * 创建 Binding
     * Exchange：Message09.EXCHANGE
     * Routing key：Message09.ROUTING_KEY
     * Queue：Message09.QUEUE
     *
     * @return Binding
     */
    @Bean
    public Binding binding09() {
        return BindingBuilder
                .bind(queue09()).to(exchange09())
                .with(Message09.ROUTING_KEY);
    }
}
