## 前言

> 因为没有成功地为IDEA配上反编译工具，所以自己下载了一个XJad工具，背景是白色的，所以忍着强迫症硬是把IDEA的主体也给换成白色了，感觉为了这篇文章付出了诸多啊....

## 字符串简介

《Thinging in Java》中有一句话：**可以证明，字符串操作是计算机程序设计中最常见的行为。**

把多个字符按照一定的顺序排列起来，就叫字符串（就像羊肉串一样，串起来的），具体是怎么排列的，你可以跟进String的源代码去看一下，会发现它其实内部维护的是一个char类型的数组：

![String类内部维护的是一个char数组](http://upload-images.jianshu.io/upload_images/7896890-036e64ee9ee2051c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```java
// 也就是说
String str = "ABCD";      // 定义一个字符串对象，其实等价于：
char[] cr = new char[]{'A','B','C','D'};
```

## 字符串的分类

其实说起来会有些别扭，为什么字符串会有分类这种东西。了解的朋友可能会知道字符串的操作除了String，还有StringBuffer和StringBuilder（区别我们在下面来说）

### 不可变的字符串

String是一个奇葩。

**String对象不可变，也就是说当对象创建完毕之后，该对象的内容（字符序列）是不允许改变的，如果内容改变则会创建一个新的String对象，返回到原地址中。**

细心的朋友也许会发现，String类维护的char数组不仅被final所修饰，并且查看JDK源码你就会发现，**String类中每一个看起来会修改String值得方法，实际上都是创建了一个全新的String对象，以包含修改后的字符串对象。**而最初的String对象则丝毫未动。我们可以简单的来看一个实例（从替换操作中就能明显看出）：

![String类中的replace方法](http://upload-images.jianshu.io/upload_images/7896890-02c6c109b166f52d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> replace方法就是替换字符串中的内容，如果替换之后跟原来的字符串相同则返回this，如果不相同则new一个新的对象返回。这明显体现了内容改变则返回新对象而不是直接修改String对象的值。

### 表面的错觉

关于String对象是否可变，有些操作确实会给人错觉，先来看一段程序：

![一个例子](http://upload-images.jianshu.io/upload_images/7896890-2644ac6bf600a649.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

从结果来看，s1的值最初是“A”，经过赋值以后，变成了“C”,经过字符串连接运算并赋值以后，变成了“BC”。String对象的内容真的改变了吗？**实际上，这只是错觉而已。**有疑惑的朋友可以去看我的上一篇笔记，你就能知道：

**String对象“A”，“B”，“C”在全程中都没有任何改变，改变的只是引用s1所指向的内容，也就是s1的值。**

### String对象的创建

有两种方式：

```java
// 第一种：直接赋一个字面量
String str1 = "ABCD";
// 第二种：通过构造器创建
String str2 = new String("ABCD");
```

那么这两种方式有什么不同呢？这里可能会涉及到一个面试题：

> 上述的两种方法分别创建了几个String对象？

回答这个问题也特别简单，首先你需要直到JVM的内存模型是怎样的，在上一篇笔记中也有简单提到，这里需要补充的是：**常量池（专门存储常量的地方，都指的是方法区中）分为编译常量池（不研究，存储字节码的相关信息）和运行常量池（存储常量数据）。**

> 先来看一张结果图：
> ![结果图](http://upload-images.jianshu.io/upload_images/7896890-e4659bc6656d9d6b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 当执行第一句话的时候，会在常量池中添加一个新的ABCD字符，str1指向常量池的ABCD
- 当执行第二句话的时候，因为有new操作符，所以会在堆空间新开辟一块空间用来存储新的String对象，因为此时常量池中已经有了ABCD字符，所以堆中的String对象指向常量池中的ABCD，而str2则指向堆空间中的String对象。

> 所以结论：
> **String str1 = "ABCD";**
> 最多创建一个String对象,最少不创建String对象.如果常量池中,存在”ABCD”,那么str1直接引用,此时不创建String对象.否则,先在常量池先创建”ABCD”内存空间,再引用.
> **String str2 = new String("ABCD");**
> 最多创建两个String对象，至少创建一个String对象。new关键字绝对会在堆空间创建一块新的内存区域，所以至少创建一个String对象。

### String对象的空值

一种是表示引用为空（null）的空值：

```java
String str1 = null;  // 没有初始化，没有分配内存空间
```

另外一种表示内容为空的空值：

```java
String str2 = ";  // 分配有内存空间，有内容。
```

> 所以当你需要判断字符串是否为空的时候，实际上应该这样：
> ![判断字符串非空](http://upload-images.jianshu.io/upload_images/7896890-514016865e382497.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 字符串的比较

![字符串的比较](http://upload-images.jianshu.io/upload_images/7896890-6ba349f9c811b3a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

从上图可以明显看出，**使用“==”，只能比较引用的内存地址是否相同，而使用“equals”方法，则比较的是字符串的内容。**

> 我们可以跟到String类的equals方法：
> ![String类的equals方法](http://upload-images.jianshu.io/upload_images/7896890-dc520a0855cdaf2c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### “+”号是怎么来连接字符串的

先来直接看一个简单的例子，程序中创建了三个String对象，str是hello和wrold两个字符串连接赋值后的对象，程序的结果很明显，但我们关心的是，hello和world是怎样连接起来的呢？

![先来看一个例子](http://upload-images.jianshu.io/upload_images/7896890-885c895308af612b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们在XJad（Java反编译程序，把生成的class反编译成java）中打开刚刚生成的class文件会发现：

![反编译的结果](http://upload-images.jianshu.io/upload_images/7896890-736f29f79984abc2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

编译器自动引入了一个**java.lang.StringBuilder**类。虽然我们在源代码中并没有使用StringBuilder类，但是编译器却自作主张地使用了它，因为它更高效。

在这个例子中，编译器创建了一个**StringBuilde**对象，用以构造最终的**String**，并为每个字符串调用了一次**StringBuilder**的**append()**方法，总计两次。最后调用**toString()**生成结果。这是编译器自动优化的结果，包括自动生成的Tester()无参数默认的构造函数也是。

现在，你也许会觉得可以随意使用**String**对象，反正编译器会为你自动地优化性能。可是在这之前，我们先要看看编译器究竟能给我们优化到什么程度（下面再详细介绍StringBuilder）。

### 可变的字符串

**StringBuilder/StringBuffer：**当对象创建完毕之后，该对象的内容可以发生改变，当内容发生改变的时候，对象保持不变。

接着上面的问题，我们继续来看一个例子：

> ![程序和程序的结果](http://upload-images.jianshu.io/upload_images/7896890-2431fab5600e3952.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
> 程序的结果显而易见，我们来看看反编译之后的代码：
> ![反编译之后的代码](http://upload-images.jianshu.io/upload_images/7896890-056c471c0b3a3f6d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
> 可以看到，对比两个对象，后者的循环部分的代码更简短、更简单，而且它只生成了一个**StringBuilder**对象。

**结论是：如果字符串操作比较简单，那就可以信赖编译器，它会为你合理地构造最终的字符串结果。但如果你还使用循环，多次地改变字符串的内容，那就更适合StringBuilder对象。**

但是如果你想要走捷径，例如**append(a+":"+c)，**则编译器就会调入陷阱，从而为你另外创建一个**StringBuilder**对象处理括号内的字符串操作。

![编译器陷阱](http://upload-images.jianshu.io/upload_images/7896890-a9ca5459d4a64468.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### String对象的比较

**StringBuilder**是Java SE5引入的，在这之前Java用的是**StringBuffer**。后者线程安全（只需要了解，该对象方法中所有的方法都是用了**synchronized**修饰符），因此开销也会大。有没有用**synchronized**修饰符，就是这两者唯一的区别。我们可以简单地来比较一下这三个String对象在拼接字符串中的性能：

> 创建好三个方法，分别测试三个类型的对象的拼接效率：
> ![测试拼接效率](http://upload-images.jianshu.io/upload_images/7896890-15ef1480ca9fbd62.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![最后在main方法中测试](http://upload-images.jianshu.io/upload_images/7896890-31e967f832ee92ce.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 面试题

最后再有一个String的面试题：

> **说说下面的String对象，彼此之间是否相等？**
> ![面试题](http://upload-images.jianshu.io/upload_images/7896890-f1adb2146cdb6116.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果你自己写几个判断相等的语句，分别判断str1和另外五个是否相等，则会发现：
**str1和str2/str3相等，和另外几个都不相等。**我们先来看一下反编译之后的代码：

> ![编译之后的代码（存在编译优化）](http://upload-images.jianshu.io/upload_images/7896890-6ebaa00780d60fd0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

知识点（纯干货）：
- 单独使用""引号创建的字符串都是直接量,编译期就已经确定存储到常量池中；
- 使用new String("")创建的对象会存储到堆内存中,是运行期才创建；
- 使用只包含直接量的字符串连接符如"aa" + "bb"创建的也是直接量编译期就能确定,已经确定存储到常量池中(str2和str3)；
- 使用包含String直接量(无final修饰符)的字符串表达式(如"aa" + s1)创建的对象是运行期才创建的,存储在堆中；
- 通过变量/调用方法去连接字符串,都只能在运行时期才能确定变量的值和方法的返回值,不存在编译优化操作.

### 文章结尾

其实还想写关于正则表达的东西的，还是改天找时间另外研究研究写一篇像样的吧。关于String的操作，就简单给一下图吧，感兴趣也可以自己百度或者跟踪进源代码里面去看，这里就不细说了：

![String类中常用的方法](http://upload-images.jianshu.io/upload_images/7896890-c749c80d3b904c5c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


> 参考资料：
>- http://study.163.com/course/courseMain.htm?courseId=1003108028 《Java零基础入门教程》
>- 《Thinking in Java》第四版

---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693

