package top.bulk.bloom.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产品demo(Product)表实体类
 */
@Data
public class Product implements Serializable {
    /**
     * 主键
     **/
    private Integer id;
    /**
     * 名称
     **/
    private String productName;
    /**
     * 价钱
     **/
    private Double productPrice;
    /**
     * 数量
     **/
    private Integer productNum;
    /**
     * 添加时间
     **/
    private LocalDateTime addTime;
    /**
     * 创建人
     **/
    private String addBy;
    /**
     * 更新时间
     **/
    private LocalDateTime updateTime;
    /**
     * 更新人
     **/
    private String updateBy;

}

