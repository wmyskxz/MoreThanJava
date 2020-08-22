![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/Java14版本特性【一文了解】/image-20200822180206407.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# 特性总览

以下是 Java 14 中的引入的部分新特性。关于 Java 14 新特性更详细的介绍可参考[这里](http://openjdk.java.net/projects/jdk/14/)。

语言及特性更改：

- Switch 表达式-标准（JEP 361）
- instanceof 的模式匹配-预览（JEP 305）
- 有用的 NullPointerExceptions（JEP 358）
- record-预览（JEP 359）
- 文本块-预览（JEP 368）

JVM 更改：

- 针对 G1 NUMA 感知内存分配的优化（JEP 345）
- 删除并发标记扫描(CMS)垃圾收集器（JEP 363）
- JFR 事件流（JEP 349）
- macOS 上的 ZGC-实验性（JEP 364）
- Windows 上的 ZGC-实验性（JEP 365）
- 弃用 ParallelScavenge + SerialOld 的 GC 组合（JEP 366）

其他特性：

- 打包工具（JEP 343）
- 非易失性映射字节缓冲区（JEP 352）
- 弃用 Solaris 和 SPARC 端口（JEP 362）
- 删除 Pack200 工具和 API（JEP 367）
- 外部存储器访问 API（JEP 370）

# 一. Switch 表达式-标准（JEP 361）

在上两个版本中保留的预留功能，如今终于在 Java 14 中获得了永久性的地位。

- [Java 12](https://www.wmyskxz.com/2020/08/22/java12-ban-ben-te-xing-xiang-jie/) 为表达是引入了 Lambda 语法，从而允许使用多个大小写标签进行模式匹配，并防止出现导致冗长代码的错误。它还强制执行穷尽情况，如果没有涵盖所有输入情况，则会抛出编译错误。

- [Java 13](https://www.wmyskxz.com/2020/08/22/java13-ban-ben-te-xing-xiang-jie/) 在第二个预览版本使用了 `yield` 替代了原有的 `break` 关键字来返回表达式的返回值。

Java 14 现在终于使这些功能成为了标准：

```java
String result = switch (day) {
    case "M", "W", "F" -> "MWF";
    case "T", "TH", "S" -> "TTS";
    default -> {
        if (day.isEmpty()) {
            yield "Please insert a valid day.";
        } else {
            yield "Looks like a Sunday.";
        }
    }
};
System.out.println(result);
```

*注意，`yield` 不是 Java 中的新关键字，它仅用于 Switch 表达式中。*

# 二. instanceof 的模式匹配-预览（JEP 305）

在 Java 14 之前，我们用于 `instanceof-and-cast` 检查对象的类型并将其转换为变量。

```java
if (obj instanceof String) {        // instanceof
  String s = (String) obj;          // cast
  if("jdk14".equalsIgnoreCase(s)){
      //...
  }
}else {
	   System.out.println("not a string");
}
```

现在，在 Java 14 中，我们可以像这样重构上面的代码：

```java
if (obj instanceof String s) {      // instanceof, cast and bind variable in one line.
    if("jdk4".equalsIgnoreCase(s)){
        //...
    }
}else {
	   System.out.println("not a string");
}
```

如果 `obj` 是的实例 `String`，则将其 `String` 强制转换为绑定变量并分配给该绑定变量  `s`。

# 三. 有用的 NullPointerExceptions（JEP 358）

空指针异常是任何开发人员的噩梦。以前，直到 Java 13 为止，调试臭名昭著的 NPE 都很棘手。开发人员不得不依靠其他调试工具，或者手动计算为空的变量/ 方法，因为堆栈跟踪只会显示行号。

在 Java 14 之前：

```java
String name = jd.getBlog().getAuthor()
 
//Stacktrace
Exception in thread "main" java.lang.NullPointerException
    at NullPointerExample.main(NullPointerExample.java:5)
```

Java 14 引入了新的 JVM 功能（带`-XX:+ShowCodeDetailsInExceptionMessages`选项），它通过更具描述性的堆栈提供了更好的见解，如下所示：

```bash
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "Blog.getAuthor()" because the return value of "Journaldev.getBlog()" is null
    at NullPointerExample.main(NullPointerExample.java:4)
```

**注意**：以上功能不是语言功能。这是对运行时环境的增强。

# 四. record-预览（JEP 359）

`record` 是存储纯数据的数据类型。引入 `record` 背后的想法是快速创建没有样板代码的简单简洁类。*（这有点类似 Kotlin 中的数据类型，这也是为什么有言论说 [Java 逐渐 "Kotlin 化"](https://www.infoq.cn/article/YgPRotsrI1qSr8cxRGlq) 的原因之一）*

通常，Java 中的类需要您实现 `equals()`、`hashCode()`、`getters` 和 `setters` 方法。虽然某些 IDE 支持此类的自动生成，但是代码仍然很冗长。使用 `record` 您只需按照以下方式定义一个类。

```java
record Author(){}
//or
record Author (String name, String topic) {}
```

Java 编译器将自动生成一个带有构造函数、私有 `final` 字段、访问器和 `equals`、 `hashCode `和 `toString` 方法的类。上一类的自动生成的 `getter` 方法是 `name()` 和 `topic()`。

编译之后，我们可以查看上面 `record Author (String name, String topic){}` 语句，编译器为我们自动生成的类：

```java
final class Author extends java.lang.Record {
    private final java.lang.String name;
    private final java.lang.String topic;

    public Author(java.lang.String name, java.lang.String topic) { /* compiled code */ }

    public java.lang.String toString() { /* compiled code */ }

    public final int hashCode() { /* compiled code */ }

    public final boolean equals(java.lang.Object o) { /* compiled code */ }

    public java.lang.String name() { /* compiled code */ }

    public java.lang.String topic() { /* compiled code */ }
}
```

此外，我们可以通过以下方式向记录添加其他字段，方法和构造函数：

```java
record Author (int id, String name, String topic) {
    static int followers;
 
    public static String followerCount() {
        return "Followers are "+ followers;
    }
 
    public String description(){
        return "Author "+ name + " writes on "+ topic;
    }
 
    public Author{
    if (id < 0) {
        throw new IllegalArgumentException( "id must be greater than 0.");
     }
   }
}
```

`record` 内定义的其他构造函数称为 Compact 构造函数。它不包含任何参数，只是规范本身构造函数的参数。

关于 `record` 需要注意的几件事：

- `record` 既不能扩展一个类，也不能被另一个类扩展。这是一个 `final class`。
- `record` 不能是抽象的。
- `record` 不能扩展任何其他类，也不能在主体内定义实例字段。实例字段只能在状态描述中定义。
- 声明的字段是私有字段和 `final` 字段。
- `record` 定义中允许使用静态字段和方法。

## record 类的引用字段内的值可以被改变

值得注意的是，对于定义为对象的字段，只有引用是不可变的。底层值可以修改。

下面显示了修改 ArrayList 的一条记录。可以看到，每当 ArrayList 被更改时，该值都会被修改。

```bash
jshell> record Author(String name, List<String> topics){}
|  已创建 记录 Author

jshell> var topicList = new ArrayList<String>(Arrays.asList("Java"));
topicList ==> [Java]

jshell> var author = new Author("Wmyskxz", topicList);
author ==> Author[name=Wmyskxz, topics=[Java]]

jshell> topicList.add("Python");
$6 ==> true

jshell> author.topics();
$7 ==> [Java, Python]

jshell>
```

## record 能实现接口

下面的代码显示了一个实现有记录接口的示例:

```java
interface Information {
  String getFullName();
}

record Author(String name, String topic) implements Information {
  public String getFullName() {
    return "Author "+ name + " writes on " + topic;
  }
}
```

## record 支持多个构造函数

记录允许声明多个有或没有参数的构造函数，如下所示:

```java
record Author(String name, String topic) {
  public Author() {
 
    this("NA", "NA");
  }
 
  public Author(String name) {
 
    this(name, "NA");
  }
}
```

## record 允许修改访问器方法

虽然 `record` 为状态描述中定义的字段生成公共访问方法，但它们也允许您在主体中重新定义访问方法，如下所示：

```java
record Author(String name, String topic) {
  public String name() {
        return "This article was written by " + this.name;
    }
}
```

## 在运行时检查 record 及其组件

`record` 为我们提供了 `isRecord()` 和 `getRecordComponents()` 来检查类是否是一条记录，并查看它的字段和类型。下面展示了它是如何做到的：

```java
jshell> record Author(String name, String topic){}
|  已创建 记录 Author

jshell> var author = new Author("Wmyskxz", "MoreThanJava");
author ==> Author[name=Wmyskxz, topic=MoreThanJava]

jshell> author.getClass().isRecord();
$3 ==> true

jshell> author.getClass().getRecordComponents();
$4 ==> RecordComponent[2] { java.lang.String name, java.lang.String topic }

jshell>
```

>**Tips**：虽然我们在上面的代码示例中向记录添加了额外的字段和方法，但请确保不要做得过火。记录被设计为普通的数据载体，如果您想实现许多其他方法，最好回到常规类。

# 五. 文本块-预览（JEP 368）

文本块是 [Java 13](https://www.wmyskxz.com/2020/08/22/java13-ban-ben-te-xing-xiang-jie/) 中的预览功能，其目的是允许轻松创建多行字符串文字。在轻松创建 HTML 和 JSON 或 SQL 查询字符串时很有用。

在 Java 14 中，文本块仍在预览中，并增加了一些新功能。我们现在可以使用：

- `\` 反斜杠用于显示美观的多行字符串块。
- `\s` 用于考虑尾随空格，默认情况下编译器会忽略它们。它保留了前面的所有空间。

```java
String text = """
                Did you know \
                Java 14 \
                has the most features among\
                all non-LTS versions so far\
                """;

String text2 = """
                line1
                line2 \s
                line3
                """;
 
String text3 = "line1\nline2 \nline3\n"

//text2 and text3 are equal.
```

# 六. 针对 G1 NUMA 感知内存分配的优化（JEP 345）

新的 [NUMA感知内存](https://en.wikipedia.org/wiki/Non-uniform_memory_access) 分配模式提高了大型计算机上的 G1 性能。添加 `+XX:+UseNUMA` 选项以启用它。

# 七. 删除并发标记扫描(CMS)垃圾收集器（JEP 363）

[Java 9 – JEP 291](https://openjdk.java.net/jeps/291) 已弃用此并发标记扫描（CMS）垃圾收集器，现在正式将其删除。

```bash
/usr/lib/jvm/jdk-14/bin/java -XX:+UseConcMarkSweepGC Test

OpenJDK 64-Bit Server VM warning: Ignoring option UseConcMarkSweepGC; support was removed in 14.0
```

# 八. JFR 事件流（JEP 349）

JDK Flight Recorder（JFR）是用于收集有关正在运行的 Java 应用程序的诊断和性能分析数据的工具。通常，我们开始记录，停止记录，然后将记录的事件转储到磁盘以进行分析，它可以很好地进行概要分析，分析或调试。

该 JEP 改进了现有的 JFR 以支持事件流，这意味着现在我们可以实时传输 JFR 事件，而无需将记录的事件转储到磁盘并手动解析它。

# 九. macOS 上的 ZGC-实验性（JEP 364）

[Java 11 – JEP 333](https://openjdk.java.net/jeps/333) 在 Linux 上引入了 Z 垃圾收集器（ZGC），现在可移植到 macOS。

# 十. Windows 上的 ZGC-实验性（JEP 365）

[Java 11 – JEP 333](https://openjdk.java.net/jeps/333) 在 Linux 上引入了 Z 垃圾收集器（ZGC），现在可移植到 Windows版本 >= 1803 的机器上。

# 十一. 弃用 ParallelScavenge + SerialOld 的 GC 组合（JEP 366）

由于较少的使用和大量的维护工作，Java 14 不赞成使用并行年轻代和串行老一代 GC 算法的组合。

```bash
/usr/lib/jvm/jdk-14/bin/java -XX:-UseParallelOldGC Test

OpenJDK 64-Bit Server VM warning: Option UseParallelOldGC was deprecated in version 14.0 and will likely be removed in a future release.
```

# 十二. 其他特性

## 打包工具（JEP 343）

`jpackage `是将 Java 应用程序打包到特定于平台的程序包中的新工具。

- Linux：deb 和 rpm
- macOS：pkg 和 dmg
- Windows：MSI 和 EXE

例如，将 `JAR` 文件打包到支持 `exe` 的 Windows 平台上。

## 非易失性映射字节缓冲区（JEP 352）

改进的 `FileChannel` API 可创建 `MappedByteBuffer` 对 [非易失性存储器（NVM）的](https://en.wikipedia.org/wiki/Non-volatile_memory) 访问，该存储器即使在关闭电源后也可以检索存储的数据。例如，此功能可确保将可能仍在高速缓存中的所有更改写回到内存中。

*ps：仅 Linux / x64 和 Linux / AArch64 OS 支持此功能！*

## 弃用 Solaris 和 SPARC 端口（JEP 362）

不再支持 Solaris / SPARC，Solaris / x64 和 Linux / SPARC 端口，更少的平台支持意味着更快地交付新功能。

## 删除 Pack200 工具和 API（JEP 367）

[Java 11 – JEP 336](https://openjdk.java.net/jeps/336) 不赞成使用 `pack200` 和 `unpack200` 工具，以及软件包中的 `Pack200` API  `java.util.jar`，现在正式将其删除。

## 外部存储器访问 API（JEP 370）

孵化器模块，允许 Java API 访问 Java 堆外部的外部内存。

外部存储器访问 API 引入了三个主要抽象：

- MemorySegment：提供对具有给定范围的连续内存区域的访问。
- MemoryAddress：提供到 MemorySegment 的偏移量（基本上是一个指针）。
- MemoryLayout：提供一种描述内存段布局的方法，该方法大大简化了使用 `var` 句柄访问 MemorySegment 的过程。使用此方法，不必根据内存的使用方式来计算偏移量。例如，一个整数或长整数数组的偏移量将有所不同，但将使用 MemoryLayout 透明地对其进行处理。

**下面是一个例子：**

```java
import jdk.incubator.foreign.*;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

public class Test {

    public static void main(String[] args) {

	     VarHandle intHandle = MemoryHandles.varHandle(int.class, ByteOrder.nativeOrder());

        try (MemorySegment segment = MemorySegment.allocateNative(1024)) {

            MemoryAddress base = segment.baseAddress();

            System.out.println(base);                 // print memory address

	          intHandle.set(base, 999);                 // set value 999 into the foreign memory

            System.out.println(intHandle.get(base));  // get the value from foreign memory
        }
    }
}
```

编译并使用孵化器模块运行 `jdk.incubator.foreign`。

```bash
$ /usr/lib/jvm/jdk-14/bin/javac --add-modules jdk.incubator.foreign Test.java

warning: using incubating module(s): jdk.incubator.foreign
1 warning

$ /usr/lib/jvm/jdk-14/bin/java --add-modules jdk.incubator.foreign Test

WARNING: Using incubator modules: jdk.incubator.foreign
MemoryAddress{ region: MemorySegment{ id=0x4aac6dca limit: 1024 } offset=0x0 }
999
```

>进一步阅读：官方文档 - https://download.java.net/java/GA/jdk14/docs/api/jdk.incubator.foreign/jdk/incubator/foreign/package-summary.html

# 参考资料

1. OpenJDK 官方说明 - http://openjdk.java.net/projects/jdk/14/
2. What is new in Java 14 - https://mkyong.com/java/what-is-new-in-java-14/
3. Java 14 Features | JournalDev - https://www.journaldev.com/37273/java-14-features

# 文章推荐

1. 这都JDK15了，JDK7还不了解？ - https://www.wmyskxz.com/2020/08/18/java7-ban-ben-te-xing-xiang-jie/
2. 全网最通透的 Java 8 版本特性讲解 - https://www.wmyskxz.com/2020/08/19/java8-ban-ben-te-xing-xiang-jie/
3. Java9的这些史诗级更新你都不知道？ - https://www.wmyskxz.com/2020/08/20/java9-ban-ben-te-xing-xiang-jie/
4. 你想了解的 JDK 10 版本更新都在这里 - https://www.wmyskxz.com/2020/08/21/java10-ban-ben-te-xing-xiang-jie/
5. 这里有你不得不了解的 Java 11 特性 - https://www.wmyskxz.com/2020/08/22/java11-ban-ben-te-xing-xiang-jie/
6. Java 12 版本特性【一文了解】 - https://www.wmyskxz.com/2020/08/22/java12-ban-ben-te-xing-xiang-jie/
7. Java 13 版本特性【一文了解】 - https://www.wmyskxz.com/2020/08/22/java13-ban-ben-te-xing-xiang-jie/
8. 「MoreThanJava」系列文集 - https://www.wmyskxz.com/categories/MoreThanJava/

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！