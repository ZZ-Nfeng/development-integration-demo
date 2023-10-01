package top.bulk.mq.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.message.Message03;

/**
 * Fanout Exchange 示例的配置类
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Configuration
public class FanoutExchangeConfiguration {
    /**
     * 创建 Queue A
     *
     * @return Queue
     */
    @Bean
    public Queue queue03A() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(Message03.QUEUE_A,
                true,
                false,
                false);
    }

    /**
     * 创建 Queue B
     *
     * @return Queue
     */
    @Bean
    public Queue queue03B() {
        return new Queue(Message03.QUEUE_B,
                true,
                false,
                false);
    }

    /**
     * 创建 Fanout Exchange
     *
     * @return FanoutExchange
     */
    @Bean
    public FanoutExchange demo03Exchange() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new FanoutExchange(Message03.EXCHANGE,
                true,
                false);
    }

    /**
     * 创建 Binding A
     * Exchange：Message03.EXCHANGE
     * Queue：Message03.QUEUE_A
     *
     * @return Binding
     */
    @Bean
    public Binding demo03BindingA() {
        return BindingBuilder
                .bind(queue03A()).to(demo03Exchange());
    }

    /**
     * 创建 Binding B
     * Exchange：Message03.EXCHANGE
     * Queue：Message03.QUEUE_B
     *
     * @return Binding
     */
    @Bean
    public Binding demo03BindingB() {
        return BindingBuilder
                .bind(queue03B()).to(demo03Exchange());
    }

}
