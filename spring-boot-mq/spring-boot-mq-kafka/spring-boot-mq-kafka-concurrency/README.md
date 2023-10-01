# Spring Boot + Kafka 并发消费


并发消费可以加速消费的速度，能够有效应对消息积压问题

虽然我们可以通过启动多个 JVM 进程，实现多进程的并发消费，但是我们也可以直接使用 api 实现

Spring-Kafka @KafkaListener 的 concurrency 属性，它可以指定并发消费的线程数。

注意，要想实现并发消费，要关注下面问题

1. Topic 的 Partition 不能是 1 ，要大一些 比如 10，不然没法进行分配
2. 这时候，指定 concurrency = 2 。其实会有两个客户端去进行消息消费，每个客户端会分配到5个 Partition
3. concurrency 不要设置的太大，否则创建的 Kafka Consumer 分配不到消费 Topic 的 Partition 分区，导致不断的空轮询
4. 在使用 Kafka 的时候，每个 Topic 的 Partition 在消息量大的时候，要注意设置的相对大一些，方便并发消费

### 配置

注意，将 Topic 的 Partition 分区设置大一些，这里以 10 分区做演示 (也可以直接在 kafka-ui 改)

```java
@Configuration
public class KafkaConfiguration {
    /**
     * 创建一个  Topic ，指定分区 10
     */
    @Bean
    public NewTopic initialTopic() {
        return new NewTopic(KafkaMessage04.TOPIC, 10, (short) 1);
    }
}
```

### 生产者

```java
@Component
public class KafkaProducer04 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult<Object, Object> asyncSend(String id) throws ExecutionException, InterruptedException {
        KafkaMessage04 message = new KafkaMessage04();
        message.setId(id);
        return kafkaTemplate.send(KafkaMessage04.TOPIC, message).get();
    }
}
```

### 消费者

```java
@Component
@Slf4j
public class KafkaConsumer04 {
    /**
     * 注意，对应 的 Topic 的 分区一定要设置多个
     */
    @KafkaListener(
            topics = KafkaMessage04.TOPIC,
            concurrency = "2",
            groupId = KafkaMessage04.GROUP_ID)
    public void onMessage(KafkaMessage04 message) {
        log.info("[{}][KafkaConsumer04][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
        // 这里执行相应的业务逻辑
    }
}
```

### 单元测试

```java
@SpringBootTest
@Slf4j
class KafkaProducer04Test {
    @Resource
    KafkaProducer04 kafkaProducer04;

    @Test
    void asyncSend() throws InterruptedException, ExecutionException {
        log.info("[{}][testASyncSend][开始执行]", LocalDateTime.now());
        // 发送十条消息测试
        for (int i = 0; i < 10; i++) {
            String id = UUID.randomUUID().toString();
            kafkaProducer04.asyncSend(id);
        }
        // 主要观察消费者的打印,是多个线程并发去处理的
        TimeUnit.SECONDS.sleep(15);
    }
}
```