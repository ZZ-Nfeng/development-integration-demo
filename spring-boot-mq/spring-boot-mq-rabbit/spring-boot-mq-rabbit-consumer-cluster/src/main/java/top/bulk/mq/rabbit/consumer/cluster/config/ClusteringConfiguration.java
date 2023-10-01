package top.bulk.mq.rabbit.consumer.cluster.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.consumer.cluster.message.Message13;

/**
 * 集群消费的 配置
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Configuration
public class ClusteringConfiguration {

    @Bean
    public TopicExchange exchange13() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new TopicExchange(Message13.EXCHANGE,
                true,
                false);
    }

}
