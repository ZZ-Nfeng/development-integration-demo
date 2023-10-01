package top.bulk.lock.zk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Service;
import top.bulk.lock.zk.config.ZookeeperProperty;
import top.bulk.lock.zk.entity.ProductStock;
import top.bulk.lock.zk.lock.BulkZookeeperLock;
import top.bulk.lock.zk.lock.CuratorZookeeperLock;
import top.bulk.lock.zk.mapper.ProductStockMapper;
import top.bulk.lock.zk.service.ProductStockService;

import javax.annotation.Resource;

/**
 * 产品库存表
 * (ProductStock)表服务实现类
 *
 * @author 散装java
 */
@Service("productStockService")
public class ProductStockServiceImpl extends ServiceImpl<ProductStockMapper, ProductStock> implements ProductStockService {
    @Resource
    ProductStockMapper productStockMapper;
    @Resource
    CuratorZookeeperLock zookeeperLock;
    @Resource
    ZookeeperProperty zookeeperProperty;

    @Override
    public String reduceStock(Integer id) {
        // 加 zk 锁
        InterProcessMutex mutex = zookeeperLock.lock("stock");
        try {
            ProductStock stock = productStockMapper.selectById(id);
            if (stock != null && stock.getStock() > 0) {
                productStockMapper.reduceStock(id);
            } else {
                throw new RuntimeException("库存不足！");
            }
        } finally {
            // 解 zk 锁
            zookeeperLock.unLock(mutex);
        }
        return "ok";
    }

    @SneakyThrows
    @Override
    public String reduceStockByMyLock(Integer id) {
        // 加 zk 锁
        BulkZookeeperLock zkLock = new BulkZookeeperLock(zookeeperProperty);
        try {
            zkLock.lock();
            ProductStock stock = productStockMapper.selectById(id);
            if (stock != null && stock.getStock() > 0) {
                productStockMapper.reduceStock(id);
            } else {
                throw new RuntimeException("库存不足！");
            }
        } finally {
            // 解 zk 锁
            zkLock.unLock();
        }
        return "ok";
    }
}

