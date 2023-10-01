package top.bulk.lock.zk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bulk.lock.zk.entity.ProductStock;

/**
 * 产品库存表
(ProductStock)表服务接口
 *
 * @author 散装java
 */
public interface ProductStockService extends IService<ProductStock> {
    /**
     * 减库存
     * @param id id
     * @return str
     */
    String reduceStock(Integer id);

    /**
     * 减库存 - 自己实现 zk 锁接口
     * @param id id
     * @return str
     */
    String reduceStockByMyLock(Integer id);
}

