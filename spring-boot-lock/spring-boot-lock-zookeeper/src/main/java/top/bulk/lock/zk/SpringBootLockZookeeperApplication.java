package top.bulk.lock.zk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 集成 zk 演示，zk 实现分布式锁
 *
 * @author 散装java
 */
@SpringBootApplication
@MapperScan("top.bulk.lock.zk.mapper")
public class SpringBootLockZookeeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLockZookeeperApplication.class, args);
    }

}
