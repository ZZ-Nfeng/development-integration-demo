package top.bulk.lock.zk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * zk 链接属性
 *
 * @author 散装java
 * @date 2023-01-03
 */
@Configuration
@ConfigurationProperties(prefix = "bulk.zookeeper")
@Data
public class ZookeeperProperty {

    private String zkServers;

    private int sessionTimeout = 30000;

    private int connectionTimeout = 5000;

    private int baseSleepTimeMs = 1000;

    private int maxRetries = 3;

}
