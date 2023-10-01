package top.bulk.search.es.repository;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.bulk.search.es.entity.EsProductEntity;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author 散装java
 * @date 2023-07-11
 */
@SpringBootTest
@Slf4j
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 测试新增
     */
    @Test
    public void insert() {
        EsProductEntity data = new EsProductEntity();
        data.setId(1);
        data.setTitle("标题1");
        data.setDescription("描述1");
        data.setCreatedTime(LocalDateTime.now());
        productRepository.save(data);
    }

    @Test
    public void query() {
        Optional<EsProductEntity> entity = productRepository.findById(1);
        System.out.println(entity.toString());
    }


    /**
     * 更新一条记录
     * 这里要注意，如果使用 save 方法来更新的话，必须是全量字段，否则其它字段会被覆盖。
     */
    @Test
    public void updateBySave() {
        EsProductEntity data = new EsProductEntity();
        data.setId(1);
        productRepository.save(data);
    }

    @Test
    public void update() {
        Integer documentId = 1;
//        UpdateRequest updateRequest = new UpdateRequest("products", documentId)
//                .doc("fieldName", "newValue");
//        // 创建一个 UpdateQuery 对象，指定要更新的文档 ID、索引名称和更新操作
//        UpdateQuery updateQuery = UpdateQuery.Builder("products", documentId)
//                .withUpdateRequest(updateRequest) // 设置更新请求，如设置要更新的字段和值
//                .build();
//
//        // 执行更新操作
//        elasticsearchOperations.update(updateQuery, IndexCoordinates.of("products"));
    }
}