package top.bulk.bloom.demo;

import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.bulk.bloom.bloom.BloomFilterHelper;
import top.bulk.bloom.bloom.RedisBloomFilter;

import javax.annotation.Resource;

/**
 * 自己写的 redis 布隆过滤器测试
 *
 * @author 散装java
 * @date 2022-12-08
 */
@SpringBootTest
public class RedisBloomFilterTest {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void test() {
        // 注意 在测试类中运行比较慢
        RedisBloomFilter<Integer> redisBloomFilter = new RedisBloomFilter<>(redisTemplate);
        int expectedInsertions = 10000;
        double fpp = 0.03;
        redisBloomFilter.delete("bloom");
        BloomFilterHelper<Integer> bloomFilterHelper = new BloomFilterHelper<>(Funnels.integerFunnel(), expectedInsertions, fpp);
        int j = 0;
        // 添加100个元素
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            redisBloomFilter.add(bloomFilterHelper, "bloom", i);
        }
        long costMs = System.currentTimeMillis() - beginTime;
        System.out.println("布隆过滤器添加100个值，耗时：" + costMs + "ms");
        for (int i = 0; i < 100; i++) {
            boolean result = redisBloomFilter.contains(bloomFilterHelper, "bloom", i);
            if (!result) {
                j++;
            }
        }
        System.out.println("漏掉了" + j + "个,验证结果耗时：" + (System.currentTimeMillis() - beginTime) + "ms");
    }
}
