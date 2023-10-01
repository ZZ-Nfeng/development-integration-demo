package top.bulk.lock.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.bulk.lock.mysql.entity.ProductStock;


/**
 * 产品库存表
 * (ProductStock)表数据库访问层
 */
public interface ProductStockMapper extends BaseMapper<ProductStock> {
    /**
     * 更新库存
     *
     * @param id      id
     * @param version 乐观锁版本号
     * @return int 影响行数
     */
    int reduceStockOptimism(@Param("id") Integer id, @Param("version") Integer version);

    /**
     * 更新操作 库存 -1
     *
     * @param id id
     * @return int
     */
    int reduceStock(@Param("id") Integer id);

    /**
     * 加锁查询
     *
     * @param id id
     * @return obj
     */
    ProductStock selectForLock(@Param("id") Integer id);
}

