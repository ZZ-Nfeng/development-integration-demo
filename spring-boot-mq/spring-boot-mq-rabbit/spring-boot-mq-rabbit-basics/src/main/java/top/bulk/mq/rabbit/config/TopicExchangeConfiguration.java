package top.bulk.mq.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.message.Message02;

/**
 * TopicExchange 配置
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Configuration
public class TopicExchangeConfiguration {
    /**
     * 创建 Queue
     *
     * @return Queue
     */
    @Bean
    public Queue queue02() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(Message02.QUEUE,
                true,
                false,
                false);
    }

    /**
     * 创建 Topic Exchange
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange exchange02() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new TopicExchange(Message02.EXCHANGE,
                true,
                false);
    }

    /**
     * 创建 Binding
     * Exchange：Message02.EXCHANGE
     * Routing key：Message02.ROUTING_KEY
     * Queue：Message02.QUEUE
     *
     * @return Binding
     */
    @Bean
    public Binding binding02() {
        return BindingBuilder
                .bind(queue02()).to(exchange02())
                .with(Message02.ROUTING_KEY);
    }
}
