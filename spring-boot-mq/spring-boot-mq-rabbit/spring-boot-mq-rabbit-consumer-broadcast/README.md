# Spring Boot + RabbitMQ 实现广播消费

注意和[集群消费](../spring-boot-mq-rabbit-consumer-cluster/README.md)对比

## 简介

和 集群消费相反， 在广播消费下，我们希望的是每一个应用的每一个副本都要能够实现消费

## 解决方案

我们只需要让每一个程序在启动的时候，都去按照规则生成一个不同的队列即可