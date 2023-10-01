package top.bulk.mq.rabbit.delay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.delay.message.Message12;

import java.util.HashMap;
import java.util.Map;

/**
 * 采用 RabbitMQ 插件的方式实现 延迟队列
 * 这种方式必须要安装 延时插件
 * @author 散装java
 * @date 2023-02-27
 */
@Configuration
public class PluginsExchangeConfiguration {

    @Bean
    public Queue queue12() {
        // Queue:名字 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
        return new Queue(
                Message12.QUEUE,
                true,
                false,
                false);
    }

    /**
     * 创建一个延迟交换机 注意类型为 “x-delayed-message”
     *
     * @return 交换机
     */
    @Bean
    public CustomExchange exchange12() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(Message12.EXCHANGE, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding binding12() {
        return BindingBuilder
                .bind(queue12()).to(exchange12())
                .with(Message12.ROUTING_KEY)
                .noargs();
    }
}
