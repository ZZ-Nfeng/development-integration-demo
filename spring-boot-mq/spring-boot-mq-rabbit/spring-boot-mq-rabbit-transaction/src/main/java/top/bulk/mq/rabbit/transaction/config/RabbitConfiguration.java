package top.bulk.mq.rabbit.transaction.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    @Resource
    RabbitTemplate rabbitTemplate;

    /**
     * 注入一个事务管理器 RabbitTransactionManager
     *
     * @return RabbitTransactionManager
     */
    @Bean
    public RabbitTransactionManager rabbitTransactionManager() {
        // 设置 RabbitTemplate 支持事务
        rabbitTemplate.setChannelTransacted(true);

        // 创建 RabbitTransactionManager 对象
        return new RabbitTransactionManager(connectionFactory);
    }
}
