# Spring Boot 集成 RabbitMQ 生产者 confirm 功能

## 配置

### application.yml 配置文件

开启发布者确认

```yaml
spring:
  # RabbitMQ 配置项，对应 RabbitProperties 配置类
  rabbitmq:
    host: ${REMOTE_URL:127.0.0.1}
    port: 5672
    username: admin
    password: admin
    # 设置 Confirm 类型为 CORRELATED 。
    publisher-confirm-type: correlated
    publisher-returns: true
```

> 在该类型下，Spring-AMQP 在创建完 RabbitMQ Channel 之后，也会自动调用 Channel#confirmSelect() 方法，将 Channel 设置为 Confirm 模式。
>

其中 `publisher-confirm-type` 参数的可以参考 类 `ConfirmType`

- **SIMPLE**: 使用同步的 Confirm 模式
- **CORRELATED**: 使用异步的 Confirm 模式
- **NONE**:  不使用 Confirm 模式

### 配置类

需要在 RabbitTemplate 中 设置 ConfirmCallback 回调处理，以及 ReturnsCallback ，详细代码参考 `RabbitConfiguration`

```java

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
```

## 生产者

详细代码 `Producer06`

```java

@Component
public class Producer06 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String id, String routingKey) {
        // 创建 Message06 消息
        Message06 message = new Message06();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Message06.EXCHANGE, routingKey, message);
    }
}
```

## 消费者

详细代码 `Consumer06`

```java

@Component
@RabbitListener(queues = Message06.QUEUE)
@Slf4j
public class Consumer06 {

    @RabbitHandler
    public void onMessage(Message06 message) {
        log.info("[Message06 onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
```

## 测试

测试参考类 `Producer06Test`