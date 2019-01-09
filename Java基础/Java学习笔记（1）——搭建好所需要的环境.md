> 前言：后来仔细思考了一下，从零开始学习Java的系列标题略长（实际改过来的也不短），并且不能正确反映写文的目的，所以决定从这一篇开始改为Java学习笔记。之前的一篇文章在一觉醒来以后也觉得有些不太好还有一些需要添加的地方，所以后来这一个系列是时刻更新的东西，用笔记来命名再好不过了。

# 搭建好我们需要的环境

在搭建环境之前，我们需要先来了解以下下面的这些名词：

| 术语名        | 缩写        | 解释|
| :-- |:--|:--|
| Java Development Kit     | JDK | 编写Java程序的从程序员使用的软件 |
| Java Runtime Environment      | JRE|   运行Java程序的用户使用的软件 |
| Standard Edition | SE |   用于桌面或简单的服务器应用的Java平台 |
| Enterprise Edition | EE | 用于复杂的服务器应用的Java平台 |
| Micro Edition | ME | 用于手机和其他小型设备的Java平台 |
| Java 2 | J2 | 一个过时的术语，用于描述1998年~2006年之间的Java版本 |
| Software Development Kit | SDK | 一个过时的术语，用于描述1998年~2006年之间的JDK |
| Update | u | Oracle的术语，用于发布修改的bug |
| NetBeans | --- | Oracle的集成开发环境 |

安装Java除了相关的集成开发环境IDE(Integrated Development Environment )，还需要下载好能支撑Java运行的JDK。这里有一个有趣的故事是：支撑Java运行的这么一个文件，这么一个系统，恰恰呢就是Java语言本身编写的。
所以先下好JDK，官网这里：http://www.oracle.com/technetwork/java/javase/downloads/index.html

进去以后点击JDK进入，然后点击Accept License Agreement，然后根据自己的系统下载不同的JDK就可以。

![下载JDK](http://upload-images.jianshu.io/upload_images/7896890-8c7819af5cc660f9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 关于Eclipse还是IDEA

我个人还是比较推荐IDEA吧，从安装软件的大小上面就能看出明显的差别...

![IDEA和Eclipse的安装包大小比较](http://upload-images.jianshu.io/upload_images/7896890-6b3aff09d1902553.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

但也不是那么绝对，IDEA据说是一把双刃剑，我现在没有明显得感受过，但是刚打开这个软件的时候，就略微有一点卡，是因为IDEA本身的检错差错功能十分强大，几乎是实时查错，反正我是一用就爱上了，放上几张图你们感受下：

![图1](http://upload-images.jianshu.io/upload_images/4047674-a05896506ce7f619?imageMogr2/auto-orient/strip)

![图2](http://upload-images.jianshu.io/upload_images/4047674-036e4e881f11c8a9?imageMogr2/auto-orient/strip)

关于IDEA的弊端其实自己想也能想得到，当项目达到一定程度的时候，它自身的强大的纠错功能，会让系统变得卡顿起来，还有就是它太强大了，开过车的人都不会像要走路，大概就是一样。
具体的优点在下面列出，这里推荐两篇[CleverFan](http://www.jianshu.com/u/8dc5811b228f)的文章：
 - [IDEA入门级教程——你怎么还在用Eclipse？](http://www.jianshu.com/p/0e2bf6a1efda)
 - [IDEA(jetbrain通用)优雅级使用教程](http://www.jianshu.com/p/3160ff832a9b)

关于IDEA的安装教程网上一搜一大堆..这里不再赘述..

## IDEA的简单介绍及设置

IDEA的社区版是免费的，我还一直在想着该怎么破解...毕竟这小几百美刀一年的价格对于我这样的平民窟铁头娃来说，实在不太友好，结果用了半天...半点提示没有...

![免费的IDEA](http://upload-images.jianshu.io/upload_images/7896890-70ab9c8984c31e1e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 据说IDEA的使用量已经超过Eclipse很久了，那么什么让IDEA这么受欢迎呢？下面摘了一些上面外链文章的精华还有一些来自于百度的精华，来说说最智能的IDE：IDEA。

### 智能提示重构代码

如果你写的代码过于复杂，或者有更好的方式来替代你写的代码，那么IDEA会给你一个提示，告诉你还可以有更好的方式。如下图：

![智能提示重构代码](http://upload-images.jianshu.io/upload_images/4047674-c29af62c10285f92?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们学java的时候学过增强的for循环，其实它的学名叫做foreach语句，上面的代码我使用了普通的for循环，IDEA告诉我，使用foreach语句更好。

### 更友好的代码提示功能

使用eclipse的都应该清楚，如果你想要输入StringBuffer，那么你必须得按着顺序输入，直接输sb是不行的，但是在IDEA里你可以这样输入。

![代码提示功能](http://upload-images.jianshu.io/upload_images/4047674-de086f83d1d6689a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

不仅如此，看下一个例子

![代码提示功能](http://upload-images.jianshu.io/upload_images/4047674-d7a11cd502368d21?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

还可以这样提示。这些只是一些很简单的例子，但是已经足够强大了。

### 什么叫智能？

上面的其实都是很基础的功能，IDEA正真智能的 地方在于它会不断的分析你的代码，并且智能的进行反馈。我们 再看一个简单的例子。

![智能分析你的代码](http://upload-images.jianshu.io/upload_images/4047674-f59dd5088f822622?imageMogr2/auto-orient/strip)

这是一个普通的structs程序。在配置文件里定义了一个action并设置了两种不同的返回值。打开我们的action，我们可以看到，你可以直接从代码的左侧找到跳转到对应配置文件的快捷按钮。如果你的某一个方法是覆盖了父类方法，那么你也可以直接查看父类方法。更人性化的是，IDEA可以分析出你的action方法可以跳转到哪些界面？你是不是也有点心动了呢？

### 强大的纠错能力

我们总是会犯一些低级错误，比如一不留神打错一个字母，可能找了好久都找不到错误所在，IDEA的纠错能力也许可以帮到你，再看一个例子。

![纠错功能](http://upload-images.jianshu.io/upload_images/4047674-e0b69aae977c345d?imageMogr2/auto-orient/strip)

我们只创建了两个jsp，当你的返回值中出现了你没创建的文件时，IDEA会提示错误，这样就可以避免你因为写错单词而造成的错误。

> 以上内容均转自上文外链第一篇文章。原文作者：CleverFan

## 配置IDEA

> 我只讲一些非常实用的配置，还有一些常用的快捷键。另外我在安装过程中遇到有搜狗输入法卡在IDEA界面的情况，升级搜狗输入法8.6之后完美解决。如果一开始的字体太小，你可以直接按住Ctrl滚动鼠标的滚轮来调节字体大小哦。

你可以在File菜单下找到Setting，或者直接按下快捷键【Ctrl+Alt+S】

![设置界面](http://upload-images.jianshu.io/upload_images/7896890-af879c70ac837665.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

学习编程一定养成习惯不要去装什么中文包，强行让自己习惯英文的界面，我有直观的感受是，现在看这些个菜单或者去Java官方查一些API函数都比较得心应手。

### 设置自动导入包

![设置自动导入包](http://upload-images.jianshu.io/upload_images/4047674-e09fee78cd6cccc4?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如上图标注 1 和 2 所示，默认 IntelliJ IDEA 是没有开启自动 import 包的功能。
- 勾选标注 1 选项，IntelliJ IDEA 将在我们书写代码的时候自动帮我们优化导入的包，比如自动去掉一些没有用到的包。
- 勾选标注 2 选项，IntelliJ IDEA 将在我们书写代码的时候自动帮我们导入需要用到的包。但是对于那些同名的包，还是需要手动Alt + Enter 进行导入的，IntelliJ IDEA 目前还无法智能到替我们做判断。

### 实时代码模板（Live Templates）

看以下的图：

![实时代码模板](http://upload-images.jianshu.io/upload_images/7896890-757fc7e0dd3a2be6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

当我们输入psvm按下Enter会自动创建man函数（事实上也可以通过输入main然后按下【Ctrl+J】智能提示功能来创建），然后输入sout按下Enter就会自动创建好System.out.println("");这么一句。同时这么赞的功能还允许用户自己定义自己的模板，具体的设置在这里：

![实时代码模板的功能](http://upload-images.jianshu.io/upload_images/4047674-49a89dd512025844?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这里创建了自己的一个模板，叫做syso，代码在下面，感兴趣的自己去研究下：

> System.out.println("$val$的值是：---"+ $val$ + "，当前方法=$CLASS_NAME$.$METHOD_NAME$()");$END$
>- \$VAR1\$、\$CLASS_NAME\$、\$METHOD_NAME\$ 都为自己定义的变量名。设置变量名只要用两个 $ 包住即可。
>- 每个变量在代码输出的时候都是一次光标位置，光标跳动顺序从左到右，每次跳动按 Enter。
>- \$END\$，表示最后都编辑完后光标所处的位置
>- \$SELECTION\$，表示设置环绕实时代码模板，环绕功能下面会模板专门进行介绍。
>- 除了两个特例，其他被\$包裹的都是自定义变量

这里有Jetbrains的官网介绍：https://www.jetbrains.com/help/idea/live-template-abbreviation.html
有兴趣的可以去了解一下，变量也可以这样定义哦。

### 文件代码模板

这个我想大家都知道吧，你每次新建一个文件的时候，总会有一些已经存在的代码或者文字，这个就是文件代码模板。

DEA 默认新建类自带的类注释格式一般不够友好或是规范，所以我们一般需要自己根据喜好或者一些要求设置。

![文件代码模板](http://upload-images.jianshu.io/upload_images/4047674-223aa072de89697e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

先看一个例子：

![例子](http://upload-images.jianshu.io/upload_images/4047674-71dc2016b5a9ca3c?imageMogr2/auto-orient/strip)

只需要如下设置就可以了：

![设置](http://upload-images.jianshu.io/upload_images/4047674-0205528fb0e843b4?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这样就可以每次在新建类的时候都能自动生成平时看代码时候别人写在开头那种屌屌的说明了。建议的格式如下：

![建议的格式](http://upload-images.jianshu.io/upload_images/7896890-85e3d8b0d5117989.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### Postfix Completion

先看一个例子：

![例子](http://upload-images.jianshu.io/upload_images/4047674-66907337080348b6?imageMogr2/auto-orient/strip)

怎么设置的呢？在设置的地方官方也给了相应的gif动图的演示，非常友好，大家可以自己去看一下。

![Postfix Completion设置](http://upload-images.jianshu.io/upload_images/7896890-d5ce046e8fb3a6ef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

简单举几个例子好了：

![拼图有点丑，别介](http://upload-images.jianshu.io/upload_images/7896890-6bfac873c17b397e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 一些快捷键

【Ctrl + D】复制当前行到下一行
【Ctrl + C】复制当前行
【Ctrl + V】粘贴到当前行
【Ctrl + / 】注释或取消注释当前行
【Ctrl + Shift + Enter】这个功能特别棒，对于强迫症患者来说特别受用，大概是这样：（你们自己具体感受下）

![自动结束代码并排版添加分号？](http://upload-images.jianshu.io/upload_images/7896890-e621905cbbbf8be6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


---

按照惯例黏一个尾巴：


> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693