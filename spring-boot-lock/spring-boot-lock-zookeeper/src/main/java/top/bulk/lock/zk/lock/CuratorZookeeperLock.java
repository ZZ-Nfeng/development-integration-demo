package top.bulk.lock.zk.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Component;

/**
 * curator 实现 Zookeeper 锁执行代码
 *
 * @author 散装java
 * @date 2022-12-30
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CuratorZookeeperLock {
    private final CuratorFramework curatorFramework;

    /**
     * 加锁
     *
     * @param lockKey 锁标识
     * @return 锁信息
     */
    public InterProcessMutex lock(String lockKey) {
        if (!CuratorFrameworkState.STARTED.equals(curatorFramework.getState())) {
            log.warn("在调用此方法之前必须启动 CuratorFramework 实例");
            return null;
        }
        String nodePath = "/curator/lock/%s";
        try {
            // 可重入锁：InterProcessMutex 不可重入锁：InterProcessSemaphoreMutex
            InterProcessMutex mutex = new InterProcessMutex(curatorFramework, String.format(nodePath, lockKey));
            final boolean locked = mutex.acquire(-1L, null);
            return locked ? mutex : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解锁
     *
     * @param lockInstance 锁实例
     */
    public void unLock(InterProcessMutex lockInstance) {
        try {
            lockInstance.release();
        } catch (Exception e) {
            log.warn("zookeeper lock release error", e);
        }
    }
}
