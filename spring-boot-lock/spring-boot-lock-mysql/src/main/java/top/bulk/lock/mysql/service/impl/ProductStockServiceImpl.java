package top.bulk.lock.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bulk.lock.mysql.entity.ProductStock;
import top.bulk.lock.mysql.mapper.ProductStockMapper;
import top.bulk.lock.mysql.service.ProductStockService;

import javax.annotation.Resource;

/**
 * 产品库存表
 * (ProductStock)表服务实现类
 */
@Service("productStockService")
public class ProductStockServiceImpl extends ServiceImpl<ProductStockMapper, ProductStock> implements ProductStockService {
    @Resource
    ProductStockMapper productStockMapper;

    /**
     * 乐观锁：
     * 我认为我操作之前不会有人操作，如果有人操作了，那我再来一次，即为无锁操作
     *
     * @param id id
     * @return String
     */
    @SneakyThrows
    @Override
    public String reduceStockOptimism(Integer id) {
        ProductStock stock = productStockMapper.selectById(id);
        if (stock != null && stock.getStock() > 0) {
            int i = productStockMapper.reduceStockOptimism(id, stock.getVersion());
            // i = 0 表示没有修改成功，说明有别人在你修改之前 已经修改了数据；需要重新再调用下当前方法
            if (i == 0) {
                // Thread.sleep 防止栈溢出
                // Thread.sleep(10);
                this.reduceStockOptimism(id);
            }
        } else {
            throw new RuntimeException("库存不足！");
        }
        return "ok";
    }

    /**
     * 基于 MySQL 实现分布式锁（悲观锁）
     * 悲观锁 ：
     * 查询的时候，不管有没有线程竞争，都在 MySQL层面就加上了锁 ，
     * 所以，整个操作，必须要在一个事务中才行，这里通过 @Transactional
     *
     * @param id id
     * @return String
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reduceStockGloomy(Integer id) {
        // 加锁查询
        ProductStock stock = productStockMapper.selectForLock(id);
        if (stock != null && stock.getStock() > 0) {
            productStockMapper.reduceStock(id);
        } else {
            throw new RuntimeException("库存不足！");
        }
        return "ok";
    }
}

