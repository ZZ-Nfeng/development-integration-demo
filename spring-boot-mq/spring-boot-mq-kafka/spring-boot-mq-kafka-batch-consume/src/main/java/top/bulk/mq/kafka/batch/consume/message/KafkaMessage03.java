package top.bulk.mq.kafka.batch.consume.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义消息体
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Data
public class KafkaMessage03 implements Serializable {

    public static final String TOPIC = "TOPIC_03";
    public static final String GROUP_ID = "CONSUMER-GROUP-03";


    private String id;
}
