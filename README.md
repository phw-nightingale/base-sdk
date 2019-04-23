# Spring Boot Base SDK For Yiban

## 前言
这个框架在几月前就写好了，但是一直没得空写文档出来，今天打算将它分享出来。
首先这个框架是在Spring Boot + Mybatis大框架下的二次封装，分为两个部分：

1.可以叫作Web基础开发框架

这是我近两年在Java Web开发上的总结和经验，封装了较多的基础操作，提供较为完善的数据库操作，包含Dao层、Service层、Controller层以及常用工具、拦截器配置、跨域配置、项目常用配置、服务端安全(JWT)等方面的配置。

2.在上面的基础上加上对易班开放平台的支持

通过易班开放平台来作为例子，展示如何在该平台上进行再次开发。

## 框架目录结构
先来看看项目的目录结构
```
|-- src/main/java/xyz/frt/yiban/basesdk,
      |-- BaseSdkApplication.java,    //Spring Boot启动文件
      |-- common, //通用工具包
      |   |-- AppConst.java,  //字符常量文件
      |   |-- AppContext.java,    //全局上下文
      |   |-- JsonResult.java,    //全局响应类，用于统一返回数据格式
      |   |-- Page.java,  //分页文件
      |-- config, //全局配置包
      |   |-- AppConfig.java, //项目配置类
      |   |-- RegisterInterceptor.java,   //拦截器配置类
      |-- controller, 
      |   |-- AuthorizeController.java,
      |   |-- BaseController.java,    //Controller基类
      |   |-- sys,
      |       |-- UserController.java,
      |-- dao,
      |   |-- BaseMapper.java,    //Mapper基类，封装了数据库常用操作
      |   |-- sys,
      |       |-- UserMapper.java,
      |-- entity,     //项目实体类
      |   |-- BaseEntity.java,    //项目实体基类，封装了数据库通用字段
      |   |-- sys,
      |       |-- User.java,
      |-- interceptor,    //拦截器配置包
      |   |-- UserInterceptor.java,   //用户权限/会话拦截器
      |-- service,
      |   |-- AuthorizeService.java,
      |   |-- AuthorizeServiceImpl.java,
      |   |-- BaseService.java,   //Service层基类
      |   |-- BaseServiceImpl.java,   //Service层基类实现类
      |   |-- sys,
      |       |-- UserService.java,
      |       |-- UserServiceImpl.java,
      |-- util,
      |   |-- BaseUtils.java, //常用工具类
      |   |-- JWTUtil.java,   //JWT工具类
      |   |-- MD5Util.java,   //Md5加密工具类
      |-- websocket,
          |-- WebSocketServer.java,   //支持WebSocket
  
```
然后时resources目录
```
|-- resources,
      |-- application.properties, //项目配置文件
      |-- generator,  
      |   |-- generatorConfig.xml,    //mybatis生成映射文件的插件
      |-- mapper,
          |-- UserMapper.xml, //mybatis映射文件
  
```

## 使用方法

**下面通过User实体类来讲述如何使用本框架**

1.首先通过mybatis映射文件的生成插件生成映射文件和实体类
```
<!-- 要生成的表 tableName是数据库中的表名或视图名 domainObjectName是实体类名-->
<table tableName="sys_user" domainObjectName="User" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false" />
```
在该注释下写入table标签的内容，tableName必须与数据库表名一致，domainObject为Java实体类名，后面的是mybatis插件自带的额外功能，我们全部关掉即可。
然后通过该插件生成相应的文件。具体生成方法请自行百度。

2.使用基类
完成生成文件后你应该新增了如下文件:
```
/resources/mapper/UserMapper.xml
/basesdk/dao/UserMapper.java
/basesdk/entity/User.java
```
然后我们对这三个文件稍微做一下修改:
```
UserMapper.xml
1.在该文件的<resultMap></resultMap>下加入
  <resultMap>
    ......
  </resultMap>
  <sql id="Base_Table_Name">
    sys_user    //此处为数据库表名
  </sql>
  
2.修改两个insert语句为如下内容
<insert id="insert" parameterType="xyz.frt.yiban.basesdk.entity.sys.User" keyProperty="id" useGeneratedKeys="true" >
    ......
</insert>
<insert id="insertSelective" parameterType="xyz.frt.yiban.basesdk.entity.sys.User" keyProperty="id" useGeneratedKeys="true">
    ......
</insert>

User.java
1.继承BaseEntity
2.删除BaseEntity中已有的字段和getter/setter
最后如下:

import xyz.frt.yiban.basesdk.entity.BaseEntity;

public class User extends BaseEntity {

    private String username;

    private String password;

    private String trueName;

    private String phone;

    private String token;

    getter/setter...
}

UserMapper.java
1.删除类中的所有方法
2.继承BaseMapper
3.打上@Mapper注解
最后如下:

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```
修改完毕。

3.Service层

新建一个UserService.java文件，内容如下:
```
public interface UserService extends BaseService<User> {

}
```
新建一个UserServiceImpl.java，内容如下:
```
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    @Override
    public BaseMapper<User> getMapper() {
        return userMapper;
    }
}

```
3.Controller层

新建一个UserController.java，内容如下:
```
@RestController
public class UserController extends BaseController<User> {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected BaseService<User> getService() {
        return userService;
    }

    @GetMapping("/users")
    public JsonResult findUsers(User user, Page page) {
        return findItems(user, page);
    }

    @GetMapping("/users/{id}")
    public JsonResult findUserByPrimaryKey(@PathVariable Integer id) {
        return findItemByPrimaryKey(id);
    }

}
```
写上两个接口，/users和/users/{id}

4.添加接口到拦截器的例外
```
找到/config/RegisterInterceptor.java

@Configuration
public class RegisterInterceptor implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;

    @Autowired
    public RegisterInterceptor(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    /**
     * 添加拦截器
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test-api/**", // 为了方便测试某些Api，可以暂时写到这下面
                        "/authorize",
                        "/sign-in",
                        "/users"    //把接口添加到这里才不会被拦截器拦截，当然你也可以关闭这个拦截器。
                        "/sign-out",
                        "/sign-up",
                        "/401",
                        "/403",
                        "/404",
                        "/");
    }
}
```

5.启动服务器

到这里，基本工作都完成了，启动一下服务器测试一下，启动成功后访问接口
```
http://127.0.0.1:8080
```
出现如下响应内容即说明项目已经跑起来了
```
{"code":200,"msg":"Welcome to Yiban Application.","data":null}
```
