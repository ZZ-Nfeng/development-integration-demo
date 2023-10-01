package top.bulk.mq.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.message.Message04;

/**
 * Headers Exchange 配置
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Configuration
public class HeadersExchangeConfiguration {
    /**
     * 创建 Queue
     *
     * @return Queue
     */
    @Bean
    public Queue queue04() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(Message04.QUEUE,
                true,
                false,
                false);
    }

    /**
     * Headers Exchange 创建
     *
     * @return HeadersExchange
     */
    @Bean
    public HeadersExchange exchange04() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new HeadersExchange(Message04.EXCHANGE,
                true,
                false);
    }

    /**
     * 创建 Binding
     * Exchange：Message04.EXCHANGE
     * Queue：Message04.QUEUE
     * Headers: Message04.HEADER_KEY + Message04.HEADER_VALUE
     *
     * @return Binding
     */
    @Bean
    public Binding binding04() {
        return BindingBuilder
                .bind(queue04()).to(exchange04())
                // 配置 Headers 匹配
                .where(Message04.HEADER_KEY).matches(Message04.HEADER_VALUE);
    }
}
