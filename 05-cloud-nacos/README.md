# Nacos注册中心

## 服务搭建

在 nacos/bin 目录下运行指令：`startup.cmd -m standalone`

## 服务注册

引入 nacos.discovery 依赖
配置 nacos 地址 `spring.cloud.nacos.server-addr`

## 服务分级存储

一级是**服务**，例如 userservice

二级是**集群**，例如杭州或上海

三级是**实例**，例如杭州机房的某台部署了 userservice 的服务器

**如何设置实例的集群属性**

修改 application.yml 文件，添加 spring.cloud.nacos.discovery.cluster-name 属性。

## NacosRule

优先选择同集群服务实例列表，本地集群找不到提供者，才去其它集群寻找，并且会报警告；确定了可用实例列表后，再采用随机负载均衡挑选实例

```java
@Bean
public IRule iRule(){
    //默认为轮询规则，这里自定义为随机规则
    return new NacosRule();
}
```

## 环境隔离

每个 namespace 都有唯一id；服务设置namespace时要写id而不是名称；不同 namespace 下的服务互相不可见。

## 配置管理

**将配置交给 Nacos 管理的步骤**

1. 在Nacos中添加配置文件
2. 在微服务中引入 nacos-config 依赖
3. 在微服务中添加 bootstrap.yml，配置 nacos 地址、当前环境、服务名称、文件后缀名。

**Nacos配置更改后，微服务可以实现热更新**

通过 @Value 注解注入，结合 @RefreshScope 来刷新

通过 @ConfigurationProperties 注入，自动刷新

注意事项：

不是所有的配置都适合放到配置中心，维护起来比较麻烦；建议将一些关键参数，需要运行时调整的参数放到 nacos 配置中心，一般都是自定义配置。

**服务默认读取的配置文件**

[服务名]-[spring.profile.active].yaml，默认配置

[服务名].yaml，多环境共享

**不同微服务共享的配置文件**

1. 通过 shared-configs 指定
2. 通过 extension-configs 指定

优先级：

环境配置 >服务名.yaml > extension-config > extension-configs > shared-configs > 本地配置

