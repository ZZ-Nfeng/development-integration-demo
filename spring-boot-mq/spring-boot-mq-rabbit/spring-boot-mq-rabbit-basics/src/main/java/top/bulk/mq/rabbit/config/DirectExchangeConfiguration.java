package top.bulk.mq.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.message.Message01;

/**
 * Direct Exchange 示例的配置类
 * 对应的消息是 Message01
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
    public Queue queue01() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(
                Message01.QUEUE,
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
    public DirectExchange exchange01() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new DirectExchange(Message01.EXCHANGE,
                true,
                false);
    }

    /**
     * 创建 Binding
     * Exchange：Message01.EXCHANGE
     * Routing key：Message01.ROUTING_KEY
     * Queue：Message01.QUEUE
     *
     * @return Binding
     */
    @Bean
    public Binding binding01() {
        return BindingBuilder
                .bind(queue01()).to(exchange01())
                .with(Message01.ROUTING_KEY);
    }
}
