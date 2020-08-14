![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（8）——过滤器和监听器/7896890-45e64864a52fc3b8.png)

## 什么是过滤器
**过滤器就是 Servlet 的高级特性之一，**就是一个具有**拦截/过滤**功能的一个东西，在生活中过滤器可以是香烟滤嘴，滤纸，净水器，空气净化器等，在 Web 中仅仅是一个**实现了 Filter 接口的 Java 类**而已。

- **特点：双向，拦截请求，拦截响应**
![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（8）——过滤器和监听器/7896890-e6d2758438da6a6f.png)

- **作用：**
过滤器可以对**所有的请求或者响应做拦截操作**


#### 为什么在 Web 开发中需要用到过滤器？

- **问题：**为什么非得使用过滤器，我直接在 Servlet 中作判断不行吗？
- **开发遵循的原则：**
**1.DRY原则（Don't Reeat Yourself，不要重复你自己）**：重复，意味着维护的成本很高。
**2.责任分离原则：**谁擅长什么功能就做什么功能，Servlet 擅长的是逻辑而不是处理请求


#### 举一个实际的例子：（处理 POST 请求中文编码的问题）

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（8）——过滤器和监听器/7896890-b2d6af2506b2f548.png)

- **Web 中过滤器的作用：**
**1.可以在请求资源之前设置请求的编码**
**2.可以进行登录校验**
**3.可以进行请求参数的内容的过滤**
**4.数据压缩 / 数据加密 / 数据格式的转换**
5.可以设置浏览器相关的数据

#### Filter 的开发和使用

对应于 Servlet 的开发步骤：
1. 定义一个类，实现 Filter 接口
2. 在 **doFilter()** 方法中对**请求**和**响应**进行过滤
3. 在 web.xml 文件中进行 Filter 的配置（告诉服务器来管理当前的 Filter）
```
<!-- web.xml -->
<filter>
    <filter-name>filter的名称</filter-name>
    <filter-class>filter类的全限定名</filter-class>
</filter>
<filter-mapping>
    <filter-name>指定对哪一个filter做的映射</filter-name>
    <url-pattern>指定对哪些资源进行过滤</url-pattern>
</filter-mapping>
```
- **注意：**此时 `<url-pattern>` 表示对哪些资源做过滤/拦截。例如：
-  `/hello.jsp`
当前 Filter 就仅仅只对 hello.jsp 资源做拦截.
-  `/index` 
当前 Filter 就仅仅只对 /index 资源做拦截.
- `/* ` 
当前 Filter 就对**所有资源做拦截**.访问任意的资源,都会先进入该过滤器器.
- `/system/*`
当前 Filter 就对以 `/system/` 打头的资源做拦截.
如`/system ` , `/system/a`,  `/system/a/b/c`, `/systema`

#### Filter 映射细节
1. 在启动服务器的时候，就创建了 Filter 对象并执行了初始化方法 init()。**Filter 先于 Servlet 存在于服务端**
2. 在应用中**允许存在多个 Filter** ，到底哪一个 Filter 先执行哪一个后执行，这**取决于在 `web.xml` 中定义的先后次序** *（如果使用注解配置，则 **Filter 的执行顺序由 Filter 的类名的字母的顺序**来决定，如 AFilter 和 BFilter，则先执行 AFilter）*
3. **一个 Filter 可以配置多个 `<url-pattern>`** 也可以对**指定的 Servlet** 做过滤（注解通过 `servletNames` 指定，配置由 `<servlet-name>` 指定）
4. **默认情况下，Filter 只对新的请求做拦截，如果是请求转发，则不会过滤。**
- `<dispatcher>`配置项指定了 Filter 的过滤时间：
- `REQUEST`：**只对请求做过滤，默认选项**，如果有该配置项则必须显式写明
- `FORWARD`：只对**请求转发(forword)**方式做过滤
- `ERROR`：只对**跳转到全局的错误页面**做过滤
- `INCLUDE`：只对**请求包含(include)**方式做过滤
- **对应的注解属性为:**`dispatcherTypes`


#### 过滤器实例
> [戳这里](https://mp.weixin.qq.com/s?__biz=MzI4Njg5MDA5NA==&mid=2247483718&idx=3&sn=d63a56a3bdafa2df90178503e4d16d9d&chksm=ebd74047dca0c951eb72b5eb85553e513bd005318ee8c6ff9777d382581f0b352b2acdecf6fe#rd)

---

## 监听器
- **作用：**
1.**监听 web 应用的创建和销毁**
2.**attribute发生的变化。**

- **web 应用：**即ServletContext对象(jsp的[隐式对象application](http://how2j.cn/k/jsp/jsp-object/580.html)) 

除了对[web应用](http://how2j.cn/k/listener/listener-context/605.html)的监听外，还能监听[session](http://how2j.cn/k/listener/listener-session/606.html)和[request](http://how2j.cn/k/listener/listener-request/607.html)的生命周期，以及他们的attribute发生的变化。

> 了解详情[戳这里](http://how2j.cn/k/listener/listener-tutorials/604.html)

---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693
