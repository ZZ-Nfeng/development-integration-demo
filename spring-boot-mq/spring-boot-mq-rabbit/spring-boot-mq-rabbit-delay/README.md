# Spring Boot + RabbitMQ 的 延迟队列 功能

目前有两种方式可以实现

1. 利用两个特性： Time To Live(TTL)、Dead Letter Exchanges（DLX）
    - RabbitMQ 提供了过期时间 TTL 机制，可以设置消息在队列中的存活时长。在消息到达过期时间时，会从当前队列中删除，并被 RabbitMQ 自动转发到对应的死信队列中。
    - 然后再来消费该死信队列，这样就可以实现一个延迟队列的效果
2. 利用 RabbitMQ 中的插件 x-delay-message

## 一、TTL 方式

此种当时整体逻辑实现 参考 `DirectExchangeConfiguration` 与  `Message11` 11 的相关配置与测试

## 二、插件方式

> ps: 安装插件的过程可以参考 ==> [RabbitMQ 安装延迟队列插件(Docker)](https://doc.bulkall.top/rabbitmq/install-delay-docker/)


此种当时整体逻辑实现 参考 `PluginsExchangeConfiguration` 与 `Message12` 12 的相关配置与测试

> ps: 如果你在插件安装之前，就已经创建了交换机，那么要把他删除了重新创建，不然不会生效！