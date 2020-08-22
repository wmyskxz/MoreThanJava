![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这里有你不得不了解的Java11特性/image-20200822100501200.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# Java 11 为什么重要？

Java 11 是继 Java 8 之后的第二个 LTS（long-term support）版本。**自 Java 11 起，Oracle JDK 将不再免费提供商业用途**。

您可以在开发阶段使用它，但要在商业上使用它，则需要购买许可证。

Java 10 是最后一个可以下载的免费 Oracle JDK。

Oracle 从 2019 年 1 月开始就停止了对 Java 8 的支持。您需要支付更多的支持费用。

如果不这样做，虽然您可以继续使用它，但不会获得任何补丁/ 安全更新。

> 自 Java 11 起，Oracle 将不再为任何单个 Java 版本提供免费的长期支持（LTS）。

尽管 Oracle JDK 不再免费，但是您始终可以从 Oracle 或其他提供商（例如 AdoptOpenJDK，Azul，IBM，Red Hat 等..）下载 [Open JDK](https://openjdk.java.net/) 构建。

## 什么是 LTS Module

从 2017 年开始，Oracle 和 Java 社区宣布了向 Java 的新 6 个月节奏的转变。它已迁移到 Oracle Java SE 产品的长期支持（LTS）模型。

LTS 版本的产品将提供 Oracle 的首要和持续的支持，目标是每三年一次。

每个 Java 版本都以一两个主要特性为模型，这些特性驱动了版本的发布。任何障碍都会推迟发布和上市时间。Jigsaw 项目就是 Java 9 的一个主要特性，它多次推迟了发布日期，并且发布时间被推迟了超过 `1.5` 年。6 个月一版的发车节奏将让特性紧随。发布的列车每 6 个月有一个时间表。赶上这列火车的特征会被留下，否则他们就等下一班火车。

## Oracle JDK 与 Open JDK

为了对开发人员更加友好，Oracle & Java 社区现在将 OpenJDK 二进制文件作为主要 JDK 进行推广。

这与早期的 JDK 二进制文件是由 Oracle 专有并由 Oracle 许可的模式相比，很大程度上减轻了人们的负担，因为 Oracle 对重新发布有各种限制。

然而，Oracle 将继续生产他们的 JDK，但仅限于长期支持版本。这是朝着对云和容器更友好的方向迈出的一步，因为开放 JDK 二进制文件可以作为容器的一部分分发。

Open JDK 的二进制文件每 6 个月发布一次，而 Oracle JDK 的二进制文件每 3 年发布一次（LTS版本）。

# 特性总览

在了解完了 Java 11 附带的负担之后，现在让我们作为开发人员分析 Java 11 中的重要功能。

以下是 Java 11 中的引入的部分新特性。关于 Java 11 新特性更详细的介绍可参考[这里](http://openjdk.java.net/projects/jdk/11/)。

- 基于嵌套的访问控制（JEP 181）
- 使用单个命令运行Java文件（JEP 330）
- Lambda 参数的局部变量语法（JEP 323）
- 动态类文件常量（JEP 309）
- HTTP 客户端（JEP 321）
- Epsilon-无操作垃圾收集器（JEP 318）
- 可扩展的低延迟垃圾收集器-ZGC（JEP 333）
- Unicode 10（JEP 327）
- 低开销堆分析（JEP 331）
- API 变更
- 其他变更
  - 删除 Java EE 和 CORBA 模块（JEP 320）
  - 飞行记录器（JEP 328）
  - ChaCha20 和 Poly1305 加密算法（JEP 329）
  - 改进 Aarch64 内部特征（JEP 315）
  - 弃用 Nashorn JavaScript 引擎（JEP 335）
  - 传输层安全性（TLS）1.3（JEP 332）
  - 弃用 Pack200 工具和 API（JEP 336）

# 一. 基于嵌套的访问控制（JEP 181）

在 Java 11 之前，从嵌套类访问主类的 `private` 方法是可能的：

```java
public class Main {
 
    public void myPublic() {
    }
 
    private void myPrivate() {
    }
 
    class Nested {

        public void nestedPublic() {
            myPrivate();
        }
    }
}
```

但是，如果我们使用反射，它就会给出一个 `IllegalStateException`：

```bash
jshell> Main ob = new Main();
ob ==> Main@533ddba

jshell> import java.lang.reflect.Method;

jshell> Method method = ob.getClass().getDeclaredMethod("myPrivate");
method ==> private void Main.myPrivate()

jshell> method.invoke(ob);
|  异常错误 java.lang.IllegalAccessException：class REPL.$JShell$15 cannot access a member of class REPL.$JShell$11$Main with modifiers "private"
|        at Reflection.newIllegalAccessException (Reflection.java:376)
|        at AccessibleObject.checkAccess (AccessibleObject.java:647)
|        at Method.invoke (Method.java:556)
|        at (#5:1)

jshell>
```

**这是因为 JVM 访问规则不允许嵌套类之间进行私有访问**。我们能通过第一种方式访问是因为 JVM 在编译时为我们隐式地创建了私有的 **桥接方法**。

而且这发生在幕后。这种桥接方法会稍微增加已部署应用程序的大小，并可能使用户和工具感到困惑。

Java 11 引入嵌套访问控制解决了这一问题。

**Java 11 将嵌套的概念和相关的访问规则引入了JVM**。这简化了 Java 源代码编译器的工作。

为此，类文件格式现在包含两个新属性：

1. 一个嵌套成员（通常是顶级类）被指定为嵌套主类。它包含一个属性（**NestMembers**）来标识其他静态已知的嵌套成员。
2. 其他每个嵌套成员都有一个属性（**NestHost**）来标识其嵌套主类。

因此，要使类型 C 和 D 成为嵌套伙伴，它们必须具有相同的嵌套主类。如果类型 C 在其 NestHost 属性中列出 D，则它声称是 D 托管的嵌套的成员。如果 D 还在其 NestMembers 属性中列出 C ，则将验证成员资格。另外，类型 D 隐式为其所托管的嵌套成员。

现在，**编译器无需生成桥接方法**。

`java.lang.Class` 在反射 API 中介绍了三种方法：`getNestHost()`，`getNestMembers()`，和`isNestmateOf()`，用于支持上述的工作。

>更多请阅读：https://www.baeldung.com/java-nest-based-access-control

# 二. 使用单个命令运行Java文件（JEP 330）

该 JEP 是在学习 Java 早期阶段的一个友好功能，但是在实际的 Java 开发中没有太大的用处，我们都使用 IDE。

假设我们现在有以下的源代码（`.java` 文件）：

```java
public class HelloJava {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

在 Java 11 编译运行需要：

```bash
$ javac HelloJava.java

$ java HelloJava

Hello World!
```

在 Java 11 中：

```bash
$ java HelloJava.java

Hello World!
```

另外，我们也可以使用 Linux Shebang 运行单个的 Java 程序：

```bash
#!/opt/java/openjdk/bin/java --source 11
public class SheBang {

    public static void main(String[] args) {

        System.out.println("Hello World!");

    }
}
```

>这里是在 Docker 中如此使用 Linux Shebang 运行 Java 的例子：https://mkyong.com/java/java-11-shebang-example-in-docker/

# 三. Lambda 参数的局部变量语法（JEP 323）

**该 JEP 是 Java 11 中唯一的语言功能的加强。**

我们知道，在 [Java 10](https://www.wmyskxz.com/2020/08/21/java10-ban-ben-te-xing-xiang-jie/) 中，引入了局部变量类型推断。因此，我们可以从 RHS 推断出变量的类型：`var list = new ArrayList<String>();`

JEP 323 允许 `var` 用于声明隐式类型的 Lambda 表达式的形式参数：

```java
List<String> list = Arrays.asList("关注", "我没有三颗心脏", "更多精彩内容分享");
String result = list.stream()
        .map((var x) -> x.toUpperCase())
        .collect(Collectors.joining(","));
System.out.println(result2);
```

上面与下面这个等效：

```java
List<String> list = Arrays.asList("关注", "我没有三颗心脏", "更多精彩内容分享");
String result = list.stream()
        .map(x -> x.toUpperCase())
        .collect(Collectors.joining(","));
```

**这（省略类型声明的形式）在 Java 8 中也是允许的，但是在 Java 10 中删除了。现在，它又回到 Java 11 中以保持一致。**

为什么支持 `var` 来声明隐式的 Lambda 参数呢？（特别是当我们只需跳过 Lambda 类型时）

答案是如果您想要像 `@NotNull` 一样 **注释参数** 时，则不能在不定义类型的情况下这样做：

```java
import org.jetbrains.annotations.NotNull;

List<String> list = Arrays.asList("关注", "我没有三颗心脏", "更多精彩内容分享", null);
String result = list.stream()
  .map((@NotNull var x) -> x.toUpperCase())
  .collect(Collectors.joining(","));
System.out.println(result3);
```

此功能也有一定的 **局限性**——您必须在所有参数上指定 `var` 类型，或者不指定任何类型。

在用于 Lambda 内部的参数声明中，不可能出现以下这几种情况：

```java
(var s1, s2) -> s1 + s2 //no skipping allowed
(var s1, String y) -> s1 + y //no mixing allowed

var s1 -> s1 //not allowed. Need parentheses if you use var in lambda.
```

# 四. 动态类文件常量（JEP 309）

为了使 JVM 对动态语言更具吸引力，Java SE 7 已将 `invokedynamic` 引入了其指令集。Java 开发人员通常不会注意到此功能，因为它隐藏在 Java 字节码中。

简而言之，通过使用 `invokedynamic`，可以将方法调用的绑定延迟到第一次调用之前。

例如，Java 语言使用此技术来实现 Lambda 表达式，这些表达式仅在首次使用时才需要出现。

如此，`invokedynamic` 已发展成为一种基本的语言功能。在 constantdynamic 中，Java 11 引入了一种类似的机制，只是它延迟的是常数值的创建。

但 Java 11 本身缺少对 constantdynamic 的支持，所以这里不做详细赘述。

> 这篇文章详细讨论了该特性的目的和内部工作原理，并展示了如何使用 Byte Buddy 库生成使用此新指令的代码，感兴趣的可以阅读一下：https://mydailyjava.blogspot.com/2018/08/hands-on-constantdynamic-class-file.html

# 五. HTTP 客户端（JEP 321）

Java 11 标准化了 Http CLient API。

新的 API 支持 HTTP / 1.1 和 HTTP / 2。它旨在提高客户端发送请求和从服务器接收响应的整体性能。它还原生支持 WebSockets。

下面是一个使用 Java 11 HttpClient 发送一个简单 GET 请求的例子：

```java
HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(10))
        .build();

HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create("https://www.wmyskxz.com"))
        .setHeader("User-Agent", "Java 11 HttpClient Bot")
        .build();

HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

HttpHeaders headers = response.headers();
headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

System.out.println(response.statusCode());
System.out.println(response.body());
```

> 关于 HttpClient 更多内容请阅读：https://mkyong.com/java/java-11-httpclient-examples/

# 六. Epsilon-无操作垃圾收集器（JEP 318）

与负责分配内存并释放内存的 JVM GC 不同，Epsilon 仅分配内存。

它为以下内容分配内存：

- 性能测试。
- 内存压力测试。
- VM 接口测试。
- 寿命极短的工作。
- 最后一滴延迟改进。（Last-drop latency improvements.）
- 最终吞吐量提高。

现在，Elipson 仅适用于测试环境。这将导致生产中的 `OutOfMemoryError` 并使应用程序崩溃。

Elipson 的好处是没有内存清除开销。因此，它将给出准确的性能测试结果，我们不再可以通过 GC 来停止它。

*注意：这是一项实验性功能。*

# 七. 可扩展的低延迟垃圾收集器-ZGC（JEP 333）

Z垃圾收集器（ZGC）是可伸缩的低延迟垃圾收集器。ZGC 可以同时执行所有昂贵的工作，而不会将应用程序线程的执行停止超过 `10` 毫秒，这使得它们适合于要求低延迟和/或使用非常大的堆（数TB）的应用程序。

Z 垃圾收集器可作为实验功能使用，可以通过命令行选项启用` -XX:+UnlockExperimentalVMOptions -XX:+UseZGC`。

*(ps：该垃圾收集器在 JDK 15 才生产准备就绪——JEP 377——所以可以不怎么关注它，因为我们大概率很长一段时间都不会用上)*

>OpenJDK-wiki：https://wiki.openjdk.java.net/display/zgc/Main

# 八. Unicode 10（JEP 327）

以下是 Unicode 10.0 发行版中可用的新表情符号：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这里有你不得不了解的Java11特性/emoji-5-sample-images-overview-emojipedia-2017.png)

关于 Unicode 10.0 的更新，你可以在 [这里](http://unicode.org/versions/Unicode10.0.0/) 看到详细的内容，概括起来就是：

> *“ Unicode 10.0增加了8,518个字符，总共136,690个字符。这些增加包括4个新脚本，总共139个脚本，以及56个新表情符号字符。”*

**代码演示：**

```java
public class PrintUnicode {

    public static void main(String[] args) {
        String codepoint = "U+1F92A";   // crazy face
        System.out.println(convertCodePoints(codepoint));
    }

    // Java, UTF-16
    // Convert code point to unicode
    static char[] convertCodePoints(String codePoint) {
        Integer i = Integer.valueOf(codePoint.substring(2), 16);
        char[] chars = Character.toChars(i);
        return chars;

    }
}
```

>**好文推荐：**
>
>在 Java 中使用 Unicode 的乐趣：https://www.codetab.org/post/java-unicode-basics/
>
>该文章详细介绍了编码和解码以及 Unicode 的基础知识，并通过 Java 编程详细的展示了 Unicode 在 Java 中的使用例子。

# 九. 低开销堆分析（JEP 331）

Java 虚拟机工具接口（JVM TI）是在 Java SE 5 引入的，它可以监控 JVM 内部事件的执行，也可以控制 JVM 的某些行为，可以实现调试、监控、线程分析、覆盖率分析工具等。

该 JEP 在 JVM TI 中添加了新的低开销的堆分析 API。

> 进一步阅读：Oracle 官方 JVM TI 文档 - https://docs.oracle.com/javase/8/docs/platform/jvmti/jvmti.html

# 十. API 变更

## 字符串新增方法

### isBlank()

**isBlank()** ——此实例方法返回一个布尔值。空字符串和仅包含空格的字符串将被视为空。

```java
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(" ".isBlank()); //true
        
        String s = "wmyskxz";
        System.out.println(s.isBlank()); //false
        String s1 = "";
        System.out.println(s1.isBlank()); //true
    }
}
```

### lines()

此方法返回字符串流，它是按行分割的所有子字符串的集合。

```bash
jshell> import java.util.stream.Collectors;

jshell> String str = "JD\nJD\nJD";
str ==> "JD\nJD\nJD"

jshell> System.out.println(str);
JD
JD
JD

jshell> System.out.println(str.lines().collect(Collectors.toList()));
[JD, JD, JD]

jshell>
```

### strip() / stripLeading() / stripTrailing()

**strip()**——删除字符串开头和结尾的空格。

**stripLeading()**——删除字符串开头的空格。

**stripTrailing()**——删除字符串结尾的空格。

```bash
jshell> String str = " 我没有三颗心脏 ";
str ==> " 我没有三颗心脏 "

jshell> System.out.print("关注" + str.strip() + "更多精彩内容");
关注我没有三颗心脏更多精彩内容
jshell> System.out.print("关注" + str.stripLeading() + "更多精彩内容");
关注我没有三颗心脏 更多精彩内容
jshell> System.out.print("关注" + str.stripTrailing() + "更多精彩内容");
关注 我没有三颗心脏更多精彩内容
jshell>
```

### repeat(int)

`repeat` 方法简单地以 `int` 形式将字符串重复多次。

```bash
jshell> String str = "关注【我没有三颗心脏】获取更多精彩内容!".repeat(3);
str ==> "关注【我没有三颗心脏】获取更多精彩内容!关注【我没有三颗心脏】获取更多精彩内容!关注【我没有三颗心脏】获取更多精彩内容!"

jshell>
```

## 文件读写字符串

Java 11 致力于 String 的读写变得方便。它引入了以下用于读写文件的方法：

- `readString()`;
- `writeString()`;

以下是代码示例：

```java
Path path = Files.writeString(Files.createTempFile("test", ".txt"), "This was posted on wmyskxz.com");
System.out.println(path);
String s = Files.readString(path);
System.out.println(s); //This was posted on wmyskxz.com
```

# 十一. 其他变更

## 删除 Java EE 和 CORBA 模块（JEP 320）

Java 9 中已经弃用了这些模块，现在将它们完全删除。

下面的包被删除：`java.xml.ws`，`java.xml.bind`，`java.activation`，`java.xml.ws.annotation`，`java.corba`，`java.transaction`，`java.se.ee`，`jdk.xml.ws`，`jdk.xml.bind`

## 飞行记录器（JEP 328）

Flight Recorder 以前是 Oracle JDK 中的商业附加组件，现已开放源代码，因为 Oracle JDK 本身已不再免费。

JFR 是一种分析工具，用于从正在运行的 Java 应用程序中收集诊断信息和分析数据。它的性能开销可以忽略不计，通常低于 `1％`。因此，它可以用于生产应用。

默认情况下，JVM 禁用了 JFR，要启动 JFR，必须使用 `-XX:+FlightRecorder` 选项启动。例如，我们要启动名为 `MyApp` 的应用程序：

```bash
java -XX：+ UnlockCommercialFeatures -XX：+ FlightRecorder MyApp
```

> Java Fliight Recorder 小试牛刀 - https://juejin.im/post/6844903684912988167

## ChaCha20 和 Poly1305 加密算法（JEP 329）

Java 11 提供了 ChaCha20 和 ChaCha20-Poly1305 密码实现。这些算法将在 SunJCE 提供程序中实现。

> 有详细了解需求的朋友可以参看：https://mkyong.com/java/java-11-chacha20-poly1305-encryption-examples/

## 改进 Aarch64 内部特征（JEP 315）

改进现有的字符串和数组内在函数，并在 AArch64 处理器上为 `java.lang.Math` 包下的 `sin`，`cos` 和 `log` 函数实现新的内在函数。

## 弃用 Nashorn JavaScript 引擎（JEP 335)

Nashorn JavaScript脚本引擎和`jjs`工具已被弃用，将来的发行版中可能会删除它。

*(ps：Nashorn 是在 Java 8 [JEP 174](https://openjdk.java.net/jeps/174) 中引入，以代替 Rhino Javascript 引擎。)*

## 传输层安全性（TLS）1.3（JEP 332）

Java 11 支持 [RFC 8446](https://tools.ietf.org/html/rfc8446) 传输层安全性（TLS）1.3协议。但是，并非所有TLS 1.3功能都已实现，有关详细信息，请参考此 [JEP 332](https://openjdk.java.net/jeps/332)。

Java 安全套接字扩展（JSSE）+ TLS 1.3 示例。

```java
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

SSLSocketFactory factory =
        (SSLSocketFactory) SSLSocketFactory.getDefault();
socket =
        (SSLSocket) factory.createSocket("google.com", 443);

socket.setEnabledProtocols(new String[]{"TLSv1.3"});
socket.setEnabledCipherSuites(new String[]{"TLS_AES_128_GCM_SHA256"});
```

## 弃用 Pack200 工具和 API（JEP 336）

该 JEP 不推荐 `pack200` 和 `unpack200` 工具以及软件包中的 `Pack200` API `java.util.jar`，并且可能会在将来的版本中删除。

*（ps：Java 14 [JEP 367](https://openjdk.java.net/jeps/367) 中删除了 Pack200 工具和 API 。）*

# 参考资料

1. OpenJDK 官方说明 - http://openjdk.java.net/projects/jdk/11/
2. Java 11 Features - https://www.journaldev.com/24601/java-11-features
3. What is new in Java 11 - https://mkyong.com/java/what-is-new-in-java-11/
4. Java 11 Nest Based Access Control | Baeldung - https://www.baeldung.com/java-nest-based-access-control

# 文章推荐

1. 这都JDK15了，JDK7还不了解？ - https://www.wmyskxz.com/2020/08/18/java7-ban-ben-te-xing-xiang-jie/
2. 全网最通透的 Java 8 版本特性讲解 - https://www.wmyskxz.com/2020/08/19/java8-ban-ben-te-xing-xiang-jie/
3. Java9的这些史诗级更新你都不知道？ - https://www.wmyskxz.com/2020/08/20/java9-ban-ben-te-xing-xiang-jie/
4. 你想了解的 JDK 10 版本更新都在这里 - https://www.wmyskxz.com/2020/08/21/java10-ban-ben-te-xing-xiang-jie/
5. 「MoreThanJava」系列文集 - https://www.wmyskxz.com/categories/MoreThanJava/

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！