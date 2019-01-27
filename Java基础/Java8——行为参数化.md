## 前言

《Java8实战》不得不说是一本好书，捧起来看起来就兴奋得不想放下，其中介绍的函数式编程实在是太令人兴奋了，不仅仅大大提高了代码的可读性，而且提高了代码的重用性，并且语法简单。

**Java 8中新增的功能是自Java 1.0发布以来18年以来，发生变化最大的一次。**我本身没有太大的体会，但新增的这些功能，每一个都让我兴奋，这里就书中的内容简单的介绍一下Java 8的这些新特性，我相信很快，你也会有跟我一样的感受。

## （1）用行为参数化把代码传递给方法

*Java 8中增加了通过API来传递代码的能力，*但这实在听起来太绕了，这到底在说什么！打个比方或许要容易理解一些，你想要写两个只有几行代码不同的方法，那现在你只需要把不同的那部分代码作为参数传递进去就可以了。

在Java 8中，这样做起来（不止于匿名类）**远远比你想象的要来得更加清晰、简洁。**行为参数化

我们现在来考虑这样一个例子：有个应用程序是帮助农民了解自己的库存的，这位农民可能想有一个查找库存中所有绿色苹果的功能。但到了第二天，他可能会告诉你：“其实我还想找出所有重量超过150克的苹果”。又过了两天，他可能会继续补充道：“要是我可以找出所有既是绿色，重量也超过150克的苹果，那就太棒了。”

想一下你应该如何应对这样不断变化的需求呢？理想的状态下，你应该把你的工作量降到最小，此外类似的新功能实现起来还应该很简单，而且易于长期维护。

#### 第一次尝试：筛选绿苹果

第一个解决方案可能是下面这样的:

```java
public static List<Apple> filterGreenApples(List<Apple> inventory){
    List<Apple> result = new ArrayList<>();
    for(Apple apple: inventory){
        if( “green”.equals(apple.getColor()) {
            result.add(apple);
        }
    }  // end for
    return result;
}
```

这样的代码看起来似乎也没什么问题，也很容易看懂，但是现在农民改主意了，他还想要筛选红苹果，又该怎么做呢？简单的方法就是复制这个方法，然后把函数名称和if判断的条件来分别匹配到红苹果上就可以了。然而，要是农民想要筛选出多种颜色：浅绿色，暗红色、黄色等，这种方法就应付不了了。**一个良好的原则是在编写类似的代码之后，尝试将其抽象化。**

#### 第二次尝试：把颜色作为参数

一种做法是给方法加一个参数，把颜色变成参数，这样就灵活地适应了变化：

```java
public static List<Apple> filterApplesByColor(List<Apple> inventory,String color){
    List<Apple> result = new ArrayList<>();
    for(Apple apple: inventory){
        if(apple.getColor().equals(color)){
            result.add(apple);
        }
    }  // end for
    return result;
}
```

现在，只要像下面这样调用方法，农民朋友就会满意了：

```
List<Apple> greenApples = filterApplesByColor(inventory,"green");
List<Apple> greenApples = filterApplesByColor(inventory,"red");
....
```

太简单了对吧？让我们把案例变得复杂一点。这位农民又跑回来和你说：“要是能区分轻的苹果和重的苹果就太好了。重的苹果一般大于150克。”

作为软件工程师，你早就想好了农民可能会要改变重量，于是你写了下面的方法，用另一个参数来应对不同的重量：

```java
public static List<Apple> filterApplesByColor(List<Apple> inventory,int weight){
    List<Apple> result = new ArrayList<>();
    for(Apple apple: inventory){
        if(apple.getWeight() > weight){
            result.add(apple);
        }
    }  // end for
    return result;
}
```

解决方案不错，但是请注意，你赋值了大部分的代码来实现遍历库存，并对每个苹果应用筛选条件。这有点儿令人失望，**因为它打破了DRY（Don't Repeat Yourself,不要重复你自己）的软件工程原则。**

如果你想要改变筛选遍历方式来提升性能呢？那就得修改所有方法的实现，而不是只改变一个。从工程工作量的角度来看，这代价太大了。

你可以将颜色和重量结合为一个方法，称为filter。不过就算这样，你还是需要一种方式来区分想要筛选哪个属性。你可以加上一个标志位来区分对颜色和重量的查询**（但绝不要这样做！很快你就会明白为什么）。**

#### 第三次尝试：对你想到的每个属性做筛选

一种把所有属性结合起来的笨拙尝试如下：

```java
public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag){
    List<Apple> result = new ArrayList<>();
    for(Apple apple: inventory){
        if((flag && apple.getColor.equals(color)) ||
            (!flag && apple.getWeight() > weight)){
            result.add(apple);
        }
    }  // end for
    return result;
}
```

你可以这么用（但真的很笨拙）:

```java
List<Apple> greenApples = filterApples(inventory, "green", 0, ture);
List<Apple> heavyApples = filterApples(inventory, "", 150, false);
```

这样的解决方案再差不过了。

首先，客户端代码看上去糟糕透了，ture和false是什么意思？此外，这个解决方案还是不能很好的应对变化的需求。如果这位农要求你对苹果的不同属性做筛选，比如大小、形状、产地等，又怎么办？而且，如果农民要求你组合属性，做更复杂的查询，比如绿色的种苹果，又改怎么办？你会有好多个重复的filter方法，或者一个巨大的非常复杂的方法。

到目前为止，你已经给filterApples方法加上了值（如String、Integer或boolean）的参数。这对于某些确定性问题可能还不错，但如今这种情况下，你需要一种更好的方式，来把苹果的选择标准告诉你的filterApples方法。

> 这就是需要行为参数化登场发挥作用的地方了。让我们后退一步来看看更高层次的抽象。一种可能的解决方案是对你的选择标准建模：你考虑的是苹果，需要根据Apple的某些属性（比如它是绿色的吗？重量超过150克吗？）来返回一个boolean值，我们把它称为谓词（即一个返回boolean值得函数）。

#### 第四次尝试：根据抽象条件筛选

让我们先来定义一个接口对选择标准建模：

```java
public interface ApplePredicate{
    boolean test (Apple apple);
}
```

现在你可以使用ApplePredicate的多个实现代表 不同的选择标准了，比如：

![](http://upload-images.jianshu.io/upload_images/7896890-6b3b61b9e067c9c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

你可以把这些标准看作filter方法的不同行为。你刚做的这些和“策略设计模式”相关，它让你定义一族算法，把它们封装起来（称为“策略”），然后在运行时选择一个算法。在这里算法簇就是ApplePredicate，不同的策略就是AppleHeavyWeightPredicate和AppleGreenColorPredicate。

但是，该怎么利用ApplePredicate的不同实现呢？你需要filterApples方法接受ApplePredicate对象，对Apple做条件测试。**这就是行为参数化：让方法接受多种行为（或战略）作为参数，并在内部使用，完成不同的行为。**

利用ApplePredicate改过之后，filter方法看起来就是这样的：

```java
public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p){
    List<Apple> result = new ArrayList<>();
    for(Apple apple:inventory){
        if(p.test(apple)){
            result.add(apple);
        }  // end if
    }    // end for
    return result;
}
```

> 这里值得暂停下来小小地庆祝一下。这段代码比我们第一次尝试的时候灵活多了，读起来、用起来也更容易！现在你可以创建不同的ApplePredicate对象，并将它们传递给filterApples方法。免费的灵活性！比如，如果农民让你找出所有重量超过150克的红苹果，你只需要创建一个类来实现ApplePredicacte对象就可以了，你的代码现在足够灵活，可以应对任何涉及苹果属性的需求变更了。

你已经做成了一件很酷的事：filterApples方法的行为取决于你通过ApplePredicate对象传递的代码，换句话说，你把filterApples方法的行为参数化了！

#### 第五次尝试：使用匿名类

这样做起来已经很棒了，还有什么问题呢？

我们都知道，人们都不愿意用那些很麻烦的功能或者概念，目前，当要把新的行为传递给filterApples方法的时候，你不得不声明好几个实现ApplePredicate接口的类，然后实例化好几个只会提到一次的ApplePredicate对象。下面这段程序总结了你目前看到的一切，这真的很啰嗦而且费时间：

![](http://upload-images.jianshu.io/upload_images/7896890-6757199f3264a67c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

费这么大劲儿，真的没什么必要。能不能做得更好呢？Java有一个机制称为*匿名类*，它可以让你同时声明和实例化一个类，它可以帮助你进一步改善代码，让它变得更简洁：

```java
List<Apple> redApples = filterApples(inventory, new Applepredicate(){
    public boolean test(Apple apple){
        return "red".equals(apple.getColor());
    }
});
```

GUI应用程序中经常使用匿名类来创建事件处理器对象（下面的例子使用的是JavaFX API,一种现代的Java UI平台）：

```java
button.setOnAction(new EventHandler<ActionEvent>(){
    public void handle(ActionEvent event){
        System.out.println("Woooo a click!");
    }
});
```

但是匿名类仍然不够好。

第一，它往往很笨重，因为它占用了很多空间，还拿前面的例子来说：

![](http://upload-images.jianshu.io/upload_images/7896890-ade67d8d57a7c16b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

第二，很多程序员觉得它用起来很让人费解，比如这里有一道经典的Java谜题，它让大多数程序员都措手不及，来试试看：

![](http://upload-images.jianshu.io/upload_images/7896890-8930c2b3599523d5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 答案是5，因为this指的是包含它的Runnable，而不是外面的类MeaningOfThis。

整体来说，啰嗦就不好。它让人不愿意使用语言的某种功能，因为编写和维护啰嗦的代码需要很长时间，而且代码也不易读。好的代码应该一目了然。

即使匿名类处理在某种程度上改善了为一个接口声明好几个实体类的啰嗦问题，但它仍然不能让人满意。在只需要传递一段简单的代码时（例如表示选择标准的boolean表达式），你还是要创建一个对象，明确地实现一个方法来定义一个新的行为（例如Predicate中的test方法或者是EventHandler中的handler方法）。

在理想的情况下，我们想鼓励程序员使用行为参数化模式，因为正如你在前面看到的，它让代码更能适应需求的变化，但也同样的，啰嗦不可避免。这也正是Java 8的语言设计者引入Lambda表达式的原因——他让传递代码的方式变得更加简洁、干净。

#### 第六次尝试：使用Lambda表达式

上面的代码在Java 8里可以用Lambda表达式重写为下面的样子：

```java
List<Apple> result = 
    filterApples(inventory, (Apple apple) -> "red".equals(apple.getColor()));
```

不得不承认这代码看上去比先前干净很多，这很好，因为它看起来更像问题陈述本身了。我们现在已经解决了啰嗦的问题，下图总结了到目前为止的工作：

![](http://upload-images.jianshu.io/upload_images/7896890-b553d0d19c39c0aa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 第七次尝试：将List类型抽象化

在通往抽象的道路上，我们还可以更近一步。目前filterApples方法还只适用于Apple。你还可以将List类型抽象画，从而超越你眼前要处理的问题：

```java
public interface Predicate<T>{
    boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> p){
    List<T> result = new ArrayList<>();
    for(T e:list){
        if(p.test(e)){
            result.add(e);
        }  // end if
    }  // end for
    return result;
}
```

现在你可以把filter方法用在香蕉、桔子、Integer或者是String的列表上了。这里有一些使用Lambda表达式的例子：

```java
List<Apple> redApples = 
    filter(inventory, (Apple apple) -> "red".equals(apple.getColor()));
List<Integer> evenNumbers = 
    filter(numbers, (Integer i) -> i % 2 == 0);
```

酷不酷？你现在在灵活性和简洁性之间找到了最佳平衡点，这在Java 8之前是不可能做到的！

#### 应用行为参数化的典型例子

> 一个是用Runnable执行代码块，用Lambda表达式的话，看起来就是这个样子的：

```java
Thread t = new Thread(() -> System.out.println("Hello world"));
```

> 另一个就是GUI事件处理：

```java
button.setOnAction((ActionEvent event) -> label.setText("Sent!!"));
```


看起来酷极了吧？不过想要熟练地运用，就要足够了解Lambda表达式，这将在下一节中再来说。


---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693