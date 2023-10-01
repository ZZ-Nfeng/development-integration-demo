package top.bulk.mq.rabbit.delay.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@Data
public class Message12 implements Serializable {

    public static final String QUEUE = "QUEUE_12";

    public static final String EXCHANGE = "EXCHANGE_12";

    public static final String ROUTING_KEY = "ROUTING_KEY_12";


    private String id;
}
