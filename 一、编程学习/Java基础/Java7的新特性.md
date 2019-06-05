## 前言

> 看大佬推荐的书单买了一本《Java 8实战》，总觉得在了解Java 8之前，是不是也应该去了解了解一下Java 7的一些特性？所以就自己百度了一些资料来学习。

***

## 当然还是要先看看官方文档啦

> 这里是详细介绍（缺点是全英文的，貌似还不是官方的，不过很详细）：https://www.oreilly.com/learning/java7-features
> 下面大部分内容均来自这一篇文章，翻译一下而已。

戳进去看看，大概还是能看懂的，看到有博客说Java 7大部分的特性都是语法糖。

***

## 1.Diamond Operator

类型判断是一个人特殊的烦恼，入下面的代码：

```java
Map<String,List<String>> anagrams = new HashMap<String,List<String>>();
```

通过类型推断后变成：

```java
Map<String,List<String>> anagrams = new HashMap<>();
```

**注：这个<>被叫做diamond(钻石)运算符，Java 7后这个运算符从引用的声明中推断类型。**

***

## 2.在switch语句中使用字符串

switch语句可以使用原始类型或枚举类型。Java引入了另一种类型，我们可以在switch语句中使用：字符串类型。

说我们有一个根据其地位来处理贸易的要求。直到现在，我们使用if-其他语句来完成这个任务。

```java
private voidprocessTrade(Trade t){
            String status = t.getStatus();
            if(status.equalsIgnoreCase(NEW)) {
                  newTrade(t);
            } else if(status.equalsIgnoreCase(EXECUTE)) {
                  executeTrade(t);
            } else if(status.equalsIgnoreCase(PENDING)) {
                  pendingTrade(t);
            }
}
```

这种处理字符串的方法是粗糙的。在Java中，我们可以使用增强的switch语句来改进程序，该语句以String类型作为参数。

```java
public voidprocessTrade(Trade t) {
    String status = t.getStatus();
    switch(status) {
        caseNEW:
            newTrade(t);
            break;
        caseEXECUTE:
            executeTrade(t);
            break;
        casePENDING:
            pendingTrade(t);
            break;
         default:
            break;
    }
}
```

在上面的程序中，状态字段总是通过使用 **String.equals()** 与案例标签来进行比较。

***
 
## 3.自动资源管理

Java中有一些资源需要手动关闭，例如**Connections，Files，Input/OutStreams**等。通常我们使用 **try-finally** 来关闭资源：

```java
public voidoldTry() {
            try{
                  fos= newFileOutputStream("movies.txt");
                  dos= newDataOutputStream(fos);
                  dos.writeUTF("Java 7 Block Buster");
            } catch(IOException e) {
                  e.printStackTrace();
            } finally{
                  try{
                        fos.close();
                        dos.close();
                  } catch(IOException e) {
                        // log the exception
                  }
            }
      }
```

然而，在Java 7中引入了另一个很酷的特性，可以自动管理资源。它的操作也很简单，我们所要做的就是在 **try** 块中申明资源如下：

```java
try(resources_to_be_cleant){

   // your code

}
```

以上方法与旧的 **try-finally** 能最终写成下面的代码：

```java
      public voidnewTry() {



            try(FileOutputStream fos = newFileOutputStream("movies.txt");

                        DataOutputStream dos = newDataOutputStream(fos)) {

                  dos.writeUTF("Java 7 Block Buster");

            } catch(IOException e) {

                  // log the exception

            }

      }
```

上面的代码也代表了这个特性的另一个方面：处理多个资源。**FileOutputStream** 和 **DataOutputStream** 在try语句中一个接一个地含在语句中，每一个都用分号(;)分隔符分隔开。我们不必手动取消或关闭流，因为当空间存在try块时，它们将自动关闭。

在后台，应该自动关闭的资源必须试验 **java.lang.AutoCloseable** 接口。

任何实现 **AutoCloseable** 接口的资源都可以作为自动资源管理的候选。**AutoCloseable** 是 **java.io.Closeable** 接口的父类，JVM会在程序退出**try**块后调用一个方法 **close()**。

***

## 4.带下划线的数字文本

数字文字绝对是对眼睛的一种考验。我相信，如果你给了一个数字，比如说，十个零，你就会像我一样数零。如果不计算从右到左的位置，识别一个文字的话，就很容易出错，而且很麻烦。Not anymore。Java在识别位置时引入了下划线。例如，您可以声明1000，如下所示：

```java
int thousand =  1_000;
```

或1000000(一百万)如下:

```java
int million  =  1_000_000
```

**请注意，这个版本中也引入了二进制文字-例如“0b1”-因此开发人员不必再将它们转换为十六进制。**

***

## 5.改进的异常处理

在异常处理区域有几处改进。Java引入了多个catch功能，以使用单个抓到块捕获多个异常类型。

假设您有一个方法，它抛出三个异常。在当前状态下，您将分别处理它们，如下所示：

```java
   public voidoldMultiCatch() {

            try{

                  methodThatThrowsThreeExceptions();

            } catch(ExceptionOne e) {

                  // log and deal with ExceptionOne

            } catch(ExceptionTwo e) {

                  // log and deal with ExceptionTwo

            } catch(ExceptionThree e) {

                  // log and deal with ExceptionThree

            }

      }
```

在一个catch块中逐个捕获一个连续的异常，看起来很混乱。我还看到了捕获十几个异常的代码。这是非常低效和容易出错的。Java为解决这只丑小鸭带来了新的语言变化。请参阅下面的方法oldMultiCatch方法的改进版本：

```java
      public voidnewMultiCatch() {

            try{

                  methodThatThrowsThreeExceptions();

            } catch(ExceptionOne | ExceptionTwo | ExceptionThree e) {

                  // log and deal with all Exceptions

            }

      }
```

多个异常通过使用 **“|”**操作符在一个catch块中捕获。这样，您不必编写数十个异常捕获。但是，如果您有许多属于不同类型的异常，那么您也可以使用“多个catch块”块。下面的代码片段说明了这一点：

```java
public voidnewMultiMultiCatch() {

            try{

                  methodThatThrowsThreeExceptions();

            } catch(ExceptionOne e) {

                  // log and deal with ExceptionOne



            } catch(ExceptionTwo | ExceptionThree e) {

                  // log and deal with ExceptionTwo and ExceptionThree

            }



      }
```

在上面的例子中，在和ExceptionThree属于不同的层次结构，因此您希望以不同的方式处理它们，但使用一个抓到块。

***

## 6.New file system API(NIO 2.0)

那些使用Java的人可能还记得框架引起的头痛。在操作系统或多文件系统之间无缝地工作从来都不是一件容易的事情.。有些方法，例如删除或重命名，在大多数情况下都是出乎意料的。使用符号链接是另一个问题。实质上API需要大修。

为了解决上述问题，Java引入了一个新的API，并在许多情况下引入了新的api。

在NIO2.0提出了许多增强功能。在处理多个文件系统时，它还引入了新的类来简化开发人员的生活。

### Working With Path（使用路径）

新的 **java.nio.file** 由包和接口组成例如：**Path,Paths,FileSystem,FileSystems**等等。

路径只是对文件路径的简单引用。它与java.io.File等价(并具有更多的特性)。下面的代码段显示了如何获取对“临时”文件夹的路径引用：

```java
public voidpathInfo() {

            Path path= Paths.get("c:\Temp\temp");

System.out.println("Number of Nodes:"+ path.getNameCount());

            System.out.println("File Name:"+ path.getFileName());

            System.out.println("File Root:"+ path.getRoot());

            System.out.println("File Parent:"+ path.getParent());

      }
```

最终控制台的输出将是:

```java
Number of Nodes:2

File Name:temp.txt

File Root:c:

File Parent:c:Temp
```

删除文件或目录就像在文件中调用delete方法(注意复数)一样简单。在类公开两个删除方法，一个抛出NoSuchFileException，另一个不抛。

下面的delete方法调用抛出NoSuchFileException，因此您必须处理它：

```java
Files.delete(path);
```

Where as Files.deleteIfExists(path) does not throw exception (as expected) if the file/directory does not exist.

> 使用 **Files.deteleIfExists(path)** 则不会抛出异常。

您可以使用其他实用程序方法，例如Files.copy(.)和Files.move(.)来有效地对文件系统执行操作。类似地，使用 **createSymbolicLink(..)** 方法使用代码创建符号链接。

### 文件更改通知

JDK 7中最好的改善算是File change notifications（文件更改通知）了。这是一个长期等待的特性，它最终被刻在NIO 2.0中。**WatchService** API 允许您在对主题(目录或文件)进行更改时接收通知事件。

> 具体的创建步骤就不给了，总之它的功能就跟它的名字一般，当文件发生更改的时候，能及时作出反馈。

***

## 7.Fork and Join（Fork/Join框架）

在一个java程序中有效地使用并行内核一直是一个挑战。很少有国内开发的框架将工作分配到多个核心，然后加入它们来返回结果集。Java已经将这个特性作为Fork/Join框架结合了起来。

基本上，在把手头的任务变成了小任务，直到小任务简单到可以不进一步分手的情况下解决。这就像一个分而治之的算法.。在这个框架中需要注意的一个重要概念是，理想情况下，没有工作线程是空闲的。他们实现了一个 work-stealing 算法，在空闲的工人“偷”工作从那些工人谁是忙。

支持Fork-Join机制的核心类是ForkJoinPool和ForkJoinTask。


> 这里是Java 7的新特性一览表：http://www.oschina.net/news/20119/new-features-of-java-7



---
> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693