package top.bulk.mq.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 集成 RabbitMQ
 * @author 散装java
 */
@SpringBootApplication
@EnableAsync
public class SpringBootMqRabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqRabbitApplication.class, args);
    }

}
