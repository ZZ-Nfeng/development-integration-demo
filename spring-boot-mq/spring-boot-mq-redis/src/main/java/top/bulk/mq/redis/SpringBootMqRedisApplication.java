package top.bulk.mq.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 集成 Redis 做消息队列
 * @author 散装java
 */
@SpringBootApplication
public class SpringBootMqRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqRedisApplication.class, args);
    }

}
