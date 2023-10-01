package top.bulk.mq.kafka.transaction.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义消息体
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Data
public class KafkaMessage06 implements Serializable {

    public static final String TOPIC = "TOPIC_06";
    public static final String GROUP_ID = "CONSUMER-GROUP-06";


    private String id;
}
