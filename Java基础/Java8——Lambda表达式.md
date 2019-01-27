> 本文内容大部分来自《Java 8实战》一书

## 前言

在上一篇文章中，我们了解了利用行为参数化来传递代码有助于应对不断变化的需求，它允许你定义一个代码块来表示一个行为，然后传递它。一般来说，利用这个概念，你就可以编写更为灵活且可重复使用的代码了。

但是你同时也看到，使用匿名类来表示不同的行为并不令人满意：代码十分啰嗦，这会影响程序员在时间中使用行为参数化的积极性。Lambda表达式很好的解决了这个问题，它可以让你很简洁地表示一个行为或传递代码。*现在你可以把Lambda表达式看作匿名功能，它基本上就是没有声明名称的方法，但和匿名类一样，它也可以作为参数传递给一个方法。*

## Lambda管中窥豹

可以把Lambda表达式理解为简洁地表示可传递的匿名函数的一种方式：它没有名称，但它由参数列表、函数主体、返回类型，可能还有一个抛出的异常列表。

Lambda表达式鼓励你采用上一篇文章中提到的行为参数化风格，最终结果就是你的额代码变得更加清晰、更加灵活。比如，利用Lambda表达式，你可以更为简洁地自定义一个Comparator对象：

![](http://upload-images.jianshu.io/upload_images/7896890-d3f5c5e9f12542c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

不得不承认，代码看起来更清晰了。要是现在觉得Lambda表达式看起来一头雾水的话也没关系，很快就会一点点的解释清楚的。现在，请注意你基本上只传递了比较两个苹果重量所需要的代码。看起来就像只传递了compare方法的主体。你很快就会学到，你甚至还可以进一步简化代码。

为了进一步说明，下面给出了Java 8五个有效的Lambda表达式的例子：

![](http://upload-images.jianshu.io/upload_images/7896890-bb79f4b577fd7e90.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Java语言设计者选择这样的语法，是因为C#和Scala等语言中的类似功能广受欢迎。Lambda的基本语法是：

```java
(parameters) -> expression
```

或**（请注意语句的花括号）**

```java
(parameters) -> { statements; }
```

你可以看到，Lambda表达式的语法很简单，我们下来来测试一下你对这个模式的了解程度：

![](http://upload-images.jianshu.io/upload_images/7896890-06b3f2c0e4b6db8d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 在哪里以及如何使用Lambda

现在你可能在想，在哪里可以使用Lambda表达式。直接公布答案：你可以在函数式接口上使用Lambda表达式。

#### 函数式接口

还记得上一篇文章中，为了参数化filter方法的行为而创建的Predicate<T>接口吗？它就是一个函数式接口！为什么呢？**因为Predicate仅仅定义了一个抽象方法：**

```java
public interface Predicate<T>{
    boolean test(T t);
}
```

一言以蔽之，**函数式接口就是之定义一个抽象方法的接口。**你已经知道了Java API中的一些其他函数式接口，如Comparator和Runnable

```java
public interface Comparator<T>{
    int compare(T o1, T o2);
}

public interface Runnable{
    void run();
}
```

> 接口现在还可以拥有**默认方法（即在类没有对方法进行是现实时，其主体为方法提供默认实现的方法，如List的sort方法）。**哪怕有很多默认方法，只要接口只定义了一个**抽象方法**，它就仍然是一个函数式接口。

为了检测是否掌握了函数式接口的概念，我们来看一个小测试：

![](http://upload-images.jianshu.io/upload_images/7896890-f6f5aac41c69a941.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

用函数式接口可以干什么呢？Lambda表达式允许你直接以内联的形式为函数式接口的抽象方法提供实现，并把整个表达式作为函数式接口的实例。这听上去可能有些绕口，但是联想到上一篇文章中的Lambda表达式改造的语句，或许就会清晰许多，它不同于使用匿名内部类来完成时的笨拙，而是更加清晰直接：

![](http://upload-images.jianshu.io/upload_images/7896890-87c3fe03f78c82c7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

你可能会想：“为什么只有在需要函数式接口的时候才可以传递Lambda呢？”语言的设计者也考虑过其他方法，例如给Java添加函数类型，但最终他们选择了现在这种方式，因为这种方式自然且能避免语言变得更加复杂。此外，大多数Java程序员都已经熟悉了具有一个抽象方法的接口的理念（例如事件处理）。

## 把Lambda付诸实践：环绕执行模式

让我们通过一个例子，看看在实践中如何利用Lambda和行为参数化来让代码更为灵活，更为简洁。资源处理（例如处理文件或数据库）时一个常见的模式就是打开一个资源，做一些处理，然后关闭资源。这个设置和清理阶段总是很相似，并且会围绕着执行处理的那些重要代码。这就是所谓的环绕执行（execute around）模式：

![](http://upload-images.jianshu.io/upload_images/7896890-d84c98d38f06f440.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 第一步：记得行为参数化

现在这段代码时有局限的。你只能读文件的第一行。如果你想要返回头两行，甚至返回使用最频繁的词，该怎么办呢？在理想的情况下，你要重用执行设置和清理的代码，并告诉processFile方法对文件执行不同的操作。这听起来是不是很耳熟？是的，你需要把processFile的行为参数化。你需要一种方法把行为传递给processFile，以便它可以利用BufferedReader执行不同的行为。

传递行为正是Lambda的拿手好戏。那要是想一次读两行，这个新的processFile方法看起来又该是什么样的呢？基本上，你需要一个接受BufferedReader并返回String的Lambda。例如，下面就是从BufferedReader中打印两行的写法：

```java
String result = processFile((BufferedReader br) -> 
                                            br.readLine() + br.readLine());
```

#### 第二步：使用函数式接口来传递行为

前面已经解释过了，Lambda仅可用于上下文是函数式接口的情况。你需要创建一个能匹配BufferedReader -> String，还可以抛出IOException异常的接口。让我们把这一接口叫做BufferedReaderProcessor吧。

```java
@FunctionalInterface
public interface BufferedReaderProcessor{
    String process(BufferedReader b) throws IOException;
}
```

> @FunctionalInterface 标注表示该接口会设计成一个函数式接口。如果你用此标注定义了一个接口，而它却不是函数式接口的话，编译器将返回一个提示原因的错误。

现在你就可以把这个接口作为新的processFile方法的参数了：

```java
public static String processFile(BufferedReaderProcessor p) throws IOException{
    ...
}
```

#### 第三步：执行一个行为

任何BufferedRader -> String形式的Lambda都可以作为参数来传递，因为它们符合BufferedReaderProcessor接口中定义的process方法的签名。现在你只需要一种方法在processFile主体内执行Lambda所代表的代码。**请记住，Lambda表达式允许你直接内联，为函数式接口的抽象方法提供实现，并且将整个表达式作为函数式接口的一个实例。**因此，你可以在processFile主体内，对得到的BufferedReaderProcessor对象调用process方法执行处理：

```java
public static String processFile(BufferedReaderProcesssor p) throws IOException{
    try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
        return p.process(br);
    }
}
```

#### 第四步：传递Lambda

现在你就可以通过传递不同的Lambda重用processFile方法，并以不同的方式处理文件了：

![](http://upload-images.jianshu.io/upload_images/7896890-05575cb8fe8ec81d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

下面的图片总结了所采取的使processFile方法更加灵活的四个步骤：

![](http://upload-images.jianshu.io/upload_images/7896890-3dd5fb3566051fab.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 使用函数式接口

如你所见的，函数式接口很有用，因为抽象方法的签名可以描述Lambda表达式的签名。Java 8的库设计师帮你在java.util.function包中引入了几个新的函数式接口。

#### Predicate

java.util.function.Predicate<T>接口定义了一个名叫test的抽象方法，它接受泛型T对象，并返回一个boolean。在你需要一个涉及类型T的布尔表达式时，就可以使用这个接口：

![](http://upload-images.jianshu.io/upload_images/7896890-d66c0b5eca69e0fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### Consumer

java.util.function.Consumer<T>定义了一个名叫accept的抽象方法，它接受泛型T的对象，没有返回（void）。你如果需要访问类型T的对象，并对其执行某些操作，就可以使用这个接口：

![](http://upload-images.jianshu.io/upload_images/7896890-b82ecf93c4a48e75.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### Function

java.util.function.Function<T,R>接口定义了一个叫做apply的方法，它接受一个泛型T的对象，并返回一个泛型R的对象。如果你需要定义一个Lambda，将输入对象的信息映射到输出，就可以使用这个接口（比如提取苹果的重量，或把字符串映射为它的长度）：

![](http://upload-images.jianshu.io/upload_images/7896890-94bc9527054fe819.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 还有更为丰富的一些函数式接口，这里列举了三个比较有代表性的。

## 方法引用

方法引用让你可以重复使用现有的方法定义，并像Lambda一样传递它们。在一些情况下，比起使用Lambda表达式，它们似乎更易读，感觉也更自然。下面就是借助Java 8API，用方法引用写的一个排序的例子：

![](http://upload-images.jianshu.io/upload_images/7896890-93999b52611588fe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

是不是更酷了？念起来就是“给库存排序，比较苹果的重量”，这样的代码读起来简直就像是在描述问题本身，太酷了。

为什么要关心方法引用呢？**方法引用可以被看作调用特定方法的Lambda的一种快捷写法。**它的基本思想是，如果一个Lambda代表的知识“直接调用这个方法”，拿最好还是用名称来调用它，而不是去描述如何调用它。

事实上，方法引用就是让你根据已有的方法实现来创建Lambda表达式，但是，显式地指明方法的名称，你的代码可读性会更好。

它是如何工作的呢？当你需要使用方法引用时，目标引用放在分隔符** :: **前，方法的名称放在后面。例如，Apple::getWeight就是引用了Apple类中定义的方法getWeight。请记住，不需要括号，因为你没有实际调用这个方法，方法引用就是Lambda表达式(Apple a) -> a.getWeight()的快捷写法。

下面给出一些在Java 8中方法引用的例子来让你更加了解：

![](http://upload-images.jianshu.io/upload_images/7896890-c3ab872955c9ad67.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

你可以把方法引用看作针对仅仅涉及单一方法的Lambda的语法糖，因为你表达同样的事情时写的代码更少了。

## Lambda 和方法引用实战

我们继续来研究开始的那个问题——用不同的排序策略给一个Apple列表排序，并展示如何把一个原始粗暴的解决方案转变得更为简明：```inventory.sort(comparing(Apple::getWeight));```

#### 第一步：传递代码

很幸运，Java 8的API已经为你提供了一个List可用的sort方法，你不用自己去实现它。那么最困难的部分已经搞定了！但是，如何把排序的策略传递给sort方法呢？你看，sort方法的签名是这样的：

```void sort(Comparator<? super E> c)```

它需要一个Comparator对象来比较两个Apple！这就是在Java中传递策略的方式：**它们必须包裹在一个对象里。**我们说sort的行为被参数化了：传递给它的排序策略不同，其行为也会不同。

你的第一个解决方案看上去是这样的：

```java
public class AppleComparator implements Comparator<Apple>{
    public int compare(Apple a1, Apple a2){
        return a1.getWeigh().compareTo(a2.getWeight());
    }
}
inventory.sort(new AppleComparator());
```

#### 第二步：使用匿名类

你可以使用*匿名类*来改进解决方案，而不是实现一个Comparator却只实例化一次：

```java
inventory.sort(new Comparator<Apple>(){
    public int compare(Apple a1, Apple a2){
        return a1.getWeight().compareTo(a2.getWeight());
    }
});
```

#### 第三步：使用Lambda表达式

但你的解决方案仍然挺啰嗦的。使用Java 8引入的Lambda改进后的代码如下：

```java
inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
```

你的代码还能变得更易读一点吗？Comparator具有一个叫做comparing的静态辅助方法，它可以接受一个Function来提取Comparable键值，并生成一个Comparator对象。它可以像下面这样用：

```Comparator<Apple> c = Comparator.comparing((Apple a1) -> a.getWeight());```

现在你可以把代码再改得紧凑一点了：

```java
import static java.util.Comparator.comparing;
inventory.sort(comparing((a) -> a.getWeight()));
```

#### 第四步：使用方法引用

前面解释过，方法引用就是替代那些转发参数的Lambda表达式的语法糖。你可以用方法引用让你的代码更加简洁（假设你已经静态导入了java.util.Comparator.comparing）：

```inventory.sort(comparing(Apple::getWeight));```

恭喜你，这就是你的最终解决方案！这笔Java 8之前的代码好在哪儿呢？它比较短；它的意思也很明显，并且代码读起来和问题描述差不多：“对库存进行排序，比较苹果的重量。”


---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693