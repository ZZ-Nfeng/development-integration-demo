package top.bulk.mq.rabbit.bath.send.config;

import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.annotation.Resource;

/**
 * rabbit 配置
 *
 * @author 散装java
 * @date 2023-02-21
 */
@Configuration
public class RabbitConfiguration {
    @Resource
    ConnectionFactory connectionFactory;

    /**
     * 注入一个批量 template
     * Spring-AMQP 通过 BatchingRabbitTemplate 提供批量发送消息的功能。如下是三个条件，满足任一即会批量发送：
     * <p>
     * 【数量】batchSize ：超过收集的消息数量的最大条数。
     * 【空间】bufferLimit ：超过收集的消息占用的最大内存。
     * 【时间】timeout ：超过收集的时间的最大等待时长，单位：毫秒。
     * 不过要注意，这里的超时开始计时的时间，是以最后一次发送时间为起点。也就说，每调用一次发送消息，都以当前时刻开始计时，重新到达 timeout 毫秒才算超时。
     *
     * @return BatchingRabbitTemplate
     */
    @Bean
    public BatchingRabbitTemplate batchRabbitTemplate() {
        // 创建 BatchingStrategy 对象，代表批量策略
        // 超过收集的消息数量的最大条数。
        int batchSize = 10;
        // 每次批量发送消息的最大内存 b
        int bufferLimit = 1024 * 1024;
        // 超过收集的时间的最大等待时长，单位：毫秒
        int timeout = 10 * 1000;
        BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(batchSize, bufferLimit, timeout);
        // 创建 TaskScheduler 对象，用于实现超时发送的定时器
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
        // 创建 BatchingRabbitTemplate 对象
        BatchingRabbitTemplate batchTemplate = new BatchingRabbitTemplate(batchingStrategy, taskScheduler);
        batchTemplate.setConnectionFactory(connectionFactory);
        return batchTemplate;
    }
}
