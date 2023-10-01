package top.bulk.mq.redis.context;

import cn.hutool.core.util.ArrayUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.bulk.mq.redis.annotation.MessageConsumer;
import top.bulk.mq.redis.annotation.MessageHandler;
import top.bulk.mq.redis.annotation.MessageListener;
import top.bulk.mq.redis.handler.AbstractMessageHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 启动配置，在项目启动时自动运行，主要用于消费者的注册
 *
 * @author 散装java
 * @date 2023-02-03
 */
@Component
@Slf4j
public class MessageConsumerStater implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 项目启动时，获取所有的 标注 MessageConsumer 注解的方法 ，开启消息监听逻辑
     *
     * @param args ApplicationArguments
     */
    @Override
    public void run(ApplicationArguments args) {
        Map<MessageListener.Mode, AbstractMessageHandler> invokers = getInvokers();
        applicationContext.getBeansWithAnnotation(MessageConsumer.class).values().parallelStream().forEach(consumer -> {
            Method[] methods = consumer.getClass().getMethods();
            if (ArrayUtil.isNotEmpty(methods)) {
                Arrays.stream(methods).parallel().forEach(method -> startMessageListener(method, invokers));
            }
        });
    }

    /**
     * 找到对应的处理方式来处理消息的消费逻辑
     *
     * @param method     消费者方法 -> 本案例对应的是 MqConsumer 中的方法
     * @param handlerMap 所有的处理方式集合
     */
    private void startMessageListener(Method method, Map<MessageListener.Mode, AbstractMessageHandler> handlerMap) {
        MessageListener listener = method.getAnnotation(MessageListener.class);
        if (null == listener) {
            return;
        }
        MessageListener.Mode mode = listener.mode();
        AbstractMessageHandler handler = handlerMap.get(mode);
        if (handler == null) {
            log.error("invoker is null");
            return;
        }
        handler.invokeMessage(method);
    }

    private Map<MessageListener.Mode, AbstractMessageHandler> getInvokers() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MessageHandler.class);
        Map<MessageListener.Mode, AbstractMessageHandler> collect =
                beansWithAnnotation.values().stream().collect(Collectors
                        .toMap(k -> k.getClass().getAnnotation(MessageHandler.class).value(), k -> (AbstractMessageHandler) k));
        return collect;
    }

    public MessageConsumerStater() {

    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
