package top.bulk.search.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import top.bulk.search.es.constant.AnalyzerConstant;

import java.time.LocalDateTime;

/**
 * 实体类
 * 标注了 @Document 注解之后，如果 es 中没有这个 Document 就会自动创建
 */
@Document(indexName = "product_index")
@Data
public class EsProductEntity {
    /**
     * @Id 注解标注字段后，Spring Data Elasticsearch 将会将该字段作为文档的唯一标识符，并在查询、存储和更新操作中使用该字段来识别和操作特定的文档。
     */
    @Id
    private Integer id;
    /**
     * analyzer 指定使用 ik 分词器的 ik_max_word 模式
     */
    @Field(type = FieldType.Text, analyzer = AnalyzerConstant.IK_MAX_WORD)
    private String title;

    @Field(type = FieldType.Text, analyzer = AnalyzerConstant.IK_MAX_WORD)
    private String description;
    /**
     * 指定时间格式，不然会出问题。
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdTime;
}
