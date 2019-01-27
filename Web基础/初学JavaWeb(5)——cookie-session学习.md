![](https://upload-images.jianshu.io/upload_images/7896890-0387853b70e04069.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## HTTP 协议
**Web 浏览器**与 **Web 服务器**之间的**一问一答的交互过程**必须遵守**一定的规则**，这样的规则就是 **HTTP 协议。**

**HTTP** 是 **hypertext transfer protocol（超文本传输协议）**的简写，它是 **TCP/IP 协议之上**的一个**应用层的协议**，用于**定义 Web 浏览器与 Web 服务器之间交互数据的过程以及数据本身的格式。**

- **特点：无状态，默认端口 80**

#### HTTP 协议到底约束了什么？
1. 约束了浏览器以**何种格式**向服务端**发送数据**
2. 约束了服务器应该以**何种格式**来**接收客户端发送的数据**
3. 约束了服务器应该以**何种格式**来**反馈数据**给浏览器
4. 约束了浏览器应该以**何种格式**来**接收服务器的反馈数据**
- **总结：**
浏览器给服务器发送数据:**一次请求**
服务器给浏览器反馈数据:**一次响应**

#### HTTP 无状态协议
**HTTP** 是一个**无状态的协议**，也就是没有记忆力，这意味着**每一次的请求都是独立的**，缺少状态意味着如果后续处理需要前面的信息，则它必须要重传，这样可能导致**每次连接传送的数据量增大。**另一方面，在服务器不需要先前信息时它的**应答就很快。**

**HTTP** 的这种特性有优点也有缺点：
- **优点：**解放了服务器，每一次的请求“点到为止”，**不会造成不必要的连接占用。**
- **缺点：**每次请求会**传输大量重复的内容信息，**并且，在**请求之间无法实现数据的共享。**

#### 主要问题：请求之间无法实现数据的共享
- 解决方案：
**1.使用参数传递机制：**
将参数拼接在请求的 URL 后面，实现数据的传递（GET方式），例如：```/param/list?username=wmyskxz```
**问题：**可以解决数据共享的问题，但是这种方式一**不安全**，二数据**允许传输量只有1kb**
**2.使用Cookie技术**
**3.使用Session技术**

---
## Cookie 技术
- **特点：客户端的技术，将共享数据保存在客户端（浏览器）中**

英文直接翻译过来就是**小甜品**，Cookie 的作用呢，通俗的说就是当一个用户通过 **HTTP** 访问一个服务器时，这个服务器会将一些 **Key/Value 键值对**返回给**客户端浏览器**，并给这些数据加上一些**限制条件**，在**条件符合时**这个用户下次访问这个服务器时，数据又被**完整地带回给服务器。**

这个作用就像是你去超市购物时，第一次给你办了一张购物卡，在这个购物卡里存放了一些你的个人信息，下次你再来这个超市的时候，你就只需要带上你的购物卡，直接购物就好了。

![](https://upload-images.jianshu.io/upload_images/7896890-d5de686e11821eb1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### Cookie 操作
1. 创建 Cookie 对象，设置共享数据
```java
Cookie c = new Cookie(String name,String value);  // 相当于办卡
```
- **注意：**一个Cookie只能**存储一个字符串类型的数据,**不能存储其他类型的数据

2. 将 Cookie 响应给浏览器
```java
response对象.addCookie(cookie对象)                // 相当于把卡交给用户
```
3. 获取请求中的 Cookie 信息
```java
Cookie[] cs = request对象.getCookies();
for(Cookie c : cs){
    if(“username”.equals(c.getName())){
        String value = c.getValue();
    }
}
```
4. 修改 Cookie 中的共享数据
1.重新创建一个新的 Cookie，名称要和要修改的数据一致
2.现获取到要修改的 Cookie 对象，再调用 ```setValue(String newValue)``` 重新设置
- **注意：修改 Cookie 中的数据，需要再次发送给浏览器（第2点）**

5. 操作 Cookie 的生命周期
- **默认：**在关闭浏览器的时候销毁 Cookie 对象
- **语法：**```void setMaxAge(int expiry)  ```
expiry > 0：设置 Cookie 对象能够**存活 expiry 秒，即使关闭浏览器，也不影响 Cookie 中的共享数据，**比如设置一个月：```setMaxAge(60*60*24*30);```
expiry = 0：**立即删除**当前的 Cookie 信息
expiry < 0：**关闭浏览器时销毁**

6. 删除 Cookie 中的共享数据
通过```setMaxAge(0)```来实现

7. Cookie 中的 key 和 value 不支持中文
设置 Cookie 时需要对中文字符串进行编码：
    ```java
    Cookie c = new Cookie("username", URLEncoder.encode(username,"UTF-8"));
    ```
    在获取 Cookie 数据的时候再进行解码：
    ```java
    username = URLDecoder.decode(value, "UTF-8");
    ```
8. Cookie 的路径和域范围
- **Cookie 的路径**
Cookie 在创建的时候，会根据当前的**Servlet的相对路径**来设置自己的路径，比如 Servlet 的```url-pattern```为 **/cookie/login**，相对路径则为：**/cookie/**
  - **出现的问题：**
只有在访问路径为 **/cookie/** 下面的资源的时候，才会将该 Cookie 发送到服务器
  - **解决方案：**
设置 Cookie 的路径：```void setPath(String uri)```
```Cookie对象.setPath("/");``` 表示当前应用中的所有的资源都能够共享该Cookie信息
- **域范围：**（了解）
在多个应用之间实现数据的共享，那么就需要设置域范围，比如：
```www.baidu.com / news.baidu.com / map.baidu.com```
- **语法：**```Cookie对象.setDomain("baidu.com");```

#### Cookie 的缺陷
Cookie 的作用其实就是一种会话跟踪技术，但存在一些缺陷：
1. 获取 Cookie 信息**比较麻烦**
2. Cookie **不支持中文**
3. 一个 Cookie 只能**存储一个字符串类型的数据**
4. Cookie 在浏览器中**有大小和数量上的限制**（不同浏览器存在不同的限制，例如FireFox一个站点最多存储50个 Cookie ，浏览器最多存储 4097个字大小的 Cookie）
5. 共享数据时保存在浏览器中，容易造成数据的泄露，**不安全**

- **最好的解决方案：将数据保存在服务端（session）**

---
## Session 技术
Session：会话，**从浏览器打开开始，直到浏览器关闭结束，**无论在这个网站中访问了多少页面，点击了多少链接，都属于同一个会话。Session 也可以称为**会话 Cookie**
- **特点：服务端技术，将数据保存在服务器**

![](https://upload-images.jianshu.io/upload_images/7896890-26ddad87e6728548.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 理解 Session

前面已经介绍了 Cookie 可以让服务端程序**跟踪每个客户端的访问**，但是每次客户端的访问都必须传回这些 Cookie，**如果 Cookie 很多，则无形增加了客户端与服务端的数据传输量，**而 Session 的出现正是为了解决这个问题。

同一个客户端每次和服务端交互时，不需要每次都传回所有的 Cookie 值，而是**只要传回一个 ID**，这个 ID 就是客户端第一次访问服务器生成的，而且**每个客户端是唯一的。**这样每个客户端就有了一个唯一的 ID，客户端只要传回这个 ID 就行了，**这个 ID 通常是 NAME 为 JSESIONID 的一个 Cookie。**

#### Session 基本操作
1. 获取 Session 对象
```request对象.getSession() ```
和参数为true的一样
```request对象.getSession(true) ```
获取Session对象,如果没有Session对象,直接创建一个新的返回,缺省值
```request对象.getSession(false)```
获取Session对象,如果没有返回null
2. 设置共享数据
```java
Session对象.setAttribute(String name, Object value)
```
- **注意：**Session 可以**存储任何类型的数据,**比如登陆用户的信息,可以封装到User对象中

3. 修改共享数据
重新设置一个同名的共享数据

4. 获取共享数据
```java
Object value = Session对象.getAttribute(String name);
```
5. 删除 Session 中的共享数据
```java
Session对象.removeAttribute(String name);
```
6. 销毁 Session
```java
void invalidate() 
```
7. Session 的超时管理
- **超时：** 在访问当前的资源的过程中,不和网页进行任何的交互,超过设定的时间就是超时
在 Tomcat 服务器中有默认的配置为30分钟，一般不需要去修改
- **语法：**``` void setMaxInactiveInterval(int interval) ```

#### Session 扩展
- **Seesion 中的共享数据的属性名的命名规范：**
通常为：XXX_IN_SESSION，例如：```Session对象.setAttribute(“USER_IN_SESSION”,user)```
- **序列化与反序列化：**
Session 中存储的对象通常需要**实现序列化接口**，因为在网络之间传输的数据格式为**二进制数据**：
  - **序列化：将对象转换成二进制数据**
  - **反序列化:将二进制数据转换成对象**
- **URL 重写**
**出现的问题：**
**当浏览器禁用Cookie之后,**那么我们的jsessionid就不能在浏览器中保存,那么**后面的请求**中就不会将 jsessionid 发送到服务器,服务器这面就**找不到数据**
**解决方案：**
1.在url后手动的拼接上 jsessionid
传递格式如 ```/path/Servlet;jsessionid=sessionid```
2.使用响应对象中的encodeURL(String path)**实现 jsessionid 的自动拼接**
```String path = resp.encodeURL("path/Servlet");```
  - **推荐方式：②**



---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693


