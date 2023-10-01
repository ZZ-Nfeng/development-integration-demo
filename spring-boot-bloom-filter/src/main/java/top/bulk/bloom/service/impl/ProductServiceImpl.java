package top.bulk.bloom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Service;
import top.bulk.bloom.entity.Product;
import top.bulk.bloom.mapper.ProductMapper;
import top.bulk.bloom.service.ProductService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品 demo(Product) 表服务实现类
 */
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    private static final String BLOOM_STR = "product_list_bloom";

    private static final String REDIS_CACHE = "product_list";
    @Resource
    RedissonClient redissonClient;

    RBloomFilter<Integer> bloomFilter;

    /**
     * 启动时候将产品加入到 布隆过滤器中
     */
    @PostConstruct
    public void init() {
        bloomFilter = redissonClient.getBloomFilter(BLOOM_STR, new JsonJacksonCodec());
        this.refreshBloom();
    }

    @Override
    public void refreshBloom() {
        bloomFilter.delete();
        //初始化布隆过滤器：预计元素为 1000000L (这个值根据实际的数量进行调整),误差率为3%
        bloomFilter.tryInit(1000000L, 0.03);
        List<Integer> productIdList = this.list(new LambdaQueryWrapper<Product>().select(Product::getId))
                .stream().map(Product::getId).collect(Collectors.toList());
        productIdList.forEach(bloomFilter::add);
    }

    @Override
    public Product getProductById(Integer id) {
        // 走布隆过滤器筛选一下，防止被缓存穿透
        boolean contains = bloomFilter.contains(id);
        // 如果布隆过滤器判断当前产品id 存在，则去查询数据库
        if (contains) {
            // 先去缓存中查
            RMap<Integer, String> productCache = redissonClient.getMap(REDIS_CACHE);
            String cacheProduct = productCache.get(id);
            if (StrUtil.isNotEmpty(cacheProduct)) {
                // 如果缓存中不是空 则返回
                return JSONUtil.toBean(cacheProduct, Product.class);
            }
            Product product = this.getById(id);
            // 如果查到了数据，那么存一份到 redis 中去
            if (BeanUtil.isNotEmpty(product)) {
                productCache.put(id, JSONUtil.toJsonStr(product));
                return product;
            }
        } else {
            log.info("布隆过滤器中不存在产品id：{}的数据", id);
        }
        return null;
    }

    @Override
    public Boolean addProduct(Product product) {
        boolean success = this.save(product);
        // 数据添加成功后，往 redis 缓存中 和 布隆过滤器中添加数据
        if (success) {
            final Integer id = product.getId();
            RMap<Integer, String> redisCache = redissonClient.getMap(REDIS_CACHE);
            redisCache.put(id, JSONUtil.toJsonStr(product));
            bloomFilter.add(id);
        }
        return success;
    }
}

