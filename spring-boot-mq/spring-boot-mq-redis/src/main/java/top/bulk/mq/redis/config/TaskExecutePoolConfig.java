package top.bulk.mq.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author 散装java
 * @date 2023-02-03
 */
@Configuration
public class TaskExecutePoolConfig {

    @Bean(name = "redisListenerContainerExecutor")
    public ThreadPoolTaskExecutor redisListenerContainerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数 注意在 redis stream 这种情况，线程数不要设置成 1 ，会出问题，总会有消息获取不到
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(10);
        //任务队列的大小
        executor.setQueueCapacity(0);
        //线程池名的前缀
        executor.setThreadNamePrefix("redisListenerContainerExecutor-");
        //允许线程的空闲时间30秒
        executor.setKeepAliveSeconds(0);
        //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 不拒绝，扔给主程序执行，确保所有的数据都正确执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程初始化
        executor.initialize();
        return executor;
    }
}
