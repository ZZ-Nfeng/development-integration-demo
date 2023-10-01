package top.bulk.mq.kafka.btach.send.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义消息体
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Data
public class KafkaMessage02 implements Serializable {

    public static final String TOPIC = "TOPIC_02";
    public static final String GROUP_ID = "CONSUMER-GROUP-02";


    private String id;
}
