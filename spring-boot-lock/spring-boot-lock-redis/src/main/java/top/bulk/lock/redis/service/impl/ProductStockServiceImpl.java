package top.bulk.lock.redis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import top.bulk.lock.redis.entity.ProductStock;
import top.bulk.lock.redis.lock.BulkRedisLock;
import top.bulk.lock.redis.mapper.ProductStockMapper;
import top.bulk.lock.redis.service.ProductStockService;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 产品库存表
 * (ProductStock)表服务实现类
 */
@Service("productStockService")
public class ProductStockServiceImpl extends ServiceImpl<ProductStockMapper, ProductStock> implements ProductStockService {
    @Resource
    ProductStockMapper productStockMapper;
    @Resource
    RedissonClient redissonClient;
    @Resource
    BulkRedisLock bulkRedisLock;

    @Override
    public String reduceStock(Integer id) {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        try {
            ProductStock stock = productStockMapper.selectById(id);
            if (stock != null && stock.getStock() > 0) {
                productStockMapper.reduceStock(id);
            } else {
                throw new RuntimeException("库存不足！");
            }
        } finally {
            lock.unlock();
        }
        return "ok";
    }

    @SneakyThrows
    @Override
    public String reduceStockByMyLock(Integer id) {
        // requestId 确保每一个请求生成的都不一样，这里使用 uuid，也可以使用其他分布式唯一 id 方案
        String requestId = UUID.randomUUID().toString().replace("-", "");
        int expireTime = 10;
        bulkRedisLock.lock(requestId, expireTime);
        // 开启续命线程，
        Thread watchDog = bulkRedisLock.watchDog(expireTime, requestId);
        watchDog.setDaemon(true);
        watchDog.start();
        try {
            ProductStock stock = productStockMapper.selectById(id);
            if (stock != null && stock.getStock() > 0) {
                productStockMapper.reduceStock(id);
            } else {
                throw new RuntimeException("库存不足！");
            }
        } finally {
            watchDog.interrupt();
            bulkRedisLock.unlock(requestId);
        }
        return "ok";
    }
}

