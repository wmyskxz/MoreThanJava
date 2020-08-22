![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/Java12版本特性【一文了解】/image-20200822162133251.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# 特性总览

以下是 Java 12 中的引入的部分新特性。关于 Java 12 新特性更详细的介绍可参考[这里](http://openjdk.java.net/projects/jdk/12/)。

JVM 更改：

- Shenandoah：低暂停时间的垃圾收集器-实验性（JEP 189）
- 及时从 G1 返回未使用的已提交内存（JEP 346）
- 可中止的 G1 混合收集（JEP 344）
- Mincrobenchmark 套件（JEP 230）
- 一个 AArch64 端口，而不是两个（JEP 340）
- 默认 CDS 存档（JEP 341）

语言更改和特性：

- Switch 表达式-预览（JEP  325）
- Teeing Collectors
- 字符串新方法
- JVM 常量 API（JEP 334）
- instanceof 的模式匹配-预览（JEP 305）
- File.mismatch 方法
- 紧凑的数字格式

# 一. Shenandoah:低暂停时间垃圾收集器-实验性（JEP 189）

Shenandoah 是一种新的低暂停和并发垃圾回收器，它减少了GC暂停时间，并且与 Java 堆大小无关（5M 或 5G 的堆大小具有相同的暂停时间，对于大型堆应用程序很有用，请阅读此 [研究论文](https://www.researchgate.net/publication/306112816_Shenandoah_An_open-source_concurrent_compacting_garbage_collector_for_OpenJDK)）。

该 GC 是一项实验性功能，我们需要使用以下选项来启用新的 Shenandoah GC。

```bash
-XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC
```

但是，[Oracle JDK 和 OpenJDK 均不包含此新的 Shenandoah GC](http://mail.openjdk.java.net/pipermail/shenandoah-dev/2018-December/008673.html)。

```bash
C:\Users\wmyskxz> java -version
java version "12" 2019-03-19
Java(TM) SE Runtime Environment (build 12+33)
Java HotSpot(TM) 64-Bit Server VM (build 12+33, mixed mode, sharing)

C:\Users\wmyskxz> java -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC
Error occurred during initialization of VM
Option -XX:+UseShenandoahGC not supported
```

[AdoptOpenJDK 12](https://adoptopenjdk.net/archive.html?variant=openjdk12&jvmVariant=hotspot) 可能包含这种新型的 GC，感兴趣的童鞋可以自行下载体验。

> 进一步阅读：Shenandoah OpenJDK 主页 - https://openjdk.java.net/projects/shenandoah/

# 二. 及时从 G1 返回未使用的已提交内存（JEP 346）

该 JEP 提高了垃圾优先（G1）收集器的性能。如果应用程序的活动少或处于空闲状态，则 G1 定期触发并发周期，以确定总体 Java 堆使用情况，并将未使用的 Java 堆内存返回给操作系统。

# 三. 可中止的G1混合收集（JEP 344）

G1 效率的提高包括：如果 G1 混合收集可能超出定义的暂停目标，则可以将其中止。这是通过将混合集合划分为强制性和可选性来实现的。

因此，G1 收集器可以优先考虑首先收集强制性集合，以满足暂停时间目标。

# 四. Mincrobenchmark 套件（JEP 230）

JDK 源代码中添加了一系列 Java Microbenchmark Harness（JMH）基准测试，对于那些有兴趣添加或修改 JDK 源代码本身的用户，现在他们可以比较性能了。

# 五. 一个 AArch64 端口，而不是两个（JEP 340）

在 Java 12 之前，64 位 [ARM体系结构](https://en.wikipedia.org/wiki/ARM_architecture) 有两个不同的源代码或端口。

- Oracle– `src/hotspot/cpu/arm`
- Red Hat？– `src/hotspot/cpu/aarch64`

Java 12 删除了 Oracle `src/hotspot/cpu/arm` 端口，仅保留了一个端口 `src/hotspot/cpu/aarch64`，并将 `aarch64` 其作为 64 位 ARM 体系结构的默认构建。

# 六. 默认 CDS 存档（JEP 341）

[类数据共享](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/class-data-sharing.html)（Class-Data-Sharing）是在 JDK 5 引入的，允许将一组类预处理成共享的归档文件，然后在运行时对其进行内存映射，以减少启动时间，这还可以在多个 JVM 共享相同的归档文件时减少动态内存占用的功能。

在 Java 12 之前，我们需要 `-Xshare: dump` 用于为 JDK 类生成 CDS 存档文件。在 Java 12 中，目录中有一个新 `classes.jsa` 文件，这是 `/bin/server/` JDK类的默认 CDS 存档文件。

从 Java 12 开始，默认情况下 CSD 为 ON。要在CDS关闭的情况下运行程序，请执行以下操作：

```bash
java -Xshare:off HelloWorld.java
```

# 七. Switch 表达式-预览（JEP  325）

传统的 `switch` 语句，我们可以通过将值分配给变量来返回值：

```java
private static String getText(int number) {
    String result = "";
    switch (number) {
        case 1, 2:
            result = "one or two";
            break;
        case 3:
            result = "three";
            break;
        case 4, 5, 6:
            result = "four or five or six";
            break;
        default:
            result = "unknown";
            break;
    };
    return result;
}
```

在 Java 12 中，我们可以使用 `break` 或 `case L ->` 从开关返回值。

```java
private static String getText(int number) {
    String result = switch (number) {
        case 1, 2:
            break "one or two";
        case 3:
            break "three";
        case 4, 5, 6:
            break "four or five or six";
        default:
            break "unknown";
    };
    return result;
}
```

`case L ->`  语法：

```java
private static String getText(int number) {
    return switch (number) {
        case 1, 2 -> "one or two";
        case 3 -> "three";
        case 4, 5, 6 -> "four or five or six";
        default -> "unknown";
    };
}
```

要启用 Java 12 预览功能需要按以下操作进行：

```bash
javac --enable-preview --release 12 Example.java
java --enable-preview Example
```

>**注意**：此开关表达式 Java 13 中具有第二个预览（`break` 改成 `yield`），并在 Java 14 中成为标准功能；

# 八. Teeing Collectors

Teeing Collector 是 Streams API 中引入的新的收集器实用程序。

该收集器具有三个参数——两个收集器和一个 `Bi` 函数。所有输入值都传递给每个收集器，结果交给 `Bi` 函数使用。（为了大家便于理解，我画了一个图👇）

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/Java12版本特性【一文了解】/image-20200822154515360.png)

典型的例子就是求取一个平均值：*(当然对于这个例子，我们可以使用 `averagingInt()` 来完成...)*

```java
double average = Stream.of(1, 2, 3, 4, 5)
    .collect(Collectors.teeing(
        summingDouble(i -> i),
        counting(),
        (sum, n) -> sum / n));
System.out.println(average);	// 输出 3.0
```

# 九. 字符串新方法

## indent(int)-缩进

此方法根据输入参数 `'n'` 的值调整字符串中每行的缩进量，并规范行终止符。

- 如果 `n > 0`，则在每行的开头插入 `n` 个空格（U + 0020）。
- 如果 `n < 0`，则从每行开头最多删除 `n` 个空格字符。如果给定的行没有足够的空格，那么将删除所有前导空格字符。制表符也被视为单个字符。
- 如果 `n = 0`，则该行保持不变。但是，行终止符仍被标准化。

下面是使用 JShell 调用的演示：

```bash
jshell> String str = "******\n   Hi\n  How are you?\n******"
str ==> "******\n   Hi\n  How are you?\n******"

jshell> str.indent(0)
$23 ==> "******\n   Hi\n  How are you?\n******\n"

jshell> str.indent(3)
$24 ==> "   ******\n      Hi\n     How are you?\n   ******\n"

jshell> str.indent(-2)
$25 ==> "******\n Hi\nHow are you?\n******\n"

jshell>
```

注意，使用 `indent()` 方法时，`\r` 会被转化为 `\n`。

## transform(Function<? super String,? extends R> f)

此方法使我们可以在给定的字符串上调用函数。该函数应该期望一个 String 参数，并产生一个 R 结果。

下面是使用 `transform()` 方法把 CSV 字符串转换为字符串列表的演示：

```bash
jshell> String str = "Hi,Hello,Wmyskxz";
str ==> "Hi,Hello,Wmyskxz"

jshell> var strList = str.transform(s -> {return Arrays.asList(s.split(","));});
strList ==> [Hi, Hello, Wmyskxz]

jshell>
```

## Optional<String> describeConstable()

Java 12 在 [JEP 334 中](https://openjdk.java.net/jeps/334) 引入了 Constants API 。如果您查看 String 类文档，它将实现 Constants API 的两个新接口——Constable 和 ConstantDesc。此方法在 Constable 接口中声明，并在 String 类中实现。

此方法返回一个 Optional，其中包含该实例的名义描述符，即实例本身。

```bash
jshell> String str = "wmyskxz";
str ==> "wmyskxz"

jshell> var s = str.describeConstable();
s ==> Optional[wmyskxz]

jshell> s.get()
$30 ==> "wmyskxz"

jshell>
```

## String resolveConstantDesc(MethodHandles.Lookup lookup)

此方法是 Constants API 的一部分，并在 ConstantDesc 接口中声明。它将实例解析为 ConstantDesc，其结果就是实例本身。

```bash
shell> import java.lang.invoke.MethodHandles;

jshell> String str = "Hello";
str ==> "Hello"

jshell> str.resolveConstantDesc(MethodHandles.lookup());
$18 ==> "Hello"

jshell>
```

# 十. JVM 常量 API（JEP 334）

此 JEP 引入了一个新软件包 `java.lang.constant`。对于不使用常量池的开发人员来说，这没什么用。

> 进一步阅读：官方文档 - https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/lang/constant/package-summary.html

# 十一. instanceof 的模式匹配-预览（JEP 305）

另一个预览语言功能。将一个类型转换为另一种类型的旧方法是：

```java
if (obj instanceof String) {
    String s = (String) obj;
    // use s in your code from here
}
```

新方法是：

```java
if (obj instanceof String s) {
    // can use s directly here
} 
```

这为我们节省了一些不必要的类型转换。

# 十二. File.mismatch 方法

Java 12 添加了以下方法来比较两个文件：

```Java
public static long mismatch(Path path, Path path2) throws IOException
```

此方法返回第一个不匹配的位置，如果没有不匹配，则返回 `-1L`。

在以下情况下，两个文件可能不匹配：

- 如果字节不相同。在这种情况下，将返回第一个不匹配字节的位置。
- 文件大小不相同。在这种情况下，将返回较小文件的大小。

下面给出了示例代码片段：*（摘录自 JournalDev）*

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMismatchExample {

    public static void main(String[] args) throws IOException {
        Path filePath1 = Files.createTempFile("file1", ".txt");
        Path filePath2 = Files.createTempFile("file2", ".txt");
        Files.writeString(filePath1,"JournalDev Test String");
        Files.writeString(filePath2,"JournalDev Test String");

        long mismatch = Files.mismatch(filePath1, filePath2);

        System.out.println("File Mismatch position... It returns -1 if there is no mismatch");

        System.out.println("Mismatch position in file1 and file2 is >>>>");
        System.out.println(mismatch);

        filePath1.toFile().deleteOnExit();
        filePath2.toFile().deleteOnExit();

        System.out.println();

        Path filePath3 = Files.createTempFile("file3", ".txt");
        Path filePath4 = Files.createTempFile("file4", ".txt");
        Files.writeString(filePath3,"JournalDev Test String");
        Files.writeString(filePath4,"JournalDev.com Test String");

        long mismatch2 = Files.mismatch(filePath3, filePath4);

        System.out.println("Mismatch position in file3 and file4 is >>>>");
        System.out.println(mismatch2);

        filePath3.toFile().deleteOnExit();
        filePath4.toFile().deleteOnExit();
    }
}
```

编译并运行上述 Java 程序时，输出为：

![Java 文件不匹配示例程序输出](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/Java12版本特性【一文了解】/java-file-mismatch-example-program-output.png)

# 十三. 紧凑的数字格式

**实例代码：**

```java
import java.text.NumberFormat;
import java.util.Locale;

public class CompactNumberFormatting {

    public static void main(String[] args)
    {
        System.out.println("Compact Formatting is:");
        NumberFormat upvotes = NumberFormat
                .getCompactNumberInstance(new Locale("en", "US"), NumberFormat.Style.SHORT);
        upvotes.setMaximumFractionDigits(1);

        System.out.println(upvotes.format(2592) + " upvotes");


        NumberFormat upvotes2 = NumberFormat
                .getCompactNumberInstance(new Locale("en", "US"), NumberFormat.Style.LONG);
        upvotes2.setMaximumFractionDigits(2);
        System.out.println(upvotes2.format(2011) + " upvotes");
    }
}
```

**输出：**

```bash
Compact Formatting is:
2.6k upvotes
2.01 thousand upvotes
```

# 参考资料

1. OpenJDK 官方说明 - http://openjdk.java.net/projects/jdk/12/
2. What is new in Java 12 - https://mkyong.com/java/what-is-new-in-java-12/
3. Java 12 Features | JournalDev - https://www.journaldev.com/28666/java-12-features
4. 39 New Features（and APIs） in JDK 12 - https://dzone.com/articles/39-new-features-and-apis-in-jdk-12

# 文章推荐

1. 这都JDK15了，JDK7还不了解？ - https://www.wmyskxz.com/2020/08/18/java7-ban-ben-te-xing-xiang-jie/
2. 全网最通透的 Java 8 版本特性讲解 - https://www.wmyskxz.com/2020/08/19/java8-ban-ben-te-xing-xiang-jie/
3. Java9的这些史诗级更新你都不知道？ - https://www.wmyskxz.com/2020/08/20/java9-ban-ben-te-xing-xiang-jie/
4. 你想了解的 JDK 10 版本更新都在这里 - https://www.wmyskxz.com/2020/08/21/java10-ban-ben-te-xing-xiang-jie/
5. 这里有你不得不了解的 Java 11 特性 - https://www.wmyskxz.com/2020/08/22/java11-ban-ben-te-xing-xiang-jie/
6. 「MoreThanJava」系列文集 - https://www.wmyskxz.com/categories/MoreThanJava/

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！