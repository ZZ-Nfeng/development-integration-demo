package top.bulk.lock.zk.config;

import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * curator zk 相关配置
 *
 * @author 散装java
 * @date 2022-12-30
 */
@Configuration
@Data
public class CuratorZookeeperLockConfig {
    @Resource
    private ZookeeperProperty zookeeperProperty;

    /**
     * 链接 zk
     * 向容器中注入  CuratorFramework 方便后面使用
     *
     * @return curatorFramework
     */
    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework() {
        //  重试策略 （1000毫秒试1次，最多试3次）
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperProperty.getBaseSleepTimeMs(), zookeeperProperty.getMaxRetries());

        return CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperty.getZkServers())
                .sessionTimeoutMs(zookeeperProperty.getSessionTimeout())
                .connectionTimeoutMs(zookeeperProperty.getConnectionTimeout())
                .retryPolicy(retryPolicy)
                .build();
    }
}
