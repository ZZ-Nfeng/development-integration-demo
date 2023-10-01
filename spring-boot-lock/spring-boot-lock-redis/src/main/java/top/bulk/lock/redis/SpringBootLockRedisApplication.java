package top.bulk.lock.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * redis 分布式锁演示
 */
@SpringBootApplication
@MapperScan("top.bulk.lock.redis.mapper")
public class SpringBootLockRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLockRedisApplication.class, args);
    }

}
