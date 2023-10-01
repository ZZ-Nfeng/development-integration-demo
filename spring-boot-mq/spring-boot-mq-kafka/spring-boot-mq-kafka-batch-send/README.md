# Spring Boot + Kafka 批量发送

在一些业务场景下，我们希望使用 Producer 批量发送消息，提高发送性能。

Kafka 提供的批量发送消息，它提供了一个 `RecordAccumulator` 消息收集器，
将发送给相同 Topic 的相同 Partition 分区的消息们，暂存收集在一起，当满足条件时候，一次性批量发送提交给 Kafka Broker 。

如下是三个发送条件，满足任一即会批量发送：

1. 【数量】batch-size ：超过收集的消息数量的最大条数。
2. 【空间】buffer-memory ：超过收集的消息占用的最大内存。
3. 【时间】linger.ms ：超过收集的时间的最大等待时长，单位：毫秒。

### 配置 

具体的配置方式如下(这里我只留下了核心配置，全部配置参考 demo 中的 `application.yml`)
> 实际开发中，linger.ms 并不会设置这么久，请根据自己的业务场景设置参数！


```yaml

spring:
  # Kafka 配置项，对应 KafkaProperties 配置类
  kafka:
    bootstrap-servers: ${REMOTE_URL:127.0.0.1}:9092 # 指定 Kafka Broker 地址，可以设置多个，以逗号分隔
    # Kafka Producer 配置项
    producer:
      batch-size: 16384 # 每次批量发送消息的最大数量
      buffer-memory: 33554432 # 每次批量发送消息的最大内存
      properties:
        linger:
          ms: 10000 # 批处理延迟时间上限。这里配置为 10 * 1000 ms 过后，不管是否消息数量是否到达 batch-size 或者消息大小到达 buffer-memory 后，都直接发送一次请求。
```

### 生产者

```java
@Component
public class KafkaProducer02 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 我们实际用起来，和普通的发送并没有区别
     * <p>
     * 因为我们发送的消息 Topic 是自动创建的，所以其 Partition 分区大小是 1 。
     * 这样，就能保证我每次调用这个方法，满足批量发送消息的一个前提，相同 Topic 的相同 Partition 分区的消息们。
     */
    public ListenableFuture<SendResult<Object, Object>> asyncSend(String id) {
        KafkaMessage02 message = new KafkaMessage02();
        message.setId(id);
        return kafkaTemplate.send(KafkaMessage02.TOPIC, message);
    }
}
```

### 消费者
```java
@Component
@Slf4j
public class KafkaConsumer02 {

    @KafkaListener(topics = KafkaMessage02.TOPIC, groupId = KafkaMessage02.GROUP_ID)
    public void onMessage(KafkaMessage02 message) {
        log.info("[{}][KafkaConsumer02][线程编号:{} 消息内容：{}]", LocalDateTime.now(), Thread.currentThread().getId(), message);
        // 这里执行相应的业务逻辑
    }
}
```

### 单元测试

```java
@SpringBootTest
@Slf4j
class KafkaProducer02Test {
    @Resource
    KafkaProducer02 kafkaProducer02;

    @Test
    void asyncSend() throws InterruptedException {
        log.info("[{}][testASyncSend][开始执行]", LocalDateTime.now());

        for (int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString();
            kafkaProducer02.asyncSend(id).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

                @Override
                public void onFailure(Throwable e) {
                    log.info("[{}][testASyncSend][发送编号：[{}] 发送异常]]", LocalDateTime.now(), id, e);
                }

                @Override
                public void onSuccess(SendResult<Object, Object> result) {
                    log.info("[{}][testASyncSend][发送编号：[{}] 发送成功，结果为：[{}]]", LocalDateTime.now(), id, result);
                }

            });

            // 每条消息间隔两秒，注意观察消费者是何时消费消息的
            Thread.sleep(2 * 1000L);
        }
        // 最终我们可以看到，其实没有达到发送要求之前，并没有将消息发出去，也不会有消息回调，最终在达到配置 linger.ms 的时间之后，才会有发送的回执
        TimeUnit.SECONDS.sleep(15);
    }
}
```