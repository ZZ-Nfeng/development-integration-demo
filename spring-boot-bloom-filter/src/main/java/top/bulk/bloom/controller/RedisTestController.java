package top.bulk.bloom.controller;

import com.google.common.hash.Funnels;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.bloom.bloom.BloomFilterHelper;
import top.bulk.bloom.bloom.RedisBloomFilter;

import javax.annotation.Resource;

/**
 * 测试 redis 的布隆过滤器 注意尽量不要使用测试类，会比较慢
 */
@RestController
public class RedisTestController {
    @Resource
    RedissonClient redissonClient;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @GetMapping("/test")
    void testRedisson() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("phoneList");
        //初始化布隆过滤器：预计元素为1000000L,误差率为3%
        bloomFilter.tryInit(1000000L, 0.03);
        //将号码10086插入到布隆过滤器中
        bloomFilter.add("10086");


        //判断下面号码是否在布隆过滤器中
        System.out.println(bloomFilter.contains("123456"));//false
        System.out.println(bloomFilter.contains("10086"));//true
    }

    @GetMapping("/testRedis")
    void testRedis() {
        String redisKey = "redisBloom";
        // 注意 在测试类中运行比较慢
        RedisBloomFilter<Integer> redisBloomFilter = new RedisBloomFilter<>(redisTemplate);
        int expectedInsertions = 10000;
        double fpp = 0.1;
        redisBloomFilter.delete(redisKey);
        BloomFilterHelper<Integer> bloomFilterHelper = new BloomFilterHelper<>(Funnels.integerFunnel(), expectedInsertions, fpp);
        int j = 0;
        // 添加100个元素
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            redisBloomFilter.add(bloomFilterHelper, redisKey, i);
        }
        long costMs = System.currentTimeMillis() - beginTime;
        System.out.println("布隆过滤器添加100个值，耗时：" + costMs + "ms");
        for (int i = 0; i < 101; i++) {
            boolean result = redisBloomFilter.contains(bloomFilterHelper, redisKey, i);
            if (!result) {
                j++;
            }
        }
        System.out.println("漏掉了" + j + "个,验证结果耗时：" + (System.currentTimeMillis() - beginTime) + "ms");
    }
}
