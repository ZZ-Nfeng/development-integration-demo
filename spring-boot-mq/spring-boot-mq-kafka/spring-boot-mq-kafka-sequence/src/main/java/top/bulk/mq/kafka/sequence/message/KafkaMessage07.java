package top.bulk.mq.kafka.sequence.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义消息体
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Data
public class KafkaMessage07 implements Serializable {

    public static final String TOPIC = "TOPIC_07";
    public static final String GROUP_ID = "CONSUMER-GROUP-07";


    private String id;
    private String msg;
}
