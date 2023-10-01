# Spring Boot + Kafka 事务 

KafkaAutoConfiguration 只有在设置了 `spring.kafka.producer.transaction-id-prefix` 属性时才配置了 kafkaTransactionManager bean。
配置了之后，就可以 直接使用 `@Transactional` 解释来控制事务了

## 配置

注意配置文件中需要改的地方有

```yaml
# 0-不应答。1-leader 应答。all-所有 leader 和 follower 应答。
spring.kafka.producer.acks = all

# 开启事务 只要配置了该参数 就会有事务管理器被注入，可直接使用
spring.kafka.producer.transaction-id-prefix = tx-bulk
```

