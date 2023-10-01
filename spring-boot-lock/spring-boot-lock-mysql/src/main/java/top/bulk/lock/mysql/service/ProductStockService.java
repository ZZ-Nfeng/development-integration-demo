package top.bulk.lock.mysql.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bulk.lock.mysql.entity.ProductStock;


/**
 * 产品库存表
 * (ProductStock)表服务接口
 */
public interface ProductStockService extends IService<ProductStock> {
    /**
     * 减库存 mysql 乐观锁
     *
     * @param id id
     * @return str
     */
    String reduceStockOptimism(Integer id);

    /**
     * 减库存 - mysql 悲观锁
     *
     * @param id id
     * @return str
     */
    String reduceStockGloomy(Integer id);
}

