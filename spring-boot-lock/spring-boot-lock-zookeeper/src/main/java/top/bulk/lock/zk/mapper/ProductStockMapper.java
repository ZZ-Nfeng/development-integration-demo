package top.bulk.lock.zk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.bulk.lock.zk.entity.ProductStock;

/**
 * 产品库存表
 * (ProductStock)表数据库访问层
 *
 * @author 散装java
 */
public interface ProductStockMapper extends BaseMapper<ProductStock> {
    /**
     * 更新库存
     *
     * @param id id
     * @return int
     */
    int reduceStock(@Param("id") Integer id);
}

