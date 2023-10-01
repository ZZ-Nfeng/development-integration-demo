package top.bulk.mq.rabbit.concurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 演示批量消费
 *
 * @author 散装java
 */
@SpringBootApplication
public class SpringBootMqRabbitConcurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqRabbitConcurrencyApplication.class, args);
    }

}
