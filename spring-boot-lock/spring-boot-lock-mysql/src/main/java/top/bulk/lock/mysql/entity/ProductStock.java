package top.bulk.lock.mysql.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品库存表
 * (ProductStock)表实体类
 */
@Data
public class ProductStock implements Serializable {
    /**
     * 主键
     **/
    private Integer id;

    private Integer stock;
    /**
     * 版本号，乐观锁使用
     */
    private Integer version;

}

