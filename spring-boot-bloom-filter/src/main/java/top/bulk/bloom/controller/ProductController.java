package top.bulk.bloom.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.*;
import top.bulk.bloom.entity.Product;
import top.bulk.bloom.service.ProductService;

import javax.annotation.Resource;

/**
 * 查询产品时候用到了布隆过滤器
 *
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    ProductService productService;

    /**
     * 根据 id 查询详情
     *
     * @param id id
     * @return json string
     */
    @GetMapping("/{id}")
    public String getProduct(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        if (BeanUtil.isEmpty(product)) {
            return "暂无该产品！";
        }
        return JSONUtil.toJsonStr(product);
    }

    /**
     * 添加产品
     *
     * @param product 产品
     * @return boolean
     */
    @PostMapping("/add")
    public Boolean addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @DeleteMapping("/{id}")
    public Boolean delProduct(@PathVariable Integer id) {
        return productService.removeById(id);
    }
}
