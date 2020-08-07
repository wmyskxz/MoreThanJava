
![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/e973d387-618a-46bd-abe5-bce322f54a72.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# Part 1. 继承概述

[上一篇文章](https://www.wmyskxz.com/2020/08/04/morethanjava-day-4-mian-xiang-dui-xiang-ji-chu/) 中我们简单介绍了继承的作用，它允许创建 **具有逻辑等级结构的类体系**，形成一个继承树。

![Animal 继承树](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/35b85013-1566-4442-a093-67dc71ec7774.png)

继承使您可以基于现有类定义新类。 新类与现有类相似，但是可能具有其他实例变量和方法。这使编程更加容易，因为您可以 **在现有的类上构建**，而不必从头开始。

继承是现代软件取得巨大成功的部分原因。 程序员能够在先前的工作基础上继续发展并不断改进和升级现有软件。

## 面向对象之前，写代码的一些问题

如果你有一个类的源代码，你可以复制代码并改变它变成你想要的样子。在面向对象编程之前，就是这样子做的。但至少有两个问题：

**❶ 很难保持仅仅有条。**

假设您已经有了几十个需要的类，并且需要基于原始类创造新的一些类，再基于新的类创造出更新的类，最终您将获得数十个源文件，这些源文件都是通过其他已更改的源文件的另外版本。

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/a7feeb86-9d37-406f-8aba-b9f395b437f7.png)

假设现在在一个源文件中发现了错误，一些基于它的源文件需要进行修复，但是对于其他源文件来说，并不需要！没有细致的写代码的计划，您最终会陷入混乱....

**❷ 需要学习原始代码。**

假设您有一个复杂的类，基本上可以完成所需的工作，但是您需要进行一些小的修改。如果您修改了源代码，即使是进行了很小的更改，也可能会破坏某些内容。因此，您必须研究原始代码以确保所做的更改正确，这可能并不容易。

**Java 的自动继承机制极大地缓解了这两个问题。**

## 单继承

用于作为新类模板的类称为 **父类** *(或超类或基类)*，基于父类创建的类称为 **子类** *(或派生类)*。

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/1744fdbf-cae1-4460-b4d9-0e48f00034eb.png)

就像上图中演示的那样，箭头从子类指向父类。*(在上图中，云表示类，而矩形表示对象，这样的表示的方法来自于 Grady Booch 写的《面向对象的分析和设计》一书。而在官方的 UML-统一建模语言 中，类和对象都用矩形表示，请注意这一点)*

在 Java 中，**子类仅从一个父类继承特征**，这被称为 **单继承** *(与人类不同)*。

有些语言允许"孩子"从多个"父母"那里继承，这被称为 **多继承**。但由于具有多重继承，有时很难说出哪个父母为孩子贡献了哪些特征 *(跟人类一样..)*。

Java 通过使用单继承避免了这些问题。*(意思 Java 只允许单继承)*

## is-a 关系

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/2fed3fa5-88ee-460c-b839-e6bc9be6c551.png)

上图显示了一个父类 *(Video 视频类)*，一个子类 *(Movie 电影类)*。它们之间的实线表示 **"is-a"** 的关系：电影是视频。 

**注意，继承是在类之间，而不是在对象之间。** *(上图两朵云都代表类)*

父类是构造对象时使用的蓝图，子类用于构造看起来像父对象的对象，但具有附加功能的对象。

### 类之间的关系简述

简单地说，类和类之间的关系有三种：`is-a`、`has-a` 和 `use-a`。

- **`is-a` 关系也叫继承或泛化**，比如学生和人的关系、手机和电子产品的关系都属于继承关系；
- **`has-a` 关系通常称之为关联**，比如部门和员工的关系、汽车和引擎的关系都属于关联关系；关联关系如果是整体和部分的关联，那么我们称之为 **聚合关系**；如果整体进一步负责了部分的生命周期 *(整体和部分是不可分割的，同时同在也同时消亡)*，那么这种就是最强的关联关系，我们称之为 **合成** 关系。
- **`use-a` 关系通常称之为依赖**，比如司机有一个驾驶的行为 *(方法)*，其中 *(的参数)* 使用到了汽车，那么司机和汽车的关系就是依赖关系。

利用类之间的这些关系，我们可以在已有类的基础上来完成某些操作，也可以在已有类的基础上创建新的类，这些都是实现代码复用的重要手段。复用现有的代码不仅可以减少开发的工作量，也有利于代码的管理和维护，这是我们在日常工作中都会使用到的技术手段。

## 层级结构

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/5f02652b-1840-4faf-a18b-ba3b5455663d.png)

上图显示了一个父类和一个子类的 **层次结构**，以及从每个类构造的一些对象。这些对象用矩形表示，以表达它们比设计的类更真实。

在层次结构中，每个类最多有一个父类，但可能有几个子类。 层次结构顶部的类没有父级。此类称为层次结构的 **根**。

另外，一个类可以是另一个子类的父类，也可以是父类的子类。就像人类一样，一个人是某些人类的孩子，也是其他人类的父母。*(但在 Java 中，一个孩子只有一个父母)*

# Part 2. 继承的实现

从父类派生子类的语法是使用 `extend` 关键字：

```java
class ChildClass extend ParentClass {
    // 子类的新成员和构造函数....
}
```

父类的成员 *(变量和方法*) 通过继承包含在子类中。其他成员将在其类定义中添加到子类。

## 视频观影 App 示例

Java 编程是通过创建类层次结构并从中实例化对象来完成的。您可以扩展自己的类或扩展已经存在的类。Java 开发工具包 *(JDK)* 为您提供了丰富的基类集合，您可以根据需要扩展这些基类。

*(如果某些类已经使用 `final` 修饰，则无法继承)*

下面演示了一个使用 `Video` 类作为基类的视频观影 App 的程序设计：

**Video 基类：**

```java
class Video {

    private String title;   // name of video
    private int length;     // number of minutes

    // constructor
    public Video(String title, int length) {
        this.title = title;
        this.length = length;
    }

    public String toString() {
        return "title=" + title + ", length=" + length;
    }

    public String getTitle() { return title;}
    public void setTitle(String title) { this.title = title;}
    public int getLength() { return length;}
    public void setLength(int length) { this.length = length;}
}
```

**Movie 电影类继承 Video：**

```java
class Movie extends Video {

    private String director;// name of the director
    private String rating;  // num of rating

    // constructor
    public Movie(String title, int length, String director, String rating) {
        super(title, length);
        this.director = director;
        this.rating = rating;
    }

    public String getDirector() { return director; }
    public String getRating() { return rating; }
}
```

这两个类均已定义：Video 类可用于构造视频类型的对象，现在 Movie 类可用于构造电影类型的对象。

Movie 类具有在 Video 中定义的成员变量和公共方法。

## 使用父类的构造函数

查看上方的示例，在 Movie 类的初始化构造函数中有一条 `super(title, length);` 的语句，是 **"调用父类 Video 中带有 title、length 参数的构造器"** 的简写形式。

由于 Movie 类的构造器不能访问 Video 类的私有字段，所以必须通过一个构造器来初始化这些私有字段。可以利用特殊的 `super` 语法调用这个构造器。

**重要说明：`super()` 必须是子类构造函数中的第一条语句。** *(这意味子类构造器总是会先调用父类的构造器)* 这件事经常被忽略，导致的结果就是一些神秘的编译器错误消息。

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/f19c167d-a96e-43b4-abc1-15106fe6cdf2.png)

如果子类的构造器没有显式地调用父类的构造器，将自动地调用父类的无参构造器。如果父类没有无参数的构造器，并且在子类的构造器中又没有显式地调用父类的其他构造器，Java 编译器就会报告一个错误。*(在我们的例子中 Video 缺少无参数的构造函数，故👆上面图片代码会报错)*

## 创建一个无参构造函数

关于构造函数的一些细节：

1. 您可以显式为类编写无参数的构造函数。
2. 如果您没有为类编写任何构造函数，那么将自动提供无参数构造函数 *（称为默认构造函数）*。
3. 如果为一个类编写了一个构造函数，则不会自动提供默认的构造函数。
4. 因此：如果您为类编写了额外的构造函数，那么，则还必须编写一个无参数构造函数 *(供子类调用)*。

在示例程序中，类 Video 包含构造函数，因此不会自动提供默认构造函数。 所以，Movie 类 `super()` 函数建议默认使用的构造函数 *(会自动调用无参数构造函数)* 会导致语法错误。

解决方法是将无参数构造函数显式放在类中 Video ，如下所示：

```java
class Video {

    private String title;   // name of video
    private int length;     // number of minutes

    // no-argument constructor
    public Video() {
        this.title = "unknown";
        this.length = 0;
    }

    // constructor
    public Video(String title, int length) {
        this.title = title;
        this.length = length;
    }

    ...
}
```

## 覆盖方法

让我们来实例化 Movie 对象：

```java
public class Tester {

    public static void main(String[] args) {
        Video video = new Video("视频1", 90);
        Movie movie = new Movie("悟空传", 139, "郭子健", "5.9");
        System.out.println(video.toString());
        System.out.println(movie.toString());
    }
}
```

**程序输出：**

```text
title=视频1, length=90
title=悟空传, length=139
```

`movie.toString()` 是 Movie 类直接继承自 Video 类，它并没有使用 Movie 对象具有的新变量，因此并不会打印导演和评分。

我们需要给 Movie 类添加新的 `toString()` 的使用方法：

```java
// 添加到 Movie 类中
public String toString() {
    return "title:" + getTitle() + ", length:" + getLength() + ", director:" + getDirector()
        + ", rating:" + getRating();
}
```

现在，Movie 拥有了自己的 `toString()` 方法，该方法使用了继承自 Video 的变量和自己定义的变量。

即使父类有一个 `toString()` 方法，子类中新定义的 `toString()` 也会 **覆盖** 父类的版本。当子类方法的 **签名** *(就是返回值 + 方法名称 + 参数列表)* 与父类相同时，子类的方法就会 **覆盖** 父类的方法。

现在运行程序，Movie 打印出了我们期望的完整信息：

```text
title=视频1, length=90
title:悟空传, length:139, director:郭子健, rating:5.9
```

> 有些人认为 `super` 与 `this` 引用是类似的概念，实际上，这样比较并不太恰当。这是因为 `super` 不是一个对象的引用，例如，不能将值 `super` 赋给另一个对象变量，它只是一个指示编译器调用父类方法的特殊关键字。

正像前面所看到的那样，在子类中可以增加字段、增加方法或覆盖父类的方法，不过，继承绝对不会删除任何字段或方法。

# Part 3. 更多细节

## protected 关键字

如果类中创建的变量或者方法使用 `protected` 描述，则指明了 "就类用户而言，这是 `private` 的，但对于任何继承于此类的导出类或者任何位于同一个 **包** 内的类来说，它是可以访问的"。下面我们就上面的例子来演示：

```java
public class Video {
    protected String title;   // name of video
    protected int length;     // number of minutes
    ...
}

public class Movie extends Video {
    ...
    public String toString() {
        return "title:" + title + ", length:" + length + ", director:" + director
            + ", rating:" + rating;
    }
    ...
}
```

在 `protected` 修饰之前，如果子类 Movie 要访问父类 Video 的 `title` 私有变量只能通过父类暴露出来的 `getTitle()` 公共方法，现在则可以直接使用。

## 向上转型

"为新的类提供方法" 并不是继承技术中最重要的方面，**其最重要的方面是用来表现新类和基类之间的关系**。这种关系可以用 **"新类是现有类的一种类型"** 这句话加以概括。

由于继承可以确保基类中所有的方法在子类中也同样有效，所以能够向基类发送的所有信息也同样可以向子类发送。例如，如果 Video 类具有一个 `play()` 方法， 那么 Movie 类也将同样具备。这意味着我们可以准确地说 **Movie 对象也是一种类型的 Video**。*(体现 `is-a` 关系)*

这一概念的体现用下面的例子来说明：

```java
public class Video {
    ...
    public void play() {}
    public static void start(Video video) {
        // ...
        video.play();
    }
    ...
}

// 测试类
public class Tester {
    public static void main(String[] args) {
        Movie movie = new Movie("悟空传", 139, "郭子健", "5.9");
        Video.start(movie);
    }
}
```

在示例中，`start()` 方法可以接受 Video 类型的引用，这是在太有趣了！

在测试类中，传递给 `start()` 方法的是一个 Movie 引用。鉴于 Java 是一个对类型检查十分严格的语言，接受某种类型 *(上例是 `Video` 类型)* 的方法同样可以接受另外一种类型 *(上例是 `Movie` 类型)* 就会显得很奇怪！

**除非你认识到 Movei 对象也是一种 Video 对象**。

在 `start()` 方法中，程序代码可以对 Video 和它所有子类起作用，**这种将 Movie 引用转换为 Video 引用的动作**，我们称之为 **向上转型** *(这样称呼是因为在继承树的画法上，基类在子类的上方...)*。

## Object 类

所有的类均具有父类，除了 `Object` 类。Java 类层次结构的最顶部就是 `Object` 类。

如果类没有显式地指明继承哪一个父类，那么它会自动地继承自 `Object` 类。如果一个子类继承了一个父类，那么父类要么继承它的父类，要么自动继承 `Object`。**最终，所有的类都将 `Object` 作为祖先。**

这意味着 Java 中的所有类都具有一些共同的特征。这些特征在被定义在 `Object` 中：

![Object 类拥有的方法](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/925a4b38-d2b2-4508-9011-988e2f5cfb49.png)

*(其中 `finalize()` 方法在 Java 9 之后弃用了，原因是因为它本身存在一些问题，可能导致性能问题：死锁、挂起和其他问题...)*

*(想看源码可以打一个 `Object`，然后按住 `Ctrl` 不放，然后点击 `Object` 就可以进入 JDK 源码查看了，源码有十分规范的注释和结构，你有时甚至会发现一些有趣的东西...)*

Java 之父 Gosling 设计的 Object 类，是对万事万物的抽象，是在哲学方向上进行的延伸思考，高度概括了事物的自然行为和社会行为。我们都知道哲学的三大经典问题：我是谁？我从哪里来？我到哪里去？在 Object 类中，这些问题都可以得到隐约的解答：

1. **我是谁？** `getClass()` 说明本质上是谁，而 `toString()` 是当前的名片；
2. **我从哪里来？** `Object()` 构造方法是生产对象的基本方式，`clone()` 是繁殖对象的另一种方式；
3. **我到哪里去？** `finalize()` 是在对象销毁时触发的方法；*(Java 9 之后已移除)*

另外，Object 还映射了社会科学领域的一些问题：

1. **世界是否因你而不同？** `hashCode()` 和 `equals()` 就是判断与其他元素是否相同的一组方法；
2. **与他人如何协调？** `wait()` 和 `notify()` 就是对象间通信与协作的一组方法；

## 理解方法调用

准确地理解如何在对象上应用方法调用非常重要。下面假设我们要调用 `x.f(args)`，`x` 是声明为 `C` 的一个对象。下面是调用过程的详细描述：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/59297eb2-70b4-455b-adff-bb104e2bac70.png)

1. 编译器查看对象的声明类型和方法名。需要注意的是：有可能存在多个名字为 `f` 但参数类型不一样的方法。例如，可能存在 `f(int)` 和 `f(String)`。编译器将会一一列举 `C` 类中所有名为 `f` 的方法和其父类中所有名为 `f` 而且可以访问的方法 *(父类中的私有方法不可访问)*。**至此，编译器一直到所有可能被调用的候选方法。**
2. 接下来，编译器要确定方法调用中提供的参数类型。如果在所有名为 `f` 的方法中存在一个与所提供参数类型完全匹配的方法，就选择这个方法。这个过程称为 **重载解析** *(overloading resolution)*。例如，对于调用 `x.f("Hello")`，编译期将会挑选 `f(String)`，而不是 `f(int)`。由于允许类型转换 *(例如，`int` 可以转换成 `double`)*，所以情况可能会变得很复杂。如果编译器没有找到与参数类型匹配的方法，或者发现经过类型转换后有多个方法与之匹配，编译器就会报错。**至此，编译器已经知道需要调用的方法的名字和参数类型。**
3. 如果是 `private` 方法、`static` 方法、`final` 方法 *(有关 `final` 修饰符会在下面讲到)* 或者构造器，那么编译器将可以明确地知道应该调用哪个方法。这称为 **静态绑定** *(static binding)*。与此对应的是，如果要调用的方法依赖于隐式参数的实际类型，那么必须在运行时 **动态绑定**。在我们的实例中，编译器会利用动态绑定生成一个调用 `f(String)` 的指令。
4. 程序运行并且采用动态绑定调用方法时，虚拟机必须调用与 `x` 所引用对象的实际类型对应的那个方法。假设 `x` 的实际类型是 `D`，它是 `C` 类的子类。如果 `D` 类定义了方法 `f(String)`，就会调用这个方法；否则，将在 `D` 类的父类中寻找 `f(String)`，以此类推。

**每次调用方法都要完成这样的搜索，时间开销相当大**。因此，虚拟机预先为每个类计算了一个 **方法表** *(method table)*， 其中列出了所有方法的签名和要调用的实际方法 *(存着各个方法的实际入口地址)*。这样一来，在真正调用方法的时候，虚拟机仅查找这个表就行了。*(以下是 Video-父类 和 Movie-子类 的方法表结构演示图)*

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/b969be72-d055-4cb6-b680-12e8f57c4254.png)

例如我们调用上述例子 Movie 类的 `play()` 方法。

```java
public void play() {};
```

由于 `play()` 方法没有参数，因此不必担心 **重载解析** 的问题。又不是 `private/ static/ final` 方法，所以将采用 **动态绑定** 的方式。

在运行时，调用 `object.play()` 的解析过程为：

1. 首先，虚拟机获取 `object` 的实际类型的方法表。这可能是 Video、Movie 的方法表，也可能是 Video 类的其他子类的方法表；
2. 接下来，虚拟机查找定义了 `play()` 签名的类。此时，虚拟机已经知道应该调用哪个方法了；*(这里如果 `object` 实际类型为 Movie 则调用 Movie.play()，为 Video 则调用 Video.play()，如果没找到才往父类去找..)*
3. 最后，虚拟机调用这个方法。

**动态绑定有一个非常重要的特性：无须对现有的代码进行修改就可以对程序进行扩展。**

假设现在新增一个类 `ShortVideo`，并且变量 `object` 有可能引用这个类的对象，我们不需要对包含调用 `object.play()` 的代码重新进行编译。如果 `object` 恰好引用一个 `ShortVideo` 类的对象，就会自动地调用 `object.play()` 方法。

> **警告**：在覆盖一个方法时，子类的方法 **不能低于** 父类方法的 **可见性** *(public > protected > private)*。特别是，如果父类方法是 `public`，子类方法必须也要声明为 `public`。

## final 关键字

有时候，我们可能希望组织人们利用某个类定义子类。**不允许扩展** *(被继承)* 的类被称为 **final 类**。如果在定义类的时候使用了 `final` 修饰符就表明这个类是 `final` 类了：

```java
public final class ShortVideo extends Video { ... }
```

类中的某个特定方法也可以被声明为 `final`。如果这样做，子类就不能覆盖这个方法 *(`final` 类中的所有方法自动地称为 `final` 方法)*。例如：

```java
public class Video {
    ...
    public final void Stop() { ... }
    ...
}
```

如果一个 **字段** 被声明为了 `final` 类型，那么对于 `final` 字段来说，**构造对象之后就不允许改变它们的值了**。不过，如果将一个类声明为 `final`，只有其中的方法自动地称为 `final`，而不包括字段，这一点需要注意。

**将方法或类声明为 `final` 的主要原因是：确保它们不会在子类中改变语义。**

### JDK 中的例子

- **Calendar** 类 *(JDK 实现的日历类)* 中的 `getTime` 和 `setTime` 方法都声明为了 `final`，这就表明 **Calendar** 类的设计者负责实现 **Data** 类与日历状态之间的转换，而不允许子类来添乱。
- 同样的，**String** 类也是 `final` 类 *(甚至面试中也经常出现)*，这意味着不允许任何人定义 **String** 的子类，换而言之，如果有一个 **String** 引用，它引用的一定是一个 **String** 对象，而不可能是其他类的对象。

### 内联

在早起的 Java 中，有些程序员为了避免动态绑定带来的系统开销而使用 `final` 关键字。如果一个方法没有被覆盖并且很短，编译器就能够对它进行优化处理，这个过程为 **内联** *(inlining)*。

例如，内联调用 `e.getName()` 会被替换为访问字段 `e.name`。

这是一项很有意义的改进，CPU 在处理当前指令时，**分支会扰乱预取指令的策略**，所以，CPU 不喜欢分支。然而，如果 `getName` 在另外一个类中 **被覆盖**，那么编译器就无法知道覆盖的代码将会做什么操作，因此也就不能对它进行内联处理了。

幸运的是，虚拟机中的 **即时编译器** *(JIT)* 比传统编译器的处理能力强得多。这种编译器可以准确地知道类之间的继承关系，并能够检测出是否有类确实覆盖了给定的方法。

如果方法很短、被频繁调用而且确实没有被覆盖，那么即时编译器就会将这个方法进行内联处理。如果虚拟机加载了另外一个子类，而这个子类覆盖了一个内联方法，那么优化器将取消对这个方法的内联。这个过程很慢，不过很少会发生这种情况。

## 抽象类

在类的自下而上的继承层次结构中，位于上层的类更具有一般性，也更加抽象。从某种角度看，祖先类更具有一般性，人们通常只是将它作为派生其他类的基类，而不是用来构造你想使用的特定的实例。

考虑一个 Person 类的继承结构：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/00ddd596-2c2c-4f94-a08b-37765be0ddfe.png)

每个人都有一些属性，如名字。学生与员工都有名字。

现在，假设需要增加一个 `getDescription()` 的方法，它返回对一个人简短的描述，学生类可以返回：`一个计算机在读的学生`，员工可以返回 `一个在阿里就职的后端工程师` 之类的。这在 Student 和 Employee 类中实现很容易，但是在 Person 类中应该提供什么内容呢？ 除了姓名，Person 类对这个人一无所知。

有一个更好的方法，就是使用 `abstract` 关键字，把该方法定义为一个 **抽象方法**，这意味着你并不需要实现这个方法，只需要定义出来就好了：*(以下代码为 Person 类中的抽象定义)*

```java
public abstract String getDescription() {}
```

为了提高程序的清晰度，包含一个或多个抽象方法的类本身必须被声明为抽象的：

```java
public abstract class Person {
    ...
    public abstract String getDescription() {}
    ...
}
```

> 《阿里Java开发规范》**强制规定抽象类命名** 使用 `Abstract` 或 `Base` 开头，这里只是做演示所以就简单用 `Person` 代替啦~

抽象方法充当着占位方法的角色，它们在子类中被继承并实现。

扩展抽象类可以由两种选择。一种是在子类中保留抽象类中的部分或所有抽象方法仍未实现，这样就必须将子类标记为抽象类 *(因为还有抽象方法)*；另一种做法就是实现全部方法，这样一来，子类就不是抽象的了。

*(即使不包含抽象方法，也可以将类声明为抽象类)*

抽象类不能实例化，也就是说，如果将一个类声明为 `abstract`，就不能创建这个类的实例，例如：`new Person();` 就是错误的，但可以创建具体子类的对象：`Person p = new Student(args);`，这里的 `p` 是一个抽象类型 `Person` 的变量，它引用了一个非抽象子类 `Student` 的实例。

# Part 4. 为什么不推荐使用继承？

先别急着奇怪和愤懑，刚学习完继承之后，就告诉说不推荐使用，这是 **有原因的！**

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/e29e8ed5-7853-4e7b-a89f-7ce7ab7b3fa2.png)

在面向对象编程中，有一条非常经典的设计原则：**组合优于继承**。使用继承有什么问题？组合相比继承有哪些优势？如何判断该用组合还是继承？下面我们就围绕这三个问题，来详细讲解一下。

> 以下内容大部分引用自：https://time.geekbang.org/column/article/169593

## 使用继承有什么问题？

上面说到，继承是面向对象的四大特性之一，用来表示类之间的 `is-a` 关系，可以解决代码复用的问题。虽然继承有诸多作用，但继承层次过深、过复杂，也会影响到代码的可维护性。我们通过一个例子来说明一下。

假设我们要设计一个关于鸟的类，我们将 “鸟类” 这样一个抽象的事物概念，定义为一个抽象类 `AbstractBird`。所有更细分的鸟，比如麻雀、鸽子、乌鸦等，都继承这个抽象类。

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/8afe27e4-35d6-4fa8-b90b-be2e05644eeb.png)

我们知道，大部分鸟都会飞，那我们可不可以在 `AbstractBird` 抽象类中，定义一个 `fly()` 方法呢？答案是否定的。尽管大部分鸟都会飞，但也有特例，比如鸵鸟就不会飞。鸵鸟继承具有 `fly()` 方法的父类，那鸵鸟就具有“飞”这样的行为，这显然不符合我们对现实世界中事物的认识。当然，你可能会说，我在鸵鸟这个子类中重写 *(override)* `fly()` 方法，让它抛出 `UnSupportedMethodException` 异常不就可以了吗？具体的代码实现如下所示：

```java
public class AbstractBird {
  //...省略其他属性和方法...
  public void fly() { //... }
}

public class Ostrich extends AbstractBird { //鸵鸟
  //...省略其他属性和方法...
  public void fly() {
    throw new UnSupportedMethodException("I can't fly.'");
  }
}
```

这种设计思路虽然可以解决问题，但不够优美。因为除了鸵鸟之外，不会飞的鸟还有很多，比如企鹅。对于这些不会飞的鸟来说，我们都需要重写 `fly()` 方法，抛出异常。

这样的设计，一方面，徒增了编码的工作量；另一方面，也违背了我们之后要讲的最小知识原则 *(Least Knowledge Principle，也叫最少知识原则或者迪米特法则)*，暴露不该暴露的接口给外部，增加了类使用过程中被误用的概率。

你可能又会说，那我们再通过 `AbstractBird` 类派生出两个更加细分的抽象类：会飞的鸟类 `AbstractFlyableBird` 和不会飞的鸟类 `AbstractUnFlyableBird`，让麻雀、乌鸦这些会飞的鸟都继承 `AbstractFlyableBird`，让鸵鸟、企鹅这些不会飞的鸟，都继承 `AbstractUnFlyableBird` 类，不就可以了吗？具体的继承关系如下图所示：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/「MoreThanJava」Day5：面向对象进阶-继承/f77b3726-9183-42a2-bf29-6cc701d228f8.png)

从图中我们可以看出，继承关系变成了三层。不过，整体上来讲，目前的继承关系还比较简单，层次比较浅，也算是一种可以接受的设计思路。我们再继续加点难度。在刚刚这个场景中，我们只关注“鸟会不会飞”，但如果我们关注更多的问题，例如 “鸟会不会叫”、”鸟会不会下单“ 等... 那这个时候，我们又该如何设计类之间的继承关系呢？

总之，继承最大的问题就在于：**继承层次过深、继承关系过于复杂会影响到代码的可读性和可维护性**。这也是为什么我们不推荐使用继承。那刚刚例子中继承存在的问题，我们又该如何来解决呢？

## 组合相比继承有哪些优势？

实际上，我们可以利用组合 *(composition)*、接口、委托 *(delegation)* 三个技术手段，一块儿来解决刚刚继承存在的问题。

我们前面讲到接口的时候说过，接口表示具有某种行为特性。针对“会飞”这样一个行为特性，我们可以定义一个 `Flyable` 接口 *(相当于定义某一种行为，下方会有代码说明)*，只让会飞的鸟去实现这个接口。对于会叫、会下蛋这些行为特性，我们可以类似地定义 `Tweetable` 接口、`EggLayable` 接口。我们将这个设计思路翻译成 Java 代码的话，就是下面这个样子：

```java
public interface Flyable {
  void fly();
}
public interface Tweetable {
  void tweet();
}
public interface EggLayable {
  void layEgg();
}
public class Ostrich implements Tweetable, EggLayable {//鸵鸟
  //... 省略其他属性和方法...
  @Override
  public void tweet() { //... }
  @Override
  public void layEgg() { //... }
}
public class Sparrow impelents Flayable, Tweetable, EggLayable {//麻雀
  //... 省略其他属性和方法...
  @Override
  public void fly() { //... }
  @Override
  public void tweet() { //... }
  @Override
  public void layEgg() { //... }
}
```

不过，我们知道，接口只声明方法，不定义实现。也就是说，每个会下蛋的鸟都要实现一遍 `layEgg()` 方法，并且实现逻辑是一样的，这就会导致代码重复的问题。那这个问题又该如何解决呢？

我们可以针对三个接口再定义三个实现类，它们分别是：实现了 `fly()` 方法的 `FlyAbility` 类、实现了 `tweet()` 方法的 `TweetAbility` 类、实现了 `layEgg()` 方法的 `EggLayAbility` 类。然后，通过 **组合和委托** 技术来消除代码重复。具体的代码实现如下所示：

```java
public interface Flyable {
  void fly()；
}
public class FlyAbility implements Flyable {
  @Override
  public void fly() { //... }
}
//省略Tweetable/TweetAbility/EggLayable/EggLayAbility

public class Ostrich implements Tweetable, EggLayable {//鸵鸟
  private TweetAbility tweetAbility = new TweetAbility(); //组合
  private EggLayAbility eggLayAbility = new EggLayAbility(); //组合
  //... 省略其他属性和方法...
  @Override
  public void tweet() {
    tweetAbility.tweet(); // 委托
  }
  @Override
  public void layEgg() {
    eggLayAbility.layEgg(); // 委托
  }
}
```

当然啦，也可以使用 `JDK 1.8` 之后支持的接口默认方法：

```java
public interface Flyable {
    default void fly() {
        // fly的 的默认实现
    }
}
```

我们知道继承主要有三个作用：表示 `is-a` 关系，支持多态特性，代码复用。而这三个作用都可以通过其他技术手段来达成。比如：

- `is-a` 关系，我们可以通过组合和接口的 `has-a` 关系来替代；
- 多态特性我们可以利用接口来实现；
- 代码复用我们可以通过组合和委托来实现；

所以，从理论上讲，通过组合、接口、委托三个技术手段，我们完全可以替换掉继承，在项目中不用或者少用继承关系，特别是一些复杂的继承关系。

## 如何判断该用组合还是继承？

尽管我们鼓励多用组合少用继承，但组合也并不是完美的，继承也并非一无是处。从上面的例子来看，继承改写成组合意味着要做更细粒度的类的拆分。这也就意味着，我们要定义更多的类和接口。类和接口的增多也就或多或少地增加代码的复杂程度和维护成本。所以，在实际的项目开发中，我们还是要根据具体的情况，来具体选择该用继承还是组合。

如果类之间的继承结构稳定 *(不会轻易改变)*，继承层次比较浅 *(比如，最多有两层继承关系)，继承关系不复杂，我们就可以大胆地使用继承。反之，系统越不稳定，继承层次很深，继承关系复杂，我们就尽量使用组合来替代继承。

除此之外，还有一些 **设计模式** 会固定使用继承或者组合。比如，**装饰者模式**（decorator pattern）、**策略模式**（strategy pattern）、**组合模式**（composite pattern）等都使用了 **组合关系**，而 **模板模式**（template pattern）使用了 **继承关系**。

前面我们讲到继承可以实现代码复用。利用继承特性，我们把相同的属性和方法，抽取出来，定义到父类中。子类复用父类中的属性和方法，达到代码复用的目的。**但是，有的时候，从业务含义上，A 类和 B 类并不一定具有继承关系**。比如，Crawler 类和 PageAnalyzer 类，它们都用到了 URL 拼接和分割的功能，但并不具有继承关系 *(既不是父子关系，也不是兄弟关系)*。**仅仅为了代码复用，生硬地抽象出一个父类出来，会影响到代码的可读性**。如果不熟悉背后设计思路的同事，发现 Crawler 类和 PageAnalyzer 类继承同一个父类，而父类中定义的却只是 URL 相关的操作，会觉得这个代码写得莫名其妙，理解不了。这个时候，**使用组合就更加合理、更加灵活**。具体的代码实现如下所示：

```java
public class Url {
  //...省略属性和方法
}

public class Crawler {
  private Url url; // 组合
  public Crawler() {
    this.url = new Url();
  }
  //...
}

public class PageAnalyzer {
  private Url url; // 组合
  public PageAnalyzer() {
    this.url = new Url();
  }
  //..
}
```

还有一些特殊的场景要求我们必须使用继承。**如果你不能改变一个函数的入参类型，而入参又非接口，为了支持多态，只能采用继承来实现**。比如下面这样一段代码，其中 FeignClient 是一个外部类，我们没有权限去修改这部分代码，但是我们希望能重写这个类在运行时执行的 `encode()` 函数。这个时候，我们只能采用继承来实现了。

```java
public class FeignClient { // Feign Client框架代码
  //...省略其他代码...
  public void encode(String url) { //... }
}

public void demofunction(FeignClient feignClient) {
  //...
  feignClient.encode(url);
  //...
}

public class CustomizedFeignClient extends FeignClient {
  @Override
  public void encode(String url) { //...重写encode的实现...}
}

// 调用
FeignClient client = new CustomizedFeignClient();
demofunction(client);
```

尽管有些人说，要杜绝继承，`100%` 用组合代替继承，但是我的观点没那么极端！之所以 “多用组合少用继承” 这个口号喊得这么响，只是因为，长期以来，我们过度使用继承。还是那句话，组合并不完美，继承也不是一无是处。只要我们控制好它们的副作用、发挥它们各自的优势，在不同的场合下，恰当地选择使用继承还是组合，这才是我们所追求的境界。

# 要点回顾

1. 继承概述 / 单继承 / `is-a` 关系 / 类之间的关系 / 层级结构；
2. 继承的实现 / 覆盖方法 / `protedcted` / 向上转型；
3. Object 类 / 方法调用 / `final` / 内联 / 为什么不推荐使用继承；

# 练习
 
暂无；

# 参考资料

1. 《Java 核心技术 卷 I》
1. 《Java 编程思想》
1. 《码出高效 Java 开发手册》
1. 设计模式之美 - 为何说要多用组合少用继承？如何决定该用组合还是继承？ - https://time.geekbang.org/column/article/169593
1. Introduction to Computer Science using Java - http://programmedlessons.org/Java9/index.html#part02 

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！