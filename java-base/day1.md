
![](https://static01.imgkr.com/temp/f12d252974d748ac884fcb0849d31997.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# Part 0. 搭建好开发环境

![](https://static01.imgkr.com/temp/0bcd0dd4ff404621a93aa1a09f34a1cc.png)

- 图片来源：https://developer.51cto.com/art/201907/599480.htm

在一切开始之前，我们需要先搭建好我们的 **开发环境** *(从前文得知 Java 程序的运行需要 JVM，编写 Java 代码需要 IDE)*，或者在您完全准备好之前可以 *暂时使用* 在线版本的 Java 环境来运行前面一些内容涉及的简单代码：[https://c.runoob.com/compile/10](https://c.runoob.com/compile/10)

## 安装 JDK 并配置环境

### JVM、JRE 和 JDK 有什么关系？

![](https://imgkr.cn-bj.ufileos.com/928ea146-1d16-4046-a70b-18a8d346d0be.png)

- **JVM（Java Virtual Machine，Java 虚拟机）**：是整个 Java 实现跨平台的最核心的部分，能够运行以 Java 语言写作的软件程序。
- **JRE（Java Runtime Environment，Java 运行环境）**：是运行 JAVA 程序所必须的环境的集合，包含 JVM 标准实现及 Java 核心类库。
- **JDK（Java Development Kid，Java 开发开源工具包）**：是针对 Java 开发人员的产品，是整个 Java 的核心，包括了 Java 运行环境 JRE、Java 工具和 Java 基础类库。

这三者的关系是一层层的嵌套关系：JDK > JRE > JVM

> JDK 包含了 Java 的编译器、调试器等一系列开发工具，所以作为开发人员我们需要安装 JDK，而某一些只需要运行编译好的 Java 程序的服务器则可以只安装 JRE 即可 *（极少数情况，通常还是安装 JDK）*。

### 下载安装 JDK

Java 程序必须运行在 JVM 之上，所以我们第一件事情就是安装 JDK。我们选择最新的 **JDK 14** 进行安装：

![官网地址：https://www.oracle.com/java/technologies/javase-downloads.html](https://imgkr.cn-bj.ufileos.com/f6b0107a-4cb9-4016-9691-cc2f0cfd4286.png)

选择合适自己电脑平台的 JDK 进行下载安装即可：

![](https://imgkr.cn-bj.ufileos.com/09182cdd-60f8-44e6-89ce-2c1820f70633.png)

### 配置环境

#### Windows 平台

> **第一步**
>
> 在 Windows 安装之后需要额外 **配置环境变量**，首先【右键我的电脑】 → 选择【属性(R)】 → 打开【高级系统设置】：
>
> ![](https://imgkr.cn-bj.ufileos.com/d7f837db-f927-4fea-b406-6129d9c1e84f.png)
>
> - 图片引用自：https://www.jikeyuan.cn/index.php/a/174.html

> **第二步**
>
> 在【高级】标签下选择【环境变量】，并对环境变量【path】进行编辑操作：
>
> ![](https://imgkr.cn-bj.ufileos.com/623953b4-ec6b-4174-840b-4f4db5d2bf7d.png)
>
> - 图片引用自：https://www.jikeyuan.cn/index.php/a/174.html

> **第三步**
>
> 新建环境变量，然后把刚才安装 jdk 的安装路径复制进去，路径截止到 `bin` 目录：
>
> ![](https://imgkr.cn-bj.ufileos.com/3ffac5a3-5fba-4ff3-98ef-6b68f7b10619.png)
> 
> - 图片引用自：https://www.jikeyuan.cn/index.php/a/174.html

> **第四步**
>
> 快捷键【Win + R】输入【cmd】调出 dos 窗口，输入【java -version】进行验证：
> 
> ![](https://imgkr.cn-bj.ufileos.com/5cd103bf-4703-4799-be21-a642f768e4ec.png)
>
> - 图片引用自：https://www.jikeyuan.cn/index.php/a/174.html

#### Mac 平台

> **第一步**
>
> 打开苹果 dos 窗口，**先确认自己使用的 `shell` 是 `zsh` 还是 `bash`**，在命令行中输入 `echo $SHELL`：
> - 如果输出 `/bin/bash` 则为 `bash`;
> - 如果输出结果为 `/bin/zsh` 则为 `zsh`。

> **第二步**
>
> 根据上面不同的结果 **修改 `shell` 配置文件**，若为 `bash`，则打开 `~/.bash_profile`，若为 `zsh` 则打开 `~/.zshrc`，在响应的文件末尾添加以下内容，并保存：
>
> `export JAVA_HOME=$(/usr/libexec/java_home)`

> **第三步**
>
> 在 `~/` 目录，命令行执行 `source` 命令：同样如果是 `bash`，则执行 `source .bash_profile`，而如果是 `zsh`，则执行 `source .zshrc`，**让刚才的修改生效**。

> **第四步**
>
> 命令行执行 `java -version` 检查是否配置成功：
>
> ![](https://imgkr.cn-bj.ufileos.com/13e1a538-400b-4ba4-a198-e092f8c5ec8a.png)

### Java 的不同版本

随着 Java 的发展，SUN 公司给 Java 分出了三个不同版本：

![](https://static01.imgkr.com/temp/08e9b837fd6945b1bff7ea0aee17ce6e.png)

- **Java SE(Standard Edition)**：标准版，包含标准的 JVM 和标准库；
- **Java EE(Enterprise Edition)**：企业版，在 SE 的基础上加了大量的 API 和库，以方便开发 Web 应用、数据库、消息服务等；
- **Java ME(Micro Edition)**：是针对嵌入式设备 "瘦身" 之后的 Java SE；

> 毫无疑问，Java SE 是 Java 平台的核心，而 Java EE 是进一步学习 Web 应用所必须的。

## 安装 IDEA 开发工具

### 体验记事本编写运行 Java 程序

**Java 源代码本质上其实就是普通的文本文件**，所以理论上来说任何可以编辑文本文件的编辑器都可以作为我们的 Java 代码编辑工具。比如：**Windows** 记事本、**Mac OS X** 下的文本编辑、**Linux** 下的 vi 等。

但是这些简单工具没有「语法的高亮提示」、「自动完成」等功能，这些功能的缺失会 **大大降低代码的编写效率**。

#### 第一步：新建一个 HelloWorld.java 文件

然后输入以下内容并保存：

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
```

- **注意**： `HelloWorld.java` 文件名不允许出现空格，以及要保证和第一行 `class` 后跟着的字符串 `HelloWorld` 保持一致；

#### 第二步：编译和运行

**将 Java 源程序编写并保存到文件之后，还需要进行编译才能运行**。编译 Java 源程序使用的是 JDK 中的 `javac` 命令，例如编译上节的 HelloWorld.java，完整命令如下：

```console
javac HelloWorld.java
```

该命令会让 Java 编译器读取 JavaWorld.java 文件的源代码，并把它编译成符合 Java 虚拟机规范的字节码文件 *(`.class` 文件)*。

![HelloWorld 源代码和字节码文件](https://imgkr.cn-bj.ufileos.com/e1710042-ad75-45de-bd5d-607f7ca2a9fd.png)

想要运行字节码文件也需要使用 JDK 中的 `java` 命令，例如运行上面生成的 `.class` 文件，完整命令如下：

```console
java HelloWorld
```

具体效果如下：

![](https://imgkr.cn-bj.ufileos.com/b565f7c3-bf5a-43b9-b57f-98e9a82cf843.png)

### 更加智能的 IDEA

![官网：https://www.jetbrains.com/idea/](https://imgkr.cn-bj.ufileos.com/7b0ae3a5-b784-42aa-9b69-9c5f95ae81f2.png)

尽管能够使用文本编辑器来编写我们的 Java 程序，但效率着实不敢恭维，所以我们一般使用更加先进的 **集成开发工具** *（俗称 IDE，Integrated Development Environment）*。

不仅仅包含更加智能的代码编辑器、编译器、调试器，还有友好的用户界面以及各式各样帮助我们提升效率的插件。

对于效率这方面，下面我们就几个方面简单感受一下。

#### 更友好的代码提示功能

不仅仅是基础的关键字的提醒，IDEA 会基于当前的上下文 *(也就是基于位于当前代码上下的代码进行分析)*，更加智能的进行过滤和提醒：

![](https://imgkr.cn-bj.ufileos.com/5faa22c9-33c8-415f-a8c1-ec4a6c5d9c6e.png)


#### 强大的纠错能力

我们总是会犯一些低级错误，比如一不留神打错一个字母，可能找了好久都找不到错误所在，IDEA 的纠错能力也许可以帮到你，看一个例子：

![简单演示，实际 IDEA 更加强大](https://imgkr.cn-bj.ufileos.com/ff7f84df-f8f1-4933-85a0-04445ebf3bc3.png)

#### 智能提示重构代码

如果你写的代码过于复杂，或者有更好的方式来替代你写的代码，那么 IDEA 会给你一个提示，告诉你还可以有更好的方式。如下图：

![IDEA 提示我有更好的遍历数组的方法](https://imgkr.cn-bj.ufileos.com/dbed0a29-9853-4130-8f62-10bf4454c4f2.gif)

#### 一些酷炫的操作

比如你看我从头写一个 `HelloWorld` 程序：

![](https://imgkr.cn-bj.ufileos.com/950966fc-97c8-4fa7-816d-3295ab740451.gif)

这应该比一个一个字符敲快多了吧...*(小声bb：文章末尾有教程哦)*

#### And More...

来一个总结：

![](https://imgkr.cn-bj.ufileos.com/013de239-dad3-4ad1-9edd-9a71c18e5a18.png)

## 验证环境是否安装成功

打开【IDEA】新建一个空白的 Java 项目：

![一路 Next 就好了](https://static01.imgkr.com/temp/f9119774a94b45f795d6ae7887ee5812.png)

右键在【scr】目录新建一个空白的【HelloIDEA】的 Java Class 文件：

![](https://imgkr.cn-bj.ufileos.com/fd18f90c-e9d3-4b37-97d5-bbd173fcaadd.png)

然后接下来像我这样操作，来编写一个【HelloIDEA】的 Java 程序：

![IDEA 弹出的提示没录到...](https://static01.imgkr.com/temp/81b6c073530548d59b0c61a1101658a8.gif)

- `psvm`：是 `public static void main` 的缩写；
- `sout`：是 `System.out` 的缩写；
- 您在 IDEA 中键入以上单词时会由 IDEA 提示智能快速地完成输入，以此来提升效率；

怕有些同学迷惑，点击左边的绿三角会弹出如下的信息，点击第一个选项就能够运行啦：

![](https://static01.imgkr.com/temp/4870ccfa48314500a04370708aa9a68f.png)

# Part 1. 标识符和保留字

![](https://static01.imgkr.com/temp/22bf37b4df254ffa91efd731ba49a39e.png)

- 图片来源：https://medium.com/@thiagonascimento/time-to-first-hello-world-11a4735602f2

```java
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

我们来看看刚才我们写的 `Hello World` 程序。

第一行：`public class HelloWorld {`

- **关键字 `public`**：称为 **访问修饰符** *(access modifier)*，用来控制程序的其他部分对这一段代码的访问级别 *(这里暂时理解为公用的，之后会更加详细地介绍)*；
- **关键字 `class`**：表明 Java 程序中的全部内容都包含在类中 *(之后会花很多功夫来说明类，这里可以仅仅把类理解为程序逻辑的一个容器，程序逻辑定义了应用软件的行为)*。
- **`HelloWorld`**：关键字 `class` 后面紧跟的是类名，它除了必须跟文件名保持一致外，还应该遵循 **Java 类名的命名规范 —— 类名以大写字母开头。如果名字由多个单词组成，每个单词的第一个字母需要大写**。
  - 例如：FirstSample 就遵循了 Java 类的命名规范的原则；
  - 这样的 **命名方式** 被称为 **驼峰命名法** *(camel case)*，首字母是大写的则称为 **大驼峰命名法**，首字母小写的则称为 **小驼峰命名法** *(如：firstSample，后续文章会提到的变量就采用这种方式)*
  
> **类名命名规范：**
>
> - **Bad**：`hello`、`Good123`、`Note_Book`、`_World`
> - **Good**：`Hello`、`Tester`、`NoteBook`

## 标识符

在编程中，某个东西的名称就被称为 **标识符**，例如上述的类名 `HelloWorld`。在 Java 中定义标识符存在以下几种规则：

1. 只能由数字、字母、下划线(_)和美元符号($)组成；
2. 第一个字符不能是数字；
3. 标识符内不允许有空格；
4. **不能使用 Java 保留字** *(下方有列出 Java 中存在的保留字)*；

> **标识符命名示范：**
>
> - **Bad**：`Lady Luck` *(坏：标识符内不允许有空格)*、`x/y` *(错误：标识符中不允许使用斜杠)*、`1stPrize` *(错误：以数字开头)*、`abc` *(坏：没有任何意义)*、`_name` *(坏：不要以 `_` 或 `$` 开头)*
> - **Good**：`userName`、`StudentManager`、`NoteBook`

## 保留字

保留字就是像 `class` 这样有特殊含义的标识符，您只能将保留字用于其设定的专属用途 *(如 `class` 除了定义类，您将不能用作其他任何目的)*。

在 Java 中存在以下的保留字：*(无需记忆，它们中的大部分都或多或少地出现在之后您的编码中.. IDEA 也会有智能的提示)*

```text
abstract class    extends implements null      strictfp     true
assert   const    false   import     package   super        try
boolean  continue final   instanceof private   switch       void
break    default  finally int        protected synchronized volatile
byte     do       float   interface  public    this         while
case     double   for     long       return    throw
catch    else     goto    native     short     throws
char     enum     if      new        static    transient
```

# Part 2. 程序的基本结构和方法简述

![](https://imgkr.cn-bj.ufileos.com/503c0f98-1dfd-4c7d-b129-c2f43f77622a.png)

## 程序基本结构

在类的第一个大括号和最后一个大括号之间描述了程序所做的一切：

```java
public class ClassName {
}
```

首先，每个源代码文件必须有一个主类 *(名字同文件名)*，在之后的文章中我们会看到一个源代码文件可能会同时存在几个类的情况发生，暂时不考虑。

在上文程序的第三行：`public static void main ( String[] args )`，展示了程序在何处开始运行，Java 虚拟机总是从指定类的 `main` 方法的代码开始执行，因此为了代码能够执行，在类的原文件中必须包含一个 `main` 方法。

对这一行稍微做一下解释：

- `public`：访问修饰符，用来描述该方法的访问权限级别，这里为所有人都能访问；
- `static`：保留字，用来定义该方法是一个静态方法；
- `void`：用来描述该方法没有任何的返回值；
- `main`：方法名；
- `(String[] args)`：描述了该方法所接收的参数；

所以刚开始接触的程序结构大概看起来像是下面这样：

```java
public class ClassName {
    public static void main(String[] args) {
        ......
    }
}
```

由于 **Java 是大小写敏感** 的，所以 `main` 这个单词不允许任何的修改。*(Java 语言规范中规定 main 方法必须声明为 public)*

> 大括号的使用风格曾经引发过许多无意义的争论，以下两种风格，哪一种更好呢？：
>
> ```java
> public class ClassName {
> }
> ```
>
> ```java
> public class ClassName 
> {
> }
> ```
>
> 没有答案，虽然对于 Java 编译器来说空白符会被省略，这两种并无差别，但作为开发者的我们，选择 Java 惯用的风格 *(第一种)* 就好了...

## println()

考虑下面这一段代码，实际上就是上面的 `HelloWorld` 程序 `main` 方法中的语句：

```java
{
    System.out.println("Hello World！");
}
```

一对大括号表示 **方法体** 的开始与结束，在这个方法中只包含一条语句。跟大多数程序设计语言一样，可以把 Java 中的语句看成是语言中的一个句子。

**在 Java 中，每个句子必须用分号结束** *(英文分号)*。

**特别需要说明，回车并不是语句的结束标志**，因此，如果需要可以将一条语句写在多行上。

在这里，我们使用了 `System.out` 这个对象并调用了它的 `println` 方法 *(点号 `.` 用于调用方法)*。它的作用是在屏幕上输出指定的信息。例如，我们想要输出 `我没有三颗心脏` 则可以这样调用：

```java
System.out.println("我没有三颗心脏");
```

Java 使用方法的通用语法是：

```java
object.method(parameters)
```

这一条语句的作用是在屏幕上输出 `Hello World!`，这一部分由字符组成的序列 *(其中不应该包含引号)* 被称为 **字符串**。它可以包含任何有效字符，包括空格和标点符号。


## 方法简述

**方法** 是根据语句构建的。程序通常包含许多方法。 我们的示例程序仅包含一种方法 *(main 方法)*。当然我们还调用了系统帮我们写好的 `System.out.println()` 方法。

### 尝试定义自己的方法

我们可以试着仿照 `main` 方法来写一个自己的方法，假设我们想要输出一段心理学三大巨头之一阿德勒的一段话：

```java
public static void printAdlerOneQuote() {
    System.out.println(
        "太在意别人的视线和评价，才会不断寻求别人的认可。"
        + "对认可的追求，才扼杀了自由。"
        + "由于不想被任何人讨厌，才选择了不自由的生活方式。"
        + "换言之，自由就是不再寻求认可。"
    );
}
```

有几点需要说明：

- **方法名采用小驼峰命名法，且尽量保证有意义的命名**：如果这里把方法名修改成 `abc` 似乎就有点儿不知所云了；
- **一行代码量不要超过可视距离**：如果全部冗余在一行，不仅看这一段代码会花额外的精力去翻看和理解，也会给其他阅读代码的人造成困扰；
- **建议：一个方法只做一件事情**。您可以看到这个方法除了像方法名描述的那样打印一句阿德勒的名言之外，没有做其他任何的操作，这样做能大大增加代码的可阅读性；

### 在 main 方法中调用

正常的方法调用需要像上面提到的那样：`object.method(parameters)`，但由于身处同一个类中，`this.printAdlerOneQuote();` 就可以简写成：`printAdlerOneQuote();`。

完整的程序代码如下：

```java
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Hello World！");
        printAdlerOneQuote();
    }

    public static void printAdlerOneQuote() {
        System.out.println(
            "太在意别人的视线和评价，才会不断寻求别人的认可。"
                + "对认可的追求，才扼杀了自由。"
                + "由于不想被任何人讨厌，才选择了不自由的生活方式。"
                + "换言之，自由就是不再寻求认可。"
        );
    }
}
```

### 练习：尝试输出自己喜欢的一段话在屏幕中

参考答案：*(上面的完整代码演示)*

# Part 3. 语法错误和 Bug

![](https://imgkr.cn-bj.ufileos.com/4dfed8d8-a6bc-4118-9928-7b6bd6071581.png)

- 图片来源：https://blog.csdn.net/csdnnews/article/details/86684475

## 简述语法错误

在源文件中，字符串文字必须仅在一行上。以下内容不合法，将无法编译：

```java
System.out.println("Hello "
    "World！");
```

您编写的代码不符合 Java 的语法规定，就会发生 **语法错误**。

在 Java 编译器将源代码文件编译成 `.class` 文件之前，会默认帮你做许多工作，检查语法就是其中一项。

例如，我们在桌面上试着创建一个【HelloWorld.java】文件然后输入一段存在错误的代码 *(您可以试着检查一下哪里出现了一处错误)*：

```java
public Class HelloWorld {
    
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
```

然后再当前目录执行 `javac HelloWorld.java` 尝试编译这个存在错误的 Java 源文件：

```console
→ javac HelloWorld.java
HelloWorld.java:1: 错误: 需要class, interface或enum
public Class HelloWorld {
       ^
HelloWorld.java:3: 错误: 需要class, interface或enum
    public static void main(String[] args) {
                  ^
HelloWorld.java:6: 错误: 需要class, interface或enum
    }
    ^
3 个错误
```

Java 编译器提示我们有三处错误，实际上我们也确实粗心地把 `class` 错误的写成了 `Class`。

编译器也未创建新的字节码文件 *(`.class`)*，因为在遇到错误时它将停止翻译。

## Bug 简述

**仅仅因为程序通过编译并且成功运行并不意味着它是正确的。**

例如，您的任务是创建一个在屏幕上输出 `Hello Wrold!`，但是您却错误地写成了 `Hello Bug!`，**运行时没有按照预期执行，则就称该程序存在 Bug！**

### Bug 起源

**Bug 一词的原意是 “臭虫” 或 “虫子”。**

第一个有记载的 Bug 是美国海军编程员、编译器的发明者格蕾斯·哈珀 *（GraceHopper）* 发现的。

`1945` 年 `9` 月 `9` 日，下午三点。哈珀中尉正领着她的小组构造一个称为“马克二型”的计算机。这还不是一个真正的电子计算机，它使用了大量的继电器，一种电子机械装置。第二次世界大战还没有结束。哈珀的小组日以继夜地工作。机房是一间第一次世界大战时建造的老建筑。那是一个炎热的夏天，房间没有空调，所有窗户都敞开散热。

突然，马克二型死机了。技术人员试了很多办法，最后定位到第 `70` 号继电器出错。哈珀观察这个出错的继电器，发现一只飞蛾躺在中间，已经被继电器打死。她小心地用摄子将蛾子夹出来，用透明胶布帖到「事件记录本」中，并注明「第一个发现虫子的实例」。

![](https://static01.imgkr.com/temp/9abe7549114c48f286d625eb0ea0eff3.png)

**从此以后，人们将计算机错误称为 Bug，与之相对应，人们将发现 Bug 并加以纠正的过程叫做 “Debug”，意即「捉虫子」或「杀虫子」。**

> - 以上内容引用自：http://www.cxyym.com/2014/11/999/

### Bug 是怎么产生的？

来几个清奇的段子吧。

#### 段子一：庞博脱口秀大会解释 Bug

![](https://static01.imgkr.com/temp/33439f14d5ed4ceeba7105f91154bff9.png)

B 站自取：[https://www.bilibili.com/video/BV1oJ411S7o4?from=search&seid=7940414495637079568](https://www.bilibili.com/video/BV1oJ411S7o4?from=search&seid=7940414495637079568)

#### 段子二：测试工程师们来到一家酒吧

![](https://static01.imgkr.com/temp/4edc97f17c0240c6a3a3b97684e8623d.png)

> 引用自知乎`@第七地区`的答案：https://www.zhihu.com/question/365343579/answer/967299388

#### 段子三：领导让我修房子

![](https://imgkr.cn-bj.ufileos.com/6d3876e0-777b-414f-9aa6-8f5e8fcf652b.png)

> 引用自知乎`@哒柏`的答案：https://www.zhihu.com/question/365343579/answer/967299388

### 地球人和程序员眼中改 Bug 的不同

修改程序中的 Bug，要经过三个步骤：

1. 找到它；
2. 想办法解决它；
3. 确认它已经被解决 *（并且没有引入其它问题）*；

说起来比较简单，在地球人 *（程序员等同于外星人）* 看起来，过程是这样的：

![](https://static01.imgkr.com/temp/9ece707f8bb34846ba7ad3d9a9d52df2.png)

但是，对于程序员来说，找一个 Bug 往往是这样的：

![](https://imgkr.cn-bj.ufileos.com/5d8ef627-c8fc-4fbe-ae5b-864e71100fe9.png)

找到之后呢，解决这个 Bug 又是一个难题：

![](https://static01.imgkr.com/temp/a417055f4b2441ccbc01ad5fee3ba373.png)

换个柱子什么的比较简单，还有更崩溃的！

![](https://imgkr.cn-bj.ufileos.com/99195cb3-7822-499a-8de6-410b1fb07436.png)

> - 以上内容引用自：http://www.accessoft.com/blog/article-show.asp?userid=11&Id=17629

# Part 4. 注释

![](https://imgkr.cn-bj.ufileos.com/81170560-c507-48e5-bb34-ebf10566def5.png)

- 图片来源：http://www.cocoachina.com/articles/20146

**注释** 是写程序的人留下的阅读笔记。

通常注释以两个字符 `//` 开头。Java 编译器将忽略那些字符以及在该行之后的所有字符。例如：

```java
public class HelloWorld {

    // 程序入口
    public static void main(String[] args) {
        // 输出 Hello World!
        System.out.println("Hello World!");
    }
}
```

这一段代码跟我们最开始的 `HelloWrold` 程序完全相同。大多数的程序编辑器 *(例如 IDEA)* 都足够聪明，可以识别注释并将其显示为无关紧要的一些颜色：

![](https://imgkr.cn-bj.ufileos.com/9e2907ff-09c4-4432-9500-8d9afd686434.png)

与大多数程序设计语言一样，Java 中的注释也不会出现在可执行程序中。因此，可以在源程序中根据需要添加任意多的注释，而不必担心可执行代码会膨胀。 

## 三种注释的方式

```java
/**
 * 文档注释
 * 可以注释多条内容
 */
public static void main(String[] args) {
    // 这是单行注释
    System.out.println("演示三种注释方式");
    /*  
    这是多行注释
     */
}
```

- **单行注释**：以 `//` 标识，只能注释一行内容；
- **多行注释**：包含在 `/*` 和 `*/` 之间，能注释多行内容，为了提高可阅读性，一般首行和尾行不写注释信息；
- **文档注释**：包含在 `/**` *(两个 `*`)* 和 `*/` 之间，也能注释多行内容，一般用在类、方法和变量上面，用来描述其作用 *(这是 Java 的一种规范，之后会更多的见识到)*；

## 几点建议

> **注释的目的是：** 尽量帮助读者了解得和作者一样多。 —— 《编写可读代码的艺术》

以下节选自《阿里巴巴 Java 开发手册》对于注释的几点要求：

1. **【强制】** 类、雷属性、类方法的注释必须使用 Javadoc 规范，使用 `/**内容*/` 格式，不得使用 `//` 方式 *(上面的演示程序中就不符合规范，需要注意)*；
1. **【强制】** 方法内部的单行注释，在被注释语句上方另起一行，使用 `//` 注释。方法内部的多行注释，使用 `/*  */` 并注意与代码对齐；
1. **【推荐】** 与其用「半吊子」英文来注释，不如用中文注释把问题说清楚。专有名词与关键字保持英文原文即可；
1. **【推荐】** 在修改代码的同时，要对注释进行相应的修改；
1. **【参考】** 对于注释的要求：
    1. 能够准确反映设计思想和代码逻辑；
    2. 能够描述业务含义，使其他程序员能够迅速了解代码背后的信息。完全没有注释的大段代码对于阅读者如同天书；注释是给自己看的，应做到即使间隔很长时间，也能清晰理解当时的思路；注释也是给继任者看得，使其能够快速接替自己的工作；
1. **【参考】** 好的命名、代码结构是自解释的，注释力求精简准确、表达到位。避免出现注释的一个极端：过多过滥的注释。因为代码的逻辑一旦修改，修改注释也是相当大的负担；

对于以上的一些建议，我相信在您之后的编程之路上会越发地体会深刻。

# 要点回顾

1. JVM、JRE、JDK 的说明和联系；
1. Java 开发环境的搭建方法；
1. 标识符和保留字的定义以及标识符的命名规范；
1. 程序的基本结构和方法定义和调用的简单方法；
1. 语法错误和 Bug *(起源、怎么产生的)*；
1. 注释的定义、三种注释的方式、注释的规范；

# 练习

## 练习 1：尝试输出自己喜欢的一段话在屏幕中

参考答案：*(上面有完整代码演示)*

## 练习 2：尝试把上方输出的内容单独实现为自己的方法并在 main 方法中调用运行

参考答案：*(上面有完整代码演示)*

## 练习 3：给自己的代码添加上注释并让朋友阅读询问是否清晰必要

参考答案：*(上面有完整代码演示)*

# 自取资料

## 相关扩展阅读资料

1. Java 教程 | 廖雪峰官方网站 - https://www.liaoxuefeng.com/wiki/1252599548343744
1. 史上最简单的 Intellij IDEA 教程 - https://github.com/judasn/IntelliJ-IDEA-Tutorial
1. 计算机发展史上十大著名软件缺陷 - https://zhuanlan.zhihu.com/p/31167236

## 推荐书籍

#### Java 核心技术·卷 I(原书第 11 版)

![](https://imgkr.cn-bj.ufileos.com/22d0f42b-de85-4757-8f11-9d1df2c7931d.png)

**推荐理由：** 这本书在知识体系完整充实的同时，又比《Thinking in Java》暴风式的知识洗礼来得轻松，新人入门书籍强烈推荐！

#### 可读代码的艺术

![](https://static01.imgkr.com/temp/b334061866d2427db3573cce77d46459.png)

**推荐理由：** 编写可阅读的代码是程序员从始至终需要提升的能力，这本书完整呈体系的结构，和朴实充实的实例，让读者通过阅读就能在实践中真实地运用起来，推荐！

# 参考资料

1. 《Thinking in Java》第四版
1. 《Java 核心技术 卷 I》第11版
1. 廖雪峰系列 Java 教程 - https://www.liaoxuefeng.com/wiki/1252599548343744
1. 历史上的第一个计算机Bug - http://www.cxyym.com/2014/11/999/

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://imgkr.cn-bj.ufileos.com/ace97ed9-3cfd-425f-85e5-c1a1e5ca7d3f.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！


