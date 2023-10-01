package top.bulk.lock.redis.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.lock.redis.service.ProductStockService;


import javax.annotation.Resource;

/**
 * 产品库存表
 * (ProductStock)表控制层
 */
@RestController
public class ProductStockController {
    @Resource
    private ProductStockService productStockService;

    /**
     * 模拟减库存操作 - redisson 实现
     *
     * @return str
     */
    @GetMapping("/reduceStock/{id}")
    public String reduceStockByRedisson(@PathVariable("id") Integer id) {
        return productStockService.reduceStock(id);
    }

    /**
     * 模拟减库存操作 - 自己实现 redis 锁接口
     *
     * @return str
     */
    @GetMapping("/reduceStockByMyLock/{id}")
    public String reduceStockByMyLock(@PathVariable("id") Integer id) {
        return productStockService.reduceStockByMyLock(id);
    }
}

