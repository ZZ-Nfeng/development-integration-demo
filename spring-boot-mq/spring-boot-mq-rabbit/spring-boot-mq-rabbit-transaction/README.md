# Spring Boot 集成 RabbitMQ 演示 事务

RabbitMQ 内置提供事务消息的支持, 不过 RabbitMQ 提供的并不是完整的的事务消息的支持，缺少了回查机制。

目前，常用的分布式消息队列，只有 RocketMQ 提供了完整的事务消息的支持.

> ps: 事务这种方式，性能很低！

## 配置

需要注入一个事务管理器

```java

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
```

## 使用

配合 `@Transactional` 声明式事务

```java

@Component
@Slf4j
public class Producer07 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 在发送消息方法上，我们添加了 @Transactional 注解，声明事务。
     * 因为我们创建了 RabbitTransactionManager 事务管理器，所以这里会创建 RabbitMQ 事务
     * <p>
     * 当然也可以使用编程式事务
     * channel.txSelect();
     * channel.basicPublish();
     * channel.txCommit();
     *
     * @param id         id
     * @param routingKey routingKey
     * @throws InterruptedException 异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncSend(String id, String routingKey) throws InterruptedException {
        // 创建 Message07 消息
        Message07 message = new Message07();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Message07.EXCHANGE, routingKey, message);
        log.info("[{}][Producer07 syncSend][此时已经发送][id:{}]", LocalDateTime.now(), id);
        /*
            睡上 10s 方便看效果
            如果同步发送消息成功后，Consumer 立即消费到该消息，说明未生效
            如果 Consumer 是 10 秒之后，才消费到该消息，说明已生效
         */
        TimeUnit.SECONDS.sleep(10);
    }
}
```

## 测试

参考 `Producer07Test`

```java

@SpringBootTest
@Slf4j
class Producer07Test {
    @Resource
    Producer07 producer07;

    @Test
    void syncSend() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        producer07.syncSend(id, Message07.ROUTING_KEY);
        log.info("[{}][test producer07 syncSend][id:{}] 发送成功", LocalDateTime.now(), id);

        TimeUnit.SECONDS.sleep(2);
    }
}
```