package top.bulk.mq.rabbit.message;

import lombok.Data;

import java.io.Serializable;

/**
 * Headers Exchange 类型的实体
 *
 * @author 散装java
 * @date 2023-02-18
 */
@Data
public class Message04 implements Serializable {
    public static final String QUEUE = "QUEUE_04";

    public static final String EXCHANGE = "EXCHANGE_04";

    public static final String HEADER_KEY = "bulk";
    public static final String HEADER_VALUE = "java";

    private String id;

}
