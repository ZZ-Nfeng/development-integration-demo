package top.bulk.bloom.bloom;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于 redis 实现布隆过滤器
 */
public class RedisBloomFilter<T> {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisBloomFilter(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 删除缓存的KEY
     *
     * @param key KEY
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据给定的布隆过滤器添加值，在添加一个元素的时候使用，批量添加的性能差
     *
     * @param bloomFilterHelper 布隆过滤器对象
     * @param key               KEY
     * @param value             值
     */
    public void add(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     *
     * @param bloomFilterHelper 布隆过滤器对象
     * @param key               KEY
     * @param value             值
     * @return 是否存在
     */
    public boolean contains(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            Boolean flag = redisTemplate.opsForValue().getBit(key, i);
            if (Boolean.FALSE.equals(flag)) {
                return false;
            }
        }
        return true;
    }
}
