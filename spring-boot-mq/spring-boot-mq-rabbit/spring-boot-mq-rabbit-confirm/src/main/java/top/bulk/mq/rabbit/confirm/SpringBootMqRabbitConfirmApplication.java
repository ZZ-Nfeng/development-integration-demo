package top.bulk.mq.rabbit.confirm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RabbitMQ 生产者 confirm 演示
 *
 * @author 散装java
 */
@SpringBootApplication
public class SpringBootMqRabbitConfirmApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqRabbitConfirmApplication.class, args);
    }

}
