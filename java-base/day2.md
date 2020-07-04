![](https://imgkr.cn-bj.ufileos.com/9eac5907-2a62-4665-911a-ba7a0927bdc8.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# Part 1. 数据类型

![](https://imgkr.cn-bj.ufileos.com/eb90cb38-53e4-4b46-9556-836f3007cbfa.png)

假设您遇到了以下撕碎的纸片，您觉得会是什么意思？

![](https://imgkr.cn-bj.ufileos.com/fb561ec0-f171-4b46-9458-e91f786a7be6.png)

在不了解上下文的情况下，很难说出 `MIX` 的含义，它可能是罗马数字 `1009`，也可以是英语单词 `mix` 或者别的什么东西。

在不知道上下文的情况下，一串字母没有任何意义。

与一串字母一样，一串 `01` 的含义取决于如何使用。而决定这一串数据如何使用的方案被称为其 **数据类型** *(跟文件类型有些类似)*。 

## 8 种基本数据类型

**Java 是一种强类型语言**。这意味着必须为每一条数据声明一种类型。在 Java 中共有 `8` 种基本类型 *(primitive type)*，其中 `4` 种整型、`2` 种浮点类型、`1` 种字符类型、`1` 种表示真值的 `boolean` *(布尔)* 类型。

### 整型

整型被用来表示没有小数部分的数值，允许是负数。Java 提供了 `4` 种整型，具体内容如下：

类型 | 存储需求 | 取值范围
:-- | :-- | :--
int | 4 字节 | -2147483648 ~ 2147483647 *(刚超过 `20` 亿，这个数值也就是 2<sup>32</sup> 对半分成正负值，`32=4字节*每个字节8位`)*
short | 2 字节 | -32768 ~ 32767
long | 8 字节 | -9223372036854775808 ~ 9223372036854775807
byte | 1 字节 | -128 ~ 127

在通常情况下，`int` 类型最常用。但如果想要表示整个地球的居住人口，那么就需要使用 `long` 类型了。`byte` 和 `short` 类型主要用于特定的应用场合，例如，底层的文件处理或者存储空间很宝贵时的大数组 *(因为节约内存)*。

### 浮点类型

浮点类型用于表示有小数部分的数值。在 Java 中有两种浮点类型，具体内容如下：

类型 | 存储需求 | 取值范围
:-- | :-- | :--
float | 4 字节 | 大约 ±3.40282347E + 38F *(大约有效数为 6 ~ 7)*
double | 8 字节 | 大约 ±1.7976931486231580E + 308 *(大约有效数为 15 位)*

`double` 表示这种类型的数值精度是 `float` 类型的两倍 *(也有人称 `double` 为双精度数值)*。

#### 浮点数精度问题

问一个问题：`0.1 + 0.2 = ？`

![](https://imgkr.cn-bj.ufileos.com/e0802eac-eb7b-40bb-b816-893ead0099f4.png)

先别奇怪，在 IDEA 中尝试着输出一下这句话就知道了：

```java
System.out.println(0.1 + 0.2);

// 输出：0.30000000000000004
```

`0.1 + 0.2` 为什么会等于 `0.30000000000000004`？而不是我们想象中的 `0.3`？

**这不是因为它们在计算时出现了错误，而是因为浮点数计算标准的要求。**

首先我们要明确一点：**编程中的浮点数并不能和数学中的小数看做同一个东西**。

- 编程中的浮点数的精度往往都是有限的，单精度的浮点数使用 `32` 位表示，而双精度的浮点数使用 `64` 位表示；
- 数学中的小数系统可以通过引入无限序列....可以表示任意的实数；

请考虑使用 **十进制** 表示 `1/3`：

```console
0.3333333333333333....
```

如果想要完整地表达 `1/3` 的精度，那么小数点之后的 `3` 需要无限地写下去。如果需要让你在一张纸上表达清晰，显然由于纸张大小的限制你无法无限地写下去...

`0.1` 和 `0.2` 在 **二进制** 中同 `1/3` 在 **十进制** 中一样，不属于整数的范畴，所以只能用近似值来代替，由于精度的限制 `0.1` 和 `0.2` 使用单精度浮点数表示的实际值为：`0.100000001490116119384765625` 和 `0.20000000298023223876953125`，把它们相加起来得到的结果与我们在一开始看到的非常相似：

![](https://imgkr.cn-bj.ufileos.com/462ec222-4fb5-4f48-8630-899dd8348338.png)

在交易系统或者科学计算的场景中，如果需要更高的精度小数，可以使用具有 `28` 个有效位数的 `decimal` 或者直接使用分数，不过这些表示方法的开销也随着有效位数的增加而提高，我们应该按照需要选择最合适的方法。

重新回到最开始的问题 — `0.1` 和 `0.2` 相加不等于 `0.3` 的原因包括以下两个：

- 使用二进制表达十进制的小数时，某些数字无法被有限位的二进制小数表示；
- 单精度和双精度的浮点数只包括 `7` 位或者 `15` 位的有效小数位，存储需要无限位表示的小数时只能存储近似值；

**在使用单精度和双精度浮点数时也应该牢记它们只有 `7` 位和 `15` 位的有效位数**。

### char 类型

`char` 用来表示单个字符。在 Java 中 `char` 类型的数据使用 `16` 位来存储和表示，而许多编程语言则仅用 `8` 位。

`char` 类型的字面量值需要用 **单引号** 括起来。例如：`'A'` 是编码值为 `65` 的 **字符常量**，它与 `"A"` 不同，`"A"` 是仅包含字符 `A` 的 **字符串** *(String 类型)*。

**强烈建议**：不要在程序中使用 `char` 类型，除非您确实需要处理 `UTF-16` 代码单元。

*(更多相关资料放入了下面的自取资料，感兴趣可以去阅读一下更多 char 类型的东西，不感兴趣跳过即可...)*

### boolean 类型

`boolean` *(布尔)* 类型有两个值：`false` 和 `true` *(注意这两个是布尔类型的字面常量也是保留字)*，用来判断逻辑条件是否成立。

## 对象类型

**Java 中的所有数据都属于「基本数据类型」或「对象」中的一种。**

虽然只有八种基本数据类型，但 Java 有许多满足您需求的相关类型的对象供您使用，例如，表示字符串的 `String` 类型。

我们会在之后的内容中更多地讨论对象 *(因为 Java 是一种面向对象的编程语言)*，现在，您需要了解以下信息：

- 基本数据类型使用少量的固定字节数 *(下面会详细介绍)*；
- 只有 `8` 种基本数据类型，您无法创建新的原始数据类型；
- 一个对象是一个较大的数据块，可能使用很多字节的内存；
- 对象类型的数据被称为 **类**；
- Java 中已经封装了足够多的类用来满足您各类的需求，您也可以发明新的类来满足程序的特定需求；

# Part 2. 变量

![](https://imgkr.cn-bj.ufileos.com/e59bfd69-e4be-4dba-ad62-3fc8e0a8218d.png)

**计算机内存中有数以十亿计的字节用于存储机器指令和数据。** 

程序运行时，某些内存用于存储机器指令，而另外一些则用于存储数据。后来，当另一个程序运行时，以前保存的机器指令中的某些字节现在可以用来保存数据，而之前保存的数据中的某些字节现在可以保存机器指令。

计算机先驱 **约翰·冯·诺依曼** *(John von Neumann)* 的想法是：**使用相同的存储器来存储指令和数据**。

回想一下，**数据类型是一种使用位模式来表示值的方案**。

![](https://imgkr.cn-bj.ufileos.com/a3a9a56a-9162-46e0-86b5-4ee21b806557.png)

可以把 **变量** 视为一个由一个或多个字节组成的小盒子，该盒子可以使用特定的数据类型保存值。

要将值存储在内存中，以后再取回它，则程序必须为每个变量指定一个名称，如 `className`/ `payAmount` **(变量名采用小驼峰命名法)**。

变量随运行程序的需要而变化。当正在运行的程序不再需要变量时，该内存部分可用于其他目的。

## 声明变量

在 Java 中，每个变量都必须有一个类型 *(type)*。在声明变量时，需要先指定变量的类型，然后是变量名：

```java
double salary;
int vacationDays;
long earthPopluation;
boolean finished;
```

可以看到，每个变量都以分号 *(`;`)* 结束。由于声明是一条完整的 Java 语句，而所有的 Java 语句都以分号结束，所以这里的分号是必须的。

### 变量命名

在 Java 中，变量命名需要遵循以下硬性规定和强烈建议遵守的非硬性规定：

#### 硬性规则
    
1. 变量名必须是一个以字母开头并由字母或数字构成的序列 *(尽管 `$` 是合法的，但不要在你自己的代码中使用这个字符，它只用在 Java 编译器或其他工具生成的名字中)*；
1. 每一个字符都有意义，且大小写敏感；
1. 不要使用 Java 中的保留字；
    
#### 《阿里巴巴 Java 开发手册》规则
    
1. **【强制】** 代码中的命名 *(所有标识符)* 均不能以下划线或美元符号开始，也不能以下划线或美元符号结束；*(反例: `$name`)*
1. **【强制]** 代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式；*(反例：`DaZhePromotion` - 打折)*
1. **【强制】** 杜绝完全不规范的缩写，避免词不达意；*(反例：`condition` 缩写成 `condi`)*
1. **【推荐】** 为了达到代码自解释的目的，任何自定义编程元素在命名时，使用尽量完整的单词组合来表达其意；*(反例：`int a;` 的随意命名方式)*

## 变量初始化

**声明一个变量之后，必须用赋值语句对变量进行显式初始化，千万不要使用未初始化的变量的值**。例如，Java 编译器认为下面的语句序列是错误的：

```java
int amount;
System.out.println(amount); // ERROR -- variable not initialized
```

要相对一个已经声明过的变量进行赋值，就需要将变量名放在等号 *(`=`)* 左侧，再把一个适当取值放在等号的右侧：

```java
int amount;
amount = 12;
```

也可以将变量的声明和初始化放在同一行中。例如：

```java
int amount = 12;
```

最后，在 Java 中可以将声明放在代码中的任何地方。

但让变量尽可能地靠近变量第一次使用的地方，这是一种良好的程序编写风格。

## 变量使用范例

我们来使用变量完成两个数的加减乘除：

```java
int num1 = 2;
int num2 = 3;
System.out.println(num1 + num2);
System.out.println(num1 - num2);
System.out.println(num1 * num2);
System.out.println(num1 / num2);
```

# Part 3. 运算符

![](https://imgkr.cn-bj.ufileos.com/39846f38-0dfe-4e04-b3a8-ce140174aedd.png)

计算机的最基本用途之一就是执行数学运算，作为一门计算机语言，Java 也提供了一套丰富的运算符来操纵变量。我们可以把运算符分成以下几组：

- 算术运算符;
- 关系运算符;
- 位运算符;
- 逻辑运算符;
- 赋值运算符;
- 其他运算符;

## 算术运算符

算术运算符用在数学表达式中，它们的作用和在数学中的作用一样。下表列出了所有的算术运算符。

表格中的实例假设整数变量 `A` 的值为 `10`，变量 `B` 的值为 `20`：

![](https://imgkr.cn-bj.ufileos.com/a69a5dbc-190c-4c29-8b11-5313bf04f3e5.png)

### 实例

下面的简单示例程序演示了算术运算符。复制并粘贴下面的 Java 程序并保存为 `Test.java` 文件，然后编译并运行这个程序：

```java
public class Test {

    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        int c = 25;
        int d = 25;
        System.out.println("a + b = " + (a + b));
        System.out.println("a - b = " + (a - b));
        System.out.println("a * b = " + (a * b));
        System.out.println("b / a = " + (b / a));
        System.out.println("b % a = " + (b % a));
        System.out.println("c % a = " + (c % a));
        System.out.println("a++   = " + (a++));
        System.out.println("a--   = " + (a--));
        // 查看  d++ 与 ++d 的不同
        System.out.println("d++   = " + (d++));
        System.out.println("++d   = " + (++d));
    }
}
```

**运行结果：**

```console
a + b = 30
a - b = -10
a * b = 200
b / a = 2
b % a = 0
c % a = 5
a++   = 10
a--   = 11
d++   = 25
++d   = 27
```

### i++ 与 ++i 到底有什么不同？

**实际上，不管是`前置 ++`，还是`后置 ++`，都是先将变量的值加 `1`，然后才继续计算的。**

**二者之间真正的区别是**：`前置 ++` 是将变量的值加 `1` 后，使用增值后的变量进行运算的，而`后置 ++` 是首先将变量赋值给一个临时变量，接下来对变量的值加 `1`，然后使用那个临时变量进行运算。

![](https://imgkr.cn-bj.ufileos.com/08c5a27f-3655-4167-b832-775506885eff.png)

## 关系运算符

下表为Java支持的关系运算符。

表格中的实例整数变量 `A` 的值为 `10`，变量 `B` 的值为 `20`：

![](https://imgkr.cn-bj.ufileos.com/c4b0c001-526f-424c-b177-11b5b7ba0c1b.png)

### 实例

下面的简单示例程序演示了关系运算符。复制并粘贴下面的 Java 程序并保存为 `Test.java` 文件，然后编译并运行这个程序：

```java
public class Test {

    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        System.out.println("a == b = " + (a == b));
        System.out.println("a != b = " + (a != b));
        System.out.println("a > b = " + (a > b));
        System.out.println("a < b = " + (a < b));
        System.out.println("b >= a = " + (b >= a));
        System.out.println("b <= a = " + (b <= a));
    }
}
```

**运行结果：**

```console
a == b = false
a != b = true
a > b = false
a < b = true
b >= a = true
b <= a = false
```

## 位运算符

Java定义了位运算符，应用于整数类型 *(int)*，长整型 *(long)*，短整型 *(short)*，字符型 *(char)*，和字节型 *(byte)* 等类型。

位运算符作用在所有的位上，并且按位运算。假设 `a = 60，b = 13;` 它们的二进制格式表示将如下：

```console
A = 0011 1100
B = 0000 1101
-----------------
A&B = 0000 1100
A | B = 0011 1101
A ^ B = 0011 0001
~A = 1100 0011
```

下表列出了位运算符的基本运算，假设整数变量 `A` 的值为 `60` 和变量 `B` 的值为 `13`：

![](https://imgkr.cn-bj.ufileos.com/b667545b-3c1c-497e-ae5f-9d40ec61ca2e.png)

### 实例

下面的简单示例程序演示了关系运算符。复制并粘贴下面的 Java 程序并保存为 `Test.java` 文件，然后编译并运行这个程序：

```java
public class Test {

    public static void main(String[] args) {
        int a = 60; /* 60 = 0011 1100 */
        int b = 13; /* 13 = 0000 1101 */
        int c = 0;
        c = a & b;       /* 12 = 0000 1100 */
        System.out.println("a & b = " + c);

        c = a | b;       /* 61 = 0011 1101 */
        System.out.println("a | b = " + c);

        c = a ^ b;       /* 49 = 0011 0001 */
        System.out.println("a ^ b = " + c);

        c = ~a;          /*-61 = 1100 0011 */
        System.out.println("~a = " + c);

        c = a << 2;     /* 240 = 1111 0000 */
        System.out.println("a << 2 = " + c);

        c = a >> 2;     /* 15 = 1111 */
        System.out.println("a >> 2  = " + c);

        c = a >>> 2;     /* 15 = 0000 1111 */
        System.out.println("a >>> 2 = " + c);
    }
} 
```

**运行结果：**

```console
a & b = 12
a | b = 61
a ^ b = 49
~a = -61
a << 2 = 240
a >> 2  = 15
a >>> 2 = 15
```

## 逻辑运算符

下表列出了逻辑运算符的基本运算，假设布尔变量 `A` 为真，变量 `B` 为假：

![](https://imgkr.cn-bj.ufileos.com/afb5f937-7e75-4a14-95fe-034ba0c278fd.png)

### 实例

下面的简单示例程序演示了关系运算符。复制并粘贴下面的 Java 程序并保存为 `Test.java` 文件，然后编译并运行这个程序：

```java
public class Test {

    public static void main(String[] args) {
        boolean a = true;
        boolean b = false;
        System.out.println("a && b = " + (a && b));
        System.out.println("a || b = " + (a || b));
        System.out.println("!(a && b) = " + !(a && b));
    }
}
```

**运行结果：**

```console
a && b = false
a || b = true
!(a && b) = true
```

### 短路逻辑运算符

当使用与逻辑运算符时，在两个操作数都为 `true` 时，结果才为 `true`，但是当得到第一个操作为 `false` 时，其结果就必定是 `false`，这时候就不会再判断第二个操作了。

事实上，如果所有的逻辑表达式都有一部分不必计算，那将获得潜在的性能提升。

**实例：**

```java
public class LuoJi {

    public static void main(String[] args) {
        int a = 5;//定义一个变量；
        boolean b = (a < 4) && (a++ < 10);
        System.out.println("使用短路逻辑运算符的结果为" + b);
        System.out.println("a的结果为" + a);
    }
}
```

**运行结果：**

```console
使用短路逻辑运算符的结果为false
a的结果为5
```

## 赋值运算符

下面是 Java 语言支持的赋值运算符：

![](https://imgkr.cn-bj.ufileos.com/51d1852a-9609-4d1e-949a-6fef481e064d.png)

### 实例

下面的简单示例程序演示了关系运算符。复制并粘贴下面的 Java 程序并保存为 `Test.java` 文件，然后编译并运行这个程序：

```java
public class Test {

    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        int c = 0;
        c = a + b;
        System.out.println("c = a + b = " + c);
        c += a;
        System.out.println("c += a  = " + c);
        c -= a;
        System.out.println("c -= a = " + c);
        c *= a;
        System.out.println("c *= a = " + c);
        a = 10;
        c = 15;
        c /= a;
        System.out.println("c /= a = " + c);
        a = 10;
        c = 15;
        c %= a;
        System.out.println("c %= a  = " + c);
        c <<= 2;
        System.out.println("c <<= 2 = " + c);
        c >>= 2;
        System.out.println("c >>= 2 = " + c);
        c >>= 2;
        System.out.println("c >>= 2 = " + c);
        c &= a;
        System.out.println("c &= a  = " + c);
        c ^= a;
        System.out.println("c ^= a   = " + c);
        c |= a;
        System.out.println("c |= a   = " + c);
    }
}
```

**运行结果：**

```console
c = a + b = 30
c += a  = 40
c -= a = 30
c *= a = 300
c /= a = 1
c %= a  = 5
c <<= 2 = 20
c >>= 2 = 5
c >>= 2 = 1
c &= a  = 0
c ^= a   = 10
c |= a   = 10
```

## 其他运算符

### 条件运算符 (?:)

条件运算符也被称为三元运算符。该运算符有 `3` 个操作数，并且需要判断布尔表达式的值。该运算符的主要是决定哪个值应该赋值给变量。

```java
variable x = (expression) ? value if true : value if false
```

### 实例

下面的简单示例程序演示了关系运算符。复制并粘贴下面的 Java 程序并保存为 `Test.java` 文件，然后编译并运行这个程序：

```java
public class Test {

    public static void main(String[] args) {
        int a, b;
        a = 10;
        // 如果 a 等于 1 成立，则设置 b 为 20，否则为 30
        b = (a == 1) ? 20 : 30;
        System.out.println("Value of b is : " + b);

        // 如果 a 等于 10 成立，则设置 b 为 20，否则为 30
        b = (a == 10) ? 20 : 30;
        System.out.println("Value of b is : " + b);
    }
}
```

**运行结果：**

```console
Value of b is : 30
Value of b is : 20
```

### instanceof 运算符

该运算符用于操作对象实例，检查该对象是否是一个特定类型（类类型或接口类型）。

`instanceof` 运算符使用格式如下：

```java
( Object reference variable ) instanceof  (class/interface type)
```

如果运算符左侧变量所指的对象，是操作符右侧类或接口 *(class/interface)* 的一个对象，那么结果为真。

下面是一个例子：

```java
String name = "James";
boolean result = name instanceof String; // 由于 name 是 String 类型，所以返回真
```

# 要点回顾

1. Java 是一种强类型语言，任何一种数据都属于 `1` 种基本类型或者对象类型 *(类)* 中的一种；
1. `8` 种基本数据类型； 
1. 为什么引入变量、如何定义使用变量以及变量名的命名规范；
1. Java 中的运算符以及使用实例；

# 练习

## 获取用户输入 Scanner

`java.util.Scanner` 是 Java5 的新特征，我们可以通过 `Scanner` 类来获取用户的输入。

下面是创建 Scanner 对象的基本语法：

```java
Scanner scanner = new Scanner(System.in);
```

在下面的示例中，我们将使用该类的 `nextLine()` 方法，该方法用于读取字符串：

```java
import java.util.Scanner;  // Import the Scanner class

class MyClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter username");

        String userName = scanner.nextLine();  // Read user input
        System.out.println("Username is: " + userName);  // Output user input
    }
}
```

### 输入类型

在上面的示例中，我们使用了 `nextLine()` 用于读取字符串的方法。要阅读其他类型，请查看下表：

![](https://imgkr.cn-bj.ufileos.com/26168eb3-f7b9-4b26-817f-7308ff381dbf.png)

- 引用自：https://www.w3schools.com/java/java_user_input.asp

## 练习 1：输入圆的半径计算周长和面积

参考答案：

```java
import java.util.Scanner;

public class Tester {

    public static void main(String[] args) {
        System.out.println("请输入圆的半径：");

        Scanner scanner = new Scanner(System.in);
        int radius = scanner.nextInt();

        // 计算周长和面积
        double area = radius * radius * 3.14;
        double perimeter = 2 * 3.14 * radius;

        System.out.println("该圆的面积为：" + area);
        System.out.println("该圆的周长为：" + perimeter);
    }
}
```

## 练习 2：输入年份判断是不是闰年

提示：

闰年需要满足：
1. 能被 `4` 整除，并且不能被 `100` 整除；
1. 或者能被 `400` 整除；

参考答案：

```java
import java.util.Scanner;

public class Tester {

    public static void main(String[] args) {
        System.out.println("请输入年份：");

        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();

        boolean leapYear;

        boolean divisbleBy4 = year % 4 == 0;
        boolean notDivisbleBy100 = year % 100 != 0;
        boolean divisibleBy400 = year % 400 == 0;
        leapYear = (divisbleBy4 && notDivisbleBy100) || divisibleBy400;

        System.out.println("该年份是否是闰年：" + leapYear);
    }
}
```

# 自取资料

## 扩展阅读推荐

1. Java 基本数据类型 | 菜鸟教程 - https://www.runoob.com/java/java-basic-datatypes.html
1. 浮点类型精度深度讨论：
    1. 为什么 0.1 + 0.2 = 0.3？ - https://draveness.me/whys-the-design-decimal-and-rational/
    1. 为什么 0.1 + 0.2 = 0.300000004？ - https://draveness.me/whys-the-design-floating-point-arithmetic/#fn:2
1. IEEE754标准: 浮点数在内存中的存储方式 - https://www.jianshu.com/p/8ee02e9bb57d
1. 为什么Java中char类型不能完整表示一个字符？ - https://fookwood.com/java-string-charset-char

## 推荐书籍

#### Java 核心技术·卷 I(原书第 11 版)

![](https://imgkr.cn-bj.ufileos.com/1ed33e34-7d6d-4d05-8849-9f60a278726c.png)

**推荐理由：** 这本书在知识体系完整充实的同时，又比《Thinking in Java》暴风式的知识洗礼来得轻松，新人入门书籍强烈推荐！

#### 码出高效：Java开发手册

![](https://imgkr.cn-bj.ufileos.com/87b25b84-8ecc-4196-980b-3f73811c48e9.png)

**推荐理由：** 阿里系出品。从最基础的计算机基础入手，到 Java 的方方面面，加上精美的配图和通俗易懂的解释，是非常适合新手阅读的一本儿关于 Java 的技术书籍。

# 参考资料

1. Introduction to Computer Science using Java - http://programmedlessons.org/Java9/index.html#part02
1. Java 运算符 | 菜鸟教程 - https://www.runoob.com/java/java-operators.html
1. 《Java 核心技术 卷I》(第11版)

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://imgkr.cn-bj.ufileos.com/ace97ed9-3cfd-425f-85e5-c1a1e5ca7d3f.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！