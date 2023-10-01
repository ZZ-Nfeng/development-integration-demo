package top.bulk.mq.rabbit.consumer.broadcast.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@Data
public class Message14 implements Serializable {

    public static final String QUEUE = "QUEUE_14";

    public static final String EXCHANGE = "EXCHANGE_14";

    private String id;
}
