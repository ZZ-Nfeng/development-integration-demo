package top.bulk.lock.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 集成 MySQL 实现分布式锁（乐观锁、悲观锁）
 */
@SpringBootApplication
@MapperScan("top.bulk.lock.mysql.mapper")
public class SpringBootLockMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLockMysqlApplication.class, args);
    }

}
