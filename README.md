# sentinel-1.6.3

## 1、sentinel-dashboard包

*增加了flow，system，authority，degrade，parameter flow规则的持久化，nacos与dashboard控制台的互相推拉。

配置文件名是：应用名-sentinel-规则类型，可以查看枚举RuleTypeEnum

- flow规则读取（流控规则）：${spring.application.name}-sentinel-flow

  ```properties
   spring.cloud.sentinel.datasource.ds.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
   spring.cloud.sentinel.datasource.ds.nacos.namespace=${spring.cloud.nacos.config.namespace}
   spring.cloud.sentinel.datasource.ds.nacos.groupId=DEFAULT_GROUP
   spring.cloud.sentinel.datasource.ds.nacos.dataId=${spring.application.name}-sentinel-flow
   spring.cloud.sentinel.datasource.ds.nacos.rule-type=flow
   spring.cloud.sentinel.datasource.ds.nacos.data-type=json
  ```

   flow规则设计到集群和单机，集群页面配置的规则会应用到所有的集群；单机配置的，规则会应用到配置规则的ip，如果一个ip下同一服务A有多个（A1,A2），则会应用到这个ip下的服务A的所有（A1，A2）。  规则相当于做了单机与集群规则的分离，但是持久化文件是同一个文件。

  集群页面展示的限流规则时从nacos拉取的，单机页展示的规则是从对应机器应用（内存中）的限流规则。每当单机规则修改或添加会同步刷新该机器的限流规则。每当集群的规则修改或添加时，会同步更新到集群。更新成功后可以在单机页面刷新规则查看是否有新增的规则。

  ------

  

- degrade规则读取（降级规则）：${spring.application.name}-sentinel-degrade

  ```properties
  spring.cloud.sentinel.datasource.ds1.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
  spring.cloud.sentinel.datasource.ds1.nacos.namespace=${spring.cloud.nacos.config.namespace}
  spring.cloud.sentinel.datasource.ds1.nacos.groupId=DEFAULT_GROUP
  spring.cloud.sentinel.datasource.ds1.nacos.dataId=${spring.application.name}-sentinel-degrade
  spring.cloud.sentinel.datasource.ds1.nacos.rule-type=degrade
  spring.cloud.sentinel.datasource.ds1.nacos.data-type=json
  ```

  degrade规则只有集群配置，意味着配置后会应用到对应服务的所有集群。

  ------

  

- param flow规则读取（热点规则）：${spring.application.name}-sentinel-params

  ```properties
  spring.cloud.sentinel.datasource.ds2.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
  spring.cloud.sentinel.datasource.ds2.nacos.namespace=${spring.cloud.nacos.config.namespace}
  spring.cloud.sentinel.datasource.ds2.nacos.groupId=DEFAULT_GROUP
  spring.cloud.sentinel.datasource.ds2.nacos.dataId=${spring.application.name}-sentinel-params
  spring.cloud.sentinel.datasource.ds2.nacos.rule-type=param_flow
  spring.cloud.sentinel.datasource.ds2.nacos.data-type=json
  ```

  param flow规则只有集群配置，意味着配置后会应用到对应服务的所有集群。

  ------

  

- system规则读取（系统规则）：${spring.application.name}-sentinel-system

  ```properties
  spring.cloud.sentinel.datasource.ds3.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
  spring.cloud.sentinel.datasource.ds3.nacos.namespace=${spring.cloud.nacos.config.namespace}
  spring.cloud.sentinel.datasource.ds3.nacos.groupId=DEFAULT_GROUP
  spring.cloud.sentinel.datasource.ds3.nacos.dataId=${spring.application.name}-sentinel-system
  spring.cloud.sentinel.datasource.ds3.nacos.rule-type=system
  spring.cloud.sentinel.datasource.ds3.nacos.data-type=json
  ```

  system只有集群配置，意味着配置后会应用到对应服务的所有集群。

  ------

  

- authority规则读取（授权规则）：

  ```properties
  spring.cloud.sentinel.datasource.ds4.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
  spring.cloud.sentinel.datasource.ds4.nacos.namespace=${spring.cloud.nacos.config.namespace}
  spring.cloud.sentinel.datasource.ds4.nacos.groupId=DEFAULT_GROUP
  spring.cloud.sentinel.datasource.ds4.nacos.dataId=${spring.application.name}-sentinel-authority
  spring.cloud.sentinel.datasource.ds4.nacos.rule-type=authority
  spring.cloud.sentinel.datasource.ds4.nacos.data-type=json
  ```

  system只有集群配置，意味着配置后会应用到对应服务的所有集群。

  ------

## 2、sentinel-core包

是修正了maven中央库与alibaba/sentinel-1.6.3 的版本差异，目前发现服务端使用这个alibaba/sentinel-1.6.3打出的jar包会导致报错，这个问题已完成修复。

  **如果不需要改动core源码则不需要自己手动拉源码打包**
  core新增了功能

1、启动时加载对应本机ip的规则或全局（集群）规则，过滤掉非本机的规则（即配置的单机规则）。

具体修改逻辑在DynamicSentinelProperty$getFlowRulesWithIp方法
