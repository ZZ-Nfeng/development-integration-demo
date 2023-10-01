package top.bulk.mq.rabbit.consumer.broadcast.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.consumer.broadcast.message.Message14;

/**
 * 广播消费的 配置
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Configuration
public class BroadcastConfiguration {

    @Bean
    public TopicExchange exchange14() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new TopicExchange(Message14.EXCHANGE,
                true,
                false);
    }

}
