package top.bulk.bloom.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.bulk.bloom.service.ProductService;

import javax.annotation.Resource;

/**
 * 定时任务 刷 布隆过滤器中的数据
 */
@Component
public class ProductSchedule {
    @Resource
    ProductService productService;

    /**
     * 每天一点执行
     * 目的是为了刷新那些被删除掉的产品
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void refreshBloom() {
        productService.refreshBloom();
    }
}
