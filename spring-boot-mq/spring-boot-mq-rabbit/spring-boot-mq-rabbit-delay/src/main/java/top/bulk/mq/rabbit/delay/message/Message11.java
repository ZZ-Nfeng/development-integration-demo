package top.bulk.mq.rabbit.delay.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 演示 direct 类型的消息对象
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Data
public class Message11 implements Serializable {
    /**
     * 普通队列
     */
    public static final String QUEUE = "QUEUE_11";
    /**
     * 延迟队列
     */
    public static final String QUEUE_DELAY = "QUEUE_DELAY_11";

    public static final String EXCHANGE = "EXCHANGE_11";

    public static final String ROUTING_KEY = "ROUTING_KEY_11";
    public static final String ROUTING_KEY_DELAY = "ROUTING_KEY_DELAY_11";

    private String id;
}
