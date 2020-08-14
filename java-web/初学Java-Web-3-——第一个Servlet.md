![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（3）——第一个Servlet/7896890-0189f15458034df3.png)

> 这学期 Java Web 课程的第一节课就简短复习了一下 Java 的一些基础知识，所以觉得 Java 的基础知识还是很重要的，但当我想要去写一篇 Java 回顾的文章的时候发现很难，因为坑实在太多了（一个头，两个大），只能另外找一些时间去写顺便巩固巩固自己的基础。

## ——【1. 什么是 Servlet 】——

学习一个东西就要先去了解它是什么东西。

Servlet 取自两个单词：Server、Applet （很符合 sun 公司的命名特点）， Java Servlet 的简称，**其实质就是运行在 Web 应用服务器上的 Java 程序，**与普通 Java 程序不同，它是位于 Web 服务器内部的服务器端的 Java 应用程序，可以对 Web 浏览器或其他 HTTP 客户端程序发送的请求进行处理。

狭义的Servlet是指Java语言实现的一个接口，广义的Servlet是指任何实现了这个Servlet接口的类，一般情况下，人们将Servlet理解为后者。Servlet运行于支持Java的应用服务器中。从原理上讲，Servlet可以响应任何类型的请求，但绝大多数情况下Servlet只用来扩展基于HTTP协议的Web服务器。

> 实际上，Servlet 就像是一个规范，想象一下我们的 USB 接口，它不仅约束了U盘的大小和形状，同样也约束了电脑的插槽，Servlet 也是如此，它不仅约束了服务器端如何实现规范，也约束着 Java Web 项目的结构，为什么这样说，我们下面再来讲，**编写一个 Servlet 其实就是按照 Servlet 规范编写一个 Java 类。**

## ——【2. Servlet 与 Servlet 容器 】——

Servlet 对象与普通的 Java 对象不同，它可以处理 Web 浏览器或其他 HTTP 客户端程序发送的 HTTP 请求，但前提是把 Servlet 对象布置到 Servlet 容器中，也就是说，其运行需要 Servlet 容器的支持。

Servlet 容器也叫做 Servlet 引擎，是 Web 服务器或应用程序服务器的一部分，用于在发送的请求和响应之上提供网络服务，解码基于 MIME 的请求，格式化基于 MIME 的响应。Servlet 没有 main 方法，不能独立运行，它必须被部署到 Servlet 容器中，由容器来实例化和调用 Servlet 的方法（如 doGet() 和 doPost() 方法），Servlet 容器在 Servlet 的生命周期内包容和管理 Servlet 。在 JSP 技术 推出后，管理和运行 Servlet / JSP 的容器也称为 Web 容器。

有了 Servlet 之后，用户通过单击某个链接或者直接在浏览器的地址栏中输入 URL 来访问 Servlet ，Web 服务器接收到该请求后，并不是将请求直接交给 Servlet ，而是交给 Servlet 容器。Servlet 容器实例化 Servlet ，调用 Servlet 的一个特定方法对请求进行处理， 并产生一个响应。这个响应由 Servlet 容器返回给 Web 服务器，Web 服务器包装这个响应，以 HTTP 响应的形式发送给 Web 浏览器。


#### Servlet 容器能提供什么？

上面我们知道了需要由 Servlet 容器来管理和运行 Servlet ，但是为什么要这样做呢？使用 Servlet 容器的原因有：

1. **通信支持：**利用容器提供的方法，你能轻松的让 Servlet 与 web 服务器对话，而不用自己建立 serversocket 、监听某个端口、创建流等。容器知道自己与 web 服务器之间的协议，所以你的 Servlet 不用担心 web 服务器（如Apache）和你自己的 web 代码之间的 API ，只需要考虑如何在 Servlet 中实现业务逻辑（如处理一个订单）。 
2.  **生命周期管理：** Servlet 容器控制着 Servlet 的生与死，它负责加载类、实例化和初始化 Servlet ，调用 Servlet 方法，以及使 Servlet 实例被垃圾回收，有了 Servlet 容器，你不需要太多的考虑资源管理。 
3. **多线程支持：**容器会自动为它所接收的每个 Servlet 请求创建一个新的 java 线程。针对用户的请求，如果 Servlet  已经运行完相应的http服务方法，这个线程就会结束。这并不是说你不需要考虑线程安全性，其实你还会遇到同步问题，不过这样能使你少做很多工作。 
4. **声明方式实现安全：**利用 Servlet 容器，你可以使用 xml 部署描述文件来配置和修改安全性，而不必将其硬编码写到 Servlet 类代码中。 
5. **JSP支持：** Servlet容器负责将 jsp 代码翻译为真正的 java 代码。

#### Servlet 生命周期

通常情况下，Servlet 容器也就是指 Web 容器，如 Tomcat、Jboss、Resin、Weblogic 等，它们对 Servlet 进行控制。当一个客户端发送 HTTP 请求时，由容器加载 Servlet 对其进行处理并做出响应。在 Web 容器中，Servlet 主要经历 4 个阶段，如下图：

![Servlet 与容器](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（3）——第一个Servlet/7896890-a26b3e8c2114d5f0.png)


Servlet 与 Web 容器的关系是非常密切的，在 Web 容器中 Servlet 主要经历了 4 个阶段，这 4 个阶段实质是 Servlet 的生命周期，由容器进行管理。

（1）在 Web 容器启动或者客户机第一次请求服务时，容器将加载 Servlet 类并将其放入到 Servlet 实例池。

（2）当 Servlet 实例化后，容器将调用 Servlet 对象的 init() 方法完成 Servlet 的初始化操作，主要是为了让 Servlet 在处理请求之前做一些初始化工作。

（3）容器通过 Servlet 的 service() 方法处理客户端请求。在 Service() 方法中，Servlet 实例根据不同的 HTTP 请求类型作出不同处理，并在处理之后作出相应的响应。

（4）在 Web 容器关闭时，容器调用 Servlet 对象的 destroy() 方法对资源进行释放。在调用此方法后，Servlet 对象将被垃圾回收器回收。

## ——【3. 第一个 Servlet 】——
#### ① 搭建 Java Web 项目
1. 创建 一个 Java 项目，并命名为 HelloServlet； **（注意：这是普通的 Java 项目而不是动态 Web 项目）**
2. 在项目的根目录下创建一个文件夹 webapp，表示 Web 项目的根；
3. 在 webapp 中创建 WEB-INF 文件夹；
4. 在 WEB-INF 文件夹中创建文件夹：classes,lib；
5. 在 WEB-INF 文件中添加 Tomcat 根目录下 conf 文件夹中的 web.xml 文件；（只保留根元素，就像下面这样）
	```
	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
	                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	  version="3.1">
	
	</web-app>
	
	```
6. 把当前项目的 classpath 路径改成 webapp/WEB-INF 下的 classes 中。

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（3）——第一个Servlet/7896890-9d55e8be6d22fd9d.png)


#### ② 编写 Servlet

1. 为该项目增加Servlet的支持.
      1.1. 把Tomcat根/lib中servlet-api.jar文件拷贝到项目下WEB-INF下的lib中
      1.2. 在项目中选择servlet-api.jar,鼠标右键,build path-->add to build path

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（3）——第一个Servlet/7896890-ec14aa271cc510e8.png)

     
2. 开发Servlet程序:
     2.1:定义一个类HelloServlet,并让该类去实现javax.servlet.Servlet接口;
     2.2:实现Servlet接口中的init,service,destory等方法.
		

```
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class HelloServlet implements Servlet{
	@Override
	public void init(ServletConfig config) throws ServletException {
	}
	@Override
	public ServletConfig getServletConfig() {
		return null;
	}
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		System.out.println("测试一下！");     // 先在这里写下测试代码
	}
	@Override
	public String getServletInfo() {
		return null;
	}
	@Override
	public void destroy() {
	}

}
```
      
**注意:若生成方法中的参数是 arg0 或则 arg1 等格式的,原因是还没有关联源代码的问题:** [关联上 tomcat src 文件即可](https://jingyan.baidu.com/article/0202781170ffa61bcc9ce5dd.html)

#### ③ 配置 Servlet

上面编写好的 HelloServlet 类仅仅是一个普通的实现类而已，而现在我想要它运行在我自己的 Tomcat 服务器中，所以应该通知 Tomcat 服务器来管理我的 HelloServlet 类，具体的做法如下：

1. 找到项目根下的WEB-INF下的web.xml文件:
2. 在根元素web-app中创建一个新的元素节点:servlet
2. 在根元素web-app中创建一个新的元素节点:servlet-mapping（Servlet 的映射）

	```
	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
	                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	  version="3.1">
	  
	
		<servlet>
			<servlet-name>HelloServlet</servlet-name>
			<servlet-class>lt.HelloServlet</servlet-class>
		</servlet>
		
		<servlet-mapping>
			<servlet-name>HelloServlet</servlet-name>
			<url-pattern>/hello</url-pattern>
		</servlet-mapping>
	
	</web-app>
	```
	> web.xml 提供路径和servlet映射关系，这意思是把/hello这个路径，映射到 HelloServlet 这个类上，需要注意的是：**< servlet> 标签下的 < servlet-name>与 < servlet-mapping> 标签下的 < servlet-name> 必须一样**（因为标签被编辑器识别故<后加了一个空格）

4. 配置 Tomcat 服务器：

	4.1. 修改默认端口为 80 端口：
		首先进入到 Tomcat 服务器的根路径下找到 conf 文件夹下的 server.xml 文件，默认是在第 70 行，找到 Connector 元素的 port 属性，修改为 80 ，重启 Tomcat 即可。
		![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（3）——第一个Servlet/7896890-7812541ce51b78e5.png)
      > Tomcat 的默认端口为8080，而 HTTP 协议的默认端口是 80，配置 80 端口的好处在于，我们平时输入的类似于 http://baidu.com 其实是默认省略了写 80端口的，它其实等价于：http://baidu.com:80/ ，所以以后我们就不用再写冒号直接输入 locahost 就能进入到 Tomcat 的配置页面了。

     4.2 部署 Java 项目（告诉 Tomcat 服务器来管理我们的项目）：
		这里直接引用 how2j.cn 的教程啦：
		![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（3）——第一个Servlet/7896890-9b8a876c38379a5a.png)

所以加上```<Context path="" docBase="F:\\Projects\\JavaProject\\HelloServlet\\webapp" />```这一句重启 Tomcat 服务器


在浏览器中输入 localhost/hello，回车，即可在 Tomcat 服务器上看到相关信息，至此第一个 Servlet 就编写成功了

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（3）——第一个Servlet/7896890-d0df0d5429c132d3.png)



## ——【4. Servlet 请求过程】——

学习 Servlet 技术，就需要有一个 Servlet 运行环境，也就是需要有一个 Servlet 容器，如这里使用的**【Tomcat】**。

Tomcat 服务器接受客户请求并做出响应的过程如下（以上面搭建的项目为例）：

① 打开浏览器发起请求：http://localhost:80/hello/index.html （假设有一个 index.html 文件）

② 服务器接收到请求后处理请求：
> htpp：所使用的协议
> localhost：ip地址，确定访问的主机
> 80：端口号
> hello：上下文路径，确定访问项目的根路径
> index.html：确定访问项目中的具体哪一个资源

③ 根据 hello 去 tomcat/conf/server.xml 文件中找到相关配置文件，根据上下文路径找到项目的根路径：
```<Context path="" docBase="F:\\Projects\\JavaProject\\HelloServlet\\webapp" />```
如果找不到根路径（因为这里默认上下文路径为空），返回 404

④ 根据资源名称去项目中的 web.xml 文件中找到相关的配置，找到配置中的```<url-pattern>```
![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/初学Java-Web（3）——第一个Servlet/7896890-bc647a56c9f6423f.png)


如果找不到 hello 的资源名称，则返回 404

⑤ 根据资源名称找到 Servlet 的全限定名，如果找不到则在启动服务器的时候报错
```java.lang.IllegalArgumentException: Servlet mapping specifies an unknown servlet name HelloServlet```

⑥ 根据找到的全限定名创建对象，在创建对象之前需要判断是否是第一次请求，使用 Tomcat 中使用 Servlet 实例缓存池来实现，若是第一次则调用对象的 init 方法。

⑦ 创建 req,resp 对象，执行 service 方法；

⑧ 使用 resp 对象给浏览器响应信息。

> 发现一边学一边写起来太麻烦了，一方面是因为自己的水平，另一方面是觉得这样太费时间了，也要开始找实习了，所以时间有些宝贵，可能这不会作为一个连续的系列，反而是一些学习的分享和感悟，emmm....事实上，Servlet也还有好多东西，包括最新支持注解方式配置等....
> 参考资料：
> 《Java Web程序设计 慕课版——明日科技》
> how2j.cn
> [理解Servlet和Servlet容器、Web服务器等概念](http://blog.csdn.net/lz233333/article/details/68065749)

---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693
