package top.bulk.lock.zk.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品库存表
 * (ProductStock)表实体类
 *
 * @author 散装java
 */
@Data
public class ProductStock implements Serializable {
    /**
     * 主键
     **/
    private Integer id;

    private Integer stock;

}

