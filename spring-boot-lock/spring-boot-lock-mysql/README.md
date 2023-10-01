# Spring Boot 基于 MySQL 实现分布式锁

## 说明

本项目会演示基于 MySQL 实现分布式锁的使用，会以常见的 “超卖”业务去演示

> 本项目中包含两种类型的锁
> 1. 基于 MySQL 实现的乐观锁
> 2. 基于 MySQL 实现的悲观锁

## 版本依赖

|技术栈|版本|
| ---------------- | -------------------------- |
|Spring Boot | 2.7.0 |
|MySQL | 5.7 |
|Maven|3.8.3|

## 启动说明

1. 更改 MySQL 配置 [application.yml](src/main/resources/application.yml)
2. 更改数据库链接配置，导入数据库 [sql](_db/lock-test.sql)
3. 点击启动类 [SpringBootLockMysqlApplication](src/main/java/top/bulk/lock/mysql/SpringBootLockMysqlApplication.java)
   启动项目
4. 访问接口 `http://localhost:8080/reduceStock/1` , 即可测试锁接口，ProductStockController 类中有多个接口，可以分别测试效果

## 乐观锁
我认为我操作之前不会有人操作，如果有人操作了，那我再来一次，即为无锁操作

使用代码如下

```java
@SneakyThrows
@Override
public String reduceStockOptimism(Integer id) {
    ProductStock stock = productStockMapper.selectById(id);
    if (stock != null && stock.getStock() > 0) {
        int i = productStockMapper.reduceStockOptimism(id, stock.getVersion());
        // i = 0 表示没有修改成功，说明有别人在你修改之前 已经修改了数据；需要重新再调用下当前方法
        if (i == 0) {
            // Thread.sleep 防止栈溢出
            // Thread.sleep(10);
            this.reduceStockOptimism(id);
        }
    } else {
        throw new RuntimeException("库存不足！");
    }
    return "ok";
}
```
`productStockMapper.reduceStockOptimism` 的 SQL 如下

```xml
<update id="reduceStockOptimism">
        update product_stock
        set stock   = stock - 1,
            version = version + 1
        where id = #{id}
          and version = #{version}
    </update>
```

## 悲观锁
查询的时候，不管有没有线程竞争，都在 MySQL层面就加上了锁

使用代码如下
```java
@Override
@Transactional(rollbackFor = Exception.class)
public String reduceStockGloomy(Integer id) {
    // 加锁查询
    ProductStock stock = productStockMapper.selectForLock(id);
    if (stock != null && stock.getStock() > 0) {
        productStockMapper.reduceStock(id);
    } else {
        throw new RuntimeException("库存不足！");
    }
    return "ok";
}
```

`productStockMapper.selectForLock(id)` SQL 如下，借助于 `for update` 实现加锁

```xml
<select id="selectForLock" resultType="top.bulk.lock.mysql.entity.ProductStock">
    select *
    from product_stock
    where id = #{id} for
    update
</select>
```

## 测试说明

1. 压测工具使用 JMeter , 当然也可以用别的，我用的这个
2. 本地测试，借助于 IDEA 的 `Allow parallel run` 功能启动多个相同的服务（模拟线上环境多个副本），注意修改端口（-Dserver.port=8089），操作可以看下图
3. 使用 Nginx 工具，将启动的多个项目做负载均衡；负载均衡配置文件可以参考 [mysql-lock-load-balance.conf](_doc/mysql-lock-load-balance.conf)
4. 接下来就是使用 JMeter 开启多个线程去压测 Nginx 暴露出来的接口了