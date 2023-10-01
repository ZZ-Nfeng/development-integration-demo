package top.bulk.mq.rabbit.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 演示 topic 类型的实体类
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Data
public class Message02 implements Serializable {
    public static final String QUEUE = "QUEUE_02";

    public static final String EXCHANGE = "EXCHANGE_02";

    public static final String ROUTING_KEY = "#.key2.key3";

    private String id;
}
