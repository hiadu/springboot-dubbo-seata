# springboot-dubbo-zookeeper-seata

SpringBoot+Dubbo+Seata分布式事务示例

前言
Seata 是 阿里巴巴开源的分布式事务中间件，以高效并且对业务0侵入的方式，解决微服务场景下面临的分布式事务问题。
事实上，官方在GitHub已经给出了多种环境下的Seata应用示例项目，地址：https://github.com/seata/seata-samples。
为什么笔者要重新写一遍呢，主要原因有两点：
官网代码示例中，依赖太多，分不清哪些有什么作用
Seata相关资料较少，笔者在搭建的过程中，遇到了一些坑，记录一下
一、环境准备
本文涉及软件环境如下：
SpringBoot 2.1.6.RELEASE
Dubbo 2.7.1
Mybatis 3.5.1
Seata 0.6.1
Zookeeper 3.4.10
1、业务场景
为了简化流程，我们只需要订单和库存两个服务。创建订单的时候，调用库存服务，扣减库存。
涉及的表设计如下：
CREATE TABLE `t_order` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `order_no` varchar(255) DEFAULT NULL,
 `user_id` varchar(255) DEFAULT NULL,
 `commodity_code` varchar(255) DEFAULT NULL,
 `count` int(11) DEFAULT '0',
 `amount` double(14,2) DEFAULT '0.00',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
 
CREATE TABLE `t_storage` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `commodity_code` varchar(255) DEFAULT NULL,
 `name` varchar(255) DEFAULT NULL,
 `count` int(11) DEFAULT '0',
 PRIMARY KEY (`id`),
 UNIQUE KEY `commodity_code` (`commodity_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
另外还需要一个回滚日志表：
CREATE TABLE `undo_log` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `branch_id` bigint(20) NOT NULL,
 `xid` varchar(100) NOT NULL,
 `rollback_info` longblob NOT NULL,
 `log_status` int(11) NOT NULL,
 `log_created` datetime NOT NULL,
 `log_modified` datetime NOT NULL,
 `ext` varchar(100) DEFAULT NULL,
 `context` varchar(100) DEFAULT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;
2、Seata下载安装
打开https://github.com/seata/seata/releases，目前最新版本是v0.6.1。
下载解压后，到seata-server-0.6.1\distribution\bin目录下可以看到seata-server.bat和seata-server.sh，选择一个双击执行。
不出意外的话，当你看到-Server started ...等字样，就正常启动了。
3、Maven依赖
由于是Dubbo项目，我们先引入Dubbo相关依赖。
<dependency>
 <groupId>org.apache.dubbo</groupId>
 <artifactId>dubbo</artifactId>
 <version>2.7.1</version>
</dependency>
<dependency>
 <groupId>org.apache.dubbo</groupId>
 <artifactId>dubbo-spring-boot-starter</artifactId>
 <version>2.7.1</version>
</dependency>
Dubbo的服务要注册到Zookeeper，引入curator客户端。
<dependency>
 <groupId>org.apache.curator</groupId>
 <artifactId>curator-framework</artifactId>
 <version>2.13.0</version>
</dependency>
<dependency>
 <groupId>org.apache.curator</groupId>
 <artifactId>curator-recipes</artifactId>
 <version>2.13.0</version>
</dependency>
最后，引入Seata。
<dependency>
 <groupId>io.seata</groupId>
 <artifactId>seata-all</artifactId>
 <version>0.6.1</version>
</dependency>
当然了，还有其他的如Mybatis、mysql-connector等就不粘了，自行引入即可。
二、项目配置
1、application.properties
这里只需要配置数据库连接信息和Dubbo相关信息即可。
server.port=8011
 
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/seata
spring.datasource.username=root
spring.datasource.password=root
 
dubbo.application.name=order-service
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.protocol.name=dubbo
dubbo.protocol.port=20881
dubbo.consumer.timeout=9999999
dubbo.consumer.check=false
2、数据源
Seata 是通过代理数据源实现事务分支，所以需要先配置一个数据源的代理，否则事务不会回滚。
@Bean
public DataSourceProxy dataSourceProxy(DataSource dataSource) {
 return new DataSourceProxy(dataSource);
}
注意，这里的DataSourceProxy类位于io.seata.rm.datasource包内。
3、Seata配置
还需要配置全局事务扫描器。有两个参数，一个是应用名称，一个是事务分组。
@Bean
public GlobalTransactionScanner globalTransactionScanner() {
 return new GlobalTransactionScanner("springboot-order", "my_test_tx_group");
}
事实上，关于Seata事务的一系列初始化工作都在这里完成。
4、配置注册中心
Seata连接到服务器的时候需要一些配置项，这时候有一个registry.conf文件可以指定注册中心和配置文件是什么。
这里有很多可选性，比如file、nacos 、apollo、zk、consul。
后面4个都是业界成熟的配置注册中心产品，为啥还有个file呢？
官方的初衷是在不依赖第三方配置注册中心的基础上快速集成测试seata功能，但是file类型本身不具备注册中心的动态发现和动态配置功能。
registry.conf文件内容如下：
registry {
 type = "file"
 file {
  name = "file.conf"
 }
}
config {
 # file、nacos 、apollo、zk、consul
 type = "file"
 file {
  name = "file.conf"
 }
}
如果你选择了file类型，通过name属性指定了file.conf，这个文件中指定了客户端或服务器的配置信息。比如传输协议、服务器地址等。
service {
 #vgroup->rgroup
 vgroup_mapping.my_test_tx_group = "default"
 #only support single node
 default.grouplist = "127.0.0.1:8091"
 #degrade current not support
 enableDegrade = false
 #disable
 disable = false
}
三、业务代码
1、库存服务
在库存服务中，拿到商品编码和购买总个数，扣减即可。
<update id="decreaseStorage">
 update t_storage set count = count-${count} where commodity_code = #{commodityCode}
</update>
然后用Dubbo将库存服务扣减接口暴露出去。
2、订单服务
在订单服务中，先扣减库存，再创建订单。最后抛出异常，然后去数据库检查事务是否回滚。
@GlobalTransactional
public void createOrder(OrderDTO orderDTO) {
 
 System.out.println("开始全局事务。XID="+RootContext.getXID());
 StorageDTO storageDTO = new StorageDTO();
 storageDTO.setCount(orderDTO.getCount());
 storageDTO.setCommodityCode(orderDTO.getCommodityCode());
  
 //1、扣减库存
 storageDubboService.decreaseStorage(storageDTO);
  
 //2、创建订单
 orderDTO.setId(order_id.incrementAndGet());
 orderDTO.setOrderNo(UUID.randomUUID().toString());
 Order order = new Order();
 BeanUtils.copyProperties(orderDTO,order);
 orderMapper.createOrder(order);
 
 throw new RuntimeException("分布式事务异常..."+orderDTO.getOrderNo());
}
值得注意的是，在订单服务事务开始的方法上，需要标注@GlobalTransactional。另外，在库存服务的方法里，不需要此注解，事务会通过Dubbo进行传播。
四、注意事项
1、数据源
请切记，Seata 是通过代理数据源实现事务分支，一定不要忘记配置数据源代理。
2、主键自增
在数据库中，表里的主键ID字段都是自增的。如果你的字段不是自增的，那么在Mybatis的insert SQL中，要将列名写完整。
比如我们可以这样写SQL：
?
1
INSERT INTO table_name VALUES (值1, 值2,....)
那么这时候就要写成：
?
1
INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
3、序列化问题
在订单表中，amount字段类型为double。在seata0.6.1版本中，默认的序列化方式为fastjson，但它会将这个字段序列化成bigdecimal类型，会导致后面类型不匹配。
但是在后续的seata0.7.0版本中(还未发布)，已经将默认的序列化方式改为了jackson。
不过无需担心，这个问题一般不会出现。笔者是因为引错了一个包，才导致发现这问题。
4、本文代码
本文示例代码在：https://github.com/taoxun/springboot-dubbo-zookeeper-seata。