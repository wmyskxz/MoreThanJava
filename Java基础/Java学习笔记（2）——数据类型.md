> 终于要写点干货了，其实思考了很久下面一篇文章要写什么，主要的纠结点在于，既想要分享那些精美的知识，又怕这些知识不太好嚼。后来想想还是对初学者不太好友算了..一来这系列文章叫做学习笔记，我的。另外写得足够有料，才能发挥笔记的作用，不然索然无味的，连收藏、喜欢的意义也没有了。

## 写在文章之前

终于写点干货了，想先简单谈谈自己的一些看法。对于我自己而言，我比较厌烦那些繁琐的无聊的知识点，反而更在乎一些实际应用的东西。但了解一些底层的东西是非常有意义的，它有助于我们理解程序。

> 每一点知识的积累，终会有用武之地。也许，它会使您在面试过程中正确地回答一道面试题；也许，它会让您更加清楚Java底层的实现方式；也许，它能让您在学业上感到更加充实...（以上摘自梁勇著的Java深入解析_前言）

## Java中的数据类型

Java是一种强类型的语言。这意味着必须为每一个变量都声明一种类型。

在Java中，你可以把数据类型分为两部分，一部分**是基本类型（primitive type）**：4种整形、2种浮点类型、1种用于表示Unicode编码的字符单元的字符类型char和1种用于表示真值的boolean类型。

另外一部分是**引用类型（reference type）**，如String和List。每个基本类型都有一个对应的引用类型，称作装箱基本类型（boxed primitive）。装箱基本类中对应于int、double、boolean的是Integer、Double和Boolean。

### Java中的特例

Java是一种完全面向对象的语言，从理论上来说，在Java中应该不存在对象以外的事务，即所有的类型都是对象。然而，在Java8中的8种基本数据类型不是对象，之所以这样设计，是因为相对于对象来说，基本数据在使用上更加方便，并且在效率上也高于对象类型。所以这就需要去了解一下Java中创建对象的过程。

### 创建对象的过程

> 当程序运行时，对象是怎么进行安排放置的呢？特别是内存是怎样分配的呢？

Java大体上会把内存分为四块区域：堆、栈、静态区、常量区。
- **堆** ： 位于RAM中，用于存放所有的java对象。
- **栈** ： 位于RAM中，引用就存在于栈中。
- **静态区**: 位于RAM中，被static修饰符修饰的变量会被放在这里
- **常量区**：位于ROM中， 很明显，放常量的。（其实常量通常直接存放在程序代码的内部，因为这样非常安全，因为它们永远都不会被改变）

所以当我们创建对象，例如实例化一个Person类：

```java
Person p = new Person()
```

首先，会在堆中开辟一块空间存放这个新来的Person对象。然后，会创建一个引用p，存放在栈中，这个引用p指向Person对象（事实上是，p的值就是Person对象的内存地址）。

这样，我们通过访问p，然后得到了Person的内存地址，进而找到了Person对象。

然后又有了这样一句代码：

```java
Person p2 = p;
```

这句代码的含义是：

创建了一个新的引用p2，保存在栈中，引用的地址也指向Person的地址。这个时候，你通过p2来改变Person对象的状态，也会改变p的结果。因为它们指向同一个对象。（String除外，之后会专门讲String）

此时，内存中是这样的：

![内存的状态](http://upload-images.jianshu.io/upload_images/4047674-3a3740af2c4def76?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

有一个很通俗的方式来讲解引用和对象。大家对于快捷方式应该不会陌生吧？我们桌面的图标大部分都是快捷方式。它并不是我们安装在电脑上的应用的可执行文件（不是.exe文件)，那么为什么点击它可以打开应用程序呢？是因为快捷方式连接了文件，这就像是引用和对象的关系了。

我们不直接对文件进行操作，而是通过快捷方式来进行操作。快捷方式不能独立存在，同样，引用也不能独立存在（你可以只创建一个引用，但是当你要使用它的时候必须得给它赋值，否则它将毫无用处）。

一个文件可以有多个快捷方式，同样一个对象也可以有多个引用。而一个引用只能同时对应一个对象。

> 在java里，“=”不能被看成是一个赋值语句，它不是在把一个对象赋给另外一个对象，它的执行过程实质上是将右边对象的地址传给了左边的引用，使得左边的引用指向了右边的对象。java表面上看起来没有指针，但它的引用其实质就是一个指针。在java里，“=”语句不应该被翻译成赋值语句，因为它所执行的确实不是一个简单的赋值过程，而是一个传地址的过程，被译成赋值语句会造成很多误解，译得不准确。

### 特例：基本数据类型

为什么要有特例呢？是因为new将对象存储在“堆”里，一是用new创建一个对象——特别是小的，简单的变量**（Java中数据定长，为了可移植性）**往往不是很明智而且有效的方法，二是因为“堆”空间本来就有限，如果频繁的操作会导致不可想象的错误，并且别忘了[第一篇文章](http://www.jianshu.com/p/e0ba0863f2f1)里面提到的，Java的设计初衷是什么。

所以针对这些类型，Java采取了与C和C++相同的方法，也就是说，不用new来创建变量，二是创建一个并非是引用的“自动”变量。这个变量直接存储“值”并置于常量区中，因此更加高效。

先来看一个例子：

```java
int i = 2;
int j = 2;
```

我们需要知道的是，在常量区中，相同的常量只会存在一个。当执行第一句代码时。先查找常量区中有没有2，没有，则开辟一个空间存放2，然后在栈中存入一个变量i，让i指向2；

执行第二句的时候，查找发现2已经存在了，所以就不开辟新空间了。直接在栈中保存一个新变量j，让j指向2；

当然，java堆每一个基本数据类型都提供了对应的包装类。我们依旧可以用new操作符来创建我们想要的变量。

```java
Integer i = new Integer(1);
Integer j = new Integer(1);
```

但是，用new操作符创建的对象是不同的，也就是说，此时，i和j指向不同的内存地址。**因为每次调用new操作符，都会在堆开辟新的空间。**

### 深入了解Integer

来看一个例子：

![一个例子](http://upload-images.jianshu.io/upload_images/7896890-39d324cd6881b659.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 第一个返回true很好理解，就像上面讲的，a和b指向相同的地址。
> 第二个返回false是为什么呢？下面细说
> 第三个返回false是因为用了new关键字来开辟了新的空间，i和j两个对象分别指向堆区中的两块内存空间。

我们可以跟踪一下Integer的源码，看看到底怎么回事。在IDEA中，你只需要按住Ctrl然后点击Integer，就会自动进入jar包中对应的类文件。

![Integer类](http://upload-images.jianshu.io/upload_images/7896890-315606fedfecd838.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

跟踪到文件的700多行，你会看到这么一段，感兴趣可以仔细读一下，不用去读也没有关系，因为你只需要知道这是Java的一个缓存机制。Integer类的内部类缓存了-128到127的所有数字。（事实上，Integer类的缓存上限是可以通过修改系统来更改的。了解就行了，不必去深究。）

![缓存机制](http://upload-images.jianshu.io/upload_images/7896890-15d2ec8f9bc63240.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 为什么引入缓存机制

这回到了为什么引入基础类型这个特例的问题上。我们看看Java语言规范是怎么规定的：

> If the value p being boxed is an integer literal of type int between -128 and 127 inclusive ([§3.10.1**](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/specs/jls/se8/html/jls-3.html%23jls-3.10.1)), or the boolean literal true or false ([§3.10.3**](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/specs/jls/se8/html/jls-3.html%23jls-3.10.3)), or a character literal between '\u0000' and'\u007f' inclusive ([§3.10.4**](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/specs/jls/se8/html/jls-3.html%23jls-3.10.4)), then let a and b be the results of any two boxing conversions of p. It is always the case that a == b.
> 
> Ideally, boxing a primitive value would always yield an identical reference. In practice, this may not be feasible using existing implementation techniques. The rule above is a pragmatic compromise, requiring that certain common values always be boxed into indistinguishable objects. The implementation may cache these, lazily or eagerly. **For other values, the rule disallows any assumptions about the identity of the boxed values on the programmer's part.** This allows (but does not require) sharing of some or all of these references. Notice that integer literals of type long are allowed, but not required, to be shared.
>
> This ensures that in most common cases, the behavior will be the desired one, without imposing an undue performance penalty, especially on small devices. Less memory-limited implementations might, for example, cache all char and short values, as well as int and long values in the range of -32K to +32K.

事实上，不光是Integer这么特别，还包括boolean还有char类型。并且文章的最后提到了**为了实现更少内存的可能。**

### 另一个特例：String
String是一个特殊的类，因为它被final修饰符所修饰，是一个不可改变的类。当然，看过java源码后你会发现，基本类型的各个包装类也被final所修饰。这里以String为例。

我们来看这样一个例子：

![例子](http://upload-images.jianshu.io/upload_images/4047674-1cb11559760bbad3?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>  执行第一句 ： 常量区开辟空间存放“abc”，s1存放在栈中指向“abc”
> 执行第二句，s2 也指向 “abc”，
> 执行第三句，因为“abc”已经存在，所以直接指向它。
> 所以三个变量指向同一块内存地址，结果都为true。
> 当s1内容改变的时候。这个时候，常量区开辟新的空间存放“bcd”，s1指向“bcd”，而s2和s3指向“abc”所以只有s2和s3相等。

这种情况下，s1,s2,s3都是字符串常量，类似于基本数据类型。（如果执行的是s1 = "abc",那么结果会都是true）

我们再看一个例子：

![例子2](http://upload-images.jianshu.io/upload_images/4047674-530e77c58db5a82c?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 执行第一行代码： 在堆里分配空间存放String对象，在常量区开辟空间存放常量“abc”，String对象指向常量，s1指向该对象。
> 执行第二行代码：s2指向上一步new出来的string对象。
> 执行第三行代码： 在堆里分配新的空间存放String对象，新对象指向常量“abc”，s3指向该对象。
> 到这里，很明显，s1和s2指向的是同一个对象
> 
>接着就很诡异了，我们让s1 依旧= “abc",但是结果s1和s2指向的地址不同了。

怎么回事呢？这就是String类的特殊之处了，new出来的String不再是上面的字符串常量，而是字符串对象。

由于String类是不可改变的，所以String对象也是不可改变的，我们每次给String赋值都相当于执行了一次new String()，然后让变量指向这个新对象，而不是在原来的对象上修改。

当然，java还提供了StringBuffer类，这个是可以在原对象上做修改的。如果你需要修改原对象，那么请使用StringBuffer类。

### 引发的问题：值传递还是引用传递？

java是值传递还是引用传递的呢？毫无疑问，java是值传递的。那么什么又叫值传递和引用传递呢？

我们先来看一个例子：

![例子](http://upload-images.jianshu.io/upload_images/4047674-cabe50a835eca720?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这是一个很经典的例子，我们希望调用了swap函数以后，a和b的值可以互换，但是事实上并没有。为什么会这样呢？

这就是因为java是值传递的。**也就是说，我们在调用一个需要传递参数的函数时，传递给函数的参数并不是我们传进去的参数本身，而是它的副本。**说起来比较拗口，但是其实原理很简单。我们可以这样理解：

一个有形参的函数，当别的函数调用它的时候，必须要传递数据。比如swap函数，别的函数要调用swap就必须传两个整数过来。

这个时候，有一个函数按耐不住寂寞，扔了两个整数过来，但是，swap函数有洁癖，它不喜欢用别人的东西，于是它把传过来的参数复制了一份，然后对复制的数据修修改改，而别人传过来的参数动根本没动。

所以，当swap函数执行完毕之后，交换了的数据只是swap自己复制的那一份，而原来的数据没变。

> 也可以理解为别的函数把数据传递给了swap函数的形参，最后改变的只是形参而实参没变，所以不会起到任何效果。

我们再来看一个复杂一点的例子(Person类添加了get，set方法)：

![例子](http://upload-images.jianshu.io/upload_images/4047674-12da05026aed184b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 可以看到，我们把p1传进去，它并没有被替换成新的对象。因为change函数操作的不是p1这个引用本身，而是这个引用的一个副本。
> 
> 你依然可以理解为，主函数将p1复制了一份然后变成了chagne函数的形参，最终指向新Person对象的是那个副本引用，而实参p1并没有改变。

再来看一个例子：

![例子2](http://upload-images.jianshu.io/upload_images/4047674-27ea492babe95e92?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 这次为什么就改变了呢？分析一下。
> 首先，new了一个Person对象，暂且叫他小明吧。然后p1指向小明。
> 小明10岁了，随着时间的推移，小明的年龄要变了，调用了一下changgeAge方法，把小明的引用传了进去。
> 传递的过程中，changgeAge也有洁癖，于是复制了一份小明的引用，这个副本也指向小明。
> 然后changgeAge通过自己的副本引用，改变了小明的年龄。
> 由于是小明这个对象被改变了，所以所有小明的引用调用方法得到的年龄都会改变
> 所以就变了。

最后简单的总结一下。

java的传值过程，其实传的是副本，不管是变量还是引用。所以，不要期待把变量传递给一个函数来改变变量本身。

### “+”是怎么连接字符串的？

> 先抛个砖：对Java程序员来说，使用运算符“+”来连接字符串是非常普遍的，当“+”两边的操作数是String类型时（如果只有一个操作数是String类型，则系统也会将另外一个操作数转换成String类型），就会执行字符串连接的运算。但是，运算符“+”是怎样连接String对象的呢？编译器又是如何实现的呢?
>
>之后我再来补这个内容，先发表啦。






### 浮点类型

> 浮点类型用于表示有小数部分的数值。在Java中有两种浮点类型，一个是4字节的float，一个是8字节的double。我们平时用来编写程序用来表示增长率、物品重量等方面也非常有用。不过，在使用浮点类型时，也需要留意一些问题。

### 浮点类型只是近似的存储

请问一个问题：0.1+0.2等于多少？请不要慌着报答案，我没有开玩笑的意思，看一下Java给出的答案你就知道了：

![Java的答案](http://upload-images.jianshu.io/upload_images/7896890-3d58b0edc9c4c1c0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
结果似乎有些令人惊讶，这么简单的算术竟然也会算错。

其实，这并不是计算错误，这只是浮点数类型存储的问题。计算机使用二进制来存储数据，而二进制无法准确的表示分数 1/10 ，就像使用十进制时，无法准确地表示 1/3 一样。

### 数量级差很大的浮点运算

当浮点数值的数量级相差很大的时候，运算又会有什么问题呢？

![数量级很大的浮点运算](http://upload-images.jianshu.io/upload_images/7896890-8234cf550c99320e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

又发生了预期外的结果。从输出结果来看，f3竟然和f4是相等的，也就是意味着对f3+1并没有改变f3的值。

这同样是因为浮点数的存储造成的，**二进制所能表示的两个相邻的浮点值之间存在一定的空隙。**浮点值越大，这个间隙也会越大。当浮点值大道一定程度的时，如果对浮点值的改变很小（例如上面的30000000+1），就不足以使浮点值发生改变。就好比蒸发掉大海中的一滴水，大海还是大海，几乎不存在变化。

> **如果想要准确的存储，就去使用BigDecimal吧，有必要了解的可以去自行百度，这里就不做过多介绍了，已经是Java封装好的类库了**

### 抛出一个有趣的问题

> 我们知道，在Java中，long类型占用了8个字节，float类型占用了4个字节。

照理来说，long类型的容量应该比float大许多，然而事实正好相反，float反而拥有比8字节long类型更大的取值范围。这同样是因为浮点数的存储格式造成的。有兴趣的可以去自行百度了解。

> 参考资料：
> http://www.jianshu.com/p/39753aad9a38 ，原文作者:CleverFan
> 《Java深入解析》——梁勇著
> 《Effective Java》——第二版
> 《Java核心技术 卷I》——第九版
> 《Java编程思想》——第四版


> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693