![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820103424921.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# 特性总览

以下是 Java 9 中的引入的部分新特性。关于 Java 9 新特性更详细的介绍可参考[这里](https://openjdk.java.net/projects/jdk9/)。

- REPL（JShell）
- 不可变集合的工厂方法
- 模块系统
- 接口支持私有化
- 钻石操作符升级
- Optional 改进
- Stream API 改进
- 反应式流（Reactive Streams）
- 进程 API
- 升级的 Try-With-Resources
- HTTP / 2
- 多版本兼容 Jar 包
- 其他
  - 改进应用安全性能
  - 统一 JVM 日志
  - G1 设为默认垃圾回收器
  - String 底层存储结构更改
  - CompletableFuture API 改进
  - I/O 流新特性
  - JavaScript 引擎 Nashorn 改进
  - 标识符增加限制
  - 改进的 Javadoc
  - 改进的 @Deprectaed 注解
  - 多分辨率图像 API
  - 变量句柄
  - 改进方法句柄（Method Handle）
  - 提前编译 AOT

# 一. Java 9 REPL（JShell）

## 什么是 REPL 以及为什么引入

**REPL**，即 **Read-Evaluate-Print-Loop** 的简称。由于 [Scala](https://zh.wikipedia.org/zh-my/Scala) 语言的特性和优势在小型应用程序到大型应用程序市场大受追捧，于是引来 Oracle 的关注，并尝试将大多数 Scala 功能集成到 Java 中。这在 Java 8 中已经完成一部分，比如 Lambda 表达式。

Scala 的最佳功能之一就是 REPL，这是一个命令行界面和 Scala 解释器，用于执行 Scala 程序。由于并不需要开启额外的 IDE *(就是一个命令行)*，它在减少学习曲线和简化运行测试代码方面有独特的优势。

于是在 Java 9 中引入了 Java REPL，也称为 `JShell`。

## JShell 基础

打开命令提示符，确保您具有 Java 9 或更高版本，键入 `jshell`，然后我们就可以开心的使用了。

**下面是简单示范：**

```bash
wmyskxz:~ wmyskxz$ jshell 
|  Welcome to JShell -- Version 9
|  For an introduction type: /help intro

jshell> 

jshell> System.out.println("Hello World");
Hello World

jshell> String str = "Hello JShell!"
str ==> "Hello JShell!"

jshell> str
str ==> "Hello JShell!"

jshell> System.out.println(str)
Hello JShell!

jshell> int counter = 0
counter ==> 0

jshell> counter++
$6 ==> 0

jshell> counter
counter ==> 1

jshell> counter+5
$8 ==> 6
```

**也可以在 Java Shell 中定义和执行类方法：**

```bash
jshell> class Hello {
   ...> public static void sayHello() {
   ...> System.out.print("Hello");
   ...> }
   ...> }
|  created class Hello

jshell> Hello.sayHello()
Hello
jshell> 
```

## Java REPL - 帮助和退出

要获得 `jshell` 工具的帮助部分，请使用`/help`命令。要从 `jshell` 退出，请使用 `/exit` 命令 *(或者直接使用 `Ctrl + D` 命令退出)*。

```bash
jshell> /help
|  Type a Java language expression, statement, or declaration.
|  Or type one of the following commands:
|  /list [<name or id>|-all|-start]
|  	list the source you have typed
|  /edit <name or id>
...

jshell> /exit
|  Goodbye
wmyskxz:~ wmyskxz$ 
```

# 二. 不可变集合的工厂方法

Java 9 中增加了一些便捷的工厂方法用于创建 **不可变** List、Set、Map 以及 Map.Entry 对象。

在 Java SE 8 和更早的版本中，如果我们要创建一个空的 **不可变** 或 **不可修改** 的列表，需要借助 `Collections` 类的 `unmodifiableList()` 方法才可以：

```java
List<String> list = new ArrayList<>();
list.add("公众号");
list.add("我没有三颗心脏");
list.add("关注走起来");
List<String> immutableList = Collections.unmodifiableList(list);
```

可以看到，为了创建一个非空的不可变列表，我们需要经历很多繁琐和冗长的步骤。为了克服这一点，Java 9 在 `List` 接口中引入了以下有用的重载方法：

```java
static <E> List<E> of(E e1)
static <E> List<E> of(E e1,E e2)	
static <E> List<E> of(E e1,E e2,E e3)
static <E> List<E> of(E e1,E e2,E e3,E e4)
static <E> List<E> of(E e1,E e2,E e3,E e4,E e5)	
static <E> List<E> of(E e1,E e2,E e3,E e4,E e5,E e6)	
static <E> List<E> of(E e1,E e2,E e3,E e4,E e5,E e6,E e7)	
static <E> List<E> of(E e1,E e2,E e3,E e4,E e5,E e6,E e7,E e8)	
static <E> List<E> of(E e1,E e2,E e3,E e4,E e5,E e6,E e7,E e8,E e9)	
static <E> List<E> of(E e1,E e2,E e3,E e4,E e5,E e6,E e7,E e8,E e9,E e10)
```

以及可变参数数目的方法：

```java
static <E> List<E> of(E... elements)  
```

可以看到 Java 9 前后的对比：

```java
// Java 9 之前
List<String> list = new ArrayList<>();
list.add("公众号");
list.add("我没有三颗心脏");
list.add("关注走起来");
List<String> unmodifiableList = Collections.unmodifiableList(list);
// 或者使用 {{}} 的形式
List<String> list = new ArrayList<>() {{
    add("公众号");
    add("我没有三颗心脏");
    add("关注走起来");
}};
List<String> unmodifiableList = Collections.unmodifiableList(list);

// Java 9 便捷的工厂方法
List<String> unmodifiableList = List.of("公众号", "我没有三颗心脏", "关注走起来");
```

*(ps: Set、Map 类似，Map 有两组方法：`of()`  和 `ofEntries()` 分别用于创建 Immutable Map 对象和 Immutable Map.Entry 对象)*

另外 Java 9 可以直接输出集合的内容，在此之前必须遍历集合才能全部获取里面的元素，这是一个很大的改进。

## 不可变集合的特征

不可变即不可修改。它们通常具有以下几个特征：

1、我们无法添加、修改和删除其元素；

2、如果尝试对它们执行添加/删除/更新操作，将会得到 `UnsupportedOperationException` 异常，如下所示：

```bash
jshell> immutableList.add("Test")
|  java.lang.UnsupportedOperationException thrown: 
|        at ImmutableCollections.uoe (ImmutableCollections.java:68)
|        at ImmutableCollections$AbstractImmutableList.add (ImmutableCollections.java:74)
|        at (#2:1)
```

3、不可变集合不允许 null 元素；

4、如果尝试使用 null 元素创建，则会报出 `NullPointerException` 异常，如下所示：

```bash
jshell> List>String> immutableList = List.of("公众号","我没有三颗心脏","关注走起来", null)
|  java.lang.NullPointerException thrown: 
|        at Objects.requireNonNull (Objects.java:221)
|        at ImmutableCollections$ListN. (ImmutableCollections.java:179)
|        at List.of (List.java:859)
|        at (#4:1)
```

5、如果尝试添加 null 元素，则会得到 `UnsupportedOperationException` 异常，如下所示：

```bash
jshell> immutableList.add(null)
|  java.lang.UnsupportedOperationException thrown: 
|        at ImmutableCollections.uoe (ImmutableCollections.java:68)
|        at ImmutableCollections$AbstractImmutableList.add (ImmutableCollections.java:74)
|        at (#3:1)
```

6、如果所有元素都是可序列化的，那么集合是可以序列化的；

# 三. 模块系统

Java 模块系统是 Oracle 在 Java 9 引入的全新概念。最初，它作为 Java SE 7 Release 的一部分启动了该项目，但是由于进行了很大的更改，它被推迟到了 Java SE 8，然后又被推迟了。最终随着 2017 年 9 月发布的 Java SE 9 一起发布。

## 为什么需要模块系统？

当代码库变得更大时，创建复杂、纠结的 “意大利面条代码” 的几率成倍增加。在 Java 8 或更早版本交付 Java 应用时存在几个基本问题：

1. **难以真正封装代码**，并且在系统的不同部分（JAR 文件）之间没有显式依赖关系的概念。每个公共类都可以由 classpath 上的任何其他公共类访问，从而导致无意中使用了本不应该是公共 API 的类。
2. 再者，类路径本身是有问题的：**您如何知道是否所有必需的 JAR 都存在，或者是否存在重复的条目？**
3. 另外，**JDK 太大了**，`rt.jar` *（`rt.jar` 就是 Java 基础类库——也就是 Java Doc 里面看到的所有类的 class 文件）*等 JAR 文件甚至无法在小型设备和应用程序中使用：因此我们的应用程序和设备无法支持更好的性能——打包之后的应用程序太大了——也很难测试和维护应用程序。

模块系统解决了这几个问题。

## 什么是 Java 9 模块系统？

模块就是代码、数据和一些资源的自描述集合。它是一组与代码、数据和资源相关的包。

每个模块仅包含一组相关的代码和数据，以支持单一职责原则（SRP)。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820062506819.png)

Java 9 模块系统的主要目标就是支持 **Java 模块化编程**。*（我们将在下面👇体验一下模块化编程）*

## 比较 JDK 8 和 JDK 9

我们知道 JDK 软件包含什么。安装 JDK 8 软件后，我们可以在 Java Home 文件夹中看到几个目录，例如 `bin`，`jre`，`lib` 等。

但是，Oracle 在 Java 9 中对该文件夹结构的更改有些不同，如下所示。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/jdk8_jdk9-450x352.png)

这里的 JDK 9 不包含 JRE。在 JDK 9 中，JRE 分为一个单独的分发文件夹。JDK 9 软件包含一个新文件夹 **“ jmods”**，它包含一组 Java 9 模块。在 JDK 9 中，没有 `rt.jar` 和 `tools.jar`。*（如下所示）*

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/jdk9_jmods_folder-1024x426.png)

**注意：** 截止今天， `jmods` 包含了 `95` 个模块。*（最终版可能更多）*

## 比较 Java 8 和 Java 9 应用程序

我们已经使用 Java 5、Java 6、Java 7 或 Java 8 开发了许多 Java 应用程序了，我们知道 Java 8 或更早版本的应用程序，顶级组件是 Package：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820063928794.png)

Java 9 应用程序与此没有太大的区别。它刚刚引入了称为 "模块" 和称为模块描述符（`module-info.java`）的新组件：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820070217576.png)

像 Java 8 应用程序将 Packages 作为顶级组件一样，Java 9 应用程序将 Module 作为顶级组件。

**注意**：每个 Java 9 模块只有一个模块和一个模块描述符。与 Java 8 包不同，我们不能在一个模块中创建多个模块。

## HelloModule 示例程序

作为开发人员，我们首先从 “HelloWorld” 程序开始学习新的概念或编程语言。以同样的方式，我们开始通过 “ HelloModule” 模块开发来学习 Java 9 新概念“ **模块化编程** ”。

### 第一步：创建一个空的 Java 项目

如果不想额外命名的话一路 Next 就好了：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820071743202.png)

### 第二步：创建 HelloModule 模块

右键项目，创建一个新的【Module】，命名为：`com.wmyskxz.core` 

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820071850067.png)

并在新 Module 的 `src` 文件夹下新建包 `module.hello`，此时项目结构：

```bash
.
└── com.wmyskxz.core
    └── src
        └── module
            └── hello
```

### 第三步：编写 HelloModule.java

在刚才创建的包下新建 `HelloModule` 文件，并编写测试用的代码：

```java
package module.hello;

public class HelloModule {
  
    public void sayHello() {
        System.out.println("Hello Module!");
    }
}
```

### 第四步：为 Module 编写模块描述符

在 IDEA 中，我们可以直接右键 `src` 文件夹，快捷创建 `module-info.java` 文件：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820072601961.png)

编写 `module-info.java` 文件，将我们刚才的包 `module.hello` 里面的内容暴露出去（给其他 Module 使用）：

```java
module com.wmyskxz.core {
    exports module.hello;
}
```

`module` 关键字后面是我们的模块名称，里面的 `exports` 写明了我们想要暴露出去的包。此时的文件目录结构：

```bash
.
└── com.wmyskxz.core
    └── src
        ├── module
        │   └── hello
        │       └── HelloModule.java
        └── module-info.java
```

### 第五步：同样的方法编写客户端

用上面同样的方法，我们在项目根目录创建一个 `com.wmyskxz.client` 的 Module，并新建 `module.client` 包目录，并创建好我们的 `HelloModuleClient` 文件的大概样子：

```java
// HelloModuleClient.java
package module.client;

public class HelloModuleClient {

    public static void main(String[] args) {

    }
}
```

如果我们想要直接调用 `HelloModule` 类，会发现 IDEA 并没有提示信息，也就是说我们无法直接引用了..

我们需要先在模块描述符（同样需要在 `src` 目录创建 `module-info.java` 文件）中显式的引入我们刚才暴露出来的 `com.wmyskxz.core` 模块：

```java
module com.wmyskxz.client {
    requires com.wmyskxz.core;
}
```

*（ps：在 IDEA 中编写完成之后需要手动 `alt + enter` 引入模块依赖）*

这一步完成之后，我们就可以在刚才的 `HelloModuleClient` 中愉快的使用 `HelloModule` 文件了：

```java
package module.client;

import module.hello.HelloModule;

public class HelloModuleClient {

    public static void main(String[] args) {
        HelloModule helloModule = new HelloModule();
        helloModule.sayHello();
    }
}
```

此时的项目结构：

```bash
.
├── com.wmyskxz.client
│   └── src
│       ├── module
│       │   └── client
│       │       └── HelloModuleClient.java
│       └── module-info.java
└── com.wmyskxz.core
    └── src
        ├── module
        │   └── hello
        │       └── HelloModule.java
        └── module-info.java

```

### 第六步：运行测试

运行代码：

```bash
Hello Module!
```

成功！

## 模块系统小结

我们从上面的例子中可以看到，我们可以指定我们想要导出和引用的软件包，没有人可以不小心地使用那些不想被导出的软件包中的类。

Java 平台本身也已经使用其自己的模块系统对 JDK 进行了模块化。启动模块化应用程序时，JVM 会根据 `requires` 语句验证是否可以解析所有模块，这比脆弱的类路径要安全得多。模块使您能够通过强力执行封装和显式依赖来更好地构建应用程序。

# 四. 接口支持私有方法

在 Java 8 中，我们可以使用 `default` 和 `static` 方法在 Interfaces 中提供方法实现。但是，我们不能在接口中创建私有方法。

为了避免冗余代码和提高重用性，Oracle Corp 将在 Java SE 9 接口中引入私有方法。从 Java SE 9 开始，我们就可以使用 `private` 关键字在接口中编写私有和私有静态方法。

这些私有方法仅与其他类私有方法一样，它们之间没有区别。以下是演示：

```java
public interface FilterProcess<T> {

    // java 7 及以前 特性  全局常量 和抽象方法
    public static final String a ="22";
    boolean process(T t);

    // java 8 特性 静态方法和默认方法
    default void love(){
        System.out.println("java8 特性默认方法");
    }
    static void haha(){
        System.out.println("java8 特性静态方法");
    }

    // java 9 特性 支持私有方法
    private void java9(){}
}
```

# 五. 钻石操作符升级

我们知道，Java SE 7 引入了一项新功能：Diamond 运算符可避免多余的代码和冗长的内容，从而提高了可读性。但是，在 Java SE 8 中，Oracle Corp（Java库开发人员）发现将 Diamond 运算符与匿名内部类一起使用时存在一些限制。他们已解决了这些问题，并将其作为 Java 9 的一部分发布。

```java
// java6 及以前
Map<String,String> map7 = new HashMap<String,String>();
// java7 和 8 <> 没有了数据类型
Map<String,String> map8 = new HashMap<>();
// java9 添加了匿名内部类的功能 后面添加了大括号 {}  可以做一些细节的操作
Map<String,String> map9 = new HashMap<>(){};
```

# 六. Optional 改进

在 Java SE 9 中，Oracle Corp 引入了以下三种方法来改进 Optional 功能。

- `stream()`；
- `ifPresentOrElse()`；
- `or()`

## 可选 stream() 方法

如果给定的 Optional 对象中存在一个值，则此 `stream()` 方法将返回一个具有该值的顺序 Stream。否则，它将返回一个空流。

Java 9 中添加的`stream()` 方法允许我们延迟地处理可选对象，下面是演示：

```java
jshell> long count = Stream.of(
   ...>     Optional.of(1),
   ...>     Optional.empty(),
   ...>     Optional.of(2)
   ...> ).flatMap(Optional::stream)
   ...>     .count();
   ...> System.out.println(count);
   ...>
count ==> 2
2
```

*(Optiona l 流中包含 3 个 元素，其中只有 2 个有值。在使用 flatMap 之后，结果流中包含了 2 个值。)*

## 可选 ifPresentOrElse() 方法

我们知道，在 Java SE 8 中，我们可以使用 `ifPresent()`、`isPresent()` 和 `orElse()` 方法来检查 Optional 对象并对其执行功能。这个过程有些繁琐，Java SE 9 引入了一种新的方法来克服此问题。

**下面是示例：**

```bash
jshell> Optional<Integer> opt1 = Optional.of(4)
opt1 ==> Optional[4]

jshell> opt1.ifPresentOrElse( x -> System.out.println("Result found: " + x), () -> System.out.println("Not Found."))
Result found: 4

jshell> Optional<Integer> opt2 = Optional.empty()
opt2 ==> Optional.empty

jshell> opt2.ifPresentOrElse( x -> System.out.println("Result found: " + x), () -> System.out.println("Not Found."))
Not Found.
```

## 可选 or() 方法

在 Java SE 9 中，使用 `or()` 方法便捷的返回值。如果  Optional 包含值，则直接返回原值，否则就返回指定的值。`or()` 方法将 Supplier 作为参数指定默认值。下面是 API 的定义：

```java
public Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)
```

**下面是有值情况的演示：**

```bash
jshell> Optional<String> opStr = Optional.of("Rams")
opStr ==> Optional[Rams]

jshell> import java.util.function.*

jshell> Supplier<Optional<String>> supStr = () -> Optional.of("No Name")
supStr ==> $Lambda$67/222624801@23faf8f2

jshell> opStr.or(supStr)
$5 ==> Optional[Rams]
```

**下面是为空情况的演示：**

```bash
jshell> Optional<String> opStr = Optional.empty()
opStr ==> Optional.empty

jshell> Supplier<Optional<String>> supStr = () -> Optional.of("No Name")
supStr ==> $Lambda$67/222624801@23faf8f2

jshell> opStr.or(supStr)
$7 ==> Optional[No Name]
```

# 七. Stream API 改进

长期以来，Streams API 可以说是对 Java 标准库的最佳改进之一。在 Java 9 中，Stream 接口新增加了四个有用的方法：*dropWhile、takeWhile、ofNullable 和 iterate*。下面我们来分别演示一下。

## takeWhile() 方法

在 Stream API 中，`takeWhile()` 方法返回与 Predicate 条件匹配的最长前缀元素。

它以 Predicate 接口作为参数。Predicate 是布尔表达式，它返回 `true` 或 `false`。对于有序和无序流，其行为有所不同。让我们通过下面的一些简单示例对其进行探讨。

**Stream API 定义：**

```java
default Stream<T> takeWhile(Predicate<? super T> predicate)
```

**有序流示例： -**

```bash
jshell> Stream<Integer> stream = Stream.of(1,2,3,4,5,6,7,8,9,10)
stream ==> java.util.stream.ReferencePipeline$Head@55d56113

jshell> stream.takeWhile(x -> x < 4).forEach(a -> System.out.println(a))
1
2
3
```

**无序流示例： -**

```bash
jshell> Stream<Integer> stream = Stream.of(1,2,4,5,3,6,7,8,9,10)
stream ==> java.util.stream.ReferencePipeline$Head@55d56113

jshell> stream.takeWhile(x -> x < 4).forEach(a -> System.out.println(a))
1
2
```

从上面的例子中我们可以看出，`takeWhile()` 方法在遇到第一个返回 `false` 的元素时，它将停止向下遍历。

## dropWhile() 方法

与 `takeWhile()` 相对应，`dropWhile()` 用于删除与条件匹配的最长前缀元素，并返回其余元素。

**Stream API 定义：**

```java
default Stream<T> dropWhile(Predicate<? super T> predicate)
```

**有序流示例： -**

```bash
jshell> Stream<Integer> stream = Stream.of(1,2,3,4,5,6,7,8,9,10)
stream ==> java.util.stream.ReferencePipeline$Head@55d56113

jshell> stream.dropWhile(x -> x < 4).forEach(a -> System.out.println(a))
4
5
6
7
8
9
10
```

**无序流示例： -**

```bash
jshell> Stream<Integer> stream = Stream.of(1,2,4,5,3,6,7,8,9,10)
stream ==> java.util.stream.ReferencePipeline$Head@55d56113

jshell> stream.dropWhile(x -> x < 4).forEach(a -> System.out.println(a))
4
5
3
6
7
8
9
10
```

## iterate() 方法

在 Stream API 中，`iterate()` 方法能够返回以 `initialValue`（第一个参数）开头，匹配 Predicate（第二个参数），并使用第三个参数生成下一个元素的元素流。

**Stream API 定义：**

```java
static <T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)
```

**IntStream 迭代示例： -**

```bash
jshell> IntStream.iterate(2, x -> x < 20, x -> x * x).forEach(System.out::println)
2
4
16
```

这里，整个元素流以数字 `2` 开始，结束条件是 `< 20`，并且在下一次迭代中，递增值是自身值的平方。

而这在 Java SE 8 中需要辅助 `filter` 条件才能完成：

```bash
jshell> IntStream.iterate(2, x -> x * x).filter(x -> x < 20).forEach(System.out::println)
2
4
16
```

## ofNullable() 方法

在 Stream API 中，`ofNullable()` 返回包含单个元素的顺序 Stream（如果非null），否则返回空 Stream。

**Java SE 9 示例： -**

```bash
jshell> Stream<Integer> s = Stream.ofNullable(1)
s ==> java.util.stream.ReferencePipeline$Head@1e965684

jshell> s.forEach(System.out::println)
1

jshell> Stream<Integer> s = Stream.ofNullable(null)
s ==> java.util.stream.ReferencePipeline$Head@3b088d51

jshell> s.forEach(System.out::println)

jshell>
```

> 注意：Stream 的子接口（如 IntStream、LongStream 等..）都继承了上述的 `4` 种方法。

# 八. 反应式流（Reactive Streams）

反应式编程的思想最近得到了广泛的流行。在 Java 平台上有流行的反应式库 RxJava 和 Reactor。反应式流规范的出发点是提供一个带非阻塞负压（ non-blocking backpressure ） 的异步流处理规范。

Java SE 9 Reactive Streams API 是一个发布/订阅框架，用于实现 Java 语言非常轻松地实现异步操作，可伸缩和并行应用程序。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820085043258.png)

*（从上图中可以很清楚地看到，Processor既可以作为订阅服务器，也可以作为发布服务器。）*

反应式流规范的核心接口已经添加到了 Java9 中的 `java.util.concurrent.Flow` 类中。

## 反应流示例

让我们从一个简单的示例开始，在该示例中，我们将实现 Flow API Subscriber 接口并使用 SubmissionPublisher 创建发布者并发送消息。

### 流数据

假设我们有一个 Employee 类，它将用于创建要从发布者发送到订阅者的流消息。

```java
package com.wmyskxz.reactive.beans;

public class Employee {

    private int id;
    private String name;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Employee(int i, String s) {
        this.id = i;
        this.name = s;
    }

    public Employee() {
    }

    @Override
    public String toString() {
        return "[id=" + id + ",name=" + name + "]";
    }
}
```

我们还有一个实用的工具类，可以为我们创建一个雇员列表：

```java
package com.wmyskxz.reactive.streams;

import com.wmyskxz.reactive.beans.Employee;
import java.util.List;

public class EmpHelper {

    public static List<Employee> getEmps() {
        return List.of(
            new Employee(1, "我没有三颗心脏"),
            new Employee(2, "三颗心脏"),
            new Employee(3, "心脏")
        );
    }
}
```

### 订阅者

```java
package com.wmyskxz.reactive.streams;

import com.wmyskxz.reactive.beans.Employee;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class MySubscriber implements Subscriber<Employee> {

    private Subscription subscription;

    private int counter = 0;

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("Subscribed");
        this.subscription = subscription;
        this.subscription.request(1); // requesting data from publisher
        System.out.println("onSubscribe requested 1 item");
    }

    @Override
    public void onNext(Employee item) {
        System.out.println("Processing Employee " + item);
        counter++;
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable e) {
        System.out.println("Some error happened");
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("All Processing Done");
    }

    public int getCounter() {
        return counter;
    }
}
```

- `Subscription`变量以保留引用，以便可以在`onNext`方法中提出请求。
- `counter`变量以保持已处理项目数的计数，请注意，其值在 `onNext` 方法中增加了。在我们的 `main` 方法中将使用它来等待执行完成，然后再结束主线程。
- 在`onSubscribe`方法中调用订阅请求以开始处理。还要注意，`onNext`在处理完项目后再次调用该方法，要求发布者处理下一个项目。
- `onError`并`onComplete`在这里没有太多作用，但在现实世界中的场景，他们应该被使用时出现的错误或资源的清理成功处理完成时进行纠正措施。

### 反应式流测试程序

 我们将`SubmissionPublisher`作为示例使用 Publisher，因此让我们看一下反应流实现的测试程序：

```java
package com.wmyskxz.reactive.streams;

import com.wmyskxz.reactive.beans.Employee;
import java.util.List;
import java.util.concurrent.SubmissionPublisher;

public class MyReactiveApp {

    public static void main(String[] args) throws InterruptedException {

        // Create Publisher
        SubmissionPublisher<Employee> publisher = new SubmissionPublisher<>();

        // Register Subscriber
        MySubscriber subs = new MySubscriber();
        publisher.subscribe(subs);

        List<Employee> emps = EmpHelper.getEmps();

        // Publish items
        System.out.println("Publishing Items to Subscriber");
        for (Employee employee : emps) {
            publisher.submit(employee);
            Thread.sleep(1000);// simulate true environment
        }

        // logic to wait till processing of all messages are over
        while (emps.size() != subs.getCounter()) {
            Thread.sleep(10);
        }
        // close the Publisher
        publisher.close();

        System.out.println("Exiting the app");
    }
}
```

上面代码中最重要的部分就是 `subscribe` 和 `submit` 方法的调用了。另外，我们应该在使用完之后关闭发布者，以避免任何内存泄漏。

当执行上述程序时，我们将得到以下输出：

```bash
Subscribed
onSubscribe requested 1 item
Publishing Items to Subscriber
Processing Employee [id=1,name=我没有三颗心脏]
Processing Employee [id=2,name=三颗心脏]
Processing Employee [id=3,name=心脏]
Exiting the app
All Processing Done
```

> 以上所有代码均可以在「MoreThanJava」项目下的 `demo-project` 下找到：[传送门](https://github.com/wmyskxz/MoreThanJava)
>
> 另外，如果您想了解更多内容请访问：https://www.journaldev.com/20723/java-9-reactive-streams

# 九. 进程 API

Java 9 增加了 ProcessHandle 接口，可以对原生进程进行管理，尤其适合于管理长时间运行的进程。

在使用 ProcessBuilder 来启动一个进程之后，可以通过 `Process.toHandle()` 方法来得到一个 ProcessHandle 对象的实例。通过 ProcessHandle 可以获取到由 ProcessHandle.Info 表示的进程的基本信息，如命令行参数、可执行文件路径和启动时间等。ProcessHandle 的 `onExit()` 方法返回一个 CompletableFuture 对象，可以在进程结束时执行自定义的动作。

**下面是进程 API 的使用示例：**

```java
final ProcessBuilder processBuilder = new ProcessBuilder("top")
    .inheritIO();
final ProcessHandle processHandle = processBuilder.start().toHandle();
processHandle.onExit().whenCompleteAsync((handle, throwable) -> {
    if (throwable == null) {
        System.out.println(handle.pid());
    } else {
        throwable.printStackTrace();
    }
});
```

# 十. 升级的 Try-With-Resources

我们知道，Java SE 7 引入了一种新的异常处理结构：Try-With-Resources 以自动管理资源。这一新声明的主要目标是 “自动的更好的资源管理”。

Java SE 9 将对该语句进行一些改进，以避免更多的冗长和提高可读性。

**Java SE 7示例**

```java
void testARM_Before_Java9() throws IOException{
   BufferedReader reader1 = new BufferedReader(new FileReader("journaldev.txt"));
   try (BufferedReader reader2 = reader1) {
     System.out.println(reader2.readLine());
   }
}
```

**Java SE 9示例：**

```java
void testARM_Java9() throws IOException{
   BufferedReader reader1 = new BufferedReader(new FileReader("journaldev.txt"));
   try (reader1) {
     System.out.println(reader1.readLine());
   }
}
```

# 十一. HTTP / 2

Java 9 提供了一种执行 HTTP 调用的新方法。这种过期过期的替代方法是旧的`HttpURLConnection`。API 也支持 WebSockets 和 HTTP / 2。需要注意的是：新的 HttpClient API 在 Java 9 中以所谓的 _incubator module_ 的形式提供。这意味着该API尚不能保证最终实现 100％。尽管如此，随着Java 9的到来，您已经可以开始使用此API：

```java
HttpClient client = HttpClient.newHttpClient();

HttpRequest req =
   HttpRequest.newBuilder(URI.create("http://www.google.com"))
              .header("User-Agent","Java")
              .GET()
              .build();


HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandler.asString());
```

# 十二. 多版本兼容 Jar 包

多版本兼容 JAR 功能能让你创建仅在特定版本的 Java 环境中运行库程序时选择使用的 class 版本。

通过 **--release** 参数指定编译版本。

具体的变化就是 META-INF 目录下 MANIFEST.MF 文件新增了一个属性：

```bash
Multi-Release: true
```

然后 META-INF 目录下还新增了一个 `versions` 目录，如果是要支持 Java 9，则在 `versions` 目录下有 9 的目录。

```bash
multirelease.jar
├── META-INF
│   └── versions
│       └── 9
│           └── multirelease
│               └── Helper.class
├── multirelease
    ├── Helper.class
    └── Main.class
```

具体的例子可以在这里查看到：https://www.runoob.com/java/java9-multirelease-jar.html，这里不做赘述。

# 其他更新

## 改进应用安全性能

Java 9 新增了 `4` 个 SHA-3 哈希算法，SHA3-224、SHA3-256、SHA3-384 和 SHA3-512。另外也增加了通过 `java.security.SecureRandom` 生成使用 DRBG 算法的强随机数。下面给出了 SHA-3 哈希算法的使用示例：

```java
final MessageDigest instance = MessageDigest.getInstance("SHA3-224");
final byte[] digest = instance.digest("".getBytes());
System.out.println(Hex.encodeHexString(digest));
```

## 统一 JVM 日志

Java 9 中 ，JVM 有了统一的日志记录系统，可以使用新的命令行选项 `-Xlog` 来控制 JVM 上所有组件的日志记录。该日志记录系统可以设置输出的日志消息的标签、级别、修饰符和输出目标等。

## G1 设为默认回收器实现

Java 9 移除了在 Java 8 中 被废弃的垃圾回收器配置组合（比如 `ParNew + SerialOld`），同时把 G1 设为默认的垃圾回收器实现（`32` 位和 `64` 位系统都是）。 另外，CMS 垃圾回收器已经被声明为废弃。Java 9 也增加了很多可以通过 jcmd 调用的诊断命令。

## String 底层存储结构更改

![String 底层从 char[] 数组换位了 byte[]](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820093906672.png)

为了对字符串采用更节省空间的内部表示，`String`类的内部表示形式从 UTF-16 `char`数组更改为`byte`带有编码标记字段的数组。新`String`类将存储基于字符串内容编码为 ISO-8859-1 / Latin-1（每个字符一个字节）或 UTF-16（每个字符两个字节）的字符。编码标志将指示使用哪种编码。

*(ps: 另外内部大部分方法也多了字符编码的判断)*

## CompletableFuture API 的改进

在 Java SE 9 中，Oracle Corp 将改进 CompletableFuture API，以解决 Java SE 8 中提出的一些问题。它们将被添加以支持某些延迟和超时，某些实用程序方法以及更好的子类化。

```java
Executor exe = CompletableFuture.delayedExecutor(50L, TimeUnit.SECONDS);
```

这里的 `delayExecutor()` 是一种静态实用程序方法，用于返回新的 Executor，该 Executor 在给定的延迟后将任务提交给默认的执行程序。

## I/O 流新特性

类 `java.io.InputStream` 中增加了新的方法来读取和复制 InputStream 中包含的数据。

- `readAllBytes`：读取 InputStream 中的所有剩余字节。
- `readNBytes`： 从 InputStream 中读取指定数量的字节到数组中。
- `transferTo`：读取 InputStream 中的全部字节并写入到指定的 OutputStream 中 。

**下面是新方法的使用示例：**

```java
public class TestInputStream {
    private InputStream inputStream;
    private static final String CONTENT = "Hello World";
    @Before
    public void setUp() throws Exception {
        this.inputStream =
            TestInputStream.class.getResourceAsStream("/input.txt");
    }
    @Test
    public void testReadAllBytes() throws Exception {
        final String content = new String(this.inputStream.readAllBytes());
        assertEquals(CONTENT, content);
    }
    @Test
    public void testReadNBytes() throws Exception {
        final byte[] data = new byte[5];
        this.inputStream.readNBytes(data, 0, 5);
        assertEquals("Hello", new String(data));
    }
    @Test
    public void testTransferTo() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.inputStream.transferTo(outputStream);
        assertEquals(CONTENT, outputStream.toString());
    }
}
```

## JavaScript 引擎 Nashorn 改进

Nashorn 是 Java 8 中引入的新的 JavaScript 引擎。Java 9 中的 Nashorn 已经实现了一些 ECMAScript 6 规范中的新特性，包括模板字符串、二进制和八进制字面量、迭代器 和 `for..of` 循环和箭头函数等。Nashorn 还提供了 API 把 ECMAScript 源代码解析成抽象语法树（ Abstract Syntax Tree，AST ） ，可以用来对 ECMAScript 源代码进行分析。

## 标识符增加限制

JDK 8 之前 `String _ = "hello;` 这样的标识符可以使用，JDK 9 之后就不允许使用了。

## 改进的 Javadoc

有时候，微小的事情会带来很大的不同。您是否之前一直像我一样一直使用 Google 查找正确的 Javadoc 页面？现在将不再需要。Javadoc 现在在 API 文档本身中包含了搜索功能。另外，Javadoc 输出现在兼容 HTML 5。另外，您会注意到每个 Javadoc 页面都包含有关类或接口来自哪个 JDK 模块的信息。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-res.img.7009bd8d-3943-49c2-9d47-69b881622a59.png)

## 改进的 @Deprecated 注解

注解 `@Deprecated` 可以标记 Java API 状态，可以是以下几种：

- 使用它存在风险，可能导致错误
- 可能在未来版本中不兼容
- 可能在未来版本中删除
- 一个更好和更高效的方案已经取代它。

Java 9 中注解增加了两个新元素：**since** 和 **forRemoval**。

- **since**: 元素指定已注解的API元素已被弃用的版本。
- **forRemoval**: 元素表示注解的 API 元素在将来的版本中被删除，应该迁移 API。

以下实例为 Java 9 中关于 Boolean 类的说明文档，文档中 `@Deprecated` 注解使用了 `since` 属性：[Boolean Class](https://docs.oracle.com/javase/9/docs/api/java/lang/Boolean.html#Boolean-boolean-)。

![JavaDoc 关于 Boolean 的说明截取](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/image-20200820095544452.png)

## 多分辨率图像 API

在 Java SE 9 中，Oracle Corp 将引入一个新的 Multi-Resolution Image API。此 API 中的重要接口是MultiResolutionImage。在 `java.awt.image` 包中可用。

MultiResolutionImage 封装了一组具有不同高度和宽度（即不同分辨率）的图像，并允许我们根据需求查询它们。

## 变量句柄

变量句柄（VarHandle）是对于一个变量的强类型引用，或者是一组参数化定义的变量族，包括了静态字段、非静态字段、数组元素等，VarHandle 支持不同访问模型下对于变量的访问，包括简单的 `read/write` 访问，`volatile read/write` 访问，以及 CAS 访问。

VarHandle 相比于传统的对于变量的并发操作具有巨大的优势，在 JDK 9 引入了 VarHandle 之后，JUC 包中对于变量的访问基本上都使用 VarHandle，比如 AQS 中的 CLH 队列中使用到的变量等。

> 了解更多戳这里：https://kknews.cc/code/amqz5on.html

## 改进方法句柄（Method Handle）

类 `java.lang.invoke.MethodHandles` 增加了更多的静态方法来创建不同类型的方法句柄：

- **`arrayConstructor：`** 创建指定类型的数组。
- **`arrayLength：`** 获取指定类型的数组的大小。
- **`varHandleInvoker 和 varHandleExactInvoker：`** 调用 VarHandle 中的访问模式方法。
- **`zero：`** 返回一个类型的默认值。
- **`empty：`** 返回 MethodType 的返回值类型的默认值。
- **`loop、countedLoop、iteratedLoop、whileLoop 和 doWhileLoop：`** 创建不同类型的循环，包括 for 循环、while 循环 和 do-while 循环。
- **`tryFinally：`** 把对方法句柄的调用封装在 try-finally 语句中。

## 提前编译 AOT

借助 Java 9，特别是[JEP 295](https://openjdk.java.net/jeps/295)，JDK 获得了**提前（ahead-of-time，AOT）** 编译器 jaotc。该编译器使用 OpenJDK 项目 [Graal](https://openjdk.java.net/projects/graal/) 进行后端代码生成，这样做的原因如下：

> JIT 编译器速度很快，但是Java程序可能非常庞大，以至于JIT完全预热需要很长时间。很少使用的Java方法可能根本不会被编译，由于重复的解释调用可能会导致性能下降
>
> 原文链接：[openjdk.java.net/jeps/295](https://openjdk.java.net/jeps/295)

**Graal OpenJDK 项目** 演示了用纯 Java 编写的编译器可以生成高度优化的代码。使用此 AOT 编译器和 Java 9，您可以提前手动编译 Java 代码。这意味着在执行之前生成机器代码，而不是像 JIT 编译器那样在运行时生成代码，这是第一种实验性的方法。

```bash
# using the new AOT compiler (jaotc is bundeled within JDK 9 and above)
jaotc --output libHelloWorld.so HelloWorld.class
jaotc --output libjava.base.so --module java.base
 
# with Java 9 you have to manually specify the location of the native code
java -XX:AOTLibrary=./libHelloWorld.so,./libjava.base.so HelloWorld
```

这将改善启动时间，因为 JIT 编译器不必拦截程序的执行。这种方法的主要缺点是生成的机器代码依赖于程序所在的平台（Linux，MacOS，windows...）。这可能导致 AOT 编译代码与特定平台绑定。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/什么？Java9的这些史诗级更新你都不知道？Java9版本特性一文打尽！/17358195b57636ad.png)

> 了解更多戳这里：https://juejin.im/post/6850418120570437646

## 更多...

完整特性列表：https://openjdk.java.net/projects/jdk9/

# 参考资料

1. OpenJDK 官方文档 - https://openjdk.java.net/projects/jdk9/
2. Java 9 Modules | JournalDev - https://www.journaldev.com/13106/java-9-modules
3. JDK 9 新特性详解 - https://my.oschina.net/mdxlcj/blog/1622984
4. Java SE 9:Stream API Improvements - https://www.journaldev.com/13204/javase9-stream-api-improvements
5. 9 NEW FEATURES IN JAVA 9 - https://www.pluralsight.com/blog/software-development/java-9-new-features
6. Java 9 新特性概述 | IBM - https://developer.ibm.com/zh/articles/the-new-features-of-Java-9/
7. Java 9 多版本兼容 jar 包 | 菜鸟教程 - https://www.runoob.com/java/java9-multirelease-jar.html

# 文章推荐

1. 这都JDK15了，JDK7还不了解？ - https://www.wmyskxz.com/2020/08/18/java7-ban-ben-te-xing-xiang-jie/
2. 全网最通透的 Java 8 版本特性讲解 - https://www.wmyskxz.com/2020/08/19/java8-ban-ben-te-xing-xiang-jie/
3. 你记笔记吗？关于最近知识管理工具革新潮心脏有话要说 - https://www.wmyskxz.com/2020/08/16/ni-ji-bi-ji-ma-guan-yu-zui-jin-zhi-shi-guan-li-gong-ju-ge-xin-chao-xin-zang-you-hua-yao-shuo/
4. 黑莓OS手册是如何详细阐述底层的进程和线程模型的？ - https://www.wmyskxz.com/2020/07/31/hao-wen-tui-jian-hei-mei-os-shou-ce-shi-ru-he-xiang-xi-chan-shu-di-ceng-de-jin-cheng-he-xian-cheng-mo-xing-de/
5. 「MoreThanJava」系列文集 - https://www.wmyskxz.com/categories/MoreThanJava/

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！