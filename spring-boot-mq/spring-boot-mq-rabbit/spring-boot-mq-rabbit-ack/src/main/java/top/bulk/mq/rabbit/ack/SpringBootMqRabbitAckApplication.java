package top.bulk.mq.rabbit.ack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试 RabbitMq 的 ack
 */
@SpringBootApplication
public class SpringBootMqRabbitAckApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqRabbitAckApplication.class, args);
    }

}
