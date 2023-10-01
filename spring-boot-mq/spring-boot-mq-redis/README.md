# Spring Boot 集成 Redis 实现消息队列功能

> Redis 发布订阅语法参考 <https://www.redis.net.cn/order/3634.html>

## 说明

本项目，演示了 Spring Boot 集成 Redis ，通过系列自定义注解实现消息队列功能 ，其中包含一下几种实现

1. 基于 pub/sub 的广播
2. 基于 list 的 lPush/brPop 的订阅
3. 基于 stream 的实现

## 版本依赖

|技术栈|版本|
| ----| ---- |
|Spring Boot | 2.7.0 |
|Redis | 6.2 |
|Maven|3.8.3|

## 启动说明

1. 更改 Redis 配置 [application.yml](src/main/resources/application.yml)
3. 点击启动类 [SpringBootLockMysqlApplication](src/main/java/top/bulk/mq/redis/SpringBootMqRedisApplication.java)
   启动项目
4. 访问接口 `http://localhost:8080/test` , 接口会向队列中推送消息，注意查看控制到消费者的输出

## 代码说明

项目中，通过 MessageConsumerStater 类，在项目启动的时候，自动的去注入相关的队列的消费者(标注了@MessageConsumer注解的类)

其中，针对消息的监听的实现是位于 [handler](src/main/java/top/bulk/mq/redis/handler) 包下的, AbstractMessageHandler
为抽象类，不同种的实现只要集成，并取重写其中的方法即可

[MqConsumer](src/main/java/top/bulk/mq/redis/context/RedisMqConsumer.java) 类为消费者，会消费相对的消息

## stream 方式

> 消费组从stream中获取到消息后，会分配给自己组中其中的一个消费者进行消费，消费者消费完毕，需要给消费组返回ACK，表示这条消息已经消费完毕了。
> 
> 当消费者从消费组获取到消息的时候，会先把消息添加到自己的pending消息列表，当消费者给消费组返回ACK的时候，就会把这条消息从pending队列删除。（每个消费者都有自己的pending消息队列）
> 
> 消费者可能没有及时的返回ACK。例如消费者消费完毕后，宕机，没有及时返回ACK，此时就会导致这条消息占用2倍的内存（stream中保存一份, 消费者的的pending消息列表中保存一份）
