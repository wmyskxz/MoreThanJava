![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/Java13版本特性【一文了解】/image-20200822162421170.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# 特性总览

以下是 Java 13 中的引入的部分新特性。关于 Java 13 新特性更详细的介绍可参考[这里](http://openjdk.java.net/projects/jdk/13/)。

- 文本块-预览（JEP 355）
- 文本块的字符串类新方法
- 开关表达式-预览（JEP 354）
- 重新实现旧版套接字 API（JEP 353）
- 动态 CDS 存档（JEP 350）
- ZGC：取消提交未使用的内存（JEP 351）
- FileSystems.newFileSystem() 方法
- 具有命名空间支持的 DOM 和 SAX 工厂

# 一. 文本块-预览（JEP 355）

这是预览功能。它使我们能够轻松地创建多行字符串。多行字符串必须写在一对三重双引号内。

使用文本块创建的字符串对象没有其他属性。这是创建多行字符串的简便方法。我们不能使用文本块来创建单行字符串。另外，开头的三重双引号后必须跟一个行终止符。

在 Java 13 之前：

```java
String html ="<html>\n" +
			  "   <body>\n" +
			  "      <p>Hello, World</p>\n" +
			  "   </body>\n" +
			  "</html>\n";


String json ="{\n" +
			  "   \"name\":\"mkyong\",\n" +
			  "   \"age\":38\n" +
			  "}\n";
```

Java 13：

```java
String html =  """
               <html>
                   <body>
                       <p>Hello, World</p>
                   </body>
               </html>
				        """;

String json = """
               {
                   "name":"mkyong",
                   "age":38
               }
               """;
```

# 二. 文本块的字符串类新方法

String 类中有三个与文本块功能关联的新方法。

1. `formatted(Object…args)`：它类似于 String `format()` 方法。添加它是为了支持文本块的格式化。
2. `stripIndent()`：用于从文本块中的每一行的开头和结尾删除附带的空格字符。文本块使用此方法，并保留内容的相对缩进。
3. `translateEscapes()`：返回一个值为该字符串的字符串，其转义序列就像在字符串文字中一样进行翻译。

```java
package com.journaldev.java13.examples;

public class StringNewMethods {

	/***
	 * New methods are to be used with Text Block Strings
	 * @param args
	 */
	@SuppressWarnings("preview")
	public static void main(String[] args) {
		
		String output = """
			    Name: %s
			    Phone: %d
			    Salary: $%.2f
			    """.formatted("Pankaj", 123456789, 2000.5555);
		
		System.out.println(output);
		
		
		String htmlTextBlock = "<html>   \n"+
				                    "\t<body>\t\t \n"+
				                        "\t\t<p>Hello</p>  \t \n"+
				                    "\t</body> \n"+
				                "</html>";
		System.out.println(htmlTextBlock.replace(" ", "*"));
		System.out.println(htmlTextBlock.stripIndent().replace(" ", "*"));
		
		String str1 = "Hi\t\nHello' \" /u0022 Pankaj\r";
		System.out.println(str1);
		System.out.println(str1.translateEscapes());
		
	}
}
```

**输出：**

```bash
Name: Pankaj
Phone: 123456789
Salary: $2000.56

<html>***
	<body>		*
		<p>Hello</p>**	*
	</body>*
</html>
<html>
	<body>
		<p>Hello</p>
	</body>
</html>
Hi	
Hello' " /u0022 Pankaj
Hi	
Hello' " /u0022 Pankaj
```

# 三. 开关表达式-预览（JEP 354）

Java 12 引入了[JEP 325 Switch表达式](https://openjdk.java.net/jeps/325)。该 JEP 放弃`brea` 关键字而改用 `yield` 关键字从 `switch` 表达式返回值。（其他均与 Java 12 没区别）

```java
// switch expressions, use yield to return, in Java 12 it was break
int x = switch (choice) {
    case 1, 2, 3:
        yield choice;
    default:
        yield -1;
};
```

*（ps：这会在 Java 14 - [JEP 361](https://openjdk.java.net/jeps/361) 中成为标准功能）*

# 四. 重新实现旧版套接字 API（JEP 353）

`java.net.Socket` 和 `java.net.ServerSocket` 的底层实现都很古老，可以追溯到 JDK 1.0，它混合了遗留的 Java 和 C 代码，很难维护和调试。这个 JEP 为套接字 API 引入了新的底层实现，这是 Java 13 中的默认实现。

在 Java 13 之前，`SocketImpl` 使用 `PlainSocketImpl`：

```java
public class ServerSocket implements java.io.Closeable {

    /**
     * The implementation of this Socket.
     */
    private SocketImpl impl;
}
```

Java 13 引入了一个新的 `NioSocketImpl` 类，作为对 `PlainSocketImpl` 的临时替换。但是，如果出现错误，我们仍然可以通过设置 `jdk.net.usePlainSocketImpl` 系统属性切换回旧的实现 `PlainSocketImpl`。

下面👇是一个简单的套接字示例。

```java
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JEP353 {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8888)){

            boolean running = true;
            while(running){
                Socket clientSocket = serverSocket.accept();
                //do something with clientSocket
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

在 Java 13 中，默认实现是 `NioSocketImpl`

```bash
D:\test>javac JEP353.java

D:\test>java JEP353

D:\test>java -XX:+TraceClassLoading JEP353  | findStr Socket

[0.040s][info   ][class,load] java.net.ServerSocket source: jrt:/java.base
[0.040s][info   ][class,load] jdk.internal.access.JavaNetSocketAccess source: jrt:/java.base
[0.040s][info   ][class,load] java.net.ServerSocket$1 source: jrt:/java.base
[0.040s][info   ][class,load] java.net.SocketOptions source: jrt:/java.base
[0.040s][info   ][class,load] java.net.SocketImpl source: jrt:/java.base
[0.044s][info   ][class,load] java.net.SocketImpl$$Lambda$1/0x0000000800ba0840 source: java.net.SocketImpl
[0.047s][info   ][class,load] sun.net.PlatformSocketImpl source: jrt:/java.base

[0.047s][info   ][class,load] sun.nio.ch.NioSocketImpl source: jrt:/java.base

[0.047s][info   ][class,load] sun.nio.ch.SocketDispatcher source: jrt:/java.base
[0.052s][info   ][class,load] java.net.SocketAddress source: jrt:/java.base
[0.052s][info   ][class,load] java.net.InetSocketAddress source: jrt:/java.base
[0.052s][info   ][class,load] java.net.InetSocketAddress$InetSocketAddressHolder source: jrt:/java.base
[0.053s][info   ][class,load] sun.net.ext.ExtendedSocketOptions source: jrt:/java.base
[0.053s][info   ][class,load] jdk.net.ExtendedSocketOptions source: jrt:/jdk.net
[0.053s][info   ][class,load] java.net.SocketOption source: jrt:/java.base
[0.053s][info   ][class,load] jdk.net.ExtendedSocketOptions$ExtSocketOption source: jrt:/jdk.net
[0.053s][info   ][class,load] jdk.net.SocketFlow source: jrt:/jdk.net
[0.053s][info   ][class,load] jdk.net.ExtendedSocketOptions$PlatformSocketOptions source: jrt:/jdk.net
[0.053s][info   ][class,load] jdk.net.ExtendedSocketOptions$PlatformSocketOptions$1 source: jrt:/jdk.net
[0.054s][info   ][class,load] jdk.net.ExtendedSocketOptions$1 source: jrt:/jdk.net
[0.054s][info   ][class,load] sun.nio.ch.NioSocketImpl$FileDescriptorCloser source: jrt:/java.base
[0.055s][info   ][class,load] java.net.Socket source: jrt:/java.base
```

# 五. 动态 CDS 存档（JEP 350）

该 JEP 扩展了 [Java 10](https://www.wmyskxz.com/2020/08/21/java10-ban-ben-te-xing-xiang-jie/) 中引入的类数据共享功能。现在，创建 CDS 存档并使用它要容易得多。

```
$ java -XX:ArchiveClassesAtExit=my_app_cds.jsa -cp my_app.jar

$ java -XX:SharedArchiveFile=my_app_cds.jsa -cp my_app.jar
```

# 六. ZGC：取消提交未使用的内存（JEP 351）

该 JEP 增强了ZGC，可以将未使用的堆内存返回给操作系统。Z垃圾收集器是 [Java 11](https://www.wmyskxz.com/2020/08/22/java11-ban-ben-te-xing-xiang-jie/) 中引入的。它会在堆内存清理之前增加一个短暂的暂停时间。但是，未使用的内存没有返回给操作系统。对于诸如 IoT 和微芯片等内存占用较小的设备，这是一个问题。

现在，它已得到增强，可以将未使用的内存返回给操作系统。

# 七. FileSystems.newFileSystem() 方法

在 FileSystems 类中添加了三种新方法，以便更容易地使用文件系统提供程序，这些提供程序将文件的内容视为文件系统。

1. `newFileSystem(Path)`
2. `newFileSystem(Path, Map<String, ?>)`
3. `newFileSystem(Path, Map<String, ?>, ClassLoader)`

# 八. 具有命名空间支持的 DOM 和 SAX 工厂

有一些新方法可以实例化支持名称空间的 DOM 和 SAX 工厂。

1. `newDefaultNSInstance()`
2. `newNSInstance()`
3. `newNSInstance(String factoryClassName, ClassLoader classLoader)`

```java
//java 13 onwards
DocumentBuilder db = DocumentBuilderFactory.newDefaultNSInstance().newDocumentBuilder(); 

// before java 13
DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance(); 
dbf.setNamespaceAware(true); 
DocumentBuilder db = dbf.newDocumentBuilder();
```

# 参考资料

1. OpenJDK 官方说明 - http://openjdk.java.net/projects/jdk/13/
2. What is new in Java 13 - https://mkyong.com/java/what-is-new-in-java-13/
3. Java 13 Features | JournalDev - https://www.journaldev.com/33204/java-13-features

# 文章推荐

1. 这都JDK15了，JDK7还不了解？ - https://www.wmyskxz.com/2020/08/18/java7-ban-ben-te-xing-xiang-jie/
2. 全网最通透的 Java 8 版本特性讲解 - https://www.wmyskxz.com/2020/08/19/java8-ban-ben-te-xing-xiang-jie/
3. Java9的这些史诗级更新你都不知道？ - https://www.wmyskxz.com/2020/08/20/java9-ban-ben-te-xing-xiang-jie/
4. 你想了解的 JDK 10 版本更新都在这里 - https://www.wmyskxz.com/2020/08/21/java10-ban-ben-te-xing-xiang-jie/
5. 这里有你不得不了解的 Java 11 特性 - https://www.wmyskxz.com/2020/08/22/java11-ban-ben-te-xing-xiang-jie/
6. Java12版本特性【一文了解】 - https://www.wmyskxz.com/2020/08/22/java12-ban-ben-te-xing-xiang-jie/
7. 「MoreThanJava」系列文集 - https://www.wmyskxz.com/categories/MoreThanJava/

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！