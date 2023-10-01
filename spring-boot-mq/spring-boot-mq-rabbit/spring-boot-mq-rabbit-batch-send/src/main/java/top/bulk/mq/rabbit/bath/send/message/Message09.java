package top.bulk.mq.rabbit.bath.send.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 演示 direct 类型的消息对象
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Data
public class Message09 implements Serializable {

    public static final String QUEUE = "QUEUE_09";

    public static final String EXCHANGE = "EXCHANGE_09";

    public static final String ROUTING_KEY = "ROUTING_KEY_09";

    private String id;
}
