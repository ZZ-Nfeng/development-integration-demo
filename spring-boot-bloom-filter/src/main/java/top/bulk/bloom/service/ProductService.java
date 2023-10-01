package top.bulk.bloom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bulk.bloom.entity.Product;

/**
 * 产品demo(Product)表服务接口
 */
public interface ProductService extends IService<Product> {
    /**
     * 查询产品详情
     *
     * @param id id
     * @return product
     */
    Product getProductById(Integer id);

    /**
     * 添加产品操作
     *
     * @param product 产品信息
     * @return 返回
     */
    Boolean addProduct(Product product);

    /**
     * 清空 布隆过滤器中的数据，然后重新添加
     * 删除后，重新添加
     */
    void refreshBloom();
}

