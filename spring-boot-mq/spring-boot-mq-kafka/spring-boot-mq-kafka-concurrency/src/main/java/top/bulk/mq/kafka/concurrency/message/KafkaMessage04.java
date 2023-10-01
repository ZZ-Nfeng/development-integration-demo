package top.bulk.mq.kafka.concurrency.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义消息体
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Data
public class KafkaMessage04 implements Serializable {

    public static final String TOPIC = "TOPIC_04";
    public static final String GROUP_ID = "CONSUMER-GROUP-04";


    private String id;
}
