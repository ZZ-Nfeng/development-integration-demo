package top.bulk.mq.rabbit.confirm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@Slf4j
public class RabbitConfiguration {
    @Resource
    ConnectionFactory connectionFactory;

    /**
     * 配置 RabbitTemplate
     *
     * @return RabbitTemplate
     */
    @Bean
    public RabbitTemplate createRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("[确认消息送到交换机(Exchange)结果][相关数据:{}][是否成功:{}][错误原因:{}]", correlationData, ack, cause);
            }
        });

        // 确认消息送到队列(Queue)回调
        // 当 Producer 成功发送消息到 RabbitMQ Broker 时，但是在通过 Exchange 进行匹配不到 Queue 时，Broker 会将该消息回退给 Producer 。
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.info("[确认消息送到队列(Queue)结果：][回应码:{}][回应信息:{}][交换机:{}][路由键:{}][发生消息:{}]"
                        , returnedMessage.getReplyCode()
                        , returnedMessage.getReplyText()
                        , returnedMessage.getExchange()
                        , returnedMessage.getRoutingKey()
                        , returnedMessage.getMessage());
            }
        });
        return rabbitTemplate;
    }
}
