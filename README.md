# 前言
- 此项目最初为毕业设计。由于想法很好，并发到了论坛，使得此小程序的用户量十分的不错。

- 每天的日活基本上千，因此想要把这个项目做的很好。所以我的毕生所学基本全部用到了这个项目中去了。

- 相信我，如果你能把此项目搞懂，在JAVA领域可以超过90%以上的人。

<div align=center><img width="800" height="400" src="https://raw.githubusercontent.com/wenbochang888/osc/test/src/main/resources/img/liuliang.jpg"/></div>

# 包结构讲解
- annotation注解
    - Retry。重试注解，比如访问HTTP接口超时，重试三次等等。具体实现在interceptor##RetryAspect。
    - TimeWatch。。计算方法执行时间注解。加上此注解，可以计算从访问到结束一个接口所花费的时间

- common
    - 一些请求的commonRequest还有一些常量等
    
- conf配置文件
    - datasource是主从sql的配置文件。大厂都会用到
    - AnnotationConfig，启动的注解
    - RedisConfig，存储redis的k,v所用的序列化。比如你要存<string, Person> person就需要序列化
    
- **controller请求入口**
    - CommentListController。开源中国动弹获取评论
    - TweetListController。开源中国动弹获取接口
    - **ZhiHuSpiderController。爬虫接口，此接口为小程序的入口，重要！！！**

- dao
    - mybatis的DAO

- init
    - 项目启动时，需要加载的配置。
    - CacheInitializer。每次项目启动都会重新刷新token，freshToken。可以参考OAuth2

- interceptor
    - 项目启动时，需要加载的配置。
    - CacheInitializer。每次项目启动都会重新刷新token，freshToken。可以参考OAuth2

- **interceptor拦截器**
    - AccessLimitInterceptor，限流的拦截器，Guava的RateLimiter
    - IPBlockInterceptor,ip拦截器
    - LogInterceptor，日志拦截器

- scheduled定时任务
    - CronTaskByFreshToken每天刷新token
    - **CronTaskBySpider，注意此爬虫。每天会定时的爬去知乎的图片**
    - CronTaskByStock，爬去股票，私人的需要

- service
    - 可以重点看下RedisHelper,ZhihuSpiderImpl,strategy等等。
    - 上面的很有帮助

- utils
    - 一些工具类，可以重点关注下。GsonUtils，WeChatNoticeUtils(这个时往微信发送消息)

# 小程序
## 《开源中国》动弹俱乐部
~~旧的项目已经不在维护。可参考controller下面的Tweet开头的内容~~

## 《知乎爬虫》
爬取知乎热门的提问的图片，放到小程序中。可参考**ZhuHuSpiderController**
<div align=center><img width="200" height="200" src="https://pic2.zhimg.com/50/v2-4c1f2eff30128ff7ce009dfc7778a3e3_r.jpg"/></div>

# 测试
深知测试的重要性，因此尽量做到代码90%以上的全覆盖。(10%有一些测试不到，比如异常，依赖其他服务等等)

## 业务层Serivice
<div align=center><img width="1000" height="500" src="https://raw.githubusercontent.com/wenbochang888/osc/test/src/main/resources/img/ImplTest1.png"/></div>
<br/>
<div align=center><img width="1000" height="500" src="https://raw.githubusercontent.com/wenbochang888/osc/test/src/main/resources/img/ImplTest2.png"/></div>