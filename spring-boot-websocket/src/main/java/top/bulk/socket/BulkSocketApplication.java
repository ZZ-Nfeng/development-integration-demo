package top.bulk.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */
@SpringBootApplication
public class BulkSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BulkSocketApplication.class, args);
        System.out.println("访问 ===> http://127.0.0.1:8080/");
    }

}
