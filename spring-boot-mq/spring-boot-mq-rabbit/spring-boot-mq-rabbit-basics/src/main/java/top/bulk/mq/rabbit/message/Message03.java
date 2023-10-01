package top.bulk.mq.rabbit.message;

import lombok.Data;

import java.io.Serializable;

/**
 * Fanout Exchange 类型消息
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Data
public class Message03 implements Serializable {
    public static final String QUEUE_A = "QUEUE_03_A";
    public static final String QUEUE_B = "QUEUE_03_B";

    public static final String EXCHANGE = "EXCHANGE_03";

    private String id;
}
