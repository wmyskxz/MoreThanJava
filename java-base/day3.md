![](https://imgkr.cn-bj.ufileos.com/48d23848-b4b1-4e9d-b1ca-134b62635d33.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# Part 1. 分支结构

![](https://imgkr.cn-bj.ufileos.com/c588aabd-1fa5-45da-89e2-99d2bf18dccd.png)

- 图片来源：http://www.jituwang.com/vector/201512/569157.html

迄今为止，我们写的 Java 代码都是一条一条语句顺序执行的，这种代码结构通常称之为 **顺序结构**。

然而仅有顺序结构并不能解决所有的问题，比如我们设计一个游戏，游戏第一关的通关条件是获得 `1000` 分，如果分数到达则进入下一关，如果未到达则 ``“Game Over”``：

![](https://imgkr.cn-bj.ufileos.com/83d8302c-e2f7-4aaf-9108-f437b0fbc2bf.png)

这里就产生了两个分支，而且这两个分支只有一个会被执行。类似的场景还有很多，我们将这种结构称之为 **「分支结构」** 或 **「选择结构」**。

「是否进入下一关」这样的决策似乎很小，但是在编程中，复杂的决策是由许多这种小的决策组成的。下面是实现是否进入下一关的程序演示：

```java
import java.util.Scanner;

public class Tester {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String answer;

        System.out.print("玩家是否达到 1000 分?(Y or N): ");
        answer = scan.nextLine();

        if ("Y".equals(answer)) {
            System.out.println("进入下一关");    // true branch
        } else {
            System.out.println("Game Over");   // false branch
        }
    }
}
```

程序首先提醒用户用单一的字符 `Y` 或 `N` 来回答：

```java
System.out.print("玩家是否达到 1000 分?(Y or N): ");
```

然后使用 `Scanner` 类来获取用户的输入：

```java
answer = scan.nextLine();
```

然后使用 `if` 关键字来判断用户输入的字符是否等于 `Y`：

```java
if ("Y".equals(answer))
```

如果相等则进入 `true branch`，否则进入 `false branch`。

> **缩进：**
>
> 这里 `if` 下方的缩进是为了让用户更容易看到程序的逻辑，编译器将忽略掉这些缩进。
> 
> 合理的缩进和程序布局很重要，没有适当的距离和缩进，看程序的逻辑有时会稍显困难。您也期望尽可能清晰地表明程序在做什么不是吗？

## if 条件语句

在 Java 中，要构造分支结构可以使用 `if`、`else` 关键字。

`if` 语句的基本语法是：

```java
if (条件) {
    // 条件满足时执行的语句
}
```

当条件满足时，则会执行 `if` 语句中的代码块儿，否则执行 `if` 语句块后面的代码。

例如：

```java
public class Tester {

    public static void main(String[] args) {
        int n = 70;
        if (n >= 60) {
            System.out.println("及格了");
        }
        System.out.println("END");
    }
}
```

尽管当 `if` 语句块只有一行语句时，可以省略花括号 `{}`：

```java
if (n >= 60)
    System.out.println("及格了");
```

当这并不是一个好主意。

假设某个时候，突然想给 `if` 语句块增加一条语句时：

```java
public class Tester {

    public static void main(String[] args) {
        int n = 50;
        if (n >= 60)
            System.out.println("及格了");
            System.out.println("恭喜你"); // 注意这条语句不是if语句块的一部分
        System.out.println("END");
    }
}
```

由于使用缩进格式，很容易把两行语句都看成 `if` 语句的执行块，但实际上只有第一行语句是 `if` 的执行块。

在使用 `git` 这些版本控制系统自动合并时更容易出问题，所以不推荐忽略花括号的写法。*(事实上，你使用 IDEA 的自动排版代码的功能会帮你自动还原成有花括号的写法，快捷键「ctrl + alt + l」)*

## else 语句

`if` 语句还可以编写一个 `else { ... }`，当条件判断为 `false` 时，将执行 `else` 的语句块：

```java
public class Tester {

    public static void main(String[] args) {
        int n = 70;
        if (n >= 60) {
            System.out.println("及格了");
        } else {
            System.out.println("挂科了");
        }
        System.out.println("END");
    }
}
```

修改上面代码的 `n` 值，观察 `if` 条件为 `true/ false` 时，程序执行的语句块。

注意，`else` 不是必须的。

还可以用多个 `if ... else if ...` 串联。例如：

```java
public class Tester {

    public static void main(String[] args) {
        int n = 70;
        if (n >= 90) {
            System.out.println("优秀");
        } else if (n >= 60) {
            System.out.println("及格了");
        } else {
            System.out.println("挂科了");
        }
        System.out.println("END");
    }
}
```

串联的效果其实相当于：

```java
if (n >= 90) {
    // n >= 90为true:
    System.out.println("优秀");
} else {
    // n >= 90为false:
    if (n >= 60) {
        // n >= 60为true:
        System.out.println("及格了");
    } else {
        // n >= 60为false:
        System.out.println("挂科了");
    }
}
```

### 注意顺序和临界条件

在串联使用多个 `if` 时，要特别注意判断顺序。观察下面的代码：

```java
public class Tester {

    public static void main(String[] args) {
        int n = 100;
        if (n >= 60) {
            System.out.println("及格了");
        } else if (n >= 90) {
            System.out.println("优秀");
        } else {
            System.out.println("挂科了");
        }
    }
}
```

执行发现，`n = 100` 时，满足条件 `n >= 90`，但输出的不是 `"优秀"`，而是 `"及格了"`，原因是 `if` 语句从上到下执行时，先判断 `n >= 60` 成功后，后续 `else` 不再执行，因此，`if (n >= 90)` 没有机会执行了。

正确的方式是按照判断范围从大到小依次判断：

```java
// 从大到小依次判断：
if (n >= 90) {
    // ...
} else if (n >= 60) {
    // ...
} else {
    // ...
}
```

或者改写成从小到大依次判断：

```java
// 从小到大依次判断：
if (n < 60) {
    // ...
} else if (n < 90) {
    // ...
} else {
    // ...
}
```

使用 `if` 时，还要特别注意边界条件。例如：

```java
public class Tester {

    public static void main(String[] args) {
        int n = 90;
        if (n > 90) {
            System.out.println("优秀");
        } else if (n >= 60) {
            System.out.println("及格了");
        } else {
            System.out.println("挂科了");
        }
    }
}
```

假设我们期望 `90` 分或更高为 `“优秀”`，上述代码输出的却是 `“及格”`，原因是 `>` 和 `>=` 效果是不同的。


# Part 2. 循环结构

![](https://imgkr.cn-bj.ufileos.com/6d223877-5b00-4f9b-a972-c854f8556c0f.png)

- 图片引用自：https://www.zlovezl.cn/articles/two-tips-on-loop-writing/

顺序结构的程序语句只能被执行一次。如果您想要同样的操作执行多次，就需要使用循环结构。

Java 中有三种主要的循环结构：

- `while` 循环；
- `do...while` 循环；
- `for` 循环 *(在 Java 5 中还引入了一种主要用于数组的增强型 `for` 循环)*；

## while 循环

`while` 是最基本的循环，它的结构为：

```java
while ( 布尔表达式 ) {
  // 循环内容
}
```

只要布尔表达式为 `true`，循环就会一直执行下去。

### 实例

```java
public class Test {

    public static void main(String args[]) {
        int x = 10;
        while (x < 20) {
            System.out.println("value of x : " + x);
            x++;
        }
    }
}
```

以上实例编译运行结果如下：

```console
value of x : 10
value of x : 11
value of x : 12
value of x : 13
value of x : 14
value of x : 15
value of x : 16
value of x : 17
value of x : 18
value of x : 19
```


## do...while 循环

对于 `while` 语句而言，如果不满足条件，则不能进入循环。但有时候我们需要即使不满足条件，也至少执行一次。

`do…whil`e 循环和 `while` 循环相似，不同的是，`do…while` 循环至少会执行一次。

```java
do {
    // 代码语句
} while (布尔表达式);
```

**注意**：布尔表达式在循环体的后面，所以语句块在检测布尔表达式之前已经执行了。 如果布尔表达式的值为 `true`，则语句块一直执行，直到布尔表达式的值为 `false`。

### 实例

```java
public class Test {

    public static void main(String args[]) {
        int x = 10;

        do {
            System.out.println("value of x : " + x);
            x++;
        } while (x < 20);
    }
}
```

以上实例编译运行结果如下：

```console
value of x : 10
value of x : 11
value of x : 12
value of x : 13
value of x : 14
value of x : 15
value of x : 16
value of x : 17
value of x : 18
value of x : 19
```

## for 循环

虽然所有循环结构都可以用 `while` 或者 `do...while` 表示，但 Java 提供了另一种语句 —— `for` 循环，使一些循环结构变得更加简单。

`for` 循环执行的次数是在执行前就确定的。语法格式如下：

```java
for(初始化; 布尔表达式; 更新) {
    // 代码语句
}
```

关于 `for` 循环有以下几点说明：

- 最先执行初始化步骤。可以声明一种类型，但可初始化一个或多个循环控制变量，也可以是空语句。
- 然后，检测布尔表达式的值。如果为 true，循环体被执行。如果为false，循环终止，开始执行循环体后面的语句。
- 执行一次循环后，更新循环控制变量。
- 再次检测布尔表达式。循环执行上面的过程。

### 实例

```java
public class Test {

    public static void main(String args[]) {

        for (int x = 10; x < 20; x = x + 1) {
            System.out.println("value of x : " + x);
        }
    }
}
```

以上实例编译运行结果如下：

```console
value of x : 10
value of x : 11
value of x : 12
value of x : 13
value of x : 14
value of x : 15
value of x : 16
value of x : 17
value of x : 18
value of x : 19
```

> 您可以再 IDEA 中快速输入 `fori` 关键字来快速创建 `for` 循环的基本结构

## 控制循环

### break 关键字

`break` 主要用在循环语句或者 `switch` 语句中，用来跳出整个语句块。

`break` 跳出最里层的循环，并且继续执行该循环下面的语句。

#### 实例

```java
public class Test {

    public static void main(String args[]) {
        int[] numbers = {10, 20, 30, 40, 50};

        for (int x : numbers) {
            // x 等于 30 时跳出循环
            if (x == 30) {
                break;
            }
            System.out.print(x);
            System.out.print("\n");
        }
    }
}
```

以上实例编译运行结果如下：

```console
10
20
```

### continue 关键字

`continue` 适用于任何循环控制结构中。作用是让程序立刻跳转到下一次循环的迭代。

在 `for` 循环中，`continue` 语句使程序立即跳转到更新语句。

在 `while` 或者 `do…while` 循环中，程序立即跳转到布尔表达式的判断语句。

#### 实例

```java
public class Tester {

    public static void main(String args[]) {
        int[] numbers = {10, 20, 30, 40, 50};

        for (int x : numbers) {
            if (x == 30) {
                continue;
            }
            System.out.print(x);
            System.out.print("\n");
        }
    }
}
```

以上实例编译运行结果如下：

```console
10
20
40
50
```

# Part 3. 构造程序逻辑

![](https://imgkr.cn-bj.ufileos.com/b00c2ec4-93b4-4569-b006-83328b9f4d30.png)

- 图片来源：http://www.mzh.ren/machine-learning-3.html

虽然迄今为止我们学习的内容只是 Java 的冰山一角，但是这些内容已经足够我们来构建程序中的逻辑。

对于编程语言的初学者来说，在学习了 Java 的核心语言元素 *（变量、类型、运算符、表达式、分支结构、循环结构等）* 之后，必须做的一件事情就是尝试用所学知识去解决现实中的问题，换句话说就是锻炼自己把用人类自然语言描述的算法 *（解决问题的方法和步骤）* 翻译成 Java 代码的能力，而这件事情必须通过大量的练习才能达成。

我们在这一 Part 为大家整理了一些经典的案例和习题，希望通过这些例子，一方面帮助大家巩固之前所学的 Java 知识，另一方面帮助大家了解如何建立程序中的逻辑以及如何运用一些简单的算法解决现实中的问题。

## 经典的例子

### 题目一：寻找水仙花数

> **说明**：水仙花数也被称为超完全数字不变数、自恋数、自幂数、阿姆斯特朗数，它是一个 `3` 位数，该数字每个位上数字的立方之和正好等于它本身，*例如：1<sup>3</sup> + 5<sup>3</sup>+ 3<sup>3</sup>=153*。

```java
public class Tester {

    public static void main(String[] args) {
        findAllDaffodilNumberAndPrint();
    }

    /**
     * 查找所有的水仙花数并打印
     */
    public static void findAllDaffodilNumberAndPrint() {
        for (int num = 100; num < 1000; num++) {
            int low = num % 10;
            int mid = num / 10 % 10;
            int high = num / 100;
            // Math.pow(x, 3) 相当于求 x 的 3 次方
            if (num == Math.pow(low, 3) + Math.pow(mid, 3) + Math.pow(high, 3)) {
                System.out.println(num);
            }
        }
    }
}
```

### 题目二：百钱百鸡问题

> **说明**：百钱百鸡是我国古代数学家张丘建在《算经》一书中提出的数学问题：鸡翁一值钱五，鸡母一值钱三，鸡雏三值钱一。百钱买百鸡，问鸡翁、鸡母、鸡雏各几何？翻译成现代文是：公鸡 `5` 元一只，母鸡 `3` 元一只，小鸡 `1` 元三只，用 `100` 块钱买一百只鸡，问公鸡、母鸡、小鸡各有多少只？

```java
public class Tester {

    public static void main(String[] args) {
        getResultAndPrint();
    }

    /**
     * 获取百钱百鸡的结果并输出
     */
    public static void getResultAndPrint() {
        for (int cockNum = 0; cockNum < 20; cockNum++) {
            for (int henNum = 0; henNum < 33; henNum++) {
                int chickNum = 100 - cockNum - henNum;
                if (5 * cockNum + 3 * henNum + chickNum / 3 == 100) {
                    System.out
                        .println("公鸡：" + cockNum + "只, 母鸡：" + henNum + "只, 小鸡：" + chickNum + "只");
                }
            }
        }
    }
}
```

上面使用的方法叫做 **穷举法**，也称为 **暴力搜索法**，这种方法通过一项一项的列举备选解决方案中所有可能的候选项并检查每个候选项是否符合问题的描述，最终得到问题的解。

这种方法看起来比较笨拙，但对于运算能力非常强大的计算机来说，通常都是一个可行的甚至是不错的选择，而且问题的解如果存在，这种方法一定能够找到它。

# 要点回顾

1. 分支结构 `if` 和 `else` 的使用和实例；
1. 循环结构 `while`、`do...while` 和 `for` 循环的使用和实例；
1. 控制循环的 `break` 和 `continue` 实例；
1. 构建程序逻辑的练习；

# 练习

## 练习 1：百分之成绩转换成等级制成绩

> **要求：**
>
> 1. 如果输入成绩在 `90` 分以上 *(含 `90` 分)* 输出 `A`；
> 1. `80 ~ 90` 分 *(不含 `90`)* 输出 `B`；
> 1. `70 ~ 80` 分 *(不含 `80`)* 输出 `C`；
> 1. `60 ~ 70` 分 *(不含 `70`)* 输出 `D`；
> 1. `60` 分以下输出 `E`；

参考答案：

```java
import java.util.Scanner;

public class Tester {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int score = scan.nextInt();
        if (score >= 90) {
            System.out.println("A");
        } else if (score >= 80) {
            System.out.println("B");
        } else if (score >= 70) {
            System.out.println("C");
        } else if (score >= 60) {
            System.out.println("D");
        } else {
            System.out.println("E");
        }
    }
}
```

## 练习 2：输入三条边长，如果能构成三角形就计算周长和面积

参考答案：

```java
import java.util.Scanner;

public class Tester {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        double a = scan.nextDouble();
        double b = scan.nextDouble();
        double c = scan.nextDouble();

        if (a + b > c && a + c > b && b + c > a) {
            double perimeter = a + b + c;
            System.out.println("三角形周长为：" + perimeter);

            double p = (a + b + c) / 2;
            double area = Math.sqrt(p * (p - a) * (p - b) * (p - c));
            System.out.println("三角形面积为：" + area);
        } else {
            System.out.println("不能构成三角形！");
        }

    }
}
```

## 练习 3：打印如下所示的三角形图案

```console
*
**
***
****
*****
```

```console
    *
   **
  ***
 ****
*****
```

```console
    *
   ***
  *****
 *******
*********
```

参考答案：

```java
import java.util.Scanner;

public class Tester {

    public static void main(String[] args) {
        System.out.println("请输入行数：");

        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();

        for (int i = 0; i < row; i++) {
            for (int j = row - i - 1; j < row; j++) {
                System.out.print("*");
            }
            // 换行
            System.out.println();
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                if (j < row - i - 1) {
                    System.out.print(" ");
                } else {
                    System.out.print("*");
                }
            }
            // 换行
            System.out.println();
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row - i - 1; j++) {
                System.out.print(" ");
            }
            for (int j = 0; j < 2 * i + 1; j++) {
                System.out.print("*");
            }
            // 换行
            System.out.println();
        }
    }
}
```

# 自取资料

## 优秀入门资料选取

1. Introduction to Computer Science using Java - http://programmedlessons.org/Java9/index.html
1. Java零基础入门教程包含面向对象 - https://study.163.com/course/courseMain.htm?courseId=1003108028
1. 网易云课堂 - 顶尖中文大学计算机专业课程体系 - https://study.163.com/curricula/cs.htm
1. TeachYourselfCS-CN (自学计算机科学) - https://github.com/keithnull/TeachYourselfCS-CN
1. C语言中文网 Java 入门系列教程 - http://c.biancheng.net/java/10/
1. 廖雪峰 Java 教程 - https://www.liaoxuefeng.com/wiki/1252599548343744
1. 注重动手能力的 Java 教程 - https://how2j.cn/

## 推荐书籍

#### Java 核心技术·卷 I(原书第 11 版)

![](https://imgkr.cn-bj.ufileos.com/1ed33e34-7d6d-4d05-8849-9f60a278726c.png)

**推荐理由：** 这本书在知识体系完整充实的同时，又比《Thinking in Java》暴风式的知识洗礼来得轻松，新人入门书籍强烈推荐！

#### 码出高效：Java开发手册

![](https://imgkr.cn-bj.ufileos.com/87b25b84-8ecc-4196-980b-3f73811c48e9.png)

**推荐理由：** 阿里系出品。从最基础的计算机基础入手，到 Java 的方方面面，加上精美的配图和通俗易懂的解释，是非常适合新手阅读的一本儿关于 Java 的技术书籍。

# 参考资料

1. 《Java 核心技术 卷I》(第11版)
1. Introduction to Computer Science using Java - http://programmedlessons.org/Java9/index.html#part02
1. 菜鸟教程 - https://www.runoob.com/java/
1. C语言中文网 Java 入门系列教程 - http://c.biancheng.net/java/10/
1. Python 100 天从新手到大师 - https://github.com/jackfrued/Python-100-Days

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://imgkr.cn-bj.ufileos.com/ace97ed9-3cfd-425f-85e5-c1a1e5ca7d3f.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！
