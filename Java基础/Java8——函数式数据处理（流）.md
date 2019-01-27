> 本篇内容大部分来自《Java 8实战》

## 流是什么？

流是`Java API`的新成员，它允许你以声明性方式处理数据集合（通过查询语句来表达，而不是临时写一个实现）。

就现在来说，你可以把它们看成遍历数据集合的高级迭代器。此外，流还可以*透明地*并行处理，你无需写任何多线程代码！

下面两段代码都是用来返回低热量的菜肴名称的，并按照卡路里排序，一个使用`java 7`写的，另一个是用`java 8`的流写的，比较一下，不用太关心java 8的语法：

![](http://upload-images.jianshu.io/upload_images/7896890-b4f78866265cbf3d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](http://upload-images.jianshu.io/upload_images/7896890-18563ca684ec7b8c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

现在，你可以看出，从软件工程师的角度来看，新的方法有几个显而易见的好处。

- **代码是以声明性方式写的：**说明想要完成什么（筛选热量低的菜肴）而不是说明如何实现一个操作（利用循环和if条件等控制流语句）。这种方法加上行为参数化让你可以轻松应对变化的需求：你很容易创建一个代码版本，利用Lambda表达式来筛选高卡路里的菜肴，而用不着去复制粘贴代码。

- **你可以把几个基础操作链接起来，**来表达复杂的数据处理流水线（在filter后面接上sorted、map和collect操作），同时保持代码清晰可读。
> 因为filter、sorted、map和coleect等操作是与具体线程模型无关的高层次构件，所以它们的内部实现可以是单线程的，也可能*透明地*充分利用你的多核架构！在实践中，这意味着你用不着为了让某些数据处理任务并行而去操心线程和锁了，`Steam API`都替你做好了！
> ![](http://upload-images.jianshu.io/upload_images/7896890-8d4b3d728dc7e707.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

并且新的`Steam APi`表达能力非常强，能写出例如下面这样的代码：

```java
Map<Dish.Type, List<Dish>> dishesByType = 
        menu.steam().collect(groupingBy(Dish::getType));
```

## 流简介

要讨论流，就要先来讨论集合，这是最容易上手的方式了。`Java 8`中的集合支持一个新的stream方法，它会返回一个流（接口定义在`java.util,steam.Steam`里）。

那么，流到底是什么呢？简短的定义就是**“从支持数据处理操作的源，生成的元素序列”**。Oh，听上去就让人头大。让我们一步步来剖析这个定义：
![](http://upload-images.jianshu.io/upload_images/7896890-bc37ab0d7390ffb9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

让我们来看一段能够体现所有这些概念的代码：

![](http://upload-images.jianshu.io/upload_images/7896890-48b16ead1be85bd1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在本例中，我们先是对menu调用steam方法，由菜单得到一个流。*数据源*是菜肴列表（菜单），它给流提供一个*元素序列。*接下来，对流应用一系列*数据处理操作：*filter、map、limit和collect。

除了collect之外，所有这些操作都会返回另一个流，这样它们就可以竭诚一条流水线，于是就可以看作对源的一个查询。最后，collect操作开始处理流水线，并返回结果。下图展示了该流操作的每个操作的简介：

![](http://upload-images.jianshu.io/upload_images/7896890-f8629e20adea9ca6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

感觉像不像变魔术呢？不过至少看起来，是挺酷的。

## 集合与流

![](http://upload-images.jianshu.io/upload_images/7896890-03efb1b41d46c2dc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上图很好的说明了在`Java 8`中的集合与流的关系，集合就像是DVD一样，保存了完整的数据结构和所有的值，而流则像是现在视频网站中的视频那样，不必提前下好完整的视频，只需要提前下载好用户播放位置的那几帧就好了。

> 请注意：流和迭代器一样，都只能遍历一次。

## 流操作

`java.util.steam.Steam`中的`Steam`接口定义了许多操作。它们可以分为两大类，一类是`中间操作`，另一类是`终端操作`。

为了方便，下面总结了一些`Steam API`提供的操作：

![](http://upload-images.jianshu.io/upload_images/7896890-ebed4fc34ec3412c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


流的使用一般包括三件事：
- **一个数据源（如集合）来执行一个查询；**
- **一个中间操作链，形成一条流的流水线；**
- **一个终端操作，执行流水线，并能生成结果**

流的流水线背后的理念有点类似于构建器模式。

> 至此，流的基本操作就已经阐述完全了，相信你已经同我一样对`Java 8`的新特性感到兴奋了吧，不可避免，因为这实在是太酷啦！

---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693

