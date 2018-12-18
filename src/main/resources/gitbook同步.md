

### Git用法：

- 切换分支   **git checkout master**
- 创建分支   **git branch dev**
- 合并分支   
    - 首先切换分支到master
    - **git merge dev  > 合并dev到master**
- 切换分支问题：
    - 存储当前分支内容   **git stash**
    - 查看存储内容   **git stash list**
- 拉取分支   **git pull**



![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_git.png)





### SpringBoot常见工具用法：

#### SpringBoot打包Jar包运行：

```bash
nohup java -jar -Dspring.profiles.active=test test.jar com.fordeal.permissioncenter.PermissionCenterApplication  >/dev/null 2>error.log &

最简洁 java -jar xxx.jar com.xxx.xxx.ApplicationDemo
```

**完整脚本：**

```bash
#!/bin/bash
pid=$(ps -ef | grep com.fordeal.permissioncenter.PermissionCenterApplication | grep 'java' | grep -v grep | awk '{print $2'})
if [ -z "$pid" ]; then
 echo 'there are not permissioncenter process. starting will be continue.'
fi
if [ -n "$pid" ]; then
 echo 'java process id is '$pid
 if ps -p $pid > /dev/null
 then
  echo $pid' will be kill'
  kill -9 $pid
 fi
fi
echo 'start permissioncenter wait.'
nohup java -jar -Dspring.profiles.active=test test.jar com.fordeal.permissioncenter.PermissionCenterApplication  >/dev/null 2>error.log &
echo 'finish starting permissioncenter'

```



#### SpringBoot多配置源：

- 可以看到下图，是一个实际的多配置源项目
- 比如我们在开发环境直接在 **application.properties**  里面直接配置如下即可：

```properties
spring.profiles.active=dev
```

- 如果想用其他数据源，一样配置这个参数即可。 但要注意，配置文件必须是  **application-xxx.properties**


![1543494203354](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_1543494203354.png)





### OA系统：

#### SpringMVC拦截器：	

- 拦截器配置   >  实现**HandlerInterceptor**方法，并且重写**preHandle**方法即可

```java
public class CommonInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Users userInfo = (Users)request.getSession().getAttribute("userInfo");
        if (userInfo  null) {
            response.sendRedirect("/admin/login/index");
            return false;
        }
        return true;
    }
}
```



#### SpringMVC拦截器配置：	

- 如果有多个拦截器，则配置拦截器规则

- 实现**WebMvcConfigurer**接口，并重写里面的**addInterceptors**方法即可

- 添加**拦截器**的时候，记得要注意自己定义一个**Bean**出来

- ***假如一个拦截器没有配置addPathPatterns，它会默认拦截所有***   > **这点要特别注意**


```java
public class InterceptorConfig  implements WebMvcConfigurer {

    @Bean
    public CommonInterceptor commonInterceptor() {
        return new CommonInterceptor();
    }

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }

    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor();
    };

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor())
//                .addPathPatterns("/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/fonts/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/open/**")
                .excludePathPatterns("/error/**")
                .excludePathPatterns("/util/get/**")
                .excludePathPatterns("/admin/login/**");

        registry.addInterceptor(permissionInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login/**");

        registry.addInterceptor(userInterceptor())
                .addPathPatterns("/open/**")
                .excludePathPatterns("/open/permission/token")
                .excludePathPatterns("/open/dept/**")
                .excludePathPatterns("/open/user/**");
    }
}
```

- **registry注册会按照注册顺序来进行**
- **addPathPatterns** 配置拦截规则
- **excludePathPatterns** 配置排除规则



#### SpringBoot多数据源：

- **有时候数据库不在同一个服务器时，要进行分库分表，这时候怎么连接**
- 首先我们要建立配置文件

```java
@Configuration
@MapperScan(basePackages = DataSourcePrimaryConf.PACKAGE, sqlSessionFactoryRef = "primarySqlSessionFactory")
public class DataSourcePrimaryConf {

	private static final String PACKAGE = "com.example.mapper.primary";
    private static final String MAPPER_LOCATION = 			        "classpath:mybatis/biz/mapper/*.xml";

	@Bean(name = "dataSourceOne")
	@ConfigurationProperties(prefix = "spring.datasource.source1")
	@Primary //设置主数据源
	public DataSource DataSourceOne(){
		DataSource dataSource = new DruidDataSource();
		return dataSource;
	}

	@Primary
	@Bean(name = "primarySqlSessionFactory")
	public SqlSessionFactory primarySqlSessionFactory(
        @Qualifier("dataSourceOne") DataSource  dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
        // 这里设置mapper的位置
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
		return sessionFactory.getObject();
	}
}
```



- 配置文件有两个**Bean**需要配置
    - 第一个是**DataSource**，配置你使用哪个数据库连接池，可以选择**druid**也可以选择**HikariDataSource**，并且注意头上面的三个注释
    - 第二个是**SqlSessionFactory**，他配置数据源，并且返回Sql工厂即可
- 注意配置文件上面的一个**注解**，**MapperScan**。
    - **basePackages **表明需要扫描哪一个接口
    - **sqlSessionFactoryRef **表明Sql工厂指向哪一个
- **其他配置文件类似即可**
- **properties**文件

```java
pring.datasource.source1.url=jdbc:mysql://120.78.159.149:3306/homework?serverTimezone=GMT&useSSL=false
spring.datasource.source1.username=root
spring.datasource.source1.password=123456
spring.datasource.source1.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.source2.url=jdbc:mysql://120.78.159.149:3306/test?serverTimezone=GMT&useSSL=false
spring.datasource.source2.username=root
spring.datasource.source2.password=123456
spring.datasource.source2.driver-class-name=com.mysql.jdbc.Driver
```



#### Mabatis逆向工程：

> Mybatis逆向工程，简单来说就是，根据table表来生成 **Entity, DAO, Mapper.xml 文件**。使你不变编写SQL语句，不用配置XML文件，就可以做到简单的**增删改查**功能，是一个非常方便的Mybatis插件。
>
> [Mybatis插件 -> 官方文档](http://www.mybatis.org/generator/index.html)



**例子：**

- application.properties文件

```properties
spring.datasource.url=jdbc:mysql://120.78.159.149:3306/permission_center
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

mybatis.generate.java.target=src/main/java
mybatis.generate.resource.target=src/main/resources
```



- generatorConfig.xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>

    <!-- 引入配置文件 -->
    <properties resource="application.properties"/>

    <!--数据库驱动-->
    <classPathEntry location="C:\Users\Chang\.m2\repository\mysql\mysql-connector-java\8.0.12\mysql-connector-java-8.0.12.jar"/>

    <context id="context" targetRuntime="MyBatis3">

        <!-- 格式化 -->
        <property name="xmlFormatter" value="org.mybatis.generator.api.dom.DefaultXmlFormatter"/>
        <property name="javaFormatter" value="org.mybatis.generator.api.dom.DefaultJavaFormatter"/>

        <!--注释-->
        <commentGenerator>
            <property name="suppressDate" value="true"/>
            <property name="suppressAllComments" value="false"/>
            <property name="addRemarkComments" value="true"/>
        </commentGenerator>

        <jdbcConnection driverClass="${spring.datasource.driver-class-name}"
                        connectionURL="${spring.datasource.url}"
                        userId="${spring.datasource.username}"
                        password="${spring.datasource.password}">
        </jdbcConnection>

        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- 注意，Entity--XML--DAO， 这三者顺序不能变化，否则插件会报错 -->
        
        <!--生成Entity类存放位置-->
        <javaModelGenerator targetPackage="com.test.mybatis.demo.entity" targetProject="${mybatis.generate.java.target}">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <!--生成映射文件XML存放位置-->
        <sqlMapGenerator targetPackage="mybatis.mapper" targetProject="${mybatis.generate.resource.target}">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <!--生成Dao类存放位置-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.test.mybatis.demo.dao.mapper" targetProject="${mybatis.generate.java.target}">
            <property name="enableSubPackages" value="true"/>
            
            <!-- 这里一定要加入，否则插入方法用不了 -->
            <property name="rootInterface" value="BaseMapper"/>
        </javaClientGenerator>

        <!--生成对应表及类名-->
        <table tableName="permission" domainObjectName="Permission"></table>
        <table tableName="role_permission_rel" domainObjectName="RolePermissionRel"></table>

    </context>
</generatorConfiguration>
```



- POM文件

```xml
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.7</version>
</plugin>
```



- **BaseMapper文件  >  用于进行插入和选择性插入**   这个有点神奇欸，看情况而定

```java
public interface BaseMapper<T> {

	int insert(T t);

	int insertSelective(T t);
}
```



- **分页 重中之重**

    - 第一步：在**xxxExample** 中，加入两个参数，并设置getter && setter  **一定要给一个默认值**，否则会报错

    ```java
    protected Integer offset = 0;
    protected Integer limit = Integer.MAX_VALUE;
    
    public int getOffset() {
        return offset;
    }
    
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
    ```

    - 第二步：在**xxxMapper.xml** 中，配置以下内容

    ```xml
     <if test="offset != null and limit != null">
         limit ${offset},${limit}
    </if>
    ```

    - 第三部：使用，在example里面直接使用即可

    ```java
    @Override
    public List<Department> listPart(int offset, int limit) {
    
        DepartmentExample example = new DepartmentExample();
        example.setOffset(offset);
        example.setLimit(limit);
        return departmentMapper.selectByExample(example);
    }
    ```



- 以下几点小 **tips: 重要**

```properties
# 如果你想debug看到sql语句，那么加入下面这一句配置
logging.level.com.test.mybatis.demo.dao.mapper=debug

# SpringBoot 包扫描
@MapperScan(value = DemoApplication.PACKAGE)
public static final String PACKAGE = "com.test.mybatis.demo.dao.mapper";

priperties加上xml扫描  mybatis.mapper-locations=classpath:mybatis/biz/mapper/*.xml

# 自动生成的Mapper，最好加上@Repository注解，否则IDEA飘红，但不影响使用，但真的很难看

# 自己写的DAO，包括Impl 一定要加上@Repository注解，否则报错，影响使用
```





#### 防重放攻击：

> 我们在设计接口的时候，最怕一个接口被用户截取用于重放攻击。重放攻击是什么呢？**就是把你的请求原封不动地再发送一次，两次...n次**，一般正常的请求都会通过验证进入到正常逻辑中，如果这个正常逻辑是插入数据库操作，那么一旦插入数据库的语句写的不好，就有可能出现多条重复的数据。



**解决办法：**

- 前端传过了一个**timestamp**，如果这个timestamp超过当前服务器60s，就认为它重复请求了

```java
long now = System.currentTimeMillis();
// NONCE_DURATION 可以设置为30s，60s都ok的
if (now - timestamp > CacheService.NONCE_DURATION) {
    log.debug("token 校验失败：timestamp已过期");
    return false;
}
```

- 如果60s内重复攻击呢？这时候可以传一个60s内不唯一的**noncekey**
- 通过**noncekey**来判断是否是重新请求

```java
if (cacheService.getNonceKey(split[1])) {
    log.debug("token 校验失败：防重放key已存在");
    return null;
}
```

- **token** 生成规则，防止API接口滥用

```java
/**
 * token 生成规则：Base64[MD5(salt + args + timestamp) + noncekey]
 * salt值可以跟前端商量指定固定值
 */
@Override
public String buildToken(String clearKey, long timestamp, String nonceKey) {
    String clearKeyWithDefault = concat(getSecretKey(),clearKey, timestamp + "");
    String md5 = MD5Util.crypt(clearKeyWithDefault);
    byte[] encode = Base64.getEncoder().encode(concat(md5, nonceKey).getBytes());
    return new String(encode);
}
```

- **token验证**

```java
/**
 * 解密 验证
 * @param secretKey
 * @param timestamp
 * @param clearKeys
 * @return
 */
@Override
public boolean extractSecret(String secretKey, long timestamp, String ...clearKeys) {
    long now = System.currentTimeMillis();
    if (now - timestamp > CacheService.NONCE_DURATION) {
        log.debug("token 校验失败：timestamp已过期");
        return false;
    }
    String nonceKey = extractNonceKey(secretKey);
    if (nonceKey  null) {
        return false;
    }
    String dataKey = getSecretKey();
    if (clearKeys != null) {
        dataKey += "_" + concat(concat(clearKeys),String.valueOf(timestamp));
    } else {
        dataKey += "_" + String.valueOf(timestamp);
    }

    String secretKeyFromCal = MD5Util.crypt(dataKey);
    byte[] encode = Base64.getEncoder().encode(concat(secretKeyFromCal, nonceKey).getBytes());
    String targetKey = new String(encode);
    return secretKey.equals(targetKey);
}
```



### Item-Center系统：



#### Java泛型：

> 在Service中，我们经常到使用到RedisTemplate。
>
> 比如我们要使用Person，我们就定义   **private RedisTemplate<String， Person> redisTemplate;**
>
> 又比如我们要使用Person，我们就定义   **private RedisTemplate<String， User> redisTemplate;**
>
> 但如果有好多好多要使用Redis呢？我们有时会定义 **RedisTemplate<Object， Object>**
>
> 这样其实是非常的不友好的(要进行强转)，所以此时我们使用到了泛型。
>
> 先举一个例，以Redis为例：



```java
@Configuration
public class RedisConfig {
	
    @Bean(name = "redisTemplate")
	public <K, V> RedisTemplate<K, V> redisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<K, V> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
}
```

```java
@Service
public interface RedisService<K, V> {

	V get(K key);

	void put(K key, V value);
}
```

```java
@Service
public class RedisServiceImpl<K, V> implements RedisService<K, V> {

    // 如果不加(required = false)这个，会报错，但运行通过没啥问题
	@Autowired(required = false)
	private RedisTemplate<K, V> redisTemplate;

	@Override
	public V get(K key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void put(K key, V value) {
		redisTemplate.opsForValue().set(key, value);
	}
}
```



- **泛型方法**

```java
/**
 * 泛型方法的基本介绍
 * 说明：
 *     1）public 与 返回值中间<T>非常重要，可以理解为声明此方法为泛型方法。
 *     2）只有声明了<T>的方法才是泛型方法，泛型类中的使用了泛型的成员方法并不是泛型方法。
 *     3）<T>表明该方法将使用泛型类型T，此时才可以在方法中使用泛型类型T。
 *     4）与泛型类的定义一样，此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛		  型。
 */
public <K, V> RedisTemplate<K, V> redisTemplate(
    RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<K, V> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
}

/**
 * 泛型方法的基本介绍
 * 说明：
 *     1）如果类上面加了泛型，那么方法前，也就是 
 *     2）public 与 返回值中间<T> 就可以不用加了，他会自动进行一个识别
 */
public class RedisServiceImpl<K, V> {

	@Override
	public V get(K key) {
		return redisTemplate.opsForValue().get(key);
	}
}
```



#### Application监听器：

> 随着应用程序启动，application事件会以如下顺序触发：
>
>  1：ApplicationStartingEvent，在程序启动的时候，除处理注册listeners和initializers之前。
>
>  2：ApplicationEnvironmentPreparedEvent，在context中档Environment被使用的时候并在context被创建之前。
>
>  3：ApplicationPreparedEvent，在bean被加载之后刷新之前触发；
>
>  4：ApplicationStaredEvent，在application和command-line调用之后context刷新之前。
>
>  **5：ApplicationReadyEvent，与4相反，此时应用以具备服务的能力，可以处理请求。 **常用
>
>  6：ApplicationFailedEvent，在启动过程中如果发生异常则触发。



![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_%E4%BC%81%E4%B8%9A%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_15440023553209.png)



- 如果我们想在SpringBoot一启动，就加载某些类，或者放入cache中，此时哦我们就可以用到监听器


```java
public class AttributeValueInitializer implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 这里写初始化代码
        init();
    }
}
```



- **结果**

```properties
2018-12-05 17:47:22.916  INFO 1464 --- [           main] com.test.mybatis.demo.DemoApplication    : Started DemoApplication in 5.544 seconds (JVM running for 6.776)   >  这里已经启动成功了 然后打印StaredEvent和ReadyEvent
ApplicationStaredEvent
ApplicationReadyEvent
```



#### Crontab表达式：

```java
// 时  日  月  周

// 每十秒执行一次
@Scheduled(cron = "0/10 * * * * ?")

// 在所有为30s的时候执行一次  比如2:30:30  2:31:30  2:32:30
@Scheduled(cron = "30 * * * * ?")

// 15:35:05秒执行一次的
@Scheduled(cron = "5 35 15 * * ?")

```



#### 全局异常拦截器ExceptionController：

```java
@ControllerAdvice
public class ExceptionController {

	@ResponseBody
	@ExceptionHandler
	public BizResult<Boolean> handleFileError(Throwable throwable) {

		String s = throwable.toString();
		BizResult<Boolean> res = new BizResult<Boolean>().setFaild(s);
		return res;
	}
}

// 同样有些特定的异常  你不想try catch的时候，也可以用全局异常拦截器
// instanceof 运算符是用来在运行时指出对象是否是特定类的一个实例
public ResponseResult<String> handleFileError(Throwable throwable) {
   
    if (throwable instanceof IndexOutOfBoundsException) {
        // 如果越界  怎么怎么样
    }
    if (throwable instanceof ArithmeticException) {
        // 如果算数异常怎么怎么样
    }
    // .......
    
    // 返回没有被发现 处理的异常
    return responseResult;
}
```



- **throwable instanceof IndexOutOfBoundsException**  因为**Throwable**是所有异常的超类


![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_%E5%BC%82%E5%B8%B8.png)





> 全局异常拦截器，假设有些地方出现了异常，**比如 5/0，或者空指针** 异常。
>
> 一般情况下直接会抛出很长很长的错误，前端也是显示500错误，这样其实是非常的不友好的。
>
> 所以我们可以定义一个全局拦截器，只要出错了，但没有被拦截，都会走这个拦截器
>
> 这样显示给前端是非常的友好的。
>
> ps.  类上面记得标注 **@ControllerAdvice**， 方法上面标注 **@ExceptionHandler + @ResponseBody **



#### Dubbo

- 服务提供方

```properties
# 必须提供字段

# 服务器名称
# 提供方应用信息，用于计算依赖关系
dubbo.application.name=provider

# 注册中心地址
# 使用zookeeper协议
dubbo.registry.address=zookeeper://120.78.159.149:2181
dubbo.registry.protocol=zookeeper

# 通信协议(协议或者端口)
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880

// 提供方使用
// 在实现类上面 直接加上  @Service(interfaceClass = ItemStatusService.class) 注解即可
// 注解 service 这个是dubbo的注解，不是spring的注解
```

- 服务消费方

```properties
# 必须提供的字段

# 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样
dubbo.application.name=consumer

# 使用zookeeper广播注册中心暴露发现服务地址
dubbo.registry.address=zookeeper://120.78.159.149:2181

// 消费方使用
// 在controller 类上面 直接加上  

	@Reference
	CalService calService;

// 直接调用即可
```



> ps.  一定记得在启动类加上 **@EnableDubbo**  注解
>
> 提供方的service文档： http://dubbo.apache.org/zh-cn/docs/user/references/xml/dubbo-service.html
>
> 消费者的reference文档：http://dubbo.apache.org/zh-cn/docs/user/references/xml/dubbo-reference.html



- 属性配置顺序

    > 以 timeout 为例，显示了配置的查找顺序，其它 retries, loadbalance, actives 等类似：
    >
    > - 方法级优先，接口级次之，全局配置再次之。
    > - 如果级别一样，则消费方优先，提供方次之。
    >
    > 其中，服务提供方配置，通过 URL 经由注册中心传递给消费方。



    ![dubbo-config-override](http://dubbo.apache.org/docs/zh-cn/user/sources/images/dubbo-config-override.jpg)











#### ZooKeeper

> ZooKeeper是什么？
> ZooKeeper是一个分布式的，开放源码的分布式应用程序协调服务，是Google的Chubby一个开源的实现，它是集群的管理者，监视着集群中各个节点的状态根据节点提交的反馈进行下一步合理操作。最终，将简单易用的接口和性能高效、功能稳定的系统提供给用户
>
> 简单来说：Zookeeper是一个注册中心，服务提供者将服务注册到Zookeeper中去，然后服务消费者去Zookeeper中拿结果即可。



```bash
# 启动ZooKeeper服务器
$ bin/zkServer.sh start

# 启动ZooKeeper客户端
$ bin/zkCli.sh

# 停止ZooKeeper服务器
$ bin/zkServer.sh stop

# 查看所有文件 很像linux的命令
ls /
ls /dubbo/com.fordeal.item.rpc.service.ItemStatusService/providers

# 删除  
## 删除只没有子节点的节点
delete /dubbo

## 删除节点，递归删除所有
rmr /dubbo

# 获取详细信息
get /dubbo

# 返回结果
# null
# cZxid = 0x23a
# ctime = Fri Dec 14 19:56:37 CST 2018
# mZxid = 0x23a
# mtime = Fri Dec 14 19:56:37 CST 2018
# pZxid = 0x23f
# cversion = 2
# dataVersion = 0
# aclVersion = 0
# ephemeralOwner = 0x0
# dataLength = 0
# numChildren = 2

```









### 重构代码规范：

#### 代码的坏味道：

- **当你感觉需要攥写注释的时候，请先尝试重构，试着让注释变得多余。**

- 重复代码(**Duplicated** **Code**)

> 同一个类的两个函数有相同的表达式
>
> 两个互为兄弟的子类内含相同的表达式  **>**  提解到超类中去
>
> 两个毫不相关的类出现重复代码  **>**  放到第三个类，用一个静态方法，或者放入其中一个类，另一个类调用它即可，看具体详情



- 过长的函数(**Long Method**)

> **每当感觉需要以注释来说明点什么的时候，我们就把需要说明的东西写进一个独立函数中，并以其用途(而非实现手法)来命名。**
>
> 如果函数内有大量的参数和临时变量，他们会对你的函数提炼形成阻碍。传递临时变量给新函数，导致可读性是非常的差的。此时你可以 **把临时变量放到子函数中去**，最终需要的变量，return回来即可
>
> 条件表达式和循环也是提炼的信号。 都可以提炼到一个独立函数中，并且以**良好的命名**来说明用途



- 过大的类 (**Large Class**)

> 如果单个类做的事情太多，类内有太多的代码，也是代码重复，混乱并最终走向死亡的源头。
>
> 按照职责拆分类。比如，先确定客户端如何使用他们，然后清除了解这个类，在进行分解



- 过长的参数列 (**Long Parameter List**)

> 太长的参数会造成前后不一致，不易使用，而且难以理解
>
> 此时，你需要把过长的参数封装到一个对象中去，这样就十分的方便了
>
> 此对象一般叫做VO，或者xxxxRequest 看个人



#### 重新组织函数：

- 提炼函数 (**Extract Method**)

**旧代码：**

```java
void print() {
    List<Integer> list = getInfo();
    int res = 0;

    System.out.println("*************");
    System.out.println("*****TEST****");
    System.out.println("*************");

    for (int x : list) {
        res += x;
    }

    System.out.println(res);
}
```



**重构后代码：**

```java
void print() {
    // 无参数提炼
    printTest();
    // 有参数提炼，传参或者返回值等
    int res = getRes();
    System.out.println(res);
}

private void printTest() {
    System.out.println("*************");
    System.out.println("*****TEST****");
    System.out.println("*************");
}

private int getRes() {
    List<Integer> list = getInfo();
    int res = 0;
    for (int x : list) {
        res += x;
    }
    return res;
}
```



> 我们可以看到，重构后只有两个函数，非常的清晰。
>
> **printTest();**
>
> **int res = getRes();**
>
> **但对于有临时变量的，这个是特别的注意。传参，还有返回值，这十分的谨慎琢磨。**



- 引用解释性变量 (**Introduce Explaining Variable**)

> 你可以用这项重构将每个条件子句提炼出来，以一个良好的命名的临时变量来解释对应条件子句的意义
>
> **总感觉可用可不用**



- 移除对参数的赋值 (**Remove Assignments to Parameters**)

> 代码对一个参数(传进一个函数的参数)赋值，是非常不友好的做法。
>
> 所以请以一个临时变量取代该参数的位置



```java
// 旧代码
int discount(int inputVal, int quantity) {
    if (inputVal > 50) {
        inputVal += 20;
    }
    return inputVal;
}

// 新代码
int discount(int inputVal, int quantity) {
    int res = inputVal;
    if (inputVal > 50) {
        res += 20;
    }
    return res;
}
```



- 以函数对象取代函数 **(Replace Method with Method Object**)

> 如果一个函数之中局部变量泛滥成灾，那么想分解这个函数是非常困难的，有时你会发现根本无法拆解一个需要拆解的函数。这种情况，你就需要**函数对象**这件法宝了。
>
> 将这个函数变为对象，函数的局部变量变为函数对象的字段。在新类里面建立一个 **compute** 的函数，然后使用分解函数进行重构即可



**下面举一个很长很长的例子，OA系统中的例子，已经运用在实际了**

**原函数：**

```java
private String getDepartmentRecur(List<DeparmentNameVO> deparmentName) {

    // 获取所有部门信息，放到map中去
    List<DeparmentNameVO> deparments = CacheUtils.departmentCache.getIfPresent("deparments");
    if (CollectionUtils.isEmpty(deparments)) {
        deparments = roleList.getDeptAllInfo();
        CacheUtils.departmentCache.put("deparments", deparments);
    }
    Map<Integer, String> map1 = new HashMap<>();
    Map<Integer, Integer> map2 = new HashMap<>();
    deparments.forEach((x) -> map1.put(x.getId(), x.getDeptName()));
    deparments.forEach((x) -> map2.put(x.getId(), x.getParentId()));

    // 利用stack构造部门递归结构
    StringBuilder sb = new StringBuilder("");
    Stack<String> stack = new Stack<>();
    deparmentName.forEach((x) -> {
        int parentId = x.getParentId();
        int deptId = x.getId();
        while (parentId != 0) {
            stack.push(map1.get(deptId)  null ? "0" : map1.get(deptId));
            deptId = parentId;
            parentId = map2.get(deptId)  null ? 0 : map2.get(deptId);
        }
        stack.add("哆啦科技");
        while (!stack.isEmpty()) {
            if (stack.size()  1) {
                sb.append(stack.pop());
            } else {
                sb.append(stack.pop() + "/");
            }
        }
        sb.append("<br>");
    });
    return sb.toString();
}
```



**说实话，这已经是我重构后的函数了，剥离出来并且重用了，一个private方法。**

**但是这个方法还是太长了，又臭又长，极其的难以维护。但我每次想抽离函数，有感觉很复杂，不止从何下手，这时，可以将其剥离为一个对象**

**重构后代码：**

```java
public class DepartmentRecur {

	@Autowired
	RoleListDao roleList;

	List<DeparmentNameVO> departmentName = null;
	Map<Integer, String> map1 = new HashMap<>();
	Map<Integer, Integer> map2 = new HashMap<>();

	public DepartmentRecur(List<DeparmentNameVO> departmentName) {
		this.departmentName = departmentName;
	}

	public String compute() {
		// 获取所有部门信息，放到map中去
		List<DeparmentNameVO> departments = getDepartmentInfo();
		propertyToMap(departments);

		// 利用stack构造部门递归结构
		String deptNameRecur = getRecurDept();
		return deptNameRecur;
	}

	private String getRecurDept() {
		StringBuilder sb = new StringBuilder("");
		Stack<String> stack = new Stack<>();
		departmentName.forEach((x) -> {
			int parentId = x.getParentId();
			int deptId = x.getId();
			while (parentId != 0) {
				stack.push(map1.get(deptId)  null ? "0" : map1.get(deptId));
				deptId = parentId;
				parentId = map2.get(deptId)  null ? 0 : map2.get(deptId);
			}
			stack.add("哆啦科技");
			while (!stack.isEmpty()) {
				if (stack.size()  1) {
					sb.append(stack.pop());
				} else {
					sb.append(stack.pop() + "/");
				}
			}
			sb.append("<br>");
		});
		return sb.toString();
	}

	private List<DeparmentNameVO> getDepartmentInfo() {
		List<DeparmentNameVO> departments = CacheUtils.departmentCache.getIfPresent("deparments");
		if (CollectionUtils.isEmpty(departments)) {
			departments = roleList.getDeptAllInfo();
			CacheUtils.departmentCache.put("departments", departments);
		}
		return departments;
	}

	private void propertyToMap(List<DeparmentNameVO> departments) {
		departments.forEach((x) -> map1.put(x.getId(), x.getDeptName()));
		departments.forEach((x) -> map2.put(x.getId(), x.getParentId()));
	}
}
```



我们可以看到，**代码长度变为了原来的一倍多。但他的结构十分的清晰。**

我们看**comput**这个函数，首先获取部门，在放到map中，在递归获取部门名字。每一个都是很小很小的函数，假设我命名十分的好的话，那么别人一看就知道在干嘛啦。

调用也十分的简单



#### 重新组织数据：

- 以对象取代数据值 （**Replace Data Value with Object**）

> 比如：一开始你可能会用一个字符串来表示 “电话号码” 的概念，但是随后你就会发现，电话号码需要 “格式化”， “抽取区号” 之类的特殊行为。
>
> 对这个 “电话号码” 你有很多的操作，这是非常不友好的。
>
> 所以必要时，将 String phone  >  Phone phone  一个对象



- 





### 第三方Jar包学习：

#### Apache-Commons学习：

##### StringUtils:

- **isEmpty()**    判断一个字符串是否为空或者null

```java
public static boolean isEmpty(final CharSequence cs) {
	return cs  null || cs.length()  0;
}

// 双胞胎
public static boolean isNotEmpty(final CharSequence cs) {
	return !isEmpty(cs);
}
```



- **join**  连接字符串数组

```java
// 源码  所有join都是重载了此方法
public static String join(final Object[] array, String separator, final int startIndex, final int endIndex) {
    if (array  null) {
        return null;
    }
    if (separator  null) {
        separator = EMPTY;
    }

    final int noOfItems = endIndex - startIndex;
    if (noOfItems <= 0) {
        return EMPTY;
    }

    final StringBuilder buf = newStringBuilder(noOfItems);

    for (int i = startIndex; i < endIndex; i++) {
        if (i > startIndex) {
            buf.append(separator);
        }
        if (array[i] != null) {
            buf.append(array[i]);
        }
    }
    return buf.toString();
}


// 例子
public static void main(String[] args) {
		
    String[] s = {"a", "bbb", "ccc", "dddd"};
    String res = StringUtils.join(s);
    System.out.println(res);
    res = StringUtils.join(s, "_");
    System.out.println(res);
}

// abbbcccdddd
// a_bbb_ccc_dddd
```



- **contains** 判断是否包含此字符串

```java
public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
    if (seq  null || searchSeq  null) {
        return false;
    }
    return CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0;
}
```



- **substring**  抽取子字符串，支持python的负数

```java
public static String substring(final String str, int start) {
    if (str  null) {
        return null;
    }

    // handle negatives, which means last n characters
    if (start < 0) {
        start = str.length() + start; // remember start is negative
    }

    if (start < 0) {
        start = 0;
    }
    if (start > str.length()) {
        return EMPTY;
    }

    return str.substring(start);
}

/**
  * StringUtils.substring(null, *)   = null
  * StringUtils.substring("", *)     = ""
  * StringUtils.substring("abc", 0)  = "abc"
  * StringUtils.substring("abc", 2)  = "c"
  * StringUtils.substring("abc", 4)  = ""
  * StringUtils.substring("abc", -2) = "bc"
  * StringUtils.substring("abc", -4) = "abc"
  */
```



- **isNumeric**  判断是否是数字

```java
public static boolean isNumeric(final CharSequence cs) {
    if (isEmpty(cs)) {
        return false;
    }
    final int sz = cs.length();
    for (int i = 0; i < sz; i++) {
        if (!Character.isDigit(cs.charAt(i))) {
            return false;
        }
    }
    return true;
}

/**
  * StringUtils.isNumeric(null)   = false
  * StringUtils.isNumeric("")     = false
  * StringUtils.isNumeric("  ")   = false
  * StringUtils.isNumeric("123")  = true
  * StringUtils.isNumeric("\u0967\u0968\u0969")  = true
  * StringUtils.isNumeric("12 3") = false
  * StringUtils.isNumeric("ab2c") = false
  * StringUtils.isNumeric("12-3") = false
  * StringUtils.isNumeric("12.3") = false
  * StringUtils.isNumeric("-123") = false
  * StringUtils.isNumeric("+123") = false
  */
```



#### Spring-util学习：

##### CollectionUtils：

- **isEmpty** 判断集合是否为空

```java
public static boolean isEmpty(@Nullable Collection<?> collection) {
    return (collection  null || collection.isEmpty());
}

// 重载了Map
public static boolean isEmpty(@Nullable Map<?, ?> map) {
    return (map  null || map.isEmpty());
}
```



#### Alibaba学习：

##### FastJson API:

```java
// 将JavaBean序列化为JSON文本
public static String toJSONString(Object object) {
    return toJSONString(object, emptyFilters);
}

// 把JSON文本parse为JavaBean 
public static <T> T parseObject(String text, Class<T> clazz) {
    return parseObject(text, clazz, new Feature[0]);
}
```



- **JavaBeanToJson:**

```java
Person person = new Person();
person.setName("小明");
person.setAge(18);
person.setParent(Lists.newArrayList("张三", "李四"));

System.out.println(person);
System.out.println(JSON.toJSONString(person));

// 结果
Person(name=小明, age=18, parent=[张三, 李四])
{"age":18,"name":"小明","parent":["张三","李四"]}
```

- **JsonToJavaBean**

```java
String json = "{\"age\":18,\"name\":\"小明\",\"parent\":[\"张三\",\"李四\"]}";
Person person = JSON.parseObject(json, Person.class);
System.out.println(person);

// 结果
Person(name=小明, age=18, parent=[张三, 李四])
```



#### EhCache学习：



> Ehcache是一个开源的基于标准的缓存，是一个纯java的在进程中的缓存，可提高性能，卸载数据库并简化可伸缩性。它是使用最广泛的基于Java的高速缓存，因为它非常强大，经过验证，功能全面，并且与其他流行的库和框架集成在一起。Ehcache从进程内缓存扩展到混合进程内/进程外部署与TB级缓存。
>
> 最主要的是，他**支持磁盘缓存**。并且与**SpringBoot能很好的结合**，只需要几个注解即可。



##### 注解：

- **@Cacheable(value = "itemCache", key = "#id")**
    - value 表示缓存的名字，key表示该应用的缓存，支持spel
    - 在方法执行前Spring先是否有缓存数据
    - **如果有直接返回**。
    - **如果没有数据，调用方法并将方法返回值存放在缓存当中**。

- @CachePut
    - 相当于Put，无论如何，一定会去数据库查内容，并且将结果放到缓存中
- @CacheEvict   
    - 将一条或者多条数据从缓存中删除。



##### POM文件：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>2.10.6</version>
</dependency>
```



##### ehcache.xml文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache updateCheck="false" dynamicConfig="false">

    <diskStore path="java.io.tmpdir"/>

    <!-- 店铺关注数量缓存 5s有效 因为店铺数量不多所以不限制大小 -->
    <cache name="followNumCache"
           timeToLiveSeconds="5"
           maxElementsInMemory="0"
           eternal="false"
           memoryStoreEvictionPolicy="LRU"
    />

    <!-- 商品缓存 10分钟有效 60s没访问失效 最大限制10000条 -->
    <cache name="itemCache"
           timeToLiveSeconds="600"
           maxElementsInMemory="10000"
           timeToIdleSeconds="60"
           overflowToDisk="false"
           eternal="false"
           memoryStoreEvictionPolicy="LRU"
    />

</ehcache>
```



- **diskStore**  为缓存磁盘路径，path="java.io.tmpdir" 表示默认路径  **固定写法**
- **name**  缓存名称,cache的唯一标识
- **timeToLiveSeconds** 缓存数据在失效前的允许存活时间 单位秒s
- **maxElementsInMemory**  内存缓存中最多可以存放的元素数量, 若放入Cache中的元素超过这个数值,则有以下两种情况
    - 若overflowToDisk=true,则会将Cache中多出的元素放入磁盘文件中
    - 若overflowToDisk=false,则根据memoryStoreEvictionPolicy策略替换Cache中原有的元素 
- **timeToIdleSeconds**  缓存数据在失效前的允许闲置时间，没有访问时，多久失效
- **memoryStoreEvictionPolicy**  内存存储与释放策 
    - LRU(Least Recently Used 最近最少使用)
    - LFU(Less Frequently Used最不常用的)
    - FIFO(first in first out先进先出)
- **eternal**  **缓存中对象是否永久有效,即是否永驻内存**



##### 应用：

```java
@SpringBootApplication
@EnableCaching
public class ItemsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemsApiApplication.class, args);
    }
}
```

- 在主程序加上 **@EnableCaching** 注解即可
- 使用的时候加上相应的注解即可   比如：**@Cacheable**

```java
@Override
@Cacheable(value = "itemCache", key = "#id")
public Items getOne(Integer id) {
    log.info("商品{}缓存未命中，将走DB查找", id);
    ItemsExample example = new ItemsExample();
    example.createCriteria().andIdEqualTo(id);
    List<Items> items = itemsMapper.selectByExample(example);
    return CollectionUtils.isNotEmpty(items) ? items.get(0) : null;
}
```





#### google-guava学习：

##### Cache学习：

- **推荐博客**   [**Guava Cache用法介绍**](https://segmentfault.com/a/1190000011105644)

- **Cache接口**  => **可满足大部分需求**

```java
public interface Cache<K, V> {
    
    // 有key返回key，没有key，返回null
    @Nullable
    V getIfPresent(@CompatibleWith("K") Object key);
    
    // 没有key，会执行callable里面的函数
    V get(K key, Callable<? extends V> valueLoader) throws ExecutionException;

    void put(K key, V value);

    long size();
    
    /** Discards any cached value for key {@code key}. */
    void invalidate(@CompatibleWith("K") Object key);
    
    /** Discards all entries in the cache. */
    void invalidateAll();
}
```



**例子**：

```java
// 过期60s，最大放入量为100
Cache<String, String> cache = CacheBuilder.newBuilder()
    .expireAfterWrite(60, TimeUnit.SECONDS)
    .maximumSize(100)
    .build();

for (int i = 0; i < 3; i++) {
    if (StringUtils.isEmpty(cache.getIfPresent("ok"))) {
        cache.put("ok", "ok");
        System.out.println("暂无缓存");
    } else {
        System.out.println(cache.getIfPresent("ok"));
    }
}

// 暂无缓存
// ok
// ok
```



- **LoadingCache**   LoadingCache是Cache的子接口, 加强了几个方法

```java
// get方法，如果get不存在，会自动加载数据到缓存中去
V get(K key) throws ExecutionException;
```



**例子：**

```java
LoadingCache<String, String> cache = CacheBuilder.newBuilder()
    .maximumSize(100).expireAfterWrite(60, TimeUnit.SECONDS)
    .build(new CacheLoader<String, String>() {
        public String load(String key) {
            return "key from cache";
        }
    });
System.out.println(cache.get("ok"));
cache.put("ok", "ok");
System.out.println(cache.get("ok"));

// key from cache
// ok
```



##### StopWatch学习：

- 用来计算一个函数执行 **花了多久的时间**

```java
Stopwatch stopwatch = Stopwatch.createStarted();
doSomething();
// 参数传一个 TimeUnit
long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
log.info("{} 执行时长： {}", desc, duration);
```



##### RateLimiter限流器的使用：

> ##### 为何要做限制
>
> 系统使用下游资源时，需要考虑下游资源所能提供资源能力。对于资源受限、处理能力不是很强的资源应当给予保护(在下游资源无法或者短时间内无法提升处理性能的情况下)。可以使用限流器或者类似保护机制，避免下游服务崩溃造成整体服务的不可用。
>
> ##### 令牌桶算法
>
> 如图所示，令牌桶算法的原理是系统会以一个恒定的速度往桶里放入令牌，而如果请求需要被处理，则需要先从桶里获取一个令牌，当桶里没有令牌可取时，则拒绝服务。
>
> ![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_1183650790-5b8520aa0966b_articlex.png)



```java
// LIMIT 相当于QPS  
private static final double LIMIT = 10;
private static RateLimiter rateLimiter = RateLimiter.create(LIMIT);
```

**注意：** LIMIT = 10，表示每秒往桶里面放10个令牌，**匀速**放置。也就是说，1000ms放10个，100ms放1个。

如果100ms请求10次，只有一次会通过，其他九次都会被拒绝。就算后面900ms没有请求。

匀速放置，注意理解这一个词汇。**匀速**



- **例子一：**

```java
// LIMIT 相当于QPS
private static final double LIMIT = 5;
private static RateLimiter rateLimiter = RateLimiter.create(LIMIT);

public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 5; i++) {
        if (tryAcquire()) {
            System.out.println("调用成功");
        } else {
            System.out.println("访问太频繁");
        }
    }
}

private static Boolean tryAcquire() {
    return rateLimiter.tryAcquire();
}

调用成功
访问太频繁
访问太频繁
访问太频繁
访问太频繁
```

**分析：** 设置QPS = 5， 但因为匀速放置令牌，程序在前200ms就进行了访问，所以，只有第一个才可以拿到令牌，后面的都没有拿到，因为还没有到时间放置。

**也就是0ms放置一个令牌，200ms放置一个令牌，400ms放置一个令牌，600ms放置一个令牌，800ms放置一个令牌**，总共五个令牌



- **例子二：**

 **注意看代码第12行**

```java
// LIMIT 相当于QPS
private static final double LIMIT = 5;
private static RateLimiter rateLimiter = RateLimiter.create(LIMIT);

public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 5; i++) {
        if (tryAcquire()) {
            System.out.println("调用成功");
        } else {
            System.out.println("访问太频繁");
        }
        TimeUnit.MILLISECONDS.sleep(200);
    }
}

private static Boolean tryAcquire() {
    return rateLimiter.tryAcquire();
}
调用成功
调用成功
调用成功
调用成功
调用成功
```

**分析：** 因为做了200ms的睡眠，所以都可以调用成功。





#### Lombok的使用：

- @Data注解  
    - 空的构造器
    - getter && setter
    - equals hashcode toString等

![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_1543837338(1).jpg)



- @AllArgsConstructor注解  > 出现了全参构造器，但是！！！！无参构造器消失了

![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_1543837420(1).jpg)



- @NoArgsConstructor   >  无参构造器

![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_1543837471(1).jpg)



- 所以我们经常在Entity加上以下几个注解

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

	String name;

	int age;

	double price;
}
```



### 设计模式：

#### 责任链模式：

> **意图：**避免请求发送者与接收者耦合在一起，让多个对象都有可能接收请求，将这些对象连接成一条链，并且沿着这条链传递请求，直到有对象处理它为止。
>
> **主要解决：**职责链上的处理者负责处理请求，客户只需要将请求发送到职责链上即可，无须关心请求的处理细节和请求的传递，所以职责链将请求的发送者和请求的处理者解耦了。
>
> **何时使用：**在处理消息的时候以过滤很多道。
>
> **如何解决：**拦截的类都实现统一接口。
>
> **关键代码：**Handler 里面聚合它自己，在 HanleRequest 里判断是否合适，如果没达到条件则向下传递，向谁传递之前 set 进去。
>
> **应用实例：** 1、红楼梦中的"击鼓传花"。 2、JS 中的事件冒泡。 3、JAVA WEB 中 Apache Tomcat 对 Encoding 的处理，Struts2 的拦截器，jsp servlet 的 Filter。
>
> **优点：** 1、降低耦合度。它将请求的发送者和接收者解耦。 2、简化了对象。使得对象不需要知道链的结构。 3、增强给对象指派职责的灵活性。通过改变链内的成员或者调动它们的次序，允许动态地新增或者删除责任。 4、增加新的请求处理类很方便。
>
> **缺点：** 1、不能保证请求一定被接收。 2、系统性能将受到一定影响，而且在进行代码调试时不太方便，可能会造成循环调用。 3、可能不容易观察运行时的特征，有碍于除错。
>
> **使用场景：** 1、有多个对象可以处理同一个请求，具体哪个对象处理该请求由运行时刻自动确定。 2、在不明确指定接收者的情况下，向多个对象中的一个提交一个请求。 3、可动态指定一组对象处理请求。
>
> **注意事项：**在 JAVA WEB 中遇到很多应用。
>
> ps。也就是说：我有一个请求，这个请求进入一条链中，第一个请求能处理就不往后走，如果不能处理，就往后走即可。

**如下图：**

![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_%E4%B8%8B%E8%BD%BD.png)



**用法以及例子：**



![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_1543999467(1).jpg)



```java
/**
 * author: vicody
 * time: 2018/11/14 4:24 PM
 * use: 校验器，采用责任链模式，每一个validator都指向下一个，如果校验通过则执行next.handle，否则抛出异常
 * @see BasicParameterValidator
 **/
public interface Validator<T> {

    Validator<T> next();

    boolean handle(T t) throws FordealException;
}
```



```java
public class BasicParameterValidator implements Validator<ValuationWO> {

    @Autowired
    private Validator<ValuationWO> tokenValidator;

    @Override
    public Validator<ValuationWO> next() {
        return tokenValidator;
    }

    @Override
    public boolean handle(ValuationWO valuationWO) throws FordealException {
        if (valuationWO.getItemId()  null || valuationWO.getSkuId()  null || valuationWO.getOrderId()  null ||
                StringUtils.isEmpty(valuationWO.getToken()) || valuationWO.getTimestamp()  null) {
            log.error("valuation check. basic validator occur error, token|itemId|orderId|skuId|timestamp can not be empty.");
            throw new FordealException(ErrorCodeEnum.ARGUMENT_ERROR_CODE);
        }
        if (StringUtils.isNotEmpty(valuationWO.getContent()) && valuationWO.getContent().length() > 500) {
            log.error("valuation check. basic validator occur error, content is too long.");
            throw new FordealException(ErrorCodeEnum.ARGUMENT_ERROR_CODE);
        }
        return next().handle(valuationWO);
    }
}
```



```java
public class TokenValidator implements Validator<ValuationWO> {

    @Autowired
    private Validator<ValuationWO> paramAbstractValidator;
    @Autowired
    private SecretService secretService;

    @Override
    public Validator<ValuationWO> next() {
        return paramAbstractValidator;
    }

    @Override
    public boolean handle(ValuationWO valuationWO) throws FordealException {
        Long timestamp = valuationWO.getTimestamp();
        String token = valuationWO.getToken();
        String itemId = String.valueOf(valuationWO.getItemId());
        String orderId = String.valueOf(valuationWO.getOrderId());
        String skuId = String.valueOf(valuationWO.getSkuId());
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(itemId) || StringUtils.isEmpty(orderId)
                ||StringUtils.isEmpty(skuId) || timestamp  null) {
            log.error("token validator occur error, token|itemId|orderId|skuId|timestamp can not be empty.");
            throw new FordealException(ErrorCodeEnum.ARGUMENT_ERROR_CODE);
        }
        boolean res = secretService.extractSecret(token, timestamp, orderId, itemId, skuId);
        if (res) {
            return next().handle(valuationWO);
        }
        throw new FordealException(ErrorCodeEnum.PERMISSION_ERROR_CODE);
    }
}
```



#### 观察者模式：

> **意图：**定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。
>
> **主要解决：**一个对象状态改变给其他对象通知的问题，而且要考虑到易用和低耦合，保证高度的协作。
>
> **何时使用：**一个对象（目标对象）的状态发生改变，所有的依赖对象（观察者对象）都将得到通知，进行广播通知。
>
> **如何解决：**使用面向对象技术，可以将这种依赖关系弱化。
>
> **关键代码：**在抽象类里有一个 ArrayList 存放观察者们。
>
> **应用实例：** 1、拍卖的时候，拍卖师观察最高标价，然后通知给其他竞价者竞价。 2、西游记里面悟空请求菩萨降服红孩儿，菩萨洒了一地水招来一个老乌龟，这个乌龟就是观察者，他观察菩萨洒水这个动作。
>
> **使用场景：**
>
> - 一个抽象模型有两个方面，其中一个方面依赖于另一个方面。将这些方面封装在独立的对象中使它们可以各自独立地改变和复用。
> - 一个对象的改变将导致其他一个或多个对象也发生改变，而不知道具体有多少对象将发生改变，可以降低对象之间的耦合度。
> - 一个对象必须通知其他对象，而并不知道这些对象是谁。
> - 需要在系统中创建一个触发链，A对象的行为将影响B对象，B对象的行为将影响C对象……，可以使用观察者模式创建一种链式触发机制。
>
> **例子：**
>
> **有一个微信公众号服务，不定时发布一些消息，关注公众号就可以收到推送消息，取消关注就收不到推送消息。**
>
> 此时，微信公众号是服务提供者，张三李四王五这些属于观察者。



**观察者：**

```java
public interface Observer {
	void update(String message);
}

public class Users implements Observer {

	private String name;
	private String message;

	public Users(String name) {
		this.name = name;
	}

	@Override
	public void update(String message) {
		this.message = message;
		read();
	}

	public void read() {
		System.out.println(name + " 收到推送消息： " + message);
	}
}
```



**消息推送者：**

```java
public interface Wechat {

	void registerObserver(Observer o);

	void removeObserver(Observer o);

	void notifyObserver();
}

public class WechatServer implements Wechat {

    // 这个最为重要，保存着所有的 观察者，然后遍历即可
	private List<Observer> list = Lists.newArrayList();
	private String message = "";

	@Override
	public void registerObserver(Observer o) {
		list.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		if (!CollectionUtils.isEmpty(list)) {
			list.remove(o);
		}
	}

	@Override
	public void notifyObserver() {
		for (int i = 0; i < list.size(); i++) {
			Observer observer = list.get(i);
			observer.update(message);
		}
	}

	public void setInformation(String s) {
		this.message = s;
		System.out.println("微信服务更新消息： " + s);
		notifyObserver();
	}
}
```



**用例：**

```java
public class Main {
	public static void main(String[] args) {

        // 消息推送者
		WechatServer server = new WechatServer();

        // 观察者
		Observer zhangsan = new Users("ZhangSan");
		Observer lisi = new Users("LiSi");
		Observer wangwu = new Users("WangWu");

		server.registerObserver(zhangsan);
		server.registerObserver(lisi);
		server.registerObserver(wangwu);
		server.setInformation("PHP是世界上最好用的语言！");

		System.out.println("----------------------------------------------");
		server.removeObserver(zhangsan);
		server.setInformation("JAVA是世界上最好用的语言！");
	}
}

结果：
微信服务更新消息： PHP是世界上最好用的语言！
ZhangSan 收到推送消息： PHP是世界上最好用的语言！
LiSi 收到推送消息： PHP是世界上最好用的语言！
WangWu 收到推送消息： PHP是世界上最好用的语言！
----------------------------------------------
微信服务更新消息： JAVA是世界上最好用的语言！
LiSi 收到推送消息： JAVA是世界上最好用的语言！
WangWu 收到推送消息： JAVA是世界上最好用的语言！
```



#### 装饰者模式：

> **意图：**动态地给一个对象添加一些额外的职责。就增加功能来说，装饰器模式相比生成子类更为灵活。
>
> **主要解决：**一般的，我们为了扩展一个类经常使用继承方式实现，由于继承为类引入静态特征，并且随着扩展功能的增多，子类会很膨胀。
>
> **何时使用：**在不想增加很多子类的情况下扩展类。
>
> **如何解决：**将具体功能职责划分，同时继承装饰者模式。
>
> **关键代码：** 1、Component 类充当抽象角色，不应该具体实现。 2、修饰类引用和继承 Component 类，具体扩展类重写父类方法。



![img](http://images.cnblogs.com/cnblogs_com/wenbochang/1355057/o_decorator_pattern_uml_diagram.jpg)

- 类似这张图片。
- **Shape** 形状，有圆形，长方形。但有的时候，又要装饰一个红色，蓝色，绿色的形状
- 所以一个接口，或者抽象类继承**Shape**， 然后把这些所有装饰都实现下来
- 然后一层一层装饰你想要的形状即可



```java
// 形状
Shape circle = new Circle();
 
// 加了红色的形状
Shape redCircle = new RedShapeDecorator(new Circle());

// 加了红色的长方形
Shape redRectangle = new RedShapeDecorator(new Rectangle());
```































