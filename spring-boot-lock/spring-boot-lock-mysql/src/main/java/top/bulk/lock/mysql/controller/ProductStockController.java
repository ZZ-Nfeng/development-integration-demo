package top.bulk.lock.mysql.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.lock.mysql.service.ProductStockService;

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
     * 模拟减库存操作 - MySQL 乐观锁 实现
     *
     * @return str
     */
    @GetMapping("/reduceStock/{id}")
    public String reduceStockOptimism(@PathVariable("id") Integer id) {
        return productStockService.reduceStockOptimism(id);
    }

    /**
     * 模拟减库存操作 - MySQL 悲观锁 实现
     *
     * @return str
     */
    @GetMapping("/reduceStockGloomy/{id}")
    public String reduceStockGloomy(@PathVariable("id") Integer id) {
        return productStockService.reduceStockGloomy(id);
    }
}

