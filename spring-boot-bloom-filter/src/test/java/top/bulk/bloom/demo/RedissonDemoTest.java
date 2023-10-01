package top.bulk.bloom.demo;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * redisson 布隆过滤器使用演示
 *
 * @author 散装java
 * @date 2022-12-07
 */
@SpringBootTest
public class RedissonDemoTest {
    @Resource
    RedissonClient redissonClient;

    @Test
    void contextLoads() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("phoneList");
        //初始化布隆过滤器：预计元素为1000000L,误差率为3%
        bloomFilter.tryInit(1000000L,0.03);
        //将号码10086插入到布隆过滤器中
        bloomFilter.add("10086");


        //判断下面号码是否在布隆过滤器中
        System.out.println(bloomFilter.contains("123456"));//false
        System.out.println(bloomFilter.contains("10086"));//true
    }
}
