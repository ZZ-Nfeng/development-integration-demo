package top.bulk.mq.redis.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 处理器注解，不同的类型使用不同的注解标准
 *
 * @author 散装java
 * @date 2023-02-03
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MessageHandler {
    MessageListener.Mode value() default MessageListener.Mode.TOPIC;
}
