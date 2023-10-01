package top.bulk.search.es.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * es 客户端配置
 * 参考 https://docs.spring.io/spring-data/elasticsearch/docs/5.1.1/reference/html/#elasticsearch.clients
 */
@Configuration
public class EsClientConfig extends AbstractElasticsearchConfiguration {
    @Value("${spring.elasticsearch.uris}")
    public String uris;

    /**
     * 高版本中，推荐使用 es 提供的 client
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/introduction.html
     *
     * @return client
     */
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(uris)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
