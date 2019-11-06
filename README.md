# 前言
此项目最初为毕业设计。由于想法很好，并发到了论坛，使得此小程序的用户量十分的不错。
每天的日活基本上千，因此想要把这个项目做的很好。所以我的毕生所学基本全部用到了这个项目中去了。

# 技术
- Springboot启动初始化参数。可参考init包下面

- SpringMVC拦截器。包含了三种拦截器：

  - 限流器。**AccessLimitInterceptor**
  
  - 防爬虫。**IPBlockInterceptor**
  
  - Token校验，放重放攻击。**TokenInterceptor**
  
- AOP + 注解。**TimeWatchAspect** 加上此注解，可以计算接口的访问时间

- 本地缓存。encache。用于缓存数据库。

- Redis。可参考**RedisHelperImpl**实现，运用了范型。同时可参考RedisConfig这个redis配置

- Mybatis逆向工程。可参考DAO包下面的方法。
- 微信通知。**WeChatNoticeUtils**

- 全局异常处理。**ExceptionController**

- 统一返回的参数。**OscResult**

- 定时任务。可参考schedule包下面的两个任务。

- 第三方包，properties多环境配置，环境变量等等。

# 小程序
## 《开源中国》动弹俱乐部
~~旧的项目已经不在维护。可参考controller下面的Tweet开头的内容~~

## 《知乎爬虫》
爬取知乎热门的提问的图片，放到小程序中。可参考**ZhuHuSpiderController**
<div align=center><img width="200" height="200" src="https://pic2.zhimg.com/50/v2-4c1f2eff30128ff7ce009dfc7778a3e3_r.jpg"/></div>