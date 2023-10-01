package top.bulk.mq.kafka.sequence.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.kafka.sequence.message.KafkaMessage07;

/**
 * @author 散装java
 * @date 2023-03-21
 */
@Configuration
public class KafkaConfiguration {
    /**
     * 创建一个  Topic ，指定分区 10
     */
    @Bean
    public NewTopic initialTopic() {
        return new NewTopic(KafkaMessage07.TOPIC, 10, (short) 1);
    }
}
