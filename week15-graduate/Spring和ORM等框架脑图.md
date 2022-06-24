# Spring和ORM等框架

## Spring

### DI、IoC

### Bean装配过程
- 可以介入的点: XXAware、InitializingBean、BeanFactoryPostProcessor、BeanPostProcessor

### AOP
- 原理：动态代理、CGLib
- 应用：事务、Cache、异步、重试Spring Retry 等等
- Before、After、AfterReturning、AfterThrowing、Around

### 循环依赖
- 构造函数 - 不可解决
- 三级缓存解决循环依赖

### Spring Boot
- 自定义starter：spring.factories、spring-boot-starter-parent
- @Import、@ImportResource

## ORM

### MyBatis
- SqlSession、SqlSessionFactory、MapperProxy
- 各类标签
- 执行器类型
- xml文件 / 注解 配置sql
- 一级缓存 / 二级缓存