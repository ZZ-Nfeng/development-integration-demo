# Spring Boot + Kafka 批量消费

在一些业务场景下，我们希望使用 Consumer 批量消费消息，提高消费速度。

### 配置

具体的配置方式如下(这里我只留下了核心配置，全部配置参考 demo 中的 `application.yml`)
> 实际开发中，请根据自己的业务场景设置参数！

```yaml
spring:
  application:
    name: spring-boot-mq-kafka-batch-consume
  # Kafka 配置项，对应 KafkaProperties 配置类
  kafka:
    bootstrap-servers: ${REMOTE_URL:127.0.0.1}:9092 # 指定 Kafka Broker 地址，可以设置多个，以逗号分隔
    consumer:
      fetch-max-wait: 10000 # 一次拉取的阻塞的最大时长，单位：毫秒。这里指的是阻塞拉取需要满足至少 fetch-min-size 大小的消息
      fetch-min-size: 25600 # 一次消息拉取的最小数据量，单位：字节
      max-poll-records: 100 # 一次消息拉取的最大数量
    # 这个配置如果配置 SINGLE ，就只会单条消费了
    listener:
      type: batch
```

### 生产者

```java
@Component
public class KafkaProducer03 {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public ListenableFuture<SendResult<Object, Object>> asyncSend(String id) {
        KafkaMessage03 message = new KafkaMessage03();
        message.setId(id);
        return kafkaTemplate.send(KafkaMessage03.TOPIC, message);
    }
}
```

### 消费者

```java
@Component
@Slf4j
public class KafkaConsumer03 {
    /**
     * ps：如果配置改成 spring.kafka.listener.type=SINGLE ，就会发现这里只会单条消费了。
     *
     */
    @KafkaListener(topics = KafkaMessage03.TOPIC, groupId = KafkaMessage03.GROUP_ID)
    public void onMessage(List<KafkaMessage03> list) {
        log.info("[{}][KafkaMessage03][消息数量：{} 消息内容：{}]", LocalDateTime.now(), list.size(), list);
        // 这里执行相应的业务逻辑
    }
}
```

### 单元测试

```java
@SpringBootTest
@Slf4j
class KafkaProducer03Test {
    @Resource
    KafkaProducer03 kafkaProducer03;

    @Test
    void asyncSend() throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString();
            kafkaProducer03.asyncSend(id).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

                @Override
                public void onFailure(Throwable e) {
                    log.info("[{}][testASyncSend][发送编号：[{}] 发送异常]]", LocalDateTime.now(), id, e);
                }

                @Override
                public void onSuccess(SendResult<Object, Object> result) {
                    log.info("[{}][testASyncSend][发送编号：[{}] 发送成功，结果为：[{}]]", LocalDateTime.now(), id, result);
                }

            });
        }
        // 最终我们可以看到，测试类中一次性发送了三条，消费者端一次性消费了三条，证明可以批量消费
        TimeUnit.SECONDS.sleep(15);
    }
}
```