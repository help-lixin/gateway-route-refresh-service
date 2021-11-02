# gateway-route-refresh-service
Gateway路由热更新

### 1. 背景
微服务流行之后,急需要一套配置中心进行管控,现今比较流行的配置中心有:Nacos(阿里)和Apollo(携程),它们都能效的支持配置的热更新,但是,
当和Spring Cloud Gateway整合之后,Spring Cloud Gateway的路由定义信息就无法实时热更新了.

### 2. 为什么不能有效的支持配置的热更新?
1) Spring Cloud Gateway是由Spring团队研发的,所以,Spring是不会向Nacos(Apollo)靠扰的.      
2) Nacos和Apollo是由国内公司研发的,它们考虑是:如果在代码上特意去支持Gateway的话,那么,代码就有一点过度设计了.  
3) 实际,Spring Cloud Gateway底层是支持路由信息的热更新的. 

### 3. 集成步骤
1. 添加依赖

```xml
<dependency>
    <groupId>help.lixin</groupId>
    <artifactId>gateway-route-refresh-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

2. 启动热更新
```properties
# 启用路由热更新
gateway.route.refresh.enabled=true
```

3. 添加配置中心的依赖(依情况添加依赖)
```xml

<!-- apollo 添加如下依赖 -->
<dependency>
    <groupId>com.ctrip.framework.apollo</groupId>
    <artifactId>apollo-client</artifactId>
    <version>${apollo.client.version}</version>
</dependency>

<!-- nacos 添加如下依赖-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <scope>provided</scope>
    <exclusions>
        <exclusion>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
### 4. 相关问题
1) 支持哪些配置中心?   
   目前仅支持:Nacos和Apollo.  