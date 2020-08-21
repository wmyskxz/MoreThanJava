![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/你想了解的JDK10版本更新都在这里/image-20200821092051478.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# 特性总览

以下是 Java 10 中的引入的部分新特性。关于 Java 10 新特性更详细的介绍可参考[这里](http://openjdk.java.net/projects/jdk/10/)。

- 基于时间的发行版本控制（JEP 322）
- 局部变量类型推断（JEP 286）
- 试验性 JIT 编译器（JEP 317）
- 应用程序类数据共享（JEP 310）
- 用于 G1 的并行 Full GC（JEP 307）
- 清理垃圾收集器接口（JEP 304）
- 其他 Unicode 语言标签扩展（JEP 314）
- 根证书（JEP 319）
- 线程本地握手（JEP 312）
- 备用存储设备上的堆分配（JEP 316）
- 删除本机头生成工具——javah（JEP 313）
- 将JDK森林合并到单个存储库中（JEP 296）
- API 变更

# 一. 基于时间的发行版本控制（JEP 322）

## 发行版本号的前世今生

自 Java 江山易主，JDK 发行版本的字符串命名方式一直是一个耐人寻味的话题。

单就下载 JDK 时，所看到的简短版本字符串形式来说，在 `7u40` 版本之前，`u` 之后的数字，代表了 JDK 发布以来的第几个修正版本，然而 Oracle 改变规则，为了彰显出安全之类的重大 **修补（Cirtical Patch Updates）版本**，采用 **奇数** 命名，而 Bug 修复、API 修改之类的 **维护版本**，则采用 **偶数**。*（另有版本号 `$MAJOR.$MINOR.$SECURITY` 的格式来区分 Bug 修正和 API 修改）*

为此，之前既有的 JDK 6/7 发布版本，还被重新命名。*（下方演示）*

```bash
   Actual                    Hypothetical
Release Type           long               short
------------           ------------------------ 
Security 2013/06       1.7.0_25-b15       7u25
Minor    2013/09       1.7.0_40-b43       7u40
Security 2013/10       1.7.0_45-b18       7u45
Security 2014/01       1.7.0_51-b13       7u51
Minor    2014/05       1.7.0_60-b19       7u60
```

就结论而言，重新命名之后，从 `7u9`、`6u37` 开始，就可以从奇偶数来判别是否为重大修补版本；至于 `7u40` 之后的版本，**重大修补版本** 是基于 `5` 的倍数，遇偶数加一，而 **维护版本** 则会是 `20` 的倍数。如此（不直观）的命名方式，被规范在了 [JEP223](http://openjdk.java.net/jeps/223) 中。*（随着 JDK 9 一起发布的）*

然而，自 JDK 8 发布之后，Oracle 的 Java 架构师 Mark Reinhold 就希望，未来 Java 发布可以基于时间，以半年为周期，持续发布新版本，让一些有用的小特性，也能被开发者使用，因此，JEP 223 的规范就不再适用了，而在 JDK 9 发布后，他们针对新版本曾经提出过基于 `$YEAR.$MONTH` 格式，然而收到了社区极大的反对，为此，还提出了三个替代方案，收集各方的意见。（https://goo.gl/7CA8B3)

那么，Java 下一个特性版本是 `8.3`？`1803`？还是 `10`？调查结果显示，社群大多数都支持 `10`，Stephen Colebourne 也发出请求（https://goo.gl/i5J44T），并表示 Java 不像 Ubuntu 这类操作系统，基于 `$YEAR.$MONTH` 并不合适。

最终，Oracle 采用了 `$FEATURE.$INTERIM.$UPDATE.$PATCH` 这样的方案，并规定在了 [JEP 322](http://openjdk.java.net/jeps/322) 中。

## JEP 322 新模式解读

通过采用基于时间的发行周期，Oracle 更改了 Java SE 平台和 JDK 的版本字符串方案以及相关的版本信息，以适用于当前和将来的基于时间的发行模型。

版本号的新模式是：

`$FEATURE.$INTERIM.$UPDATE.$PATCH`

- **$ FEATURE**：计数器将每 `6` 个月递增一次，并基于功能发布版本，例如：JDK 10，JDK 11。
- **$ INTERIM**：对于包含兼容错误修复和增强功能但没有不兼容更改的非功能版本，计数器将增加。通常，这将是零，因为六个月内不会有任何临时发布。这保留了对发布模型的将来修订。
- **$ UPDATE**：计数器将增加，用于解决安全问题，回归和较新功能中的错误的兼容更新版本。此功能会在功能发布后一个月更新，此后每三个月更新一次。2018 年 4 月版本是 `JDK 10.0.1`，7 月版本是 `JDK 10.0.2`，依此类推
- **$ PATCH**：计数器将增加以用于紧急释放以解决严重问题。

并添加了新的 API 以通过编程的方式获取这些计数器：

```java
Version version = Runtime.version();
version.feature();
version.interim();
version.update();
version.patch();
```

现在，让我们来看一下返回版本信息的 Java 启动器：

```bash
$ java -version
java version "10" 2018-03-20
Java(TM) SE Runtime Environment 18.3 (build 10+46)
Java HotSpot(TM) 64-Bit Server VM 18.3 (build 10+46, mixed mode)
```

版本号格式为 `10`，因为没有其他计数器为零。发布日期已添加。可以将 `18.3` 理解为 Year 2018＆3rd Month，版本 `10 + 46` 是版本 `10` 的 `46` 版本。对于 `JDK 10.0.1` 的假设版本 `93`，版本将为 `10.0.1 + 93`。

因此，对于 Java 9 来说，目前可以看到的版本，会是 `9.0.4` 这样的格式，至于 2018 年 3 月发布的特性版本为 `10`，到了 `9` 月提供的新版本，就是 `11`，JDK 11 预计会是长期支援版本，所以，在版本字串上，也会特别显示 LTS（long-term support）。

# 二. 局部变量类型推断（JEP 286）

## 概述

**JDK 10 中最明显的增强功能之一是使用初始化程序对局部变量进行类型推断。**

在 Java 9 之前，我们必须明确写出局部变量的类型，并确保它与用于初始化它的初始化程序兼容：

```java
String message = "Good bye, Java 9";
```

在 Java 10 中，这是我们可以声明局部变量的方式：

```java
@Test
public void whenVarInitWithString_thenGetStringTypeVar() {
    var message = "Hello, Java 10";
    assertTrue(message instanceof String);
}
```

我们没有提供 `message` 的具体类型，相反，我们把 `message` 标记为了 `var`，编译器将从右侧的初始化程序的类型推断出 `message` 的类型。*（上面的例子中 `message` 为 `String` 类型）*

**请注意，此功能仅适用于带有初始化程序的局部变量**。它不能用于成员变量、方法参数、返回类型等——初始化程序是必须的，否则，编译器无法推断出其类型。

这个功能有助于我们减少样板式的代码，例如：

```java
Map<Integer, String> map = new HashMap<>();
```

现在可以改写为：

```java
var idToNameMap = new HashMap<Integer, String>();
```

这也有助于我们把重点放在变量名，而不是变量类型上。

要注意的另一件事是 **`var` 不是关键字**——这确保了使用 `var` 作为函数或变量名的程序的向后兼容性。`var` 是一个保留类型名，就像 `int` 一样。

最后，**使用 `var` 不会增加运行时的开销，也不会使 Java 称为动态类型的语言**。变量的类型仍然是在编译时进行判断，以后也无法更改。

## 非法使用 var 的情况解析

1、如果没有初始化程序，`var` 将无法工作：

```java
var n; // error: cannot use 'var' on variable without initializer
```

2、如果将其初始化为 null，也不会起作用：

```java
var emptyList = null; // error: variable initializer is 'null'
```

3、不适用于非局部变量：

```java
public var word = "hello"; // error: 'var' is not allowed here
```

4、Lambda 表达式需要显式的类型，因此无法使用 `var`：

```java
var p = (String s) -> s.length() > 10; // error: lambda expression needs an explicit target-type
```

5、数组初始化程序也不支持：

```java
var arr = { 1, 2, 3 }; // error: array initializer needs an explicit target-type
```

## 使用 var 的准则

在某些情况下，我们可以合法使用 `var`，但这样做并不是一个好主意。

例如，在代码的可读性降低的情况下：

```java
var result = obj.prcoess();
```

在这里，尽管可以合法使用 `var`，但很难理解 `process()` 返回的类型，从而让代码的可读性降低。

> `java.net`(OpenJDK 官网)上专门有一篇文章介绍了 [Java 中的局部变量类型推断的书写准则](https://openjdk.java.net/projects/amber/LVTIstyle.html)，该文章讨论了在使用此功能时应该注意的姿势和如何使用的一些良好建议。

另外，最好避免使用 `var` 的另一种情况是在流水线较长的流中：

```java
var x = emp.getProjects.stream()
  .findFirst()
  .map(String::length)
  .orElse(0);
```

另外，将 `var` 与不可引用类型一起使用可能会导致意外错误。

比如，如果我们将 `var` 与匿名类实例一起使用：

```java
@Test
public void whenVarInitWithAnonymous_thenGetAnonymousType() {
    var obj = new Object() {};
    assertFalse(obj.getClass().equals(Object.class));
}
```

现在，如果我们尝试将另一个 Object 分配给 `obj`，则会出现编译错误：

```java
obj = new Object(); // error: Object cannot be converted to <anonymous Object>
```

这是因为 `obj` 的推断类型不是 Object。

# 三. 试验性 JIT 编译器（JEP 317）

[Graal](https://github.com/oracle/graal/blob/master/compiler/README.md) 是用Java编写的，与 HotSpot JVM 集成的动态编译器。它专注于高性能和可扩展性。它也是 JDK 9 中引入的实验性 Ahead-of-Time（AOT）编译器的基础。

JDK 10 使 Graal 编译器可以用作 Linux / x64 平台上的实验性 JIT 编译器。

要将 Graal 用作 JIT 编译器，请在 Java 命令行上使用以下选项：

```bash
-XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler
```

请注意，这是一个实验性功能，我们不一定会获得比现有JIT编译器更好的性能。

> 想要了解更多内容的童鞋请参考 Chris Seaton 的演讲：https://chrisseaton.com/truffleruby/jokerconf17/
>
> *（ps：长文 + 特别底层警告..）*

# 四. 应用程序类数据共享(JEP 310)

JDK 5 中引入的类数据共享允许将一组类预处理成共享的归档文件，然后在运行时对其进行内存映射，以减少启动时间，这还可以在多个 JVM 共享相同的归档文件时减少动态内存占用。

CDS 只允许引导类装入器，将该特性限制为系统类。应用程序 CDS (AppCDS）扩展了 CDS 以允许内置的系统类装入器。内置的平台类装入器和用于装入归档类的自定义类装入器。这使得对应用程序类使用该特性成为可能。

我们可以使用以下步骤来使用这个功能：

**1、获取要存档的类列表**

以下命令会将*HelloWorld* 应用程序加载的类转储到*hello.lst中*：

```bash
$ java -Xshare:off -XX:+UseAppCDS -XX:DumpLoadedClassList=hello.lst \ 
    -cp hello.jar HelloWorld
```

**2、创建 AppCDS 存档**

以下命令使用*hello.lst* 作为输入创建*hello.js a* ：

```bash
$ java -Xshare:dump -XX:+UseAppCDS -XX:SharedClassListFile=hello.lst \
    -XX:SharedArchiveFile=hello.jsa -cp hello.jar
```

**3、使用 AppCDS 存档**

以下命令以*hello.jsa* 作为输入启动*HelloWorld* 应用程序：

```bash
$ java -Xshare:on -XX:+UseAppCDS -XX:SharedArchiveFile=hello.jsa \
    -cp hello.jar HelloWorld
```

**AppCDS 是用于 JDK 8 和 JDK 9 的 Oracle JDK 中的一项商业功能。现在它是开源的，并且可以公开使用。**

# 五. 用于 G1 的并行 Full GC(JEP 307)

G1 垃圾收集器是自 JDK 9 以来的默认垃圾收集器。但是，G1 的 Full GC 使用了单线程的 mark-sweep-compact 算法。

它已 **更改为** Java 10 中 **的并行mark-sweep-compact算法** ，有效地减少了 Full GC 期间的停滞时间。

# 六. 清理垃圾收集器接口（JEP 304）

这个 JEP 是未来的变化。通过引入公共垃圾收集器接口，它改善了不同垃圾收集器的代码隔离。

次更改为内部 GC 代码提供了更好的模块化。将来将有助于在不更改现有代码库的情况下添加新 GC，还有助于删除或保留以前的 GC。

> **官方的动机解释：** （[传送门](http://openjdk.java.net/jeps/304)）
>
> 当前，每个垃圾收集器实现都由其 `src/hotspot/share/gc/$NAME` 目录内的源文件组成，例如 G1 在中 `src/hotspot/share/gc/g1`，CMS 在 `src/hotspot/share/gc/cms` 等中。但是，在 HotSpot 中散布着一些零散的信息。例如，大多数 GC 需要某些障碍，这些障碍需要在运行时，解释器 C1 和 C2 中实现。这些障碍并不包含在 GC 的具体目录，但在共享解释器，而不是实施，C1 和 C2 的源代码（通常由长守卫`if`- `else`-`chains`）。同样的问题也适用于诊断代码，例如 `MemoryMXBeans`。此源代码布局有几个缺点：
> 
>
> 1. 对于GC开发人员，实施新的垃圾收集器需要有关所有这些地方的知识，以及如何扩展它们以满足其特定需求的知识。
>
>    
>
> 2. 对于不是 GC 开发人员的 HotSpot 开发人员，在哪里为给定 GC 找到特定的代码段会造成混乱。
>
>    
>
> 3. 在构建时很难排除特定的垃圾收集器。该`#define` `INCLUD E_ALL_GCS`长期以来建立与唯一内置串行收集JVM的一种方式，但这种机制变得过于呆板。
>
> 较干净的 GC 接口将使实现新的收集器更加容易，使代码更加清洁，并且在构建时排除一个或多个收集器也更加容易。添加一个新的垃圾收集器应该是实现一组有据可查的接口，而不是弄清 HotSpot 中所有需要更改的地方。

# 七. 其他Unicode语言标签扩展（JEP 314）

此功能增强了 `java.util.Locale` 和相关 API，以实现 BCP 47 语言标签的其他 Unicode 扩展。从 Java SE 9 开始，受支持的 BCP 47 U 语言扩展名是 "ca" 和 "nu"。该 JEP 将增加对以下附加扩展的支持：

- cu（货币类型）
- fw（一周的第一天）
- rg（区域覆盖）
- tz（时区）

为了支持这些附加扩展，对以下各种 API 进行了更改以提供基于 U 或附加扩展的信息：

```java
java.text.DateFormat::get*Instance
java.text.DateFormatSymbols::getInstance
java.text.DecimalFormatSymbols::getInstance
java.text.NumberFormat::get*Instance
java.time.format.DateTimeFormatter::localizedBy
java.time.format.DateTimeFormatterBuilder::getLocalizedDateTimePattern
java.time.format.DecimalStyle::of
java.time.temporal.WeekFields::of
java.util.Calendar::{getFirstDayOfWeek,getMinimalDaysInWeek}
java.util.Currency::getInstance
java.util.Locale::getDisplayName
java.util.spi.LocaleNameProvider
```

# 八. 根证书（JEP 319）

cacerts 密钥库（迄今为止到目前为止是空的）旨在包含一组根证书，这些根证书可用于建立对各种安全协议所使用的证书链的信任。

结果，在 OpenJDK 构建中，诸如 TLS 之类的关键安全组件默认情况下不起作用。

**借助 Java 10，Oracle 将** Oracle Java SE Root CA 程序中 **的根证书开源了** ，以使 OpenJDK 构建对开发人员更具吸引力，并减少了这些构建与 Oracle JDK 构建之间的差异。

# 九. 线程本地握手（JEP 312）

这是用于提高 JVM 性能的内部特性。

握手操作是在线程处于安全点状态时为每个 JavaThread 执行的回调。回调由线程本身或 VM 线程执行，同时保持线程处于阻塞状态。

这个特性提供了一种无需执行全局 VM 安全点即可在线程上执行回调的方法。**使停止单个线程，而不是停止所有线程或不停止线程成为可能**，而且代价低廉。

# 十. 备用存储设备上的堆分配(JEP 316)

应用程序的内存消耗越来越大，本地云应用程序、内存中的数据库、流应用程序都在增加。为了满足这些服务，有各种可用的内存架构。这个特性增强了 HotSpot VM 在用户指定的备用内存设备(比如NV-DIMM)上分配 Java 对象堆的能力。

这个 JEP 的目标是具有与 DRAM 相同语义(包括原子操作的语义)的可选内存设备，因此，可以在不更改现有应用程序代码的情况下，将其用于对象堆，而不是用于 DRAM。

# 十一. 删除本机头生成工具—javah (JEP 313)

这是一个从 JDK 中删除 `javah` 工具的常规更改。工具功能是作为 JDK 8 的一部分在 `javac` 中添加的，它提供了在编译时编写使 `javah` 无用的本机头文件的能力。

# 十二. 将JDK森林合并到单个存储库中(JEP 296)

多年来，有各种各样的 Mercurial 存储库用于 JDK 代码基。不同的存储库确实提供了一些优势，但它们也有各种操作上的缺点。作为这个变化的一部分，JDK 森林的许多存储库被合并到一个存储库中，以简化和简化开发。

# 十三. API 变更

Java 10 添加和删除了 API。Java 9 引入了增强的弃用，其中某些 API 被标记为将在未来的版本中删除。

于是这些 API 被删除了：你可以在 [这里](https://cr.openjdk.java.net/~iris/se/10/latestSpec/#APIs-removed) 找到被删除的 API。

新增API: Java 10 中新增 73 个API。您可以在 [这里](https://cr.openjdk.java.net/~iris/se/10/latestSpec/apidiffs/overview-summary.html) 找到添加的 API 并进行比较。

让我们来看看对我们直接有用的部分。

## 不可修改集合的改进

Java 10 中有一些与不可修改集合相关的更改

### copyOf()

`java.util.List`、`java.util.Map` 和 `java.util.Set` 都有了一个新的静态方法 `copyOf(Collection)`。

它返回给定 Collection 的不可修改的副本：

```bash
jshell> var originList = new ArrayList<String>();
originList ==> []

jshell> originList.add("欢迎关注公众号：");
$2 ==> true

jshell> originList.add("我没有三颗心脏");
$3 ==> true

jshell> var copyList = List.copyOf(originList)
copyList ==> [欢迎关注公众号：, 我没有三颗心脏]

jshell> originList.add("获取更多精彩内容")
$5 ==> true

jshell> System.out.println(copyList)
[欢迎关注公众号：, 我没有三颗心脏]

jshell> copyList.add("获取更多精彩内容")
|  异常错误 java.lang.UnsupportedOperationException
|        at ImmutableCollections.uoe (ImmutableCollections.java:73)
|        at ImmutableCollections$AbstractImmutableCollection.add (ImmutableCollections.java:77)
|        at (#7:1)

jshell>
```

### toUnmodifiable()

`java.util.Collectors` 获得其他方法来将 Stream 收集到不可修改的 List、Map 或 Set 中：

```java
@Test(expected = UnsupportedOperationException.class)
public void whenModifyToUnmodifiableList_thenThrowsException() {
    List<Integer> evenList = someIntList.stream()
      .filter(i -> i % 2 == 0)
      .collect(Collectors.toUnmodifiableList());
    evenList.add(4);
}
```

任何尝试修改此类集合的尝试都会导致 `java.lang.UnsupportedOperationException` 运行时异常。

## Optinal 新增方法 orElseThrow()

`java.util.Optional`，`java.util.OptionalDouble`，`java.util.OptionalInt` 和 `java.util.OptionalLong` 都有一个新方法 `orElseThrow()`，它不接受任何参数，如果不存在任何值，则抛出 `NoSuchElementException`：

```java
@Test
public void whenListContainsInteger_OrElseThrowReturnsInteger() {
    Integer firstEven = someIntList.stream()
      .filter(i -> i % 2 == 0)
      .findFirst()
      .orElseThrow();
    is(firstEven).equals(Integer.valueOf(2));
}
```

它与现有的 `get()` 方法同义，并且现在是它的首选替代方法。

# 参考资料

1. OpenJDK 官方说明 - http://openjdk.java.net/projects/jdk/10/
2. Java 10 Features | JournalDev - https://www.journaldev.com/20395/java-10-features
3. 想跳舞的 Java | 林信良 - https://www.ithome.com.tw/voice/122249
4. Java 10 Performance Improvements | Baeldung - https://www.baeldung.com/java-10-performance-improvements

# 文章推荐

1. 这都JDK15了，JDK7还不了解？ - https://www.wmyskxz.com/2020/08/18/java7-ban-ben-te-xing-xiang-jie/
2. 全网最通透的 Java 8 版本特性讲解 - https://www.wmyskxz.com/2020/08/19/java8-ban-ben-te-xing-xiang-jie/
3. Java9的这些史诗级更新你都不知道？ - https://www.wmyskxz.com/2020/08/20/java9-ban-ben-te-xing-xiang-jie/
4. 「MoreThanJava」系列文集 - https://www.wmyskxz.com/categories/MoreThanJava/

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！