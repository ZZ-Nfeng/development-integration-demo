package top.bulk.mq.rabbit.sequence.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message15 implements Serializable {

    public static final String QUEUE_0 = "QUEUE_15_0";
    public static final String QUEUE_1 = "QUEUE_15_1";
    public static final String QUEUE_2 = "QUEUE_15_2";
    public static final String QUEUE_3 = "QUEUE_15_3";

    public static final int QUEUE_COUNT = 4;

    public static final String EXCHANGE = "EXCHANGE_15";

    private int id;
    private String msg;
}
