# Spring Boot 集成 Kafka 集成篇

## 简介

### 什么是 Kafka

> 更多介绍可以看这里 <https://www.oschina.net/p/kafka>

Kafka 是一种高吞吐量的分布式发布订阅消息系统，她有如下特性：

- 通过 O (1) 的磁盘数据结构提供消息的持久化，这种结构对于即使数以 TB 的消息存储也能够保持长时间的稳定性能。
- 高吞吐量：即使是非常普通的硬件 Kafka 也可以支持每秒数十万的消息。
- 支持通过 Kafka 服务器和消费机集群来分区消息。
- 支持 Hadoop 并行数据加载。

### Spring-Kafka

在 Spring 生态中，提供了 Spring-Kafka 项目，让我们更简便的使用 Kafka

更多的信息可以参考官网 <https://docs.spring.io/spring-kafka/reference/html/>

### 示例版本简介

|技术栈|版本|
|---|---|
|Java|1.8|
|Spring Boot| 2.7.0|
|Maven | 3.8.3|
|Kafka| 3.4 |

### Kafka 部署

这里提供一下以 Docker Compose 的部署方式，一键启动，十分方便（内含 ui 控制台）

<https://doc.bulkall.top/kafka/install-docker-compose/>

## 集成

### 导入依赖

需要导入 spring-kafka 以及 spring-boot-starter-json 就可以实现基本的应用

```xml

<dependencies>
    <!-- 引入 spring-kafka 依赖 内置了 kafka-clients ,ps: 目前好像没有 spring-boot-kafka-starter  -->
    <!-- https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>

    <!-- 实现对 JSON 的自动化配置 -->
    <!-- 因为，Kafka 对复杂对象的 Message 序列化时，我们会使用到 JSON
         同时，spring-boot-starter-json 引入了 spring-boot-starter ，而 spring-boot-starter 又引入了 spring-boot-autoconfigure 。
         spring-boot-autoconfigure 实现了 spring-kafka 的自动化配置
     -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-json</artifactId>
    </dependency>
</dependencies>
```

### 配置

一般的配置都通过 `application.yml` 来实现的，也可以通过相应的配置类来实现

本示例只演示最简单的使用

```yaml
spring:
  application:
    name: spring-boot-mq-kafka-basics
  # Kafka 配置项，对应 KafkaProperties 配置类
  kafka:
    bootstrap-servers: ${REMOTE_URL:127.0.0.1}:9092 # 指定 Kafka Broker 地址，可以设置多个，以逗号分隔
    # Kafka Producer 配置项
    producer:
      acks: 1 # 0-不应答。1-leader 应答。all-所有 leader 和 follower 应答。
      retries: 3 # 发送失败时，重试发送的次数
      key-serializer: org.apache.kafka.common.serialization.StringSerializer # 消息的 key 的序列化
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer # 消息的 value 的序列化
    # Kafka Consumer 配置项
    consumer:
      auto-offset-reset: earliest # 设置消费者分组最初的消费进度为 earliest 。
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
```
### 使用 - 生产者

```java
@Component
public class KafkaProducer01 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 发送同步消息
     */
    public SendResult<Object, Object> syncSend(String id) throws ExecutionException, InterruptedException {
        KafkaMessage01 message = new KafkaMessage01();
        message.setId(id);
        // 同步发送消息
        return kafkaTemplate.send(KafkaMessage01.TOPIC, message).get();
    }

    /**
     * 发送异步消息
     */
    public ListenableFuture<SendResult<Object, Object>> asyncSend(String id) {
        KafkaMessage01 message = new KafkaMessage01();
        message.setId(id);
        // 异步发送消息
        return kafkaTemplate.send(KafkaMessage01.TOPIC, message);
    }
}
```

### 使用 - 消费者

```java
@Component
@Slf4j
public class KafkaConsumer01 {
    /**
     * topics topic名称
     * groupId 是设置消费组
     */
    @KafkaListener(topics = KafkaMessage01.TOPIC,
            groupId = "consumer-group-01-" + KafkaMessage01.TOPIC)
    public void onMessage(KafkaMessage01 message) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
```

### 测试 

参考测试类 `KafkaProducer01Test` 