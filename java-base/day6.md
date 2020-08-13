![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/「MoreThanJava」Day6：面向对象进阶——多态/image-20200811201534863.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# Part 1. 多态概述

多态，简而言之就是 **同一个行为** 具有 **多个不同表现形式** 或形态的能力。在面向对象的程序设计中，**多态的能力是通过数据抽象和继承之后得来的**。

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/%E3%80%8CMoreThanJava%E3%80%8DDay4%EF%BC%9A%E9%9D%A2%E5%90%91%E5%AF%B9%E8%B1%A1%E5%9F%BA%E7%A1%80/ae55c7cc-ea6e-45f6-8014-307eb774bb38.png)

比如，有一杯水，我不知道它是温的、冰的还是烫的，但是我一摸我就知道了，我摸水杯的这个动作 *(方法)*，对于不同温度的水 *(运行时不同的对象类型)*，就会得到不同的结果，这就是多态。

**代码演示：**

```java
// 基类定义
public class Water {
    public void showTem() { }
}
// 冰水
public class IceWater extends Water {

    @Override
    public void showTem() { System.out.println("我的温度是: 0度"); }
}
// 温水
public class WarmWater extends Water {
    @Override
    public void showTem() { System.out.println("我的温度是: 40度"); }
}
// 开水
public class HotWater extends Water {
    @Override
    public void showTem() { System.out.println("我的温度是: 100度"); }
}
// 测试类
public class TestWater {
    public static void main(String[] args) {
        Water w = new WarmWater();
        w.showTem();

        w = new IceWater();
        w.showTem();

        w = new HotWater();
        w.showTem();
    }
}
```

**结果输出：**

```text
我的温度是: 40度
我的温度是: 0度
我的温度是: 100度
```

这里的方法 `showTem()` 就相当于你去摸水杯。我们定义的 `Water` 类型的引用变量 `w` 就相当于水杯，你在水杯里放了什么温度的水，那么我摸出来的感觉就是什么。就像代码中的那样，放置不同温度的水，得到的温度也就不同，但水杯是同一个。

## 里氏替换原则（LSP）

面向对象的设计原则有一条关于多态的原则，它的描述大概是这样子的：**子类对象** *(object of subtype/derived class)* 能够 **替换** 程序 *(program)* 中 **父类对象** *(object of base/parent class)*  出现的 **任何地方**，并且 **保证原来程序的逻辑行为 *(behavior)* 不变及正确性不被破坏**。

这么说可能有点抽象，简单说就是 **子类和父类的行为应该保持一致**。

### 哪些代码明显违背了 LSP？

实际上，里式替换原则还有另外一个更加能落地、更有指导意义的描述，那就是 **“Design By Contract”**，中文翻译就是 **“按照协议来设计”**。定义中父类和子类之间的关系，也可以替换成接口和实现类之间的关系。

为了更好地理解这句话，我举几个违反里式替换原则的例子来解释一下。

#### 1 - 子类违背父类声明要实现的功能

父类中提供的 `sortOrdersByAmount()` 订单排序函数，是按照金额从小到大来给订单排序的，而子类重写这个 `sortOrdersByAmount()` 订单排序函数之后，是按照创建日期来给订单排序的。那子类的设计就违背里式替换原则。

#### 2 - 子类违背父类对输入、输出、异常的约定

在父类中，某个函数约定：运行出错的时候返回 `null`；获取数据为空的时候返回空集合（empty collection）。而子类重载函数之后，实现变了，运行出错返回异常（exception），获取不到数据返回 `null`。那子类的设计就违背里式替换原则。

#### 3 - 子类违背父类注释中所罗列的任何特殊说明

父类中定义的 `withdraw()` 提现函数的注释是这么写的：“用户的提现金额不得超过账户余额……”，而子类重写 `withdraw()` 函数之后，针对 VIP 账号实现了透支提现的功能，也就是提现金额可以大于账户余额，那这个子类的设计也是不符合里式替换原则的。

> 当然，当前的大环境下，注释的可信度还是得斟酌斟酌.. *(不可尽信..)*

# Part 2. 向上转型 && 向下转型

## 再谈向上转型

在 [上一篇文章](https://www.wmyskxz.com/2020/08/07/morethanjava-day-5-mian-xiang-dui-xiang-jin-jie-ji-cheng-xiang-jie/) 里面我们已经谈到 —— **对象既可以作为它本身的类型使用，也可以作为它基类的类型使用**。而这种把对某个对象的引用视为其基类型的引用的做法被称为 **向上转型** *(因为在继承树的画法中，基类位于子类上方)*。

语句 `Water w = new WarmWater();` 就是向上转型的典型代码，这会将子类类型 `WarmWater` 转成父类的 `Water` 类型。

### 存在问题

**❶ 向上转型时，子类单独定义的方法会丢失。**

例如，我们如果在温水中定义一个喝水的方法 `drink()`，那么当 `w` 引用指向 `WarmWater` 类实例的时候是访问不到 `drink()` 方法的，`w.drink()` 会报错。

**❷ 子类引用不能指向父类对象。**

`HotWater hotWater  = (HotWater)new Water();` 这样是不行的。

### 向上转型的好处

- 减少重复代码，提高代码可读性；
- 提高系统扩展性；

举个例子，比如我现在有许多不同温度的水，如果不用向上转型，摸水杯这个动作我需要这样写：

```java
// Water 类中方法定义
public void showTem(IceWater water) { water.showTem(); }
public void showTem(WarmWater water) { water.showTem(); }
public void showTem(HotWater water) { water.showTem(); }
// 测试类中调用
water.showTem(new IceWater());
water.showTem(new WarmWater());
water.showTem(new HotWater());
```

每一种不同温度的水我都需要在 `Water` 中单独定义一个方法 *(因为都是不同的类型)*，数量一多，就会变得非常冗余和复杂。

但使用向上转型，一切就轻松多了：

```java
// Water 类中方法定义
public void showTem(Water water) { water.showTem(); }
// 测试类中调用
water.showTem(new IceWater());
water.showTem(new WarmWater());
water.showTem(new HotWater());
```

就算新添加一种温度的水，我也只需要继承 `Water` 实现 `showTem()` 方法就行了，原有的代码几乎不需要修改。这也体现了软件设计原则中重要的 **开闭原则 —— 对扩展开放，对修改封闭。**

## 向下转型

与向上转型相对应的就是向下转型了 —— 也就是把父类对象转为子类对象。*(这有大坑...)*

还是用上面的摸水杯的例子来说明，我们先在温水 `WarmWater` 中加入一个喝水的方法：

```java
public class WarmWater extends Water {
    @Override
    public void showTem() { System.out.println("我的温度是: 40度"); }
    // 新增加的喝水的方法
    public void drink() { System.out.println("喝水..."); }
}
```

**示例代码：**

```java
class Tester {
    public static void main(String[] args) {
        Water water = new WarmWater();// 子类实例赋给父类引用 - 向上转型
        WarmWater warmWater = (WarmWater) water;// Water向下转型为WarmWater
        warmWater.drink();

        IceWater iceWater = (IceWater) water;// Water向下转型为IceWater
        iceWater.drink();// IDE 提示无法找到 drink() 方法
    }
}
```

为什么第一段代码不报错呢？因为 `water` 本身就是 `WarmWater` 类型的对象，所以它理所当然的可以向下转型为 `WarmWater` 类型了，也理所当然的不能转型为 `IceWater`，这就好像你见过 **一条狗突然变成一只猫** 的情况吗？

**再来看下列代码：**

```java
class Tester {
    public static void main(String[] args) {
        Water water = new Water();
        WarmWater warmWater = (WarmWater) water;
        // 下列代码报错：java.lang.ClassCastException: class Water cannot be cast to class WarmWater
        warmWater.drink();
    }
}
```

上面例子想要说明的是，**`Water` 类型的对象 *(父类型)* 不能向下转型为任何类型的对象**。这就好像你去考古，你发现了一个新生物，你知道它是一种动物，但你不能直接说它是猫或者狗...

### 向下转型注意事项

- 向下转型的前提是父类对象指向的是子类对象；*(也就是对应上面实例代码中向下转型 `WarmWater` 的情况.. `new WarmWater()` 首先得完成向上的转型..)*
- 向下转型只能转型为本类对象；*(猫是不能变成狗的.. 对应上方 `WamWater` 类型就不能转成 `IceWarm` 类型的情况)*

### 向下转型的意义

有的小伙伴可能看到这里有点懵了.. 向下转型需要先向上转型，这转来转去好玩儿是吗？

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/「MoreThanJava」Day6：面向对象进阶——多态/6af89bc8gw1f8q76jxt2xj202a025dfn.png)

向上转型让我们有了统一处理一类抽象事物的能力，这大大减少了我们的重复代码，并增加了我们代码的可扩展性。可事实上是，尽管我们尽力抽象一类事物，让他们尽可能地保证行为的统一，但总有例外！*(就像 [上一次](https://www.wmyskxz.com/2020/08/07/morethanjava-day-5-mian-xiang-dui-xiang-jin-jie-ji-cheng-xiang-jie/) 我们讨论继承时提到的鸟类的例子，并不是所有鸟都能飞或者叫！)* 

所以当例外来临时，我们就可以及时判断并做对应的处理。*(这也比较符合现实的情况)*

最典型的例子就是 JDK 中的某一些集合类，对于集合类来说，并不需要记住存储所有存储对象的类型，而是统一抽象成了 `Node` 类型，就拿 `HashMap` 来说吧，存储一个元素 *(`putVal()` 方法)* 时就要判定当前节点时属于链表还是红黑树的部分：

![为防止 "新小伙伴" 看着代码头晕，简化处理了一下](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/「MoreThanJava」Day6：面向对象进阶——多态/image-20200811180639052.png)

# Part 3. 多态经典案例分析

我们来看一个经典的例子：

```java
// A 类
public class A {
    public String show(D object) { return "A and D"; }
    public String show(A object) { return "A and A"; }
}
// B 类
public class B extends A {
    public String show(B object) { return "B and B"; }
    @Override
    public String show(A object) { return "B and A"; }
}
// C 类
public class C extends B{ }
// D 类
public class D extends B{ }
```

**测试类：**

```java
public class Tester {

    public static void main(String[] args) {
        A a = new A();
        A aRefB = new B();
        B b = new B();
        C c = new C();
        D d = new D();

        System.out.println("1-" + a.show(b));
        System.out.println("2-" + a.show(c));
        System.out.println("3-" + a.show(d));

        System.out.println("4-" + aRefB.show(b));
        System.out.println("5-" + aRefB.show(c));
        System.out.println("6-" + aRefB.show(d));

        System.out.println("7-" + b.show(b));
        System.out.println("8-" + b.show(c));
        System.out.println("9-" + b.show(d));
    }
}
```

**输出结果：**

```text
1-A and A
2-A and A
3-A and D
4-B and A
5-B and A
6-A and D
7-B and B
8-B and B
9-A and D
```

前三个比较容易，因为 B、C 都本质上是 A 类，所以 `1` 和 `2` 都进入了 A 类中签名为 `show(A)` 的方法。

但是第四个非常奇怪，A 对象类型引用了一个 B 类型的实例，输出是 `B and A`，而不是想象中的 `B and B`，为什么呢？

这里有一个新知识点：**决定调用哪个方法的是引用变量类型**。

拿这里的 `aRefB.show(b)` 来说好了，`aRefB` 虽然是 A 类型的引用，但首先会查找 B 对象中的方法 *(因为它实际的指向是 B)*，而引用 `b` 正好是一个 B 类型 *(实质上是 is-a A 类型)*，所以符合 B 对象中签名为 `show(A)` 的方法，就输出了 `B and A`。如果 B 类型中没有符合签名的方法，那么会从父类中查找，继续这个过程直到找到或者报错。

如果你能理解这个过程，并分析其他的情况，那么说明你真的掌握了。

再来分析 `b.show(d)` 输出 `A and D` 的情况，就简单很多了：B 对象中不存在 `show(D)` 这样的签名，所以从父类 A 中查找，故输出了 `A and D`。

# 要点回顾

1. 多态概述 / 里氏替换原则 / 向上向下转型；
2. 典型多态案例分析 / 练习；

# 练习

## 练习 1：工资结算系统

> 某公司有三种类型的员工，分别是部门经理、程序员和销售员。需要设计一个工资结算系统，根据提供的员工信息来计算月薪。
>
> 部门经理的月薪是每月固定 `15000` 元；
> 程序员的月薪按每月工作时间计算，每小时 `150` 元；
> 销售员的月薪是 `1200` 底薪加上销售额 `5%` 的提成；

**抽象员工类：**

```java
public abstract class AbstractEmployee {
    private String name;

    public AbstractEmployee(String name) {
        this.name = name;
    }

    // 获取工资
    public abstract double getSalary();
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

**项目经理类：**

```java
public class Manager extends AbstractEmployee {
    public Manager(String name) {
        super(name);
    }

    @Override
    public double getSalary() { return 15000; }
}
```

**程序员类：**

```java
public class Programer extends AbstractEmployee {
    private Integer workHours;

    public Programer(String name, Integer workHours) {
        super(name);
        this.workHours = workHours;
    }

    // 仅提供单独的 set 方法，工作时间理论上来说是一个私人的消息..
    public void setWorkHours(Integer workHours) {
        this.workHours = workHours;
    }
    @Override
    public double getSalary() { return 150 * workHours; }
}

```

**销售员类：**

```java
public class Salesman extends AbstractEmployee {
    private Integer salesAmount;

    public Salesman(String name, Integer salesAmount) {
        super(name);
        this.salesAmount = salesAmount;
    }

    // 也仅提供 set 方法，并不是所有人都能访问销售人员的销售金额
    public void setSalesAmount(Integer salesAmount) {
        this.salesAmount = salesAmount;
    }
    @Override
    public double getSalary() { return 1200 + 0.05 * salesAmount; }
}

```

**测试类：**

```java
import java.util.List;

public class Tester {
    public static void main(String[] args) {
        // 项目经理张三、996程序员李四、月销售过万的明星销售员王五
        List<AbstractEmployee> employees = List
            .of(new Manager("张三"), new Programer("李四", (21 - 9) * 6), new Salesman("王五", 10000));

        // 发工资..
        for (AbstractEmployee employee : employees) {
            System.out.println(employee.getName() + "工资为：" + employee.getSalary());
        }
    }
}
```

**程序输出：**

```text
张三工资为：15000.0
李四工资为：10800.0
王五工资为：1700.0
```

*(ps：有感受到来自于现实主义的正义光辉洒在你的身上吗？)*

# 参考资料

1. 《Java 核心技术 卷 I》
2. 《Java 编程思想》
3. Introduction to Computer Science using Java - http://programmedlessons.org/Java9/index.html
4. 重新认识java（五） ---- 面向对象之多态（向上转型与向下转型） - https://blog.csdn.net/qq_31655965/article/details/54746235
5. 极客时间 | 设计模式之美 - https://time.geekbang.org/column/article/177110
6. Python 100 天从新手到大师 - https://github.com/jackfrued/Python-100-Days

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！