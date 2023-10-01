package top.bulk.mq.rabbit.consumer.cluster.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@Data
public class Message13 implements Serializable {

    public static final String QUEUE = "QUEUE_13";

    public static final String EXCHANGE = "EXCHANGE_13";

    private String id;
}
