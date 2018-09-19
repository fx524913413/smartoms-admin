


### 架构详解
#### 服务鉴权
通过`JWT`的方式来加强服务之间调度的权限验证，保证内部服务的安全性。
#### 监控
利用Spring Boot Admin 来监控各个独立Service的运行状态；利用Hystrix Dashboard来实时查看接口的运行状态和调用频率等。
#### 负载均衡
将服务保留的rest进行代理和网关控制，除了平常经常使用的node.js、nginx外，Spring Cloud系列的zuul和rebbion，可以帮我们进行正常的网关管控和负载均衡。其中扩展和借鉴国外项目的扩展基于JWT的`Zuul限流插件`，方面进行限流。
#### 服务注册与调用
基于Eureka来实现的服务注册与调用，在Spring Cloud中使用Feign, 我们可以做到使用HTTP请求远程服务时能与调用本地方法一样的编码体验，开发者完全感知不到这是远程方法，更感知不到这是个HTTP请求。
#### 熔断机制
因为采取了服务的分布，为了避免服务之间的调用“雪崩”，采用了`Hystrix`的作为熔断器，避免了服务之间的“雪崩”。



------

# `启动指南`
## 须知
本项目是一个`前后端分离`的项目，所以后端的服务必须先启动，在后端服务启动完成后，再启动前端的工程。
## 代码有漏
下载完后端代码后，记得先安装`lombok插件`，否则你的IDE会报代码缺失。
## 后端工程启动
### 环境须知
- mysql一个，redis一个，rabbitmq一个
- jdk1.8
- IDE插件一个，`lombok插件`，具体百度即可

### 运行步骤
- 运行数据库脚本
- 修改配置数据库配置
- 按`顺序`运行main类

### 项目结构
```
├─smartoms-admin
│  │  
│  ├─center-modules--------------公共服务模块（基础系统、搜索、OSS）
│  │ 
│  ├─center-auth-----------------鉴权中心
│  │ 
│  ├─center-gate-----------------网关负载中心
│  │ 
│  ├─center-common---------------通用脚手架
│  │ 
│  ├─center-eureka---------------服务注册中心
│  │   
│  ├─center-control--------------运维中心（监控、链路）
│  │
│  └─center-sidebar--------------调用第三方语言
│
```
----