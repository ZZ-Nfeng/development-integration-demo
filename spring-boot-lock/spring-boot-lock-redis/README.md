# Spring Boot 集成 Redis 分布式锁

## 简介

本项目会演示 Redis 分布式锁的使用，会以常见的 “超卖”业务去演示
> 本示例代码主要包括两部分
> 1. Redisson 实现的分布式锁使用演示
> 2. 自己实现的 Redis 分布式锁使用演示

## 版本依赖

|技术栈|版本|
| ---------------- | -------------------------- |
|Spring Boot | 2.7.0 |
|MySQL | 5.7 |
|Redisson | 3.18.0 |
|Maven|3.8.3|

## 启动说明

1. 更改 MySQL 配置和 Redis 配置 [application.yml](src/main/resources/application.yml)
2. 更改数据库链接配置，导入数据库 [sql](_db/lock-test.sql)
3. 点击启动类 [SpringBootLockRedisApplication](src/main/java/top/bulk/lock/redis/SpringBootLockRedisApplication.java)
   启动项目
4. 访问接口 `http://localhost:8080/reduceStock/1` , 即可测试锁接口，ProductStockController 类中有多个接口，可以分别测试效果


## 自定义 redis 分布式锁
### 锁定义 
```java
@Slf4j
@Component
public class BulkRedisLock {
    private static final String LOCK_PREFIX = "redisLock";
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 尝试获取锁
     *
     * @param requestId  请求id
     * @param expireTime 过期时间  单位毫秒
     * @return true false
     */
    public boolean lock(String requestId, int expireTime) {
        //这里利用redis的set命令
        //使用redis保证原子操作（判断是否存在，添加key，设置过期时间）
        while (true) {
            if (Boolean.TRUE.equals(stringRedisTemplate.boundValueOps(LOCK_PREFIX).
                    setIfAbsent(requestId, expireTime, TimeUnit.SECONDS))) {
                return true;
            }
        }
    }

    /**
     * 将锁释放掉
     * <p>
     * 为何解锁需要校验 requestId 因为不是自己的锁不能释放
     * 客户端A加锁，一段时间之后客户端A解锁，在执行 lock 之前，锁突然过期了。
     * 此时客户端B尝试加锁成功，然后客户端A再执行 unlock 方法，则将客户端B的锁给解除了。
     *
     * @param requestId 请求id
     * @return true false
     */
    public boolean unlock(String requestId) {
        //这里使用Lua脚本保证原子性操作
        String script = "if  redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long res = stringRedisTemplate.execute(redisScript, Collections.singletonList(LOCK_PREFIX), requestId);
        return new Long(1).equals(res);
    }

    /**
     * 创建续命子线程
     *
     * @param time      操作预期耗时
     * @param requestId 唯一标识
     * @return 续命线程 Thread
     */
    public Thread watchDog(int time, String requestId) {
        return new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(time * 2 / 3);
                    //重置时间
                    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                            "return redis.call('expire', KEYS[1],ARGV[2]) " +
                            "else return '0' end";
                    List<Object> args = new ArrayList();
                    args.add(requestId);
                    args.add(time);
                    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
                    stringRedisTemplate.execute(redisScript, Collections.singletonList(LOCK_PREFIX), args);
                } catch (Exception e) {
                    log.info("watchDog异常：{}", e.getMessage());
                    return;
                }
            }
        });
    }
}
```

### 锁使用

```java
public String reduceStockByMyLock(Integer id) {
     // requestId 确保每一个请求生成的都不一样，这里使用 uuid，也可以使用其他分布式唯一 id 方案
     String requestId = UUID.randomUUID().toString().replace("-", "");
     int expireTime = 10;
     bulkRedisLock.lock(requestId, expireTime);
     // 开启续命线程，
     Thread watchDog = bulkRedisLock.watchDog(expireTime, requestId);
     watchDog.setDaemon(true);
     watchDog.start();
     try {
         ProductStock stock = productStockMapper.selectById(id);
         if (stock != null && stock.getStock() > 0) {
             productStockMapper.reduceStock(id);
         } else {
             throw new RuntimeException("库存不足！");
         }
     } finally {
         watchDog.interrupt();
         bulkRedisLock.unlock(requestId);
     }
     return "ok";
 }
```

## Redisson 实现分布式锁
### 锁定义
Redisson 已经定义好了，具体可以参考 `RLock` 实现类

### 锁使用
```java
public String reduceStock(Integer id) {
     RLock lock = redissonClient.getLock("lock");
     lock.lock();
     try {
         ProductStock stock = productStockMapper.selectById(id);
         if (stock != null && stock.getStock() > 0) {
             productStockMapper.reduceStock(id);
         } else {
             throw new RuntimeException("库存不足！");
         }
     } finally {
         lock.unlock();
     }
     return "ok";
 }
```



## 测试说明

1. 压测工具使用 JMeter , 当然也可以用别的，我用的这个
2. 本地测试，借助于 IDEA 的 `Allow parallel run` 功能启动多个相同的服务（模拟线上环境多个副本），注意修改端口（-Dserver.port=8089），操作可以看下图
3. 使用 Nginx 工具，将启动的多个项目做负载均衡；负载均衡配置文件可以参考 [redis-lock-load-balance.conf](_doc/redis-lock-load-balance.conf)
4. 接下来就是使用 JMeter 开启多个线程去压测 Nginx 暴露出来的接口了