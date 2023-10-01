package top.bulk.mq.kafka.ack.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义消息体
 * <p>
 * 需要配置 trusted packages
 * 否则会出现异常：The class 'top.bulk.mq.kafka.basics.message.KafkaMessage01' is not in the trusted packages: [java.util, java.lang]. If you believe this class is safe to deserialize, please provide its name. If the serialization is only done by a trusted source, you can also enable trust all (*).
 *
 * @author 散装java
 * @date 2023-03-14
 */
@Data
public class KafkaMessage05 implements Serializable {

    public static final String TOPIC = "TOPIC_05";
    public static final String GROUP_ID = "CONSUMER-GROUP-05";

    private Integer id;
}
