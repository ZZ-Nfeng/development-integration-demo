package top.bulk.lock.zk.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import top.bulk.lock.zk.config.ZookeeperProperty;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 自己实现一个 zk 锁
 *
 * @author 散装java
 * @date 2023-01-03
 */
public class BulkZookeeperLock {

    private ZooKeeper zk = null;
    /**
     * zookeeper 连接
     */
    private final CountDownLatch connectLatch = new CountDownLatch(1);

    /**
     * zookeeper节点等待
     */
    private final CountDownLatch waitLatch = new CountDownLatch(1);

    /**
     * 操作的当前节点
     */
    private String currentNode = null;
    /**
     * 当前节点要监视的上一个节点
     */
    private String waitPath = null;

    public BulkZookeeperLock(ZookeeperProperty property) throws IOException, InterruptedException, KeeperException {

        //建立和zookeeper集群的连接
        zk = new ZooKeeper(property.getZkServers(), property.getSessionTimeout(), new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 连接建立时, 打开latch, 唤醒 wait 在该 latch 上的线程
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    connectLatch.countDown();
                }

                // 发生了 waitPath 的删除事件 唤醒 waitLatch
                if (watchedEvent.getType() == Event.EventType.NodeDeleted && watchedEvent.getPath().equals(waitPath)) {
                    waitLatch.countDown();
                }
            }
        });

        //等待建立连接，即连接建立之后才会执行之后的代码。
        connectLatch.await();

        //判断根节点"/locks"是否存在
        Stat stat = zk.exists("/locks", false);
        if (stat == null) {
            //根节点 “/locks” 不存在，创建根节点
            zk.create("/locks", "lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    /**
     * 加锁方法，加锁就是在根节点“/locks”，下创建节点，不过要判断是否能获取锁。
     * 若当前节点是最小的，则可以获取锁，否则应该监听它前一个节点
     */
    public void lock() {
        try {
            // 创建节点 /locks/seq-0000001
            currentNode = zk.create("/locks/seq-", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            Thread.sleep(10);

            // 注意, 没有必要监听"/locks"的子节点的变化情况
            List<String> children = zk.getChildren("/locks", false);

            if (children.size() == 1) {
                // 列表中只有一个子节点, 那肯定就是 currentNode , 说明client 获得锁
                return;
            } else {
                //对根节点下的所有临时顺序节点进行从小到大排序
                Collections.sort(children);

                //获取当前节点名称，如 seq-0000001
                String thisNode = currentNode.substring("/locks/".length());

                //获取当前节点在children中的位置
                int index = children.indexOf(thisNode);

                if (index == -1) {
                    System.out.println("数据异常");
                } else if (index == 0) {
                    // index为0表示，thisNode 在列表的子节点中最小，可以获取到锁
                    return;
                } else {
                    // 否则监听当前节点的上一个节点
                    waitPath = "/locks/" + children.get(index - 1);
                    // 在 waitPath 上注册监听器, 当 waitPath 被删除时,zookeeper 会回调监听器的 process 方法
                    zk.getData(waitPath, true, new Stat());
                    // 进入等待锁状态，如果说，上一个节点他进行了解锁，那么就可以由上面的 waitLatch.countDown(); 唤醒 继续执行，即加锁成功
                    waitLatch.await();
                    return;
                }
            }

        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解锁方法，解锁就是删除当前节点
     */
    public void unLock() {
        try {
            zk.delete(currentNode, -1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }


}
