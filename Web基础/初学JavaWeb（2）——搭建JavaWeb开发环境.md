![](https://upload-images.jianshu.io/upload_images/7896890-2093e17bea460810.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 虽然说 html 和 css 等前端技术，是对于 Web 来说不可或缺的技术，但是毕竟更为简单一些，所以就不详细介绍了，没有基础的同学可以去[菜鸟教程](http://www.runoob.com/)或者[W3school](http://www.w3school.com.cn/)进行自主学习，最好的方式还是做一做简单的项目，相信你很快就可以上手啦。(有空把我仿的天猫给大家拿出来..)

---

## ——【1. JDK 的安装】——
在搭建环境之前呢，还是给搬一下一些名词的介绍和解释：

| 术语名        | 缩写        | 解释|
| ------------- |:-------------:|:-----|
| Java Development Kit     | JDK | 编写Java程序的从程序员使用的软件 |
| Java Runtime Environment      | JRE|   运行Java程序的用户使用的软件 |
| Standard Edition | SE |   用于桌面或简单的服务器应用的Java平台 |
| Enterprise Edition | EE | 用于复杂的服务器应用的Java平台 |
| Micro Edition | ME | 用于手机和其他小型设备的Java平台 |
| Java 2 | J2 | 一个过时的术语，用于描述1998年~2006年之间的Java版本 |
| Software Development Kit | SDK | 一个过时的术语，用于描述1998年~2006年之间的JDK |

再来引用一张图片来说明 Java SE 和 Jave EE 的区别：
> ![](https://upload-images.jianshu.io/upload_images/7896890-7598a4014e3681d4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### JDK 的下载
JDK的官方下载地址在这：[http://www.oracle.com/technetwork/java/javase/downloads/index.html](https://link.jianshu.com/?t=http://www.oracle.com/technetwork/java/javase/downloads/index.html)
最新的JDK已经升级到了9.0，而且据说10.0都快出来了，可能对于一些新学 Java 的同学来说，8的特性还没熟悉，9不知道是啥，10.0 眼看着都要出来了...没关系，我也是差不多这样的感受...

![下载JDK](https://upload-images.jianshu.io/upload_images/7896890-1d9f0d63a970abac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### JDK 的安装
上图就是一个JDK的下载过程，安装很简单，我就直接粘网上的图了**（重要的是记住JDK安装的路径用于配置环境变量）**：

![JDK 安装过程](https://upload-images.jianshu.io/upload_images/7896890-cebe189d85b2f197.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### JDK 环境变量的配置
Java环境变量涉及到三个名词：JAVA_HOME、path、classpath。

JAVA_HOME代表JDK安装主目录，path代表JDK下可执行文件目录，classpath代表运行java程序时需要查找class文件的目录。

依据上面的安装步骤：

**JAVA_HOME**应该设置为：D:\DevelopTools\JAVA\JDK

**path**应该设置为：%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;**（注意其中的分号）**

**classpath**应该设置为：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;**（注意最前面的点代表当前路径，JDK1.5之后这项不用设置了）**

设置方式如下：

> ![第一步](https://upload-images.jianshu.io/upload_images/7896890-0d9743df88cfd7f4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> ![第二步](https://upload-images.jianshu.io/upload_images/7896890-834a801ccff8eaa8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> ![第三步](https://upload-images.jianshu.io/upload_images/7896890-3e29c0172f7678b4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
> 到此就已经成功配置好了 JDK 所需要的环境变量，我们下面来验证一下。

#### 验证 JDK 是否安装成功
打开命令提示符，WINDOWS 下的快捷键为【Win + R】，然后输入 cmd ，输入【java -version】查看是否输出正确地版本信息，如果成功，则 JRE 配置成功。（我直接贴的老图啦，所以....）

![JRE 配置成功](https://upload-images.jianshu.io/upload_images/7896890-afcd436746c47e5a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

输入【javac】看是否输出编译信息，若有，则 JDK 配置成功：

 ![JDK 配置成功](https://upload-images.jianshu.io/upload_images/7896890-9ce212cdd86347b4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

至此，JDK 就彻底安装成功了。

---

![](https://upload-images.jianshu.io/upload_images/7896890-8ec728678627b5d4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## ——【2.安装和配置 Tomcat 服务器】——
正如上一篇说到的，Tomcat 是使用 Java 语言编写的一个服务器（程序），所以要运行 Tomcat ，必须配置好相关的 JDK 或 JRE。Tomcat 的官方网站是：`http://tomcat.apache.org/`   你可以去官网下载最新的版本，也可以下载我提供的绿色版本（v8.5.5）：https://pan.baidu.com/s/1gROPZCovNrsMxkWLFbZ9IQ（密码:142q）

以我提供的绿色版本为例，安装步骤如下：

#### ① 解压提供的 Tomcat 到 D 盘
并记录下其路径，如 D:\apache-tomcat-8.5.5.

#### ② 配置 CATALINA_HOME 环境变量到上面记下的路径：

![配置 Tomcat 的环境变量](https://upload-images.jianshu.io/upload_images/7896890-ed2ff0d5386258a0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### ③ 启动 Tomcat 服务器

找到 D:\apache-tomcat-8.5.5 路径下的 bin 目录中的 startup.bat 文件，双击运行，如果输出启动信息，并未输出任何异常，则 Tomcat 安装正确**（注意，弹出的命令提示符窗口不能关闭，否则服务器将停止运行！）**

![Tomcat 成功启动](https://upload-images.jianshu.io/upload_images/7896890-fc209705ccc487d0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### ④ 打开浏览器验证 Tomcat

打开浏览器，在地址栏输入地址：http://localhost:8080/ 或者 http://127.0.0.1:8080/ ，如果能够打开 Tomcat 配置页面，则说明 Tomcat 配置成功：

![Tomcat 配置页面](https://upload-images.jianshu.io/upload_images/7896890-fae8b489e0909f74.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 关闭 Tomcat 服务器

在 Tomcat 开启的命令提示符界面按下【Ctrl + C】键，将关闭 Tomcat 服务器。再次尝试访问 Tomcat 配置页面，若出现网页不能访问提示，则说明 Tomcat 关闭成功：

![Tomcat 关闭成功](https://upload-images.jianshu.io/upload_images/7896890-cb51285c022c0f6e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 在 Tomcat 中新建 Web 工程
**步骤如下：**（[下载文件-密码klhk](https://pan.baidu.com/s/18LpiltoAJA32f5ksyhkn8g)）
① 找到 Tomcat 目录下的 webapps 目录。
② 在 webapps 目录下新建一个目录： first_webapp。**（注意 first 和 webapp 中间为下划线）**
③ 在 first_webapp 中建立一个目录： WEB-INF。**（注意大小写，WEB 和 INF 之间为横线）**
④ 将上面的下载文件下载下来以后，将提供的 web.xml 文件复制到 WEB-INF 目录中。
⑤ 将提供的 index.html 文件复制到 first_webapp 目录下。
⑥ 启动 Tomcat。
⑦ 在浏览器中输入网址：http://localhose:8080/first_webapp ，查看是否能够正确访问，如果可以正确显示页面，则表示 Tomcat 中创建新项目成功。

---

![](https://upload-images.jianshu.io/upload_images/7896890-9acd3d85bb965e64.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## ——【3.安装配置 MySql 服务器】——

不管怎样，服务器端总是要有数据库的，这里就给出两个绿色版本的工具，来建立一个 Web 开发所需要的 MySql 服务器：
链接：https://pan.baidu.com/s/1i-H4SUMcn2y5_arluK923A 密码：1n04
> - **xampp** 是一套 Web 开发套件，里面包含了 mysql、apache、tomcat 等常用组件。这里我们主要利用其中的 mysql 组件来完成数据库的开发。
> - **heidiSQL** 是一款绿色版的 SQL 管理软件，能实现数据库的一些常用功能。

**步骤如下：**
① 将提供的 xampp 工具解压到 D 盘根目录下。**（注意 xampp 一定要解压到根目录下才有效。所谓根目录是指
D:\xampp\目录下不能再嵌套 xampp 目录，而是直接在 D:\xampp\目录下能够找到 mysql 目录。切记！！）**
② 双击 xampp 目录下的 xampp-control.exe 文件运行，并显示如下界面：
![XAMPP Control 界面](https://upload-images.jianshu.io/upload_images/7896890-5809de81fd592dca.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

③ 为了避免和本机上已经安装的 MySql 服务器冲突，我们需要更改 xampp 的服务名称：单机界面上的 Config 按
钮，显示如下界面：
![更改 xampp 服务名称](https://upload-images.jianshu.io/upload_images/7896890-0041ed69cf0dfea7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

④ 点击“Service and Port Settings”按钮，将出现如下界面：
![](https://upload-images.jianshu.io/upload_images/7896890-2ed9787cf609403f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

⑤ 点击 MySQL 标签页，配置 MySql，将 MySql 的 Service Name 从 mysql 更改为 mysql-xampp，然后保存：
![更改名称](https://upload-images.jianshu.io/upload_images/7896890-e41da7f359742232.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

⑥ 关闭 xampp。**（ 注意 如果闭 关闭 xampp  后，xampp 。 任然在后台运行，必须从右下角系统托盘处完全关闭。）**然后重新打开 xampp-control.exe 文件：
![](https://upload-images.jianshu.io/upload_images/7896890-fa0caa490fe1bb5b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

⑦ 点击 MySql 对应的 Start 按钮。如果 MySql 服务启动成功，则显示如下：
![MySql 服务启动成功](https://upload-images.jianshu.io/upload_images/7896890-77f7c9c22b3fe674.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 其中，MySql 字符的背景色变为绿色表示启动成功，PID 表示 MySql 在操作系统中的进程号，Port 表示 MySql 进程所监听的端口号。

⑧ 打开提供的 heidiSQL 工具（绿色软件），将显示如下界面：
![heidiSQL 工具界面](https://upload-images.jianshu.io/upload_images/7896890-508a72a1007c9a1b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

⑨ 点击 New 按钮，新建 MySql 连接：
![](https://upload-images.jianshu.io/upload_images/7896890-27ba879b7ecb728a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

⑩ 在 hostname/IP 处填写：127.0.0.1 或者 localhost。在 User 处填写：root（这是 MySql 的登录用户名）。在 Password 处填写：root（这是 MySql 的登录密码。 **注意：如果是自己下载的 xampp  工具，则密码为空。使用的我提供的 xampp  工具，则 MySql  登录密码被我改成 root** ）在 Port 处填写：3306（这是 MySql 默认的监听端口），随后点击【Open】按钮，将显示如下界面：
![](https://upload-images.jianshu.io/upload_images/7896890-e4b1cbd954a1a6db.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 左边列出了目前 MySql 中的所有数据库，右边是当前数据库的内容。（请勿更改和删除默认数据库的内容）

#### 在 Tomcat 中连接数据库
将提供的 MySql 的 JDBC 驱动文件 mysql-connector-java-xxxx-bin.jar 文件复制到 Tomcat 目录下的 lib 目录中

至此，就成功搭建好了 MySql 服务器。


---

![](https://upload-images.jianshu.io/upload_images/7896890-b00ed47e7729312c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## ——【4.安装 Eclipse-JEE】——

#### ① 下载 Eclipse - Jee
进入官网 http://www.eclipse.org/downloads/eclipse-packages 选择Eclipse  IDE for Java EE Developers，根据自己电脑情况选择32位或者64位：
![](https://upload-images.jianshu.io/upload_images/7896890-298f33f5deb3562c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

下好以后直接解压到相应位置，并找到 **eclipse.exe**，右键 --> 发送到 --> 桌面快捷方式就行了。

#### ② 首次打开设置工作空间
由于我们已经配置好了 JDK 的相关信息，所以这里的 Eclipse 可以直接打开：
![指定工作空间](https://upload-images.jianshu.io/upload_images/7896890-bb2a7938ed5cea78.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

工作空间大家设置成自己喜欢。熟悉的路径即可，以后建立的工程文件都在设置的工作空间里面。

#### ③ 进行简单配置
- **配置 Tomcat 服务器：**
  1.进入 Eclipse 后首先将 Tomcat 服务器位置通知 Eclipse。选择菜单项“Window”→“Preferences”
  2.打开“Preferences”对话框，在左边找到“Server”选项，并展开，选中其中“Runtime Environments”项目：

![](https://upload-images.jianshu.io/upload_images/7896890-41e9c8476c9e43c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

  3.点击右边界面中“Add”按钮，在弹出的界面中找到“Apache Tomcat 8.5”项目并选中：

![](https://upload-images.jianshu.io/upload_images/7896890-16e63ba0fd2c7026.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

  4.点击下一步，并在 Name 处输入：Apache Tomcat v8.5。然后将目标目录选定为刚刚安装的 Tomcat 目录

![](https://upload-images.jianshu.io/upload_images/7896890-39ee47d65cb23c43.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

  5.最后点击“Finish”按钮完成服务器的配置

- **配置默认的格式：**
  1.选择菜单项“Window”→“Preferences”，找到下方的“Web”，将其中红色框标记的选项里的【Encoding（编码格式）】均改为 UTF-8，是为防止使用中文乱码的问题。

![配置格式](https://upload-images.jianshu.io/upload_images/7896890-8c102147bbe01f1d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

  2.点击“Apply and Close”完成配置。

#### ④ 创建一个动态网页项目测试
- 1.选中 Eclipse 菜单项“New”→“Dynamic Web Project”，新建一个动态网页项目
- 2.在新建项目界面中 project name 处输入工程名称 second_webapp（注意 second 和 webapp 中间为下划线）。并记住此名字。在 Runtime 中选择刚刚配置的 Tomat。

![](https://upload-images.jianshu.io/upload_images/7896890-0d365908b1d0b27a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 3.点击“Next”按钮，并对配置不做修改，再次点击“Next”按钮，在下一个界面中，选中“Generate web.xml”选项。最后点击“Finish”按钮创建工程。

![](https://upload-images.jianshu.io/upload_images/7896890-00d15586da9c2718.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 4.在 second_webapp 上点击右键，并选中“Properties”菜单项，若看到工程默认编码为 GBK，则在对话框中将工程的编码方式改为 UTF-8，目的是避免以后中文出现乱码：

![](https://upload-images.jianshu.io/upload_images/7896890-34602d469d492830.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 5.展开 second_webapp 工程，并在“WebContent”项目上点击鼠标右键，并选中菜单项目“New”→“HTML
File”，然后输入网页的名字 index.html，并点击“Finish”按钮：

![](https://upload-images.jianshu.io/upload_images/7896890-fc77032a30d42f59.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 6.此时将显示 HTML 文件内容（默认为 HTML 5 格式），编辑 index.html 文件，成如下内容：
``` html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>我的第二个网页</title>
</head>
<body>
Java EE 环境部署成功！
</body>
</html>

```
- 7.编辑完成后，点击 Eclipse 菜单：“Run”→“Run As”→“Run on Server”**（注意：此时在 Eclipse 外面不能启动Tomcat 服务器，即 Tomcat 的命令提示窗口不能已经打开）**，在弹出的对话框中选择，刚刚配置的 Tomcat 环境，边点击“Finish”按钮启动 Tomcat 服务器：

![](https://upload-images.jianshu.io/upload_images/7896890-1e386be820188f6e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 8.等待服务器启动完成。然后在浏览器中输入网址：http://localhost:8080/second_webapp/index.html，查看网页。如果能正确显示页面，则 Eclipse 新建项目成功。

----

至此，Java Web 的开发环境就已经全部搭建好了，关于为什么选择 Eclipse 而不选择 IDEA ，是因为这学期开的 Java Web 课程中使用的也是 Eclipse ，为了避免麻烦，所以还是用 Eclipse 吧。

#### 参考资料：
> [1.迷路的国王博客](http://www.cnblogs.com/kangjianwei101/p/5621750.html)
> 2.本学校的实验说明


---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693


