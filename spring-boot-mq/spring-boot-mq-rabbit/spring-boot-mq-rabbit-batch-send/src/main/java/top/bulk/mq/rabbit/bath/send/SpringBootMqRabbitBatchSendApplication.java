package top.bulk.mq.rabbit.bath.send;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 演示 RabbitMQ 批量发送
 *
 * @author 散装java
 */
@SpringBootApplication
public class SpringBootMqRabbitBatchSendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqRabbitBatchSendApplication.class, args);
    }

}
