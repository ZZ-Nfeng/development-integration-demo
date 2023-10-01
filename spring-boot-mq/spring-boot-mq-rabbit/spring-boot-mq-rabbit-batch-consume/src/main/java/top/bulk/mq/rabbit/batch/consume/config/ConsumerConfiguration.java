package top.bulk.mq.rabbit.batch.consume.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 配置消费者
 *
 * @author 散装java
 * @date 2023-02-24
 */
@Configuration
public class ConsumerConfiguration {
    @Resource
    ConnectionFactory connectionFactory;
    @Resource
    SimpleRabbitListenerContainerFactoryConfigurer configurer;

    /**
     * 配置一个批量消费的 SimpleRabbitListenerContainerFactory
     */
    @Bean(name = "consumer10BatchContainerFactory")
    public SimpleRabbitListenerContainerFactory consumer10BatchContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        // 这里是重点 配置消费者的监听器是批量消费消息的类型
        factory.setBatchListener(true);

        // 一批十个
        factory.setBatchSize(10);
        // 等待时间 毫秒 , 这里其实是单个消息的等待时间 指的是单个消息的等待时间
        // 也就是说极端情况下，你会等待 BatchSize * ReceiveTimeout 的时间才会收到消息
        factory.setReceiveTimeout(10 * 1000L);
        factory.setConsumerBatchEnabled(true);

        return factory;
    }
}
