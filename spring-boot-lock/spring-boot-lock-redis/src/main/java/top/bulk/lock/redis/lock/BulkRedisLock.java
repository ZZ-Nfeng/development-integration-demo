package top.bulk.lock.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 自定义实现 redis 分布式锁
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class BulkRedisLock {
    private static final String LOCK_PREFIX = "redisLock";
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 尝试获取锁
     *
     * @param requestId  请求id
     * @param expireTime 过期时间  单位毫秒
     * @return true false
     */
    public boolean lock(String requestId, int expireTime) {
        // 也可以使用 lua 脚本 "return redis.call('set',KEYS[1], ARGV[1],'NX','PX',ARGV[2])"
        // 使用redis保证原子操作（判断是否存在，添加key，设置过期时间）
        while (true) {
            if (Boolean.TRUE.equals(stringRedisTemplate.boundValueOps(LOCK_PREFIX).
                    setIfAbsent(requestId, expireTime, TimeUnit.SECONDS))) {
                return true;
            }
        }
    }

    /**
     * 将锁释放掉
     * <p>
     * 为何解锁需要校验 requestId 因为不是自己的锁不能释放
     * 客户端A加锁，一段时间之后客户端A解锁，在执行 lock 之前，锁突然过期了。
     * 此时客户端B尝试加锁成功，然后客户端A再执行 unlock 方法，则将客户端B的锁给解除了。
     *
     * @param requestId 请求id
     * @return true false
     */
    public boolean unlock(String requestId) {
        // 这里使用Lua脚本保证原子性操作
        String script = "if  redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long res = stringRedisTemplate.execute(redisScript, Collections.singletonList(LOCK_PREFIX), requestId);
        return new Long(1).equals(res);
    }

    /**
     * 创建续命子线程
     *
     * @param time      操作预期耗时
     * @param requestId 唯一标识
     * @return 续命线程 Thread
     */
    public Thread watchDog(int time, String requestId) {
        return new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(time * 2 / 3);
                    //重置时间
                    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                            "return redis.call('expire', KEYS[1],ARGV[2]) " +
                            "else return '0' end";
                    List<Object> args = new ArrayList();
                    args.add(requestId);
                    args.add(time);
                    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
                    stringRedisTemplate.execute(redisScript, Collections.singletonList(LOCK_PREFIX), args);
                } catch (Exception e) {
                    // sleep interrupted 是因为 sleep
                    // log.info("watchDog异常：{}", e.getMessage());
                    return;
                }
            }
        });
    }
}
