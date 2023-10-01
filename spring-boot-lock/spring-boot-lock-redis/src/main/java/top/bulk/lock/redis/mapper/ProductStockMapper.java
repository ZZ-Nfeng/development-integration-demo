package top.bulk.lock.redis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.bulk.lock.redis.entity.ProductStock;


/**
 * 产品库存表
 * (ProductStock)表数据库访问层
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

