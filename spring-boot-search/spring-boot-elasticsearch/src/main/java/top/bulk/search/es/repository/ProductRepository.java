package top.bulk.search.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.bulk.search.es.entity.EsProductEntity;

/**
 * Repository
 * 创建一个继承自 ElasticsearchRepository 的接口，用于定义操作 Elasticsearch 的方法。
 * <p>
 * 其中两个泛型，分别代表表实体类类型和主键类型。
 */
@Repository
public interface ProductRepository extends ElasticsearchRepository<EsProductEntity, Integer> {
}
