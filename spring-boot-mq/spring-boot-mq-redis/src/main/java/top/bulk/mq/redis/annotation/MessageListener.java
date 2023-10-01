package top.bulk.mq.redis.annotation;

import java.lang.annotation.*;

/**
 * 监听注解
 *
 * @author 散装java
 * @date 2023-02-03
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageListener {

    String value() default "";

    String topic() default "";

    String channel() default "";

    String streamKey() default "";

    /**
     * group-name是关联到流的消费者组的名称。
     */
    String consumerGroup() default "";

    /**
     * consumer-name是客户端用于在消费者组内标识自己的字符串
     */
    String consumerName() default "";

    /**
     * 读取未ack
     *
     */
    boolean pending() default false;

    Mode mode() default Mode.TOPIC;

    enum Mode {
        /**
         * topic 模式，主题订阅
         */
        TOPIC(),
        /**
         * pub/sub 模式 订阅发布 广播
         */
        PUBSUB(),
        /**
         * stream 模式
         */
        STREAM()
    }
}
