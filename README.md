# Spring Boot Base SDK For Yiban

## 前言
这个框架在几月前就写好了，但是一直没得空写文档出来，今天打算将它分享出来。
首先这个框架是在Spring Boot + Mybatis大框架下的二次封装，分为两个部分：
1. 可以叫作Web基础开发框架

这是我近两年在Java Web开发上的总结和经验，封装了较多的基础操作，提供较为完善的数据库操作，包含Dao层、Service层、Controller层以及常用工具、拦截器配置、跨域配置、项目常用配置、服务端安全(JWT)等方面的配置。
2. 在上面的基础上加上对易班开放平台的支持

通过易班开放平台来作为例子，展示如何在该平台上进行再次开发。

## 框架目录结构
先来看看项目的目录结构
```
'|-- src/main/java/xyz/frt/yiban/basesdk',
  '    |-- BaseSdkApplication.java',    //Spring Boot启动文件
  '    |-- common', //通用工具包
  '    |   |-- AppConst.java',  //字符常量文件
  '    |   |-- AppContext.java',    //全局上下文
  '    |   |-- JsonResult.java',    //全局响应类，用于统一返回数据格式
  '    |   |-- Page.java',  //分页文件
  '    |-- config', //全局配置包
  '    |   |-- AppConfig.java', //项目配置类
  '    |   |-- RegisterInterceptor.java',   //拦截器配置类
  '    |-- controller', 
  '    |   |-- AuthorizeController.java',
  '    |   |-- BaseController.java',    //Controller基类
  '    |   |-- sys',
  '    |       |-- UserController.java',
  '    |-- dao',
  '    |   |-- BaseMapper.java',    //Mapper基类，封装了数据库常用操作
  '    |   |-- sys',
  '    |       |-- UserMapper.java',
  '    |-- entity',     //项目实体类
  '    |   |-- BaseEntity.java',    //项目实体基类，封装了数据库通用字段
  '    |   |-- sys',
  '    |       |-- User.java',
  '    |-- interceptor',    //拦截器配置包
  '    |   |-- UserInterceptor.java',   //用户权限/会话拦截器
  '    |-- service',
  '    |   |-- AuthorizeService.java',
  '    |   |-- AuthorizeServiceImpl.java',
  '    |   |-- BaseService.java',   //Service层基类
  '    |   |-- BaseServiceImpl.java',   //Service层基类实现类
  '    |   |-- sys',
  '    |       |-- UserService.java',
  '    |       |-- UserServiceImpl.java',
  '    |-- util',
  '    |   |-- BaseUtils.java', //常用工具类
  '    |   |-- JWTUtil.java',   //JWT工具类
  '    |   |-- MD5Util.java',   //Md5加密工具类
  '    |-- websocket',
  '        |-- WebSocketServer.java',   //支持WebSocket
  ''
```
然后时resources目录
```
'|-- resources',
  '    |-- application.properties', //项目配置文件
  '    |-- generator',  
  '    |   |-- generatorConfig.xml',    //mybatis生成映射文件的插件
  '    |-- mapper',
  '        |-- UserMapper.xml', //mybatis映射文件
  ''
```

## 使用方法

**下面通过User实体类来讲述如何使用本框架**

1. 首先通过mybatis映射文件的生成插件生成映射文件和实体类
