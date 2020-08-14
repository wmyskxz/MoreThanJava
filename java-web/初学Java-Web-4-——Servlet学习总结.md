![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-65cb65e46c091671.png)


> 经过一段时间的学习，对于Servlet有了新的不一样的见解，在这里做一下总结，将近来学习到的知识总结一下。

***

### Servlet 的请求流程

1. **浏览器发出请求：**http://localhost:80/xxx1/xxx2 （80端口可以默认不写，因为这是http协议默认的端口，平时我们访问https://www.baidu.com/ 时其实访问的是https://www.baidu.com:80/）

2. **服务器解析请求信息：**
    - **http:**协议名称
    - **localhost:**访问的是互联网中的**哪一台计算机**
    - **80:**从主机当中找到**对应 80 端口的程序 **（**这里即为 Tomcat 服务器**）
    - **/xxx1:**当前项目的**上下文路径** （即在 server.xml 中配置主机时配置的 **path属性**）
    - **/xxx2:**当前**请求的资源名**

3. **解析** Tomcat 服务器根目录下的 **/config/server.xml** 文件：
`<Context docBase="D:\javaPros\test\webapp" path="xxx1" />`
判断哪一个`<Context />```元素的 **path属性** 属性为 ```xxx1`
    - 若找不到，则返回 **404错误**
    - 若找到了，则解析该```<Context />```元素，得到```docBase```属性，获取当前访问 Web 项目的跟的绝对路径：```D:\javaPros\test\webapp```

4. 从`D:\javaPros\test\webapp`下的 **WEB-INF** 下找到 **web.xml** 文件
判断 **web.xml** 文件中是否有 `<url-pattern>` 的文本内容为 **/xxx2**
    - 若找不到，则返回 **404错误**
    - 若找到了，则继续**获取该资源对应 Servlet 类的全限名称：** xxx.xxx

5. 判断 **Servlet 实例缓存池** 中是否有 xxx.xxx 的对象

```java
Map<String,Servlet> cache = ......(Tomcat提供的);
	key:存Servlet类的全限定名称
	value:该Servlet类的对象.
Servlet obj = cache.get("xxx.xxx");
	if(obj==null){
		//Servlet实例缓存中没有该类的对象,第一次.
		GOTO 6:
	}else{
		//有对象,非第一次.
		GOTO 8:
	}
}
```

6. **使用反射**调用构造器，**创建对应的对象**

`obj = Class.forName("xxx.xxx").newInstance();`

把当前创建的 **Servlet 对象**，存放在缓存之中，**供给下一次的使用.**

`cache.put("xxx.xxx",obj);`

7. 创建 **ServletConfig 对象**，并调用 **init()** 方法

`obj.init(config);`

8. 创建 **ServletRequest 对象和 ServletResponse 对象**，并调用 **service()**方法

`obj.service(req,resp);`

9. 在 **service()** 方法中对浏览器作出响应操作。

---

### Servlet 生命周期

在 Web 容器中，Servlet 主要经历 4 个阶段，如下图：

![Servlet 生命周期](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-2599bf65a828350d.png)

1. **加载 Servlet：**当 Tomcat **第一次访问 Servlet** 的时候，Tomcat 会负责**创建 Servlet 的实例。**
2. **初始化 Servlet：**当 Servlet 被实例化之后，Tomcat 会调用 **init()** 方法来初始化这个对象。
3. **处理服务：**当浏览器**访问 Servlet** 的时候，Servlet 会调用 **service()** 方法处理请求。
4. **销毁：**当 **Tomcat 关闭**或者**检测到 Servlet 要从 Tomcat 删除**的时候，会自动调用 **destroy()** 方法，让该实例所占用的资源释放掉。一个 Servlet 如果长时间不被使用的话，也会被 Tomcat 自动销毁。

- **简单总结：**只要访问 Servlet ，就会调用其对应的 **service()** 方法，**init()** 方法只会在第一次访问 Serlvet 的时候才会被调用。

> 这一部分参考文章：[这里是链接](https://mp.weixin.qq.com/s?__biz=MzI4Njg5MDA5NA==&mid=2247483680&idx=3&sn=d5380ff58c5077271ac9c43d2d96f6c1&chksm=ebd74021dca0c93733255324df8c1e522dbe36ccaf8c2c4bcca4765113a120eb9851ca0e2442#rd)
---

### Servlet 提供处理请求的方法

[前面的文章](https://www.jianshu.com/p/bbdc459b9187)里面提到过，广义上，**Servlet** 即实现了 **Servlet 接口** 的类，当我们创建一个自定义类，实现 **Servlet 接口** 的时候，会发现有 5 个方法需要重写，有init【初始化】，destroy【销毁】,service【服务】,ServletConfig【Servlet配置】,getServletInfo【Serlvet信息】。

这样做的话，我们每次都需要实现 5 个方法，太麻烦了！

我们可以直接**继承 HttpServlet** 类，该类已经默认实现了 Servlet 接口中的所有方法，在编写 Servlet 的时候，你只需要**重写你需要的方法**就好了，并且该类还在原有 Servlet 接口上添加了一些与 HTTP 协议处理相关的方法，比 Servlet 接口的功能**更强大。**

- Servlet 处理请求的方法一共有三种：
① 实现 **service()** 方法。
② 重写 **doGet()** 和 **doPost()** 方法，并在 **doGet()** 中添加一句```this.doPost(req, resp);```（因为无论是get或post请求提交的数据，处理方式都基本相同，下同）
③ 重写 **doGet()** 和 **doPost()** 方法，并在 **doPost()** 中添加一句```this.doGet()(req, resp);```
- **推荐方式①。**

---

### Servlet 是单例的

#### 为什么Servlet是单例的

**浏览器多次对Servlet的请求，**一般情况下，**服务器只创建一个Servlet对象，**也就是说，Servlet对象**一旦创建了，**就会**驻留在内存中，为后续的请求做服务，直到服务器关闭。**

#### 每次访问请求对象和响应对象都是新的

对于**每次访问请求，**Servlet引擎都会**创建一个新的HttpServletRequest请求对象和一个新的HttpServletResponse响应对象，**然后将这**两个对象作为参数传递给它调用的Servlet的service()方法，**service方法再根据请求方式分别调用doXXX方法。

#### 线程安全问题

当多个用户访问Servlet的时候，**服务器会为每个用户创建一个线程。**当多个用户并发访问Servlet共享资源的时候就会出现线程安全问题。

**原则：**
    1. 如果一个变量**需要多个用户共享**，则应当在访问该变量的时候，**加同步机制synchronized (对象){}**
    2. 如果一个变量**不需要共享，**则**直接在 doGet() 或者 doPost()定义**.这样不会存在线程安全问题

> 这一部分参考文章：[这里是链接](https://mp.weixin.qq.com/s?__biz=MzI4Njg5MDA5NA==&mid=2247483680&idx=4&sn=2fdf4d0075d093389c03697ebdb9f47d&chksm=ebd74021dca0c937a240f47578b9c5f40093a307f6537d79d5a2fd12721c5311a9d89d5c5583#rd)

---

### HttpServletRequest 和 HttpServletResponse 对象

对于**每次访问**请求，**Servlet引擎**都会创建一个**新的HttpServletRequest请求对象**和一个**新的HttpServletResponse响应对象**，即 request 和 response 对象。

既然 request 对象代表 http 请求，那么我们**获取浏览器提交过来的数据，就找 request 对象** 即可。response 对象代表 http 响应，那么我们**向浏览器输出数据，找 response 对象**即可。

### HttpServletRequest 常用方法
- **String getContextPath():**
获取上下文路径,<Context path="上下文" ../>
- **String getHeader(String headName):**
根据指定的请求头获取对应的请求头的值.
- **String getRequestURI():**
返回当期请求的资源名称.  上下文路径/资源名
- **StringBuffer getRequestURL():**
返回浏览器地址栏的内容
- **String getRemoteAddr():**
返回请求服务器的客户端的IP

#### 获取请求参数的方法：
- **String getParameter(String name):**
根据参数名称,获取对应参数的值.
- **String[] getParameterValues(String name):**
根据参数名称,获取该参数的多个值.
- **Enumeration<String> getParameterNames():**
获取所有请求参数的名字
- **Map<String,String[]> getParameterMap():**
返回请求参数组成的Map集合.
**key:**参数名称
**value:**参数值,封装在String数组中.

### HttpServletResponse 常用方法
- **OutputStream getOutputStream():**
获取字节输出流:**文件下载**
- **Writer getWriter():**
获取字符输出流:**输出内容**
设置文件输出的编码格式和内容类型：```resp.setContentType("text/html;charset=utf-8");```

---
### GET 和 POST 的区别
要知道，GET 和 POST 都是请求方式
- **GET：**
浏览器器地址栏：```http://localhost/test.html```**?name=wmyskxz&sex=male**
这里提交了两个参数，一个是```name```属性值为```wmyskxz```，另一个是```sex```属性值为```male```，这是一种直接的请求方式，在请求资源后面跟上 **?** 符号与参数连接，其他的参数使用 **&** 符号连接。
- **缺点：**
1.暴露请求信息，**不安全**
2.请求信息不能超过**1kb**，可传输的信息有限，不能上传图片

- **POST：**
浏览器地址栏：```http://localhost/test.html#```
- **优点：**
1.隐藏了请求信息，**较安全**（但仍可以通过相关工具访问到数据）
2.POST 方式**没有限制**请求的数据大小，可以做图片的上传

> 但并不是所有的数据都需要使用 POST 请求来完成，事实上，GET 请求方式会比 POST 请求更快，当数据小并且安全性要求不是那么高的时候，GET 仍然是很好的选择.(并且 GET 相较 POST 简单)

---

### 请求中文乱码的处理
在 **Tomcat 服务器**中，接受请求的时候，**默认的编码方式为 ISO-8859-1**，而该编码方式只占一个字节，不支持中文（两个字节），所以当我们做请求的时候，会出现乱码的问题
- **解决方案：**
1.对乱码使用 **ISO-8859-1** 解码，转换成**byte数组**，恢复为二进制
`byte[] data = name.getBytes("ISO-8859-1");`
2.对byte数组重新进行 UTF-8 编码：
`name = new String(data,"UTF-8");`
但是这样会出现一个问题，那就是当表单数据太多的时候，这样反复解码-编码，会很繁琐。
- **终极解决方案：**
**1.对于 POST 请求：**
设置请求的编码方式：```request.setCharacterEncoding("UTF-8");```
**注意：**必须在获取**第一个参数之前设置**，并且该方式**只对 POST 方式有效。**
**2.对于 GET 请求：**
重新设置 Tomcat 的编码方式，修改 Tomcat 的配置文件:
`Tomcat根目录/conf/server.xml(修改端口的那一行)`
![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-cd366b295c8ed531.png)

---
### Servlet 细节
- 1.一个 Servlet 可以有**多个** ```<url-pattern>``` ，可以使用多个资源名称找到当前的 Servlet
- 2.配置 Servlet 可以使用**通配符**（*）
`*`表示任意字符
`/*`：可以使用**任意的字符**访问当前的 Servlet
`*.xxx`：如 wmyskxz.wudi
- 3.**自定义的 Servlet 的 ```<servlet-name>``` 不能够为 default ，**使用它会造成项目下面的静态资源找不到，在 ```Tomcat/conf/web.xml``` 文件中配置一个名字为default的Servlet,该Servlet在负责访问项目下的静态资源
![web.xml 中配置的默认项](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-23070ab172726a2e.png)
- 4.关于 **Servlet 的初始化操作，**如果初始化操作非常的耗时，那么第一个请求的用户的用户体验就非常差
**解决思路：**将初始化操作**向前移,**在服务器启动的时候执行 Servlet 的初始化
![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-afae824b9279c9ff.png)
---

### 通过注解配置 Servlet 
这是 Servlet 3.0 提出的新特性，支持注解配置，这大大简化了我们的工作。

在之前的开发工作中，我们总是去 ```web.xml``` 文件中进行配置，至少会出现8行：

![web.xml 中配置 Servlet](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-1e9520edba4da3bc.png)

而当一个项目中存在**很多 Servlet **，那么配置文件就会变得**非常臃肿，不便于后期的维护，**在 Servlet 3.0 推出之后，我们可以使用**注解来配置 Servlet，**上面 8 行的配置可以简化为下面的简单的注解：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-9bb5cb4ed2098464.png)

或者也可以使用属性 ```value``` 定义访问的 URL，**只有 URL 这个属性是必要的，**```name``` 是可以缺省的值，而默认的 ```value``` 也可以省略不写，所以可以简写成：
```java
@WebServlet("/foreServlet")
```

---
### Web 组件之间的跳转方式
#### **1.请求转发（forward）**
又叫做**直接转发方式，**客户端和浏览器**只发出一次请求，**Servlet、HTML、JSP或其它信息资源，由**第二个信息资源响应该请求，**在请求对象request中，保存的对象对于**每个信息资源是共享的。**

比如：从 AServlet 请求转发到 BServlet

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-881fc9bb05d46ac8.png)

- **语法：**

```java
request.getRequestDispatcher(path).forward(request, response);
```

*参数：*`path`，要跳转到的资源路径：**上下文路径 / 资源路径**
- **特点：**
**1.地址栏中的地址【不会】改变**
通常看作是服务端的跳转
**2.只有一个请求**
**3.资源是共享的，**也就是说在两个 Servlet 中可以共享请求的资源
可以通过`request.setAttribute(String var1,Object var2)`**设置要共享的数据资源**，并通过`request.getAttribute(String var1);`来**获取传递的资源**
**4.【可以】访问 WEB-INF 中的资源**
**WEB-INF** 文件夹是 Java Web 应用的**默认安全目录，**即客户端无法直接访问，只有服务端可以访问的目录。
如果想在页面中**直接访问**其中的文件，**必须通过web.xml文件**对要访问的文件进行**相应映射**才能访问。
**注意：**在实际的开发中，可以把不希望用户直接访问到（通过浏览器输入地址栏）的网页放在文件夹中**通过此方式访问。**
**5.请求转发【不能】跨域访问**
所谓的同域，是指**域名，协议，端口均相同**

#### 2.URl 重定向（redirect）
又叫做**间接转发方式（Redirect）**实际是**两次HTTP请求，**服务器端在响应第一次请求的时候，让浏览器再向另外一个URL发出请求，从而达到转发的目的。

比如:从AServlet重定向到BServlet

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web(4)——Servlet学习总结/7896890-c49539085575bc26.png)

- **语法：**

```java
response.sendRedirect(String location);
```

*参数：*`location`，转发到的资源路径
- **特点：**
**1.地址栏中的地址【会】发生改变**
通常看作是客户端跳转
**2.有两个请求**
**3.在两个 Servlet 中不可以共享请求中的数据**
**4.最终的响应由 BServlet 来决定，和 AServlet 没有关系**
**5.【不可以】访问 WEB-INF 中的资源**
**6.请求转发【能】跨域访问**
就像是在网页中点开了新的链接一样
- **总结：**URL 重定向相当于是将重定向的资源路径，重新复制到浏览器地址栏中按下回车一样，**重新发送一次新的请求。**

#### 3.请求包含（include）

---

### MVC 模式
MVC 是一种分层的设计模式 。
- **M 代表 模型（Model）**
模型是什么呢？ 模型就是**数据，**就是dao,bean
- **V 代表 视图（View）**
视图是什么呢？ 就是网页, JSP，用来**展示模型中的数据**
- **C 代表 控制器（controller)**
控制器是什么？ **控制器的作用就是把不同的数据(Model)，显示在不同的视图(View)上。**

这部分可以参考一下[这里](http://how2j.cn/k/mvc/mvc-tutorials/561.html)



---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693



