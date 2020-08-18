![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这都出Java15了，Java7的特性还没搞明白？/image-20200818141117247.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# 特性总览

以下是 Java 7 中引入的部分新特性，关于 Java 7 更详细的介绍可参考[官方文档](https://www.oracle.com/java/technologies/javase/jdk7-relnotes.html)。

- `java.lang` 包

  - Java 7 多线程下自定义类加载器的优化
  
- Java 语言特性

  - 改进的类型推断；
  - 使用 `try-with-resources` 进行自动资源管理
  - `switch` 支持 `String`；
  - `catch` 多个异常；
  - 数字格式增强（允许数字字面量下划线分割）；
  - 二进制字面量；
  - 增强的文件系统；
  - `Fork/Join` 框架；
  
- Java 虚拟机 *(JVM)*

  - 提供新的 G1 收集器；
  - 加强对动态调用的支持；
  - 新增分层编译支持；
  - 压缩 Oops；
  - 其他优化；
- 其他；

# 多线程下自定义类加载器的优化

在 Java 7 之前，某些情况下的自定义类加载器容易出现死锁问题。下面👇来简单分析演示一下[官方给的例子](https://docs.oracle.com/javase/7/docs/technotes/guides/lang/cl-mt.html) *(下面用中文伪代码还原了一下)*：

```text
// 类的继承情况：
class A extends B
class C extends D

// 类加载器：
Custom Classloader CL1:
    直接加载类 A
    委托 CL2 加载类 B
Custom Classloader CL2:
    直接加载类 C
    委托 CL1 加载类 D
    
// 多线程下的情况：
Thread 1:
    使用 CL1 加载类 A
    → 定义类 A 的时候会触发 loadClass(B)，这时会尝试 锁住🔐 CL2    
Thread 2：
    使用 CL2 加载类 C
    → 定义 C 的时候会触发 loadClass(D)，这时会尝试 锁住🔐 CL1
➡️ 造成 死锁☠️
```

造成死锁的重要原因出在 JDK 默认的 `java.lang.ClassLoader.loadClass()` 方法上：

![JDK 7 和 JDK 6 loadClass 方法的对比](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这都出Java15了，Java7的特性还没搞明白？/image-20200818092624120.png)

可以看到，JDK 6 及之前的 `loadClass()` 的 `synchronized` 关键字是加在方法级别的，那么这就意味加载类时获取到的是一个 `ClassLoader` 级别的锁。

我们来描述一下死锁产生的情况：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这都出Java15了，Java7的特性还没搞明白？/image-20200818095052649.png)

文字版的描述如下：

- **线程1**：CL1 去 `loadClass(A)` 获取到了 CL1 对象锁，因为 A 继承了类 B，`defineClass(A)` 会触发 `loadClass(B)`，尝试获取 CL2 对象锁；
- **线程2**：CL2 去 `loadClass(C)` 获取到了 CL2 对象锁，因为 C 继承了类 D，`defineClass(C)` 会触发 `loadClass(D)`，尝试获取 CL1 对象锁
- **线程1** 尝试获取 CL2 对象锁的时候，CL2 对象锁已经被 **线程2** 拿到了，那么 **线程1** 等待 **线程2** 释放 CL2 对象锁。
- **线程2** 尝试获取 CL1 对像锁的时候，CL1 对像锁已经被 **线程1** 拿到了，那么 **线程2** 等待 **线程1** 释放 CL1 对像锁。
- 然后两个线程一直在互相等中…从而产生了死锁现象...

究其原因就是因为 `ClassLoader` 的锁太粗粒度了。在 Java 7 中，在使用具有并行功能的类加载器的时候，将专门用一个带有 **类加载器和类名称组合的对象** 用于进行同步操作。*(感兴趣可以看一下 `loadClass()` 内部的 `getClassLoadingLock(name)` 方法)* 

Java 7 之后，之前线程死锁的情况将不存在：

```text
线程1：
  使用CL1加载类A（锁定CL1 + A）
    defineClass A触发
      loadClass B（锁定CL2 + B）

线程2：
  使用CL2加载类C（锁定CL2 + C）
    defineClass C触发
      loadClass D（锁定CL1 + D）
```

# 改进的类型推断

在 Java 7 之前，使用泛型时，您必须为变量类型及其实际类型提供类型参数：

```Java
Map<String, List<String>> map = new HashMap<String, List<String>>();
```

在 Java 7 之后，编译器可以通过识别空白菱形推断出在声明在左侧定义的类型：

```Java
Map<String, List<String>> map = new HashMap<>();
```

# 自动资源管理

在 Java 7 之前，我们必须使用 `finally` 块来清理资源，但防止系统崩坏的清理资源的操作并不是强制性的。在 Java 7 中，我们无需显式的资源清理，它允许我们使用 `try-with-resrouces` 语句来借由 JVM 自动完成清理工作。

Java 7 之前：

```Java
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader(path));
    return br.readLine();
} catch (Exception e) {
    log.error("BufferedReader Exception", e);
} finally {
    if (br != null) {
        try {
            br.close();
        } catch (Exception e) {
            log.error("BufferedReader close Exception", e);
        }
    }
}
```

Java 7 及之后的写法：

```Java
try (BufferedReader br = new BufferedReader(new FileReader(path)) {
    return br.readLine();
} catch (Exception e) {
    log.error("BufferedReader Exception", e);
}
```

# switch 支持 String

`switch` 在 Java 7 中能够接受 `String` 类型的参数，实例如下：

```Java
String s = ...
switch(s) {
case "condition1":
    processCondition1(s);
    break;
case "condition2":
    processCondition2(s);
    break;
default:
    processDefault(s);
    break;
} 
```

# catch 多个异常

自Java 7开始，`catch` 中可以一次性捕捉多个异常做统一处理。示例如下：

```Java
public void handle() {
    ExceptionThrower thrower = new ExceptionThrower();
    try {
        thrower.manyExceptions();
    } catch (ExceptionA | ExceptionB ab) {
        System.out.println(ab.getClass());
    } catch (ExceptionC c) {
        System.out.println(c.getClass());
    }
}
```

> **请注意**：如果 `catch` 块处理多个异常类型，则 `catch` 参数隐式为 `final` 类型，这意味着，您不能在 `catch` 块中为其分配任何值。

# 数字格式增强

为了解决长数字可读性不好的问题，在 Java 7 中支持了使用下划线分割的数字表达形式：

```Java
/**
 * Supported in int
 * */
int improvedInt = 10_00_000;
/**
 * Supported in float
 * */
float improvedFloat = 10_00_000f;
/**
 * Supported in long
 * */
float improvedLong = 10_00_000l;
/**
 * Supported in double
 * */
float improvedDouble = 10_00_000; 
```

# 二进制字面量

在 Java 7 中，您可以使用整型类型 *(**`byte`**、**`short`**、**`int`**、**`long`**)* 并加上前缀 `0b` *(或 **`0B`**)* 来创建二进制字面量。这在 Java 7 之前，您只能使用八进制值 *(前缀为 **`0`**)* 或十六进制值 *(前缀为 **`0x`** 或者 **`0X`**)* 来创建：

```Java
int sameVarOne = 0b01010000101;
int sameVarTwo = 0B01_010_000_101;
byte byteVar = (byte) 0b01010000101;
short shortVar = (short) 0b01010000101  
```

# 增强的文件系统

Java 7 推出了全新的`NIO 2.0` API以此改变针对文件管理的不便，使得在`java.nio.file`包下使用`Path`、`Paths`、`Files`、`WatchService`、`FileSystem`等常用类型可以很好的简化开发人员对文件管理的编码工作。

## 1 - Path 接口 和 Paths 类

`Path`接口的某些功能其实可以和`java.io`包下的`File`类等价，当然这些功能仅限于只读操作。在实际开发过程中，开发人员可以联用`Path`接口和`Paths`类，从而获取文件的一系列上下文信息。

- `int getNameCount()`: 获取当前文件节点数
- `Path getFileName()`: 获取当前文件名称
- `Path getRoot()`: 获取当前文件根目录
- `Path getParent()`: 获取当前文件上级关联目录

联用`Path`接口和`Paths`类型获取文件信息：

```Java
Path path = Paths.get("G:/test/test.xml");
System.out.println("文件节点数:" + path.getNameCount());
System.out.println("文件名称:" + path.getFileName());
System.out.println("文件根目录:" + path.getRoot());
System.out.println("文件上级关联目录:" + path.getParent());
```

## 2 - Files 类

联用`Path`接口和`Paths`类可以很方便的访问到目标文件的上下文信息。当然这些操作全都是只读的，如果开发人员想对文件进行其它非只读操作，比如文件的创建、修改、删除等操作，则可以使用`Files`类型进行操作。

Files类型常用方法如下：

- `Path createFile()`: 在指定的目标目录创建新文件
- `void delete()`: 删除指定目标路径的文件或文件夹
- `Path copy()`: 将指定目标路径的文件拷贝到另一个文件中
- `Path move()`: 将指定目标路径的文件转移到其他路径下，并删除源文件

使用`Files`类型复制、粘贴文件示例：

```Java
Files.copy(Paths.get("/test/src.xml"), Paths.get("/test/target.xml"));
```

使用 `Files` 类型来管理文件，相对于传统的 I/O 方式来说更加方便和简单。因为具体的操作实现将全部移交给 `NIO 2.0` API，开发人员则无需关注。

## 3 - WatchService

Java 7 还为开发人员提供了一套全新的文件系统功能，那就是文件监测。 在此或许有很多朋友并不知晓文件监测有何意义及目，那么请大家回想下调试成热发布功能后的 Web 容器。当项目迭代后并重新部署时，开发人员无需对其进行手动重启，因为 Web 容器一旦监测到文件发生改变后，便会自动去适应这些“变化”并重新进行内部装载。Web 容器的热发布功能同样也是基于文件监测功能，所以不得不承认，文件监测功能的出现对于 Java 文件系统来说是具有重大意义的。

文件监测是基于事件驱动的，事件触发是作为监测的先决条件。开发人员可以使用`java.nio.file`包下的`StandardWatchEventKinds`类型提供的3种字面常量来定义监测事件类型，值得注意的是监测事件需要和`WatchService`实例一起进行注册。

`StandardWatchEventKinds`类型提供的监测事件：

- `ENTRY_CREATE`：文件或文件夹新建事件；
- `ENTRY_DELETE`：文件或文件夹删除事件；
- `ENTRY_MODIFY`：文件或文件夹粘贴事件；

使用`WatchService`类实现文件监控完整示例：

```Java
public static void testWatch() {
    /* 监控目标路径 */
    Path path = Paths.get("G:/");
    try {
        /* 创建文件监控对象. */
        WatchService watchService = FileSystems.getDefault().newWatchService();

        /* 注册文件监控的所有事件类型. */
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        /* 循环监测文件. */
        while (true) {
            WatchKey watchKey = watchService.take();

            /* 迭代触发事件的所有文件 */
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                System.out.println(event.context().toString() + " 事件类型：" + event.kind());
            }

            if (!watchKey.reset()) {
                return;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

通过上述程序示例我们可以看出，使用`WatchService`接口进行文件监控非常简单和方便。首先我们需要定义好目标监控路径，然后调用`FileSystems`类型的`newWatchService()`方法创建`WatchService`对象。接下来我们还需使用`Path`接口的`register()`方法注册`WatchService`实例及监控事件。当这些基础作业层全部准备好后，我们再编写外围实时监测循环。最后迭代`WatchKey`来获取所有触发监控事件的文件即可。

# Fork/ Join 框架

## 1 - 什么是 Fork/ Join 框架

Java 7 提供的一个用于并行执行任务的框架，是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架。比如我们要计算 `1 + 2 + .....+ 10000`，就可以分割成 `10` 个子任务，让每个子任务分别对 `1000` 个数进行运算，最终汇总这 `10` 个子任务的结果。 

Fork/Join 的运行流程图如下：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这都出Java15了，Java7的特性还没搞明白？/image-20200818082737882.png)

## 2 - 工作窃取算法

工作窃取 *(`work-stealing`)* 算法是指某个线程从其他队列里窃取任务来执行。**核心思想是：自己的活干完了去看看别人有没有没有干完的活儿，如果有就拿过来帮他干。**

工作窃取的运行流程图如下：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这都出Java15了，Java7的特性还没搞明白？/image-20200818084222591.png)

工作窃取算法的优点是充分利用线程进行并行计算，并减少了线程间的竞争，其缺点是在某些情况下还是存在竞争，比如双端队列里只有一个任务时。并且消耗了更多的系统资源，比如创建多个线程和多个双端队列。

## 3 - 简单示例

让我们通过一个简单的需求来使用下`Fork／Join`框架，需求是：计算`1 + 2 + 3 + 4`的结果。

使用`Fork/Join`框架首先要考虑到的是如何分割任务，如果我们希望每个子任务最多执行两个数的相加，那么我们设置分割的阈值是`2`，由于是`4`个数字相加，所以`Fork/Join`框架会把这个任务`fork`成两个子任务，子任务一负责计算`1 + 2`，子任务二负责计算`3 + 4`，然后再`join`两个子任务的结果。

因为是有结果的任务，所以必须继承`RecursiveTask`，实现代码如下：

```java
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * CountTask.
 *
 * @author blinkfox on 2018-01-03.
 * @originalRef http://blinkfox.com/2018/11/12/hou-duan/java/java7-xin-te-xing-ji-shi-yong/#toc-heading-5
 */
public class CountTask extends RecursiveTask<Integer> {

    /** 阈值. */
    public static final int THRESHOLD = 2;

    /** 计算的开始值. */
    private int start;

    /** 计算的结束值. */
    private int end;

    /**
     * 构造方法.
     *
     * @param start 计算的开始值
     * @param end 计算的结束值
     */
    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 执行计算的方法.
     *
     * @return int型结果
     */
    @Override
    protected Integer compute() {
        int sum = 0;

        // 如果任务足够小就计算任务.
        if ((end - start) <= THRESHOLD) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 如果任务大于阈值，就分裂成两个子任务来计算.
            int middle = (start + end) / 2;
            CountTask leftTask = new CountTask(start, middle);
            CountTask rightTask = new CountTask(middle + 1, end);

            // 等待子任务执行完，并得到结果，再合并执行结果.
            leftTask.fork();
            rightTask.fork();
            sum = leftTask.join() + rightTask.join();
        }
        return sum;
    }

    /**
     * main方法.
     *
     * @param args 数组参数
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool fkPool = new ForkJoinPool();
        CountTask task = new CountTask(1, 4);
        Future<Integer> result = fkPool.submit(task);
        System.out.println("result:" + result.get());
    }

}
```

# 虚拟机增强

Oracle 官网介绍：https://docs.oracle.com/javase/7/docs/technotes/guides/vm/enhancements-7.html

## 1 - 提供新的 G1 收集器

Java 7 引入了一个被称为 **Garbage-First** *(G1)* 的垃圾收集器。G1 是服务器式的垃圾收集器 *(设计初衷是尽量缩短处理超大堆——大于 `4GB`——时产生的停顿)*，适用于具有大内存多处理器的计算机。

与之前收集器不同的是 G1 没有使用 Java 7 之前连续的内存模型：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这都出Java15了，Java7的特性还没搞明白？/image-20200818104952597.png)

而是将整个 **堆空间** 划分为了多个大小相等的独立区域 *(Region)*，虽然还保留有新生代和老年代的概念，但新生代和老年代不再是物理隔阂了，它们都是一部分 *(可以不连续)* Region的集合：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/这都出Java15了，Java7的特性还没搞明白？/image-20200818105735023.png)

G1 完全可以预测停顿时间，并且可以为内存密集型应用程序提供更高的吞吐量。

>⚠️ 对于 G1 和垃圾收集器不熟悉的同学赶紧来[这里](https://www.wmyskxz.com/2018/05/16/java-mian-shi-zhi-shi-dian-jie-xi-san-jvm-pian/)补课啦!!!

## 2 - 加强对动态调用的支持

Java 7 之前字节码指令集中，四条方法调用指令 *(`invokevirtual`、`invokespeicial`、`invokestatic`、`invokeinterface`)* 的第一个参数都是 **被调用方法的符号引用**，但动态类型的语言只有在 **运行期** 才能确定接受的参数类型。这样，在 Java 虚拟机上实现的动态类型语言就不得不使用“曲线救国”的方式 *(如编译时留个占位符类型，运行时动态生成字节码实现具体类型到占位符类型的适配)* 来实现，这样势必让动态类型语言实现的复杂度增加，也可能带来额外的性能或者内存开销。

为了从 JVM 底层解决这个问题 *(早在 1997 年出版的《Java 虚拟机规范》第一版中就规划了这样一个愿景：“在未来，我们会对 Java 虚拟机进行适当的扩展，以便更好的支持其他语言运行于 Java 虚拟机之上”)*, Java 7 新引入了 `invokedynamic` 指令以及 `java.lang.invoke` 包。

> 想进一步了解可以阅读：
>
> - 解析 JDK 7 的动态类型语言支持 | nfoQ - https://www.infoq.cn/article/jdk-dynamically-typed-language
> - 动态调用 101 - https://www.infoworld.com/article/2860079/invokedynamic-101.html
> - 官方文档 - https://docs.oracle.com/javase/7/docs/api/java/lang/invoke/package-summary.html

## 3 -  分层编译

Java 7 中引入的 **分层编译** 为服务器 VM 带来了客户端一般的启动速度。通常，服务器 VM 使用 **解释器** 来收集有关「提供给 **编译器** 的方法」的分析信息。在分层模式中，除了 **解释器** 之外，**客户端编译器** 还用于生成方法的编译版本，这些方法收集关于自身的分析信息。由于编译后的代码比  **解释器** 要快得多，程序在分析阶段执行时会有更好的性能。在许多情况下，可以实现比客户机 VM 更快的启动，因为服务器编译器生成的最终代码可能在应用程序初始化的早期阶段就已经可用了。分层模式还可以获得比常规服务器 VM 更好的峰值性能，因为更快的分析阶段允许更长的分析周期，这可能产生更好的优化。*(ps: 官方文档如是说...)*

支持 32 位和 64 位模式，以及压缩 Oops。在 java 命令中使用 `-XX:+TieredCompilation` 标志来启用分层编译。

*(ps: 这在 Java 8 是默认开启的)*

## 4 - 压缩 Oops (CompressOops)

HotSpot JVM 使用名为 `oops` 或 `Ordinary Object Pointers` 的数据结构来表示对象。这些 `oops` 等同于本地C指针。 `instanceOops` 是一种特殊的 `oop`，表示 Java 中的对象实例。

在 `32` 位的系统中，对象头指针占 `4` 字节，只能引用 `4 GB` 的内存，在 `64` 位系统中，对象头指针占 `8` 字节。更大的指针尺寸带来了问题：

1. **更容易 GC**，因为占用空间更大了；
2. **降低了 CPU 缓存命中率**，因为一条 `cache line` 中能存放的指针数变少了；

为了能够保持 `32` 位的性能，oop 必须保留 `32` 位。那么，如何用 `32` 位 `oop` 来引用更大的堆内存呢？答案是——**压缩指针** *(CompressedOops)*。JVM 被设计为硬件友好，对象都是按照 `8` 字节对齐填充的，这意味着使用指针时的偏移量只会是 `8` 的倍数，而不会是下面中的 `1-7`，只会是 `0` 或者 `8`：

```text
mem:  | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
        ^                               ^
```

这就允许了我们不再保留所有的引用，而是每隔 `8` 个字节保存一个引用：

```text
mem:  | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
        ^                               ^
        |    ___________________________|
        |   |
heap: | 0 | 1 |
```

CompressedOops，可以让跑在 `64` 位平台下的 JVM，不需要因为更宽的寻址，而付出 Heap 容量损失的代价 *(其中还涉及零基压缩优化——Zero-Based Compressed OOPs 技术)*。 不过它的实现方式是在机器码中植入压缩与解压指令，可能会给 JVM 增加额外的开销。

> 想要了解更多戳这里：
>
> - JVM 中的压缩 OOP | Baeldung - https://www.baeldung.com/jvm-compressed-oops
> - 官方文档 - https://docs.oracle.com/javase/7/docs/technotes/guides/vm/performance-enhancements-7.html

## 其他优化

### 将 interned 字符串移出 perm gen

在 JDK 7 中，interned 字符串不再在 Java 堆的永久生成中分配，而是在 Java 堆的主要部分 *(称为年轻代和年老代)* 中分配，与应用程序创建的其他对象一起分配。这一更改将导致驻留在主 Java 堆中的数据更多，而驻留在永久生成中的数据更少，因此可能需要调整堆大小。由于这一变化，大多数应用程序在堆使用方面只会看到相对较小的差异，但加载许多类或大量使用 `String.intern()` 方法的较大应用程序将看到更显著的差异。

*(ps: `String.intern()` 方法是运行期扩展方法区常量池的一种手段)*

### NUMA 收集器增强

Java 7 对 `Parallel Scavenger` 垃圾收集器进行了扩展，以利用具有 NUMA *(非统一内存访问)* 体系结构的计算机的优势。大多数现代计算机都基于 NUMA 架构，在这种架构中，访问内存的不同部分需要花费不同的时间。通常，系统中的每个处理器都具有提供低访问延迟和高带宽的本地内存，以及访问速度相当慢的远程内存。

在 Java HotSpot 虚拟机中，已实现了 NUMA 感知的分配器，以利用此类系统并为 Java 应用程序提供自动内存放置优化。分配器控制堆的年轻代的 `eden` 空间，在其中创建大多数新对象。分配器将空间划分为多个区域，每个区域都放置在特定节点的内存中。分配器基于以下假设：分配对象的线程将最有可能使用该对象。为了确保最快地访问新对象，分配器将其放置在分配线程本地的区域中。可以动态调整区域的大小，以反映在不同节点上运行的应用程序线程的分配率。这甚至可以提高单线程应用程序的性能。另外，年轻一代，老一代和永久一代的“从”和“到”幸存者空间为其打开了页面交错。这样可以确保所有线程平均平均具有对这些空间的相等的访问延迟。

### 版本号大于 50 的类文件必须使用 typechecker 进行验证

从 Java 6 开始，Oracle 的编译器使用 StackMapTable 制作类文件。基本思想是，编译器可以显式指定对象的类型，而不是让运行时执行此操作。这样可以在运行时提供极小的加速，以换取编译期间的一些额外时间和已编译的类文件 *(前面提到的 StackMapTable)* 中的某些复杂性。

作为一项实验功能，Java 6 编译器默认未启用它。 如果不存在 StackMapTable，则运行时默认会验证对象类型本身。

版本号为 `51` 的类文件 *(也就是 Java 7 的类文件)* 是使用类型检查验证程序专门验证的，因此，方法在适当时必须具有 StackMapTable 属性。对于版本 `50` 的类文件，如果文件中的堆栈映射丢失或不正确，则 HotSpot JVM 将故障转移到类型推断验证程序。对于版本为 `51` *(JDK 7 默认版本)* 的类文件，不会发生此故障转移行为。

# 参考资料

1. [Oracle 官方文档](https://www.oracle.com/java/technologies/javase/jdk7-relnotes.html) - https://www.oracle.com/java/technologies/javase/jdk7-relnotes.html
2. 闪烁之狐 - Java7新特性及使用 - http://blinkfox.com/2018/11/12/hou-duan/java/java7-xin-te-xing-ji-shi-yong/#toc-heading-5
3. JVM - 指针压缩 - https://chanjarster.github.io/post/jvm/oop-compress/

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！