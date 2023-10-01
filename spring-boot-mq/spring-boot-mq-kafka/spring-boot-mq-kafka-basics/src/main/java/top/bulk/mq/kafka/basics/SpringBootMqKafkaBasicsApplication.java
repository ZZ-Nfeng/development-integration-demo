package top.bulk.mq.kafka.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SpringBootMqKafkaBasicsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqKafkaBasicsApplication.class, args);
    }

}
