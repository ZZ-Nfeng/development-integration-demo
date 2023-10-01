package top.bulk.mq.redis.controller;

import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.mq.redis.pojo.Message;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 请求控制层
 *
 * @author 散装java
 * @date 2023-02-03
 */
@RestController
public class RedisMqController {
    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    /**
     * 主题订阅
     *
     * @param key     主题
     * @param message 消息
     */
    @GetMapping("/topic/{key}/")
    public void topic(@PathVariable String key, Message<String> message) {
        redisTemplate.opsForList().leftPush(key, JSONUtil.toJsonStr(message));

    }

    /**
     * 广播消息
     *
     * @param key     key
     * @param message 消息
     */
    @GetMapping("/pubsub/{key}")
    public void pubsub(@PathVariable String key, Message<String> message) {
        redisTemplate.convertAndSend(key, JSONUtil.toJsonStr(message));
    }

    @GetMapping("/test")
    public void test() {
        Message<String> message = new Message<>();
        message.setId(UUID.randomUUID().toString());
        message.setContent("0001");
        redisTemplate.convertAndSend("pubsub", JSONUtil.toJsonStr(message));
        redisTemplate.opsForList().leftPush("topic1", JSONUtil.toJsonStr(message));
    }

    @GetMapping("/test-stream")
    public String stream() {
        Message<String> message = new Message<>();
        message.setId(UUID.randomUUID().toString());
        message.setContent("0001");
        ObjectRecord<Object, Object> record = ObjectRecord.create("streamKey", JSONUtil.toJsonStr(message));
        // 将消息添加至消息队列中
        RecordId recordId = redisTemplate.opsForStream().add(record);
        return recordId == null ? "null" : recordId.getValue();
    }
}
