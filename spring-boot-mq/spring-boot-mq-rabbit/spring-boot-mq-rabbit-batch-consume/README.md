# Spring Boot 集成 RabbitMQ 演示批量消费

在一些业务场景下，我们希望使用 Consumer 批量消费消息，提高消费速度。
> 官方文档 <https://docs.spring.io/spring-amqp/docs/current/reference/html/#receiving-batch>
>

在 Spring-AMQP 中，我们可以通过对 `SimpleRabbitListenerContainerFactory` 进行配置实现批量消费能力。

## 配置

```java

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
```

## 生产者

```java

@Component
public class Producer10 {

    @Resource
    RabbitTemplate rabbitTemplate;

    public void sendSingle(String id, String routingKey) {
        Message10 message = new Message10();
        message.setId(id);
        rabbitTemplate.convertAndSend(Message10.EXCHANGE, routingKey, message);
    }
}
```

## 消费者

```java
package top.bulk.mq.rabbit.batch.consume.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.bulk.mq.rabbit.batch.consume.message.Message10;

import java.time.LocalDateTime;
import java.util.List;


/**
 * direct 消费者
 *
 * @author 散装java
 * @date 2023-02-18
 */
@RabbitListener(queues = Message10.QUEUE, containerFactory = "consumer10BatchContainerFactory")
@Component
@Slf4j
public class Consumer10 {
    /**
     * 批量消费
     *
     * @param message 一批消息
     */
    @RabbitHandler
    public void onMessage(List<Message10> message) {
        log.info("[{}][Consumer10 批量][线程编号:{}][消息个数：{}][消息内容：{}]"
                , LocalDateTime.now()
                , Thread.currentThread().getId()
                , message.size()
                , message);
    }

    /**
     * 单个消费
     *
     * @param message 一个消息
     */
    @RabbitHandler
    public void onMessage(Message10 message) {
        log.info("[{}][Consumer10 单个][线程编号:{}][消息内容：{}]"
                , LocalDateTime.now()
                , Thread.currentThread().getId()
                , message);
    }
}

```

## 测试

```java

@SpringBootTest
@Slf4j
class Producer10Test {
    @Resource
    Producer10 producer10;

    @Test
    void sendSingle() throws InterruptedException {
        // 假设 一秒一个，发送 15 个，观察消费者的情况
        for (int i = 0; i < 15; i++) {
            TimeUnit.SECONDS.sleep(1);
            String id = UUID.randomUUID().toString();
            producer10.sendSingle(id, Message10.ROUTING_KEY);
            if (i == 9) {
                log.info("[{}][test producer10 sendSingle] 发送成功10个", LocalDateTime.now());
            }
        }
        log.info("[{}][test producer10 sendSingle] 发送成功", LocalDateTime.now());

        TimeUnit.SECONDS.sleep(20);
    }
}
```

测试输出如下

```text
2023-02-24 16:54:32.386  INFO 3600 --- [           main] t.b.m.r.b.c.producer.Producer10Test      : [2023-02-24T16:54:32.386][test producer10 sendSingle] 发送成功10个
2023-02-24 16:54:32.418  INFO 3600 --- [ntContainer#0-1] t.b.m.r.b.consume.consumer.Consumer10    : [2023-02-24T16:54:32.418][Consumer10 批量][线程编号:15][消息个数：10][消息内容：[Message10(id=1b32b92c-1189-4cd2-aa46-e9e3bd61fc73), Message10(id=3f5cbcb1-35f8-461a-8d54-d2634c7e2ffc), Message10(id=ac5c8e96-1399-46e9-bd94-c27785f33815), Message10(id=3978baf8-b072-43e2-a30e-681b059ecc80), Message10(id=ed3513eb-0357-4c93-83d4-567e78953f41), Message10(id=578df2d3-0f1f-4b8f-b585-d814fc964cf6), Message10(id=78e2d376-e0e9-4c48-a77d-fbe6fddb84ce), Message10(id=4a347180-1fd2-43de-a595-24718eb49447), Message10(id=996f3d9c-2ea4-4791-982c-0eb3ffecee96), Message10(id=bb8f4437-d369-40d2-932a-c955aa6da018)]]
2023-02-24 16:54:37.387  INFO 3600 --- [           main] t.b.m.r.b.c.producer.Producer10Test      : [2023-02-24T16:54:37.387][test producer10 sendSingle] 发送成功
2023-02-24 16:54:47.415  INFO 3600 --- [ntContainer#0-1] t.b.m.r.b.consume.consumer.Consumer10    : [2023-02-24T16:54:47.415][Consumer10 批量][线程编号:15][消息个数：5][消息内容：[Message10(id=7881615c-fc1f-495c-8446-38e349138fbd), Message10(id=199ff028-1130-4ee5-a89b-e1a698d2dd61), Message10(id=b1ade3b3-5624-4ac6-95fe-fc1bc458d525), Message10(id=4105df5c-f8e5-47a7-a99d-8f76b98f1ef9), Message10(id=48da9a53-b29c-44e1-bf25-97a34b2635e4)]]
```


