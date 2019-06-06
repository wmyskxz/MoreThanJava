![](https://upload-images.jianshu.io/upload_images/7896890-d8538c85f415d63e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 为什么要学习 JSP
#### Servlet 的短板：
Servlet 的出现，是为了**解决动态输出网页的问题。**

虽然这样做目的能达到，但是存在一些**缺陷：**
- 在 Servlet 输出网页片段非常恶心**（可读性差，维护起来也很麻烦）** 
- 没有体现**责任分离**的原则（做自己擅长做的事）

#### 责任分离
- **Servlet** 能够很好地**组织业务逻辑代码，**但是在 Java 源文件中通过字符串拼接的方式生成动态 HTML 内容会导致**代码维护困难、可读性差**
- **JSP** 虽然规避了 Servlet 在生成 HTML 内容方面的劣势，但是在 HTML 中混入大量、复杂的业务逻辑同样也是不可取的
- *参考：[知乎@David](https://www.zhihu.com/question/37962386)*
- **注意：JSP实质上就是一个Servlet**

#### MVC 模式
既然 Servlet 和 JSP 都有各自的优势和短板，那么为什么不结合起来扬长避短呢？答案是肯定的——MVC(Model-View-Controller)模式非常适合解决这一问题。

MVC模式（Model-View-Controller）是[软件工程](https://link.zhihu.com/?target=https%3A//zh.wikipedia.org/wiki/%25E8%25BD%25AF%25E4%25BB%25B6%25E5%25B7%25A5%25E7%25A8%258B)中的一种[软件架构](https://link.zhihu.com/?target=https%3A//zh.wikipedia.org/wiki/%25E8%25BD%25AF%25E4%25BB%25B6%25E6%259E%25B6%25E6%259E%2584)模式，把软件系统分为三个基本部分：模型（Model）、视图（View）和控制器（Controller）：
- Controller——负责转发请求，对请求进行处理
- View——负责界面显示
- Model——业务功能编写（例如算法实现）、数据库设计以及数据存取操作实现

在JSP/Servlet开发的软件系统中，这三个部分的描述如下所示：

![](https://upload-images.jianshu.io/upload_images/7896890-1471c8cf3ca17fc7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

1.Web浏览器发送HTTP请求到服务端，被Controller(Servlet)获取并进行处理（例如参数解析、请求转发）
2.Controller(Servlet)调用核心业务逻辑——Model部分，获得结果
3.Controller(Servlet)将逻辑处理结果交给View（JSP），动态输出HTML内容
4.动态生成的HTML内容返回到浏览器显示

MVC模式在Web开发中的好处是非常明显，它规避了JSP与Servlet各自的短板，Servlet只负责业务逻辑而不会通过out.append()动态生成HTML代码；JSP中也不会充斥着大量的业务代码。这大大提高了代码的可读性和可维护性。
- *参考：[知乎@David](https://www.zhihu.com/question/37962386)*

---

## JSP 的执行原理
- **总结：**
当访问一个 JSP 页面时，该页面请求将会讲给服务器中的 **JSP 引擎**去处理，它**负责解释和执行 JSP 页面，**每个 JSP 页面在**第一次被访问时**，JSP 引擎就会将它翻译成一个继承自 ```org.apache.jasper.runtime.HttpJspBase```类的 **Servlet 源程序，接着再编译成 class 类文件**，再由 Web 容器**像调用普通 Servlet 程序一样**的方式来装载和解释执行这个由 JSP 页面翻译成的 Servlet 程序。



[详细资料在这里，感兴趣的戳我](https://blog.csdn.net/oncealong/article/details/51393266)

---
## JSP 的语法
像这样冗杂繁复的知识点，直接给两个好一点的链接记下就好了：
[1.W3Cschool](https://www.w3cschool.cn/jsp/jsp-jstl.html)
[2.菜鸟教程](http://www.runoob.com/jsp/jsp-tutorial.html)

---
## JSP 三大指令
- **特点：**
并**不向客户端产生任何输出**，指令在 JSP **整个文件范围内有效**，并且为**翻译阶段提供了全局信息**
- **指令的语法格式：**
**<%@ 指令名称   属性名=属性值   属性名=属性值%>**


####  ——【page指令】——
- **作用：**
定义 JSP 页面的各种属性
- **属性：**
1.language：指示JSP页面中使用脚本语言。默认值java，目前只支持java。
2.extends：指示 JSP 对应的 Servlet 类的父类。**不要修改。**
3.`*`import：导入JSP中的Java脚本使用到的类或包。（如同Java中的import语句）
JSP 引擎自动导入以下包中的类：
```javax.servlet.*```
```javax.servlet.http.*```
```javax.servlet.jsp.*```
**注意：一个import属性可以导入多个包，用逗号分隔。**
4.`*`sessioin：指示JSP页面是否创建 HttpSession 对象。**默认值是true，创建**
5.`*`buffer：指示 JSP 用的输出流的缓存大小.**默认值是8Kb。**
6.autoFlush：自动刷新输出流的缓存。
7.isThreadSafe：指示页面是否是线程安全的（过时的）。**默认是true。**
true：不安全的。
false：安全的。指示 JSP 对应的 Servlet 实现 SingleThreadModel 接口。
8.`*`errorPage:**指示当前页面出错后转向（转发）的页面。**

```
> 配置全局错误提示页面：
> web.xml 文件中添加：
<error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/error.jsp</location>
</error-page>
<error-page>
    <error-code>404</error-code>
    <location>/404.jsp</location>
</error-page>
```
9.`*`isErrorPage:指示当前页面是否产生 Exception 对象。
10.`*`contentType：指定当前页面的 MIME 类型。作用与 Servlet 中的```response.setContentType()``` 作用完全一致
11.`*`pageEncoding：**通知引擎读取 JSP 时采用的编码（因为要翻译）**
12.`*`isELIgnored：是否忽略EL表达式。${1+1}。**默认值是false。**

- **page 指令最简单的使用方式：**
```
<%@ page pageEncoding="UTF-8"%>
```

#### ——【include】——
- **作用：**
包含其他的组件
- **语法：**
```<%@include file=""%>```
file 指定要包含的目标组件。路径如果以 **"/"**（当前应用）就是绝对路径。
- **原理：**
把目标组件的内容加到源组件中，输出结果。

#### 静态包含和动态包含的区别：
- **静态包含：**
```<%@include file="被包含的页面的路径"%>```
**包含的时机：**在 JSP 文件被**翻译**的时候**合并在一起**
最终会被翻译成**一个 class 文件**
- **动态包含：**
``` <jsp:include page="被包含页面的路径"></jsp:include>```
**包含的时机：**在**运行阶段合并代码**
最终将得到**两个 class 文件**
- **总结：在实际开发中，能用静的就别用动的**

#### ——【taglib】——
- **作用：**
引入外部的标签
- **语法：**
```<%@taglib uri="标签名称空间" prefix="前缀"%>```
例如：```<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>```

---

## JSP 九大内置对象
**内置对象：**JSP 中事**先创建好的对象**，可以**直接拿来使用**

名称| 类型| 描述
:-: | :-: | :-:
pageContext | PageContext| 表示当前的JSP对象
request | HttpServletRequest| 表示一次请求对象
session | HttpSession| 表示一次会话对象,**session="true"**
application	|	ServletContext		|	表示当前应用对象
response		|	HttpServletResponse	|	表示一次响应对象
exception	|		Throwable		|	表示异常对象,isErrorPage="true"
config	|		ServletConfig	|		表示当前JSP的配置对象
out		|	JspWriter		|	表示一个输出流对象
page	|		Object		|	表示当前页面

---
## JSP 四大作用域
名称| 类型| 描述
:-: | :-: | :-:
pageContext		 | PageContext		 | 表示当前的JSP对象
request		 | HttpServletRequest	 | 表示一次请求对象
session	 | 	HttpSession		 | 表示一次会话对象,**session="true"**
application		 | ServletContext	 | 	表示当前应用对象

---

## EL（表达式语言）
- **需求：从作用域中获取共享数据,如果没有对应的数据,返回空字符串**
在PageContext中提供了下面的方法:```abstract  Object findAttribute(String name) ```来获取共享数据，从**page，request，session，application**作用域中**按顺序搜索,**如果找到立即返回,反之,返回null

所以我们可以这样来完成要求：
```<%=pageContext.findAttribute("msg")==null?"":pageContext.findAttribute("msg") %>```
这样的代码虽然能够完成需求，但是总的来说：**太麻烦！**

- 如果我们使用 EL 表达式，该如何实现呢？
```${msg}```等价于```<%=pageContext.findAttribute("msg")==null?"":pageContext.findAttribute("msg") %>```
这样看起来就**简单**多了！

- **EL的特点：**
1.从作用域中获取共享数据
2.从page,request,session,application作用域中**按顺序搜索**
3.**如果共享数据为null,就输出空字符串**（这是EL最重要的特点）

#### 使用EL表达式从指定的作用域中获取共享数据:
![](https://upload-images.jianshu.io/upload_images/7896890-fb4b68a07268b5a9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- **使用EL表达式访问对象的属性的两种方式：**
1.**${对象.属性名}       :**通常使用这种方式,**属性名比较规范**
2.**${对象[“属性名”]} :**当**属性名不规范**的时候使用这种方式,比如:name-age

- **使用El表达式获取应用的上下文路径：**
在EL表达式中有一个**隐含的对象pageContext**
而在pageContext中有一个request属性,在request对象中有一个contextPath属性,那么获取contextPage的方法:
```${pageContext.request.contextPath}```
在 Tomcat 7 以后,EL表达式不仅支持属性的访问,而且还支持访问方法
```${pageContext.getRequest().getContextPath()}```
- **empty 运算符：**
empty 运算符主要用来判断值是否为空**（NULL,空字符串，空集合），返回 true / false**

---

## JSTL
- **作用：消除 JSP 中的 Java 代码**
- 在 JSP 中使用 JSTL 的步骤：
1.**引入入jar包：**在 Tomcat 中的实例项目 examples 中找到对应的两个jar包
standard-1.1.2.jar ，jstl-1.1.2.jar
2.在对应的 JSP 页面中引入要使用的标签库,比如引入核心标签库
```<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>```

> 下面列出 JSTL 主要提供的 5 大类标签库（搬自[菜鸟教程](http://www.runoob.com/jsp/jsp-jstl.html)），先对这些标签初步有一个印象，然后下面给一些常用的标签的一些用法。

#### JSTL 核心标签
 核心标签是最常用的JSTL标签。引用核心标签库的语法如下：
```
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
```
| 标签 | 描述 |
| :- | :- |
| [<c:out>](http://www.runoob.com/jsp/jstl-core-out-tag.html) | 用于在JSP中显示数据，就像<%= ... > |
| [<c:set>](http://www.runoob.com/jsp/jstl-core-set-tag.html) | 用于保存数据 |
| [<c:remove>](http://www.runoob.com/jsp/jstl-core-remove-tag.html) | 用于删除数据 |
| [<c:catch>](http://www.runoob.com/jsp/jstl-core-catch-tag.html) | 用来处理产生错误的异常状况，并且将错误信息储存起来 |
| [<c:if>](http://www.runoob.com/jsp/jstl-core-if-tag.html) | 与我们在一般程序中用的if一样 |
| [<c:choose>](http://www.runoob.com/jsp/jstl-core-choose-tag.html) | 本身只当做<c:when>和<c:otherwise>的父标签 |
| [<c:when>](http://www.runoob.com/jsp/jstl-core-choose-tag.html) | <c:choose>的子标签，用来判断条件是否成立 |
| [<c:otherwise>](http://www.runoob.com/jsp/jstl-core-choose-tag.html) | <c:choose>的子标签，接在<c:when>标签后，当<c:when>标签判断为false时被执行 |
| [<c:import>](http://www.runoob.com/jsp/jstl-core-import-tag.html) | 检索一个绝对或相对 URL，然后将其内容暴露给页面 |
| [<c:forEach>](http://www.runoob.com/jsp/jstl-core-foreach-tag.html) | 基础迭代标签，接受多种集合类型 |
| [<c:forTokens>](http://www.runoob.com/jsp/jstl-core-foreach-tag.html) | 根据指定的分隔符来分隔内容并迭代输出 |
| [<c:param>](http://www.runoob.com/jsp/jstl-core-param-tag.html) | 用来给包含或重定向的页面传递参数 |
| [<c:redirect>](http://www.runoob.com/jsp/jstl-core-redirect-tag.html) | 重定向至一个新的URL. |
| [<c:url>](http://www.runoob.com/jsp/jstl-core-url-tag.html) | 使用可选的查询参数来创造一个URL |
#### 格式化标签
 JSTL格式化标签用来格式化并输出文本、日期、时间、数字。引用格式化标签库的语法如下：
```
<%@ taglib prefix="fmt" 
           uri="http://java.sun.com/jsp/jstl/fmt" %>
```
| 标签 | 描述 |
| :- | :- |
| [<fmt:formatNumber>](http://www.runoob.com/jsp/jstl-format-formatnumber-tag.html) | 使用指定的格式或精度格式化数字 |
| [<fmt:parseNumber>](http://www.runoob.com/jsp/jstl-format-parsenumber-tag.html) | 解析一个代表着数字，货币或百分比的字符串 |
| [<fmt:formatDate>](http://www.runoob.com/jsp/jstl-format-formatdate-tag.html) | 使用指定的风格或模式格式化日期和时间 |
| [<fmt:parseDate>](http://www.runoob.com/jsp/jstl-format-parsedate-tag.html) | 解析一个代表着日期或时间的字符串 |
| [<fmt:bundle>](http://www.runoob.com/jsp/jstl-format-bundle-tag.html) | 绑定资源 |
| [<fmt:setLocale>](http://www.runoob.com/jsp/jstl-format-setlocale-tag.html) | 指定地区 |
| [<fmt:setBundle>](http://www.runoob.com/jsp/jstl-format-setbundle-tag.html) | 绑定资源 |
| [<fmt:timeZone>](http://www.runoob.com/jsp/jstl-format-timezone-tag.html) | 指定时区 |
| [<fmt:setTimeZone>](http://www.runoob.com/jsp/jstl-format-settimezone-tag.html) | 指定时区 |
| [<fmt:message>](http://www.runoob.com/jsp/jstl-format-message-tag.html) | 显示资源配置文件信息 |
| [<fmt:requestEncoding>](http://www.runoob.com/jsp/jstl-format-requestencoding-tag.html) | 设置request的字符编码 |


 #### SQL标签

JSTL SQL标签库提供了与关系型数据库（Oracle，MySQL，SQL Server等等）进行交互的标签。引用SQL标签库的语法如下：
```
<%@ taglib prefix="sql" 
           uri="http://java.sun.com/jsp/jstl/sql" %>
```

| 标签 | 描述 |
| :- | :- |
| [<sql:setDataSource>](http://www.runoob.com/jsp/jstl-sql-setdatasource-tag.html) | 指定数据源 |
| [<sql:query>](http://www.runoob.com/jsp/jstl-sql-query-tag.html) | 运行SQL查询语句 |
| [<sql:update>](http://www.runoob.com/jsp/jstl-sql-update-tag.html) | 运行SQL更新语句 |
| [<sql:param>](http://www.runoob.com/jsp/jstl-sql-param-tag.html) | 将SQL语句中的参数设为指定值 |
| [<sql:dateParam>](http://www.runoob.com/jsp/jstl-sql-dateparam-tag.html) | 将SQL语句中的日期参数设为指定的java.util.Date 对象值 |
| [<sql:transaction>](http://www.runoob.com/jsp/jstl-sql-transaction-tag.html) | 在共享数据库连接中提供嵌套的数据库行为元素，将所有语句以一个事务的形式来运行 |

## XML 标签

JSTL XML标签库提供了创建和操作XML文档的标签。引用XML标签库的语法如下：

```
<%@ taglib prefix="x" 
           uri="http://java.sun.com/jsp/jstl/xml" %>
```

在使用xml标签前，你必须将XML 和 XPath 的相关包拷贝至你的<Tomcat 安装目录>\lib下:

*   **XercesImpl.jar**

    下载地址： [http://www.apache.org/dist/xerces/j/](http://www.apache.org/dist/xerces/j/)

*   **xalan.jar**

    下载地址： [http://xml.apache.org/xalan-j/index.html](http://xml.apache.org/xalan-j/index.html)

| 标签 | 描述 |
| :- | :- |
| [<x:out>](http://www.runoob.com/jsp/jstl-xml-out-tag.html) | 与<%= ... >,类似，不过只用于XPath表达式 |
| [<x:parse>](http://www.runoob.com/jsp/jstl-xml-parse-tag.html) | 解析 XML 数据 |
| [<x:set>](http://www.runoob.com/jsp/jstl-xml-set-tag.html) | 设置XPath表达式 |
| [<x:if>](http://www.runoob.com/jsp/jstl-xml-if-tag.html) | 判断XPath表达式，若为真，则执行本体中的内容，否则跳过本体 |
| [<x:forEach>](http://www.runoob.com/jsp/jstl-xml-foreach-tag.html) | 迭代XML文档中的节点 |
| [<x:choose>](http://www.runoob.com/jsp/jstl-xml-choose-tag.html) | <x:when>和<x:otherwise>的父标签 |
| [<x:when>](http://www.runoob.com/jsp/jstl-xml-choose-tag.html) | <x:choose>的子标签，用来进行条件判断 |
| [<x:otherwise>](http://www.runoob.com/jsp/jstl-xml-choose-tag.html) | <x:choose>的子标签，当<x:when>判断为false时被执行 |
| [<x:transform>](http://www.runoob.com/jsp/jstl-xml-transform-tag.html) | 将XSL转换应用在XML文档中 |
| [<x:param>](http://www.runoob.com/jsp/jstl-xml-param-tag.html) | 与<x:transform>共同使用，用于设置XSL样式表 |


 #### JSTL函数

JSTL包含一系列标准函数，大部分是通用的字符串处理函数。引用JSTL函数库的语法如下：
```
<%@ taglib prefix="fn" 
           uri="http://java.sun.com/jsp/jstl/functions" %>
```

| 函数 | 描述 |
| :- | :- |
| [fn:contains()](http://www.runoob.com/jsp/jstl-function-contains.html) | 测试输入的字符串是否包含指定的子串 |
| [fn:containsIgnoreCase()](http://www.runoob.com/jsp/jstl-function-containsignoreCase.html) | 测试输入的字符串是否包含指定的子串，大小写不敏感 |
| [fn:endsWith()](http://www.runoob.com/jsp/jstl-function-endswith.html) | 测试输入的字符串是否以指定的后缀结尾 |
| [fn:escapeXml()](http://www.runoob.com/jsp/jstl-function-escapexml.html) | 跳过可以作为XML标记的字符 |
| [fn:indexOf()](http://www.runoob.com/jsp/jstl-function-indexof.html) | 返回指定字符串在输入字符串中出现的位置 |
| [fn:join()](http://www.runoob.com/jsp/jstl-function-join.html) | 将数组中的元素合成一个字符串然后输出 |
| [fn:length()](http://www.runoob.com/jsp/jstl-function-length.html) | 返回字符串长度 |
| [fn:replace()](http://www.runoob.com/jsp/jstl-function-replace.html) | 将输入字符串中指定的位置替换为指定的字符串然后返回 |
| [fn:split()](http://www.runoob.com/jsp/jstl-function-split.html) | 将字符串用指定的分隔符分隔然后组成一个子字符串数组并返回 |
| [fn:startsWith()](http://www.runoob.com/jsp/jstl-function-startswith.html) | 测试输入字符串是否以指定的前缀开始 |
| [fn:substring()](http://www.runoob.com/jsp/jstl-function-substring.html) | 返回字符串的子集 |
| [fn:substringAfter()](http://www.runoob.com/jsp/jstl-function-substringafter.html) | 返回字符串在指定子串之后的子集 |
| [fn:substringBefore()](http://www.runoob.com/jsp/jstl-function-substringbefore.html) | 返回字符串在指定子串之前的子集 |
| [fn:toLowerCase()](http://www.runoob.com/jsp/jstl-function-tolowercase.html) | 将字符串中的字符转为小写 |
| [fn:toUpperCase()](http://www.runoob.com/jsp/jstl-function-touppercase.html) | 将字符串中的字符转为大写 |
| [fn:trim()](http://www.runoob.com/jsp/jstl-function-trim.html) | 移除首位的空白符 |

#### JSTL 中常用的标签
**1.逻辑判断标签(if,choose-when-otherwise)**
- **<c:if> 标签**
- 语法格式
```
<c:if test="<boolean>" var="<string>" scope="<string>">
   ...
</c:if>
```
-  属性
<c:if>标签有如下属性：

属性 |	描述 	 |	是否必要 |	 	默认值
:- |	:- |	:- |	:- |	
test |	 	条件 	 |	是 	 |	无
var |	 	用于存储条件结果的变量 	 |	否 	 |	无
scope 	 |	var属性的作用域 |	 	否 	 |	page
- **演示实例：**
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>c:if 标签实例</title>
</head>
<body>
<c:set var="salary" scope="session" value="${2000*2}"/>
<c:if test="${salary > 2000}">
   <p>我的工资为: <c:out value="${salary}"/><p>
</c:if>
</body>
</html>
```
运行结果如下：
```
我的工资为: 4000
```

- **<c:choose>, <c:when>, <c:otherwise> 标签**
```<c:choose>```标签与 Java switch 语句的功能一样，用于在众多选项中做出选择。
switch 语句中有 case ，而```<c:choose>```标签中对应有 `<c:when>`，switch语句中有 default，而`<c:choose>`标签中有`<c:otherwise>`。
- 语法格式
```
<c:choose>
    <c:when test="<boolean>">
        ...
    </c:when>
    <c:when test="<boolean>">
        ...
    </c:when>
    ...
    ...
    <c:otherwise>
        ...
    </c:otherwise>
</c:choose>
```
- 属性
`<c:choose>`标签没有属性。
`<c:when>`标签只有一个属性，在下表中有给出。
`<c:otherwise>`标签没有属性。
`<c:when>`标签的属性如下：

属性 | 	描述 	 | 	是否必要 	 | 	默认值
:-  | 	:-  | 	:-  | 	:-  | 	
test 	 | 	条件 | 	 	是 	 | 	无

- 实例演示
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>c:choose 标签实例</title>
</head>
<body>
<c:set var="salary" scope="session" value="${2000*2}"/>
<p>你的工资为 : <c:out value="${salary}"/></p>
<c:choose>
    <c:when test="${salary <= 0}">
       太惨了。
    </c:when>
    <c:when test="${salary > 1000}">
       不错的薪水，还能生活。
    </c:when>
    <c:otherwise>
        什么都没有。
    </c:otherwise>
</c:choose>
</body>
</html>
```
运行结果如下：
```
你的工资为 : 4000

不错的薪水，还能生活。
```

**2.循环遍历标签(foreach)**
- **<c:forEach>标签**
- 语法格式
```
<c:forEach
    items="<object>"
    begin="<int>"
    end="<int>"
    step="<int>"
    var="<string>"
    varStatus="<string>">

    ...
```
-  属性
`<c:forEach>`标签有如下属性：

属性 |	描述 	|	是否必要|	 	默认值
:- |	:- |	:- |	:- |	
items 	|	要被循环的信息 	|	否 	|	无
begin 	|	开始的元素（0=第一个元素，1=第二个元素）|	 	否 	|	0
end 	|	最后一个元素（0=第一个元素，1=第二个元素）|	 	否 	|	Last element
step |		每一次迭代的步长|	 	否 	|	1
var 	|	代表当前条目的变量名称 	|	否 	|	无
varStatus |		代表循环状态的变量名称 	|	否 	|	无

- 实例演示
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>c:forEach 标签实例</title>
</head>
<body>
<c:forEach var="i" begin="1" end="5">
   Item <c:out value="${i}"/><p>
</c:forEach>
</body>
</html>
```
运行结果如下：
```
Item 1
Item 2
Item 3
Item 4
Item 5
```

**3.在 JSP 中实现日期的格式化:**
- **<fmt:formatDate> 标签**
- 语法格式
```
<fmt:formatDate
  value="<string>"
  type="<string>"
  dateStyle="<string>"
  timeStyle="<string>"
  pattern="<string>"
  timeZone="<string>"
  var="<string>"
  scope="<string>"/>
```
- 属性
`<fmt:formatDate>`标签有如下属性：

属性	| 描述 	| 是否必要	| 默认值
:- | :- | :- | :- | 
value	| 要显示的日期	| 是	| 无
type	| DATE, TIME, 或 BOTH	| 否	| date
dateStyle	| FULL, LONG, MEDIUM, SHORT, 或 DEFAULT	| 否	| default
timeStyle	| FULL, LONG, MEDIUM, SHORT, 或 DEFAULT	| 否	| default
pattern	| 自定义格式模式	| 否	| 无
timeZone	| 显示日期的时区	| 否	| 默认时区
var	| 存储格式化日期的变量名	| 否	| 显示在页面
scope	| 存储格式化日志变量的范围	| 否	| 页面

- 实例演示
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>JSTL fmt:dateNumber 标签</title>
</head>
<body>
<h3>日期格式化:</h3>
<c:set var="now" value="<%=new java.util.Date()%>" />

<p>日期格式化 (1): <fmt:formatDate type="time" 
            value="${now}" /></p>
<p>日期格式化 (2): <fmt:formatDate type="date" 
            value="${now}" /></p>
<p>日期格式化 (3): <fmt:formatDate type="both" 
            value="${now}" /></p>
<p>日期格式化 (4): <fmt:formatDate type="both" 
            dateStyle="short" timeStyle="short" 
            value="${now}" /></p>
<p>日期格式化 (5): <fmt:formatDate type="both" 
            dateStyle="medium" timeStyle="medium" 
            value="${now}" /></p>
<p>日期格式化 (6): <fmt:formatDate type="both" 
            dateStyle="long" timeStyle="long" 
            value="${now}" /></p>
<p>日期格式化 (7): <fmt:formatDate pattern="yyyy-MM-dd" 
            value="${now}" /></p>

</body>
</html>
```
以上实例运行结果：
```
日期格式化:

日期格式化 (1): 11:19:43

日期格式化 (2): 2016-6-26

日期格式化 (3): 2016-6-26 11:19:43

日期格式化 (4): 16-6-26 上午11:19

日期格式化 (5): 2016-6-26 11:19:43

日期格式化 (6): 2016年6月26日 上午11时19分43秒

日期格式化 (7): 2016-06-26
```

---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693