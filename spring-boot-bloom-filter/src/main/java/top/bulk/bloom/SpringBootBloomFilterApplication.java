package top.bulk.bloom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot 整合布隆过滤器 demo
 *
 * @author 散装java
 * @date 2022-12-06
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("top.bulk.bloom.mapper")
public class SpringBootBloomFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootBloomFilterApplication.class, args);
    }

}
