package top.bulk.mq.rabbit.sequence.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bulk.mq.rabbit.sequence.message.Message15;

import java.util.HashMap;

/**
 * @author 散装java
 * @date 2023-02-18
 */
@Configuration
@SuppressWarnings("all")
public class DirectExchangeConfiguration {
    @Bean
    public Queue queue15_0() {
        return creatQueue(Message15.QUEUE_0);
    }


    @Bean
    public Queue queue15_1() {
        return creatQueue(Message15.QUEUE_1);
    }

    @Bean
    public Queue queue15_2() {
        return creatQueue(Message15.QUEUE_2);
    }

    @Bean
    public Queue queue15_3() {
        return creatQueue(Message15.QUEUE_3);
    }

    @Bean
    public DirectExchange exchange15() {
        // name: 交换机名字 | durable: 是否持久化 | exclusive: 是否排它
        return new DirectExchange(Message15.EXCHANGE, true, false);
    }


    @Bean
    public Binding binding15_0() {
        return BindingBuilder.bind(queue15_0()).to(exchange15()).with("0");
    }

    @Bean
    public Binding binding15_1() {
        return BindingBuilder.bind(queue15_1()).to(exchange15()).with("1");
    }

    @Bean
    public Binding binding15_2() {
        return BindingBuilder.bind(queue15_2()).to(exchange15()).with("2");
    }

    @Bean
    public Binding binding15_3() {
        return BindingBuilder.bind(queue15_3()).to(exchange15()).with("3");
    }

    /**
     * 创建一个 单活 模式的队列
     * 注意 ：
     * <p>
     * 如果一个队列已经创建为非x-single-active-consumer，而你想更改其为x-single-active-consumer，要把之前创建的队列删除
     *
     * @param name
     * @return queue
     */
    private Queue creatQueue(String name) {
        // 创建一个 单活模式 队列
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-single-active-consumer", true);
        return new Queue(name, true, false, false, args);
    }
}
