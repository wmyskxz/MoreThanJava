![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/「MoreThanJava」Day7：接口/image-20200813144940633.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**，本系列 Java 基础教程是自己在结合各方面的知识之后，对 Java 基础的一个总回顾，旨在 **「帮助新朋友快速高质量的学习」**。
- 当然 **不论新老朋友** 我相信您都可以 **从中获益**。如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# Part 1. 接口概述

Java 是单继承的。这意味着子类仅从一个父类继承。通常，这就是你需要的。有时候多继承会提供方便，但也会造成混乱，例如，当继承的两个父类具有不同版本的签名相同的两个方法时该调用哪一个呢？

**接口为 Java 提供了多继承的一些优点，而没有缺点。**

## 接口的概念

在 Java 程序设计语言中，接口不是类，**而是对希望符合这个接口的类的一组需求。**

我们 [之前](https://www.wmyskxz.com/2020/08/07/morethanjava-day-5-mian-xiang-dui-xiang-jin-jie-ji-cheng-xiang-jie/) 接触的 **抽象类**，性格偏内向，描述的是一组相对具体的特征，比如某品牌特定型号的汽车，底盘架构、控制电路、刹车系统等是抽象出来的共同特征，但根据动感型、舒适型、豪华型的区分，内饰、车头灯、显示屏等都可以存放不同版本的具体实现。

而 **接口** 是开放的，性格偏外向，它就像一份合同，定义了方法名、参数列表、返回值，甚至是抛出异常的类型。谁都可以实现它，但如果想实现它的类就必须遵守这份接口约定的合同。

> 想一想比较熟悉的 USB 接口：它不仅仅约束了 U 盘 *(实现类)* 的大小和形状，同样也约束了电脑插槽 *(使用类)*。在编程中，接口类似。

## 接口的定义

在 Java 中使用 `interface` 关键字来定义接口。接口是顶级的 "类"，虽然关键字是 `interface`，但编译之后的字节码扩展名还是 `.class`。一个典型接口的结构如下：

```java
public interface InterfaceName {
  	constant definitions
    method headers (without implementations).
}
```

比如，我们在 [前面文章](https://www.wmyskxz.com/2020/08/07/morethanjava-day-5-mian-xiang-dui-xiang-jin-jie-ji-cheng-xiang-jie/) 讨论「为什么不推荐使用继承？」中举的鸟类的例子，任何能飞的鸟都必须实现如下接口：

```java
public interface Flyable {
  	void fly();
}
```

接口中的所有方法都自动是 `public`。因此，在接口中声明方法时，不必提供关键字 `public`。*(在 Java 9 中允许了接口定义声明为 `private` 的方法，在这之前都是不允许的..)*

> 想一想接口就像是合同一样，所以任何不清晰的细节都是不允许的。因此，接口中只允许明确的方法定义和常量出现。*(下方的例子中演示了一个不被允许的接口定义 —— 因为 `y` 变量没有确定的值)*
>
>
> ```java
> interface ErrorInterfaceDefine {
>   public final int x = 32;
>   public double y;   // No variables allowed
> 
>   public double addup();
> }
> ```

这看起来有点儿像类的定义，但没有任何对象能够构建一个接口 *(`new`一个接口.. 因为接口是绝对抽象的，不允许实现..)*，但你可以定义一个类实现 *(关键字 `impelents`)* 接口，一旦你这么做了，你就可以构造这个 *(实现接口的)* 类的对象。

例如麻雀既能飞、也能叫、还能下蛋：*(实现多个接口使用 `,` 分隔)*

```java
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

## 接口的属性

**❶** 接口不是类，不能使用 `new` 运算符实例化一个接口，但却可以用来引用实现了这个接口的类对象：

```java
Comparable x = new Employee(...); // OK provided Emloyee implements Comparable
```

**❷** 与建立类的继承层次一样，也可以扩展接口！比如，假设这里有一个名为 `Moveable` 的接口：

```java
public interface Moveable {
  	void move(double x, double y);
}
```

然后，可以假设一个名为 `Powered` 的接口扩展了以上的 `Moveable` 接口：

```java
public interface Powered extends Moveable {
  	double milesPerGallon();
}
```

**❸** 虽然在接口中不能包含实例字段，但是可以包含常量。比如：

```java
public interface Powered extends Moveable {
  	double SPEED_LIMIT = 95;  // a public static final constant
  	double milesPerGallon();
}
```

**❹** 另外有一些接口之定义了常量，而没有定义方法。例如，标准库中的 `SwingConstants` 就是这样一个接口，其中只包含了 `NORTH`、`SOUTH` 和 `HORIZONTAL` 等常量。任何实现 `SwingConstants` 接口的类都自动地继承了这些常量，并可以在方法中引用它们，而不必采用 `SwingConstants.NORTH` 这样繁琐的书写形式。不过，这样使用接口更像是退化，所以建议最好不要这样使用...

![SwingConstants 源码部分截图](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/「MoreThanJava」Day7：接口/image-20200812072319833.png)

➡️ **一个类只能有一个父类，但可以实现很多个接口**。这就为定义类的行为提供了极大的灵活性。*(我们之前也讨论过——在讨论继承的章节——这里不再赘述)*

## 静态和私有方法

➡️ 在 **Java 8** 中，允许在接口中增加静态方法 *(允许不构建对象而直接使用的具体方法)*。理论上讲，没有任何理由认为这是不合法的，**只是这有违将接口作为抽象规范的初衷**。

目前为止，通常的做法都是将静态方法放在 **伴随类** *(可以理解为操作继承接口的实用工具类)* 中。在标准库中，你可以看到成对出现的接口和实用工具类，如 `Collection/ Collections` 或 `Path/ Paths`。

在 **Java 11** 中，`Path` 接口就提供了一个与之工具类 `Paths.get()` 等价的方法 *(该方法用于将一个 URI 或者字符串序列构造成一个文件或目录的路径)*： 

```java
public interface Path {
    public static Path of(String first, String... more) { ... }
    public static Path of(URI uri) { ... }
}
```

这样一来，`Paths` 类就不再是必要的了。类似地，如果实现你自己的接口时，没有理由再额外提供一个带有实用方法的工具类。

➡️ 另外，在 **Java 9** 中，接口中的方法可以是 `private`。`private` 方法可以是静态方法或实例方法。由于私有方法只能在接口本身的方法中使用，所以它们的用法很有限，只能作为接口中其他方法的辅助方法。

## 默认方法

在 **Java 8** 中，允许为接口方法提供一个默认的实现。必须用 `default` 修饰符标记这样一个方法，例如 JDK 中的 `Iterator` 接口：

```java
public interface Iterator<E> {
  	boolean hasNext();
  	E next();
  	default void remove() { throw new UnsupportedOperationExceition("remove"); }
}
```

这将非常有用！如果你要实现一个迭代器，就需要提供 `hasNext()` 和 `next()` 方法。这些方法没有默认实现——它们依赖于你要遍历访问的数据结构。不过，如果你的迭代器是 **只读** 的，那么就不用操心实现 `remove()` 方法。

默认方法也可以调用其他方法，例如，我们可以改造 `Collection` 接口，定义一个方便的 `isEmpty()` 方法：

```java
public interface Collection {
  	int size(); // an abstract method
  	default boolean isEmpty() { return size() == 0; }
}
```

这样，实现 `Collection` 的程序员就不用再操心实现 `isEmpty()` 方法了。

*(事实上这也是 `AbstractCollection` 抽象类的定义——所有的集合具体实现几乎都继承了 `AbstractCollection`抽象类——但为什么顶层的 `Collection` 接口不做这样的修改呢？我起初是怀疑有一些特殊的集合为空的定义有特殊性，但我没有找到..几乎所有的集合为空判定都为自身的元素等于 `0`。所以答案是什么呢？是解决默认方法冲突的 "类优先" 原则！👇)*

### 解决默认方法冲突

如果先在一个接口中将一个方法定义为默认方法，然后又在类或另一个接口中定义同样的方法，会发生什么？

```java
// 测试接口 1
public interface TestInterface1 {
    default void sameMethod() { System.out.println("Invoke TestInterface1 method！"); }
}
// 测试接口 2
public interface TestInterface2 {
    default void sameMethod() { System.out.println("Invoke TestInterface2 method！"); }
}
// 继承两个接口的测试类
public class TestObject implements TestInterface1, TestInterface2 {

    @Override
    public void sameMethod() {
      	// 这里也可以选择两个接口中的一个默认实现
      	// 如： TestInterface1.super.sameMethod();
        System.out.println("Invoke Object method！");
    }
}
// 测试类
public class Tester {

    public static void main(String[] args) {
        TestObject testObject = new TestObject();
        testObject.sameMethod();
    }
}
```

**测试输出：**

```text
Invoke Object method！
```

➡️ 对于 `Scale` 或者 `C++` 这些语言来说，解决这种具有 **二义性** 的情况规则会很复杂，`Java` 的规则则简单得多：

1. **类优先**。如果本类中提供了一个具体方法符合签名，则同名且具有相同参数列表的接口中的默认方法会被忽略；
2. **接口冲突**。如果一个接口提供了一个默认方法，另一个接口提供了一个同名且参数列表相同的方法 *(顺序和类型都相同)* ，则必须覆盖这个方法来解决冲突 *(就是👆代码的情况，不覆盖编译器不会编译..)*；

Java 设计者更强调一致性，让程序员自己来解决这样的二义性似乎也显得很合理。如果至少有一个接口提供了一个实现，编译器就会报告错误，程序员就必须解决这个二义性。*(如果两个接口都没有为共享方法提供默认实现，则不存在冲突，要么实现，要么不实现..)*

➡️ 我们只讨论了两个接口的命名冲突。现在来考虑另一种情况，一个类继承自一个类，同时实现了一个接口，从父类继承的方法和接口拥有同样的方法签名，又将怎么办呢？

```java
// 测试接口
public interface TestInterface {
    default void sameMethod() { System.out.println("Invoke TestInterface Method！"); }
}
// 父类
public class Father {
    void sameMethod() { System.out.println("Invoke Father Method！"); }
}
// 子类
public class Son extends Father implements TestInterface {
    @Override
    public void sameMethod() {
        System.out.println("Invoke Son Method！");
    }
}
// 测试类
public class Tester {
    public static void main(String[] args) { new Son().sameMethod(); }
}
```

**程序输出：**

```text
Invoke Son Method！
```

还记得我们说过的方法调用的过程吗 *(先找本类的方法找不到再从父类找)*？加上这里提到的 "类优先" 原则 *(本类中有方法则直接调用)*，这很容易理解！

> 千万不要让一个默认方法重新定义 `Object` 类中的某个方法。例如，不能为 `toString()` 或 `equals()` 定义默认方法，尽管对于 List 之类的接口这可能很有吸引力，但由于 **类优先原则**，这样的方法绝对无法超越 `Object.toString()` 或者 `Object.equals()`。
>
> *(这里就对应上方思考为什么不在 `Collection` 中定义默认的 `isEmpty()` 方法的答案)*

# Part 2. 接口与工厂模式

> 这一部分节选自 **极客时间 | 设计模式之美**：https://time.geekbang.org/column/article/197254
>
> 原作者：王争

接口是实现多重继承的途径，而生成遵循某个接口的对象的典型方式就是 **工厂方法设计模式**。这与直接调用构造器构造对象不同，我们在工厂对象上调用的是创建方法，而该工厂对象将生成接口的某个实现对象。

理论上，通过这种方式，**我们的代码将完全与接口的实现分离**，这就使得我们可以透明地将某个实现替换为另一个实现。下面我们来举例演示一下。

## 简单工厂模式

假设我们现在需要根据文件的后缀名 *(json、xml、yaml)* 来选择不同的解析器 *(JsonRuleConfigParser、XmlRuleConfigParser)*，将存储在文件中的配置解析成内存对象 RuleConfig：

```java
public class RuleConfigSource {
  public RuleConfig load(String ruleConfigFilePath) {
    String ruleConfigFileExtension = getFileExtension(ruleConfigFilePath);
    IRuleConfigParser parser = null;
    if ("json".equalsIgnoreCase(ruleConfigFileExtension)) {
      parser = new JsonRuleConfigParser();
    } else if ("xml".equalsIgnoreCase(ruleConfigFileExtension)) {
      parser = new XmlRuleConfigParser();
    } else if ("yaml".equalsIgnoreCase(ruleConfigFileExtension)) {
      parser = new YamlRuleConfigParser();
    } else {
      throw new InvalidRuleConfigException(
             "Rule config file format is not supported: " + ruleConfigFilePath);
    }

    String configText = "";
    //从ruleConfigFilePath文件中读取配置文本到configText中
    RuleConfig ruleConfig = parser.parse(configText);
    return ruleConfig;
  }

  private String getFileExtension(String filePath) {
    //...解析文件名获取扩展名，比如rule.json，返回json
    return "json";
  }
}
```

➡️ 为了让代码逻辑更加清晰，可读性更好，我们要善于 **将功能独立的代码块封装成函数**。按照这个设计思路，我们可以将代码中涉及 `parser` 创建的部分逻辑剥离出来，抽象成 `createParser()` 函数。重构之后的代码如下所示：

```java
public RuleConfig load(String ruleConfigFilePath) {
    String ruleConfigFileExtension = getFileExtension(ruleConfigFilePath);
    IRuleConfigParser parser = createParser(ruleConfigFileExtension);
    if (parser == null) {
      throw new InvalidRuleConfigException(
              "Rule config file format is not supported: " + ruleConfigFilePath);
    }

    String configText = "";
    //从ruleConfigFilePath文件中读取配置文本到configText中
    RuleConfig ruleConfig = parser.parse(configText);
    return ruleConfig;
  }

  private String getFileExtension(String filePath) {
    //...解析文件名获取扩展名，比如rule.json，返回json
    return "json";
  }

  private IRuleConfigParser createParser(String configFormat) {
    IRuleConfigParser parser = null;
    if ("json".equalsIgnoreCase(configFormat)) {
      parser = new JsonRuleConfigParser();
    } else if ("xml".equalsIgnoreCase(configFormat)) {
      parser = new XmlRuleConfigParser();
    } else if ("yaml".equalsIgnoreCase(configFormat)) {
      parser = new YamlRuleConfigParser();
    }
    return parser;
  }
}
```

➡️ 为了让类的职责更加单一、代码更加清晰，我们还可以进一步将 `createParser()` 函数剥离到一个单独的类中，让这个类只负责对象的创建。而这个类就是我们现在要将的 **简单工厂** 模式类。具体的代码如下所示：

```java
public class RuleConfigSource {
  public RuleConfig load(String ruleConfigFilePath) {
    String ruleConfigFileExtension = getFileExtension(ruleConfigFilePath);
    IRuleConfigParser parser = RuleConfigParserFactory.createParser(ruleConfigFileExtension);
    if (parser == null) {
      throw new InvalidRuleConfigException(
              "Rule config file format is not supported: " + ruleConfigFilePath);
    }

    String configText = "";
    //从ruleConfigFilePath文件中读取配置文本到configText中
    RuleConfig ruleConfig = parser.parse(configText);
    return ruleConfig;
  }

  private String getFileExtension(String filePath) {
    //...解析文件名获取扩展名，比如rule.json，返回json
    return "json";
  }
}

public class RuleConfigParserFactory {
  public static IRuleConfigParser createParser(String configFormat) {
    IRuleConfigParser parser = null;
    if ("json".equalsIgnoreCase(configFormat)) {
      parser = new JsonRuleConfigParser();
    } else if ("xml".equalsIgnoreCase(configFormat)) {
      parser = new XmlRuleConfigParser();
    } else if ("yaml".equalsIgnoreCase(configFormat)) {
      parser = new YamlRuleConfigParser();
    }
    return parser;
  }
}
```

*(这样的 Factory 代码暂称为第一种实现)*

在类的命名中体现设计模式是非常好的方式 *(例如这里的 `RuleConfigParserFactory`)*。大部分工厂类都是以 `“Factory”` 这个单词结尾的，但也不是必须的，比如 Java 中的 `DateFormat`、`Calender`。

除此之外，工厂类中创建对象的方法一般都是 `create` 开头，比如代码中的 `createParser()`，但有的也命名为 `getInstance()`、`createInstance()`、`newInstance()`，有的甚至命名为 `valueOf()` *（比如 Java String 类的 `valueOf()` 函数）* 等等，这个我们根据具体的场景和习惯来命名就好。

➡️ 在上面的代码实现中，我们每次调用 RuleConfigParserFactory 的 `createParser()` 的时候，都要创建一个新的 `parser`。实际上，如果 `parser` 可以复用，为了节省内存和对象创建的时间，我们可以将 `parser` 事先创建好缓存起来。当调用 `createParser()` 函数的时候，我们从缓存中取出 `parser` 对象直接使用：

```java
public class RuleConfigParserFactory {
  private static final Map<String, RuleConfigParser> cachedParsers = new HashMap<>();

  static {
    cachedParsers.put("json", new JsonRuleConfigParser());
    cachedParsers.put("xml", new XmlRuleConfigParser());
    cachedParsers.put("yaml", new YamlRuleConfigParser());
  }

  public static IRuleConfigParser createParser(String configFormat) {
    if (configFormat == null || configFormat.isEmpty()) {
      return null;//返回null还是IllegalArgumentException全凭你自己说了算
    }
    IRuleConfigParser parser = cachedParsers.get(configFormat.toLowerCase());
    return parser;
  }
}
```

*(这样的 Factory 代码暂称为第二种实现)*

这有点类似于单例模式和简单工厂模式的结合。

但上面两种实现的简单工厂，都有违背 **开闭原则** *(对扩展开放，对修改关闭)*。想象一下现在我们如果要新增一种 `parser`，那么势必会修改 `RuleCOnfigParserFactory` 里面的代码！但好在就日常的使用来说，如果不是需要频繁地添加新的 `parser`，只是偶尔修改一下 RuleConfigParserFactory 代码，稍微不符合开闭原则，也是完全可以接受的。

## 工厂方法

回看👆我们上方的第一种实现，如果可能的话，我们的 `if-else` 代码会随着文件种类的增加列得越来越长，最终不仅可读性很差，也变得更加难以维护 *(复杂度增加)*，而且也不怎么优雅。

如果我们非得去掉 `if-else` 分支逻辑的话，应该怎么办呢？比较经典处理方法就是利用多态。按照多态的实现思路，对上面的代码进行重构。重构之后的代码如下所示：

```java
public interface IRuleConfigParserFactory {
  IRuleConfigParser createParser();
}

public class JsonRuleConfigParserFactory implements IRuleConfigParserFactory {
  @Override
  public IRuleConfigParser createParser() {
    return new JsonRuleConfigParser();
  }
}

public class XmlRuleConfigParserFactory implements IRuleConfigParserFactory {
  @Override
  public IRuleConfigParser createParser() {
    return new XmlRuleConfigParser();
  }
}

public class YamlRuleConfigParserFactory implements IRuleConfigParserFactory {
  @Override
  public IRuleConfigParser createParser() {
    return new YamlRuleConfigParser();
  }
}
```

实际上，这就是工厂方法模式的典型代码实现。这样当我们新增一种 parser 的时候，只需要新增一个实现了 IRuleConfigParserFactory 接口的 Factory 类即可。所以，**工厂方法模式比起简单工厂模式更加符合开闭原则。**

从上面的工厂方法的实现来看，一切都很完美，但是实际上存在挺大的问题。问题存在于这些工厂类的使用上。接下来，我们看一下，如何用这些工厂类来实现 RuleConfigSource 的 `load()` 函数。具体的代码如下所示：

```java

public class RuleConfigSource {
  public RuleConfig load(String ruleConfigFilePath) {
    String ruleConfigFileExtension = getFileExtension(ruleConfigFilePath);

    IRuleConfigParserFactory parserFactory = null;
    if ("json".equalsIgnoreCase(ruleConfigFileExtension)) {
      parserFactory = new JsonRuleConfigParserFactory();
    } else if ("xml".equalsIgnoreCase(ruleConfigFileExtension)) {
      parserFactory = new XmlRuleConfigParserFactory();
    } else if ("yaml".equalsIgnoreCase(ruleConfigFileExtension)) {
      parserFactory = new YamlRuleConfigParserFactory();
    } else {
      throw new InvalidRuleConfigException("Rule config file format is not supported: " + ruleConfigFilePath);
    }
    IRuleConfigParser parser = parserFactory.createParser();

    String configText = "";
    //从ruleConfigFilePath文件中读取配置文本到configText中
    RuleConfig ruleConfig = parser.parse(configText);
    return ruleConfig;
  }

  private String getFileExtension(String filePath) {
    //...解析文件名获取扩展名，比如rule.json，返回json
    return "json";
  }
}
```

从上面的代码实现来看，工厂类对象的创建逻辑又耦合进了 `load()` 函数中，跟我们最初的代码版本非常相似，引入工厂方法非但没有解决问题，反倒让设计变得更加复杂了。那怎么来解决这个问题呢？

我们可以为工厂类再创建一个简单工厂，也就是 **工厂的工厂**，用来创建工厂类对象。这段话听起来有点绕，我把代码实现出来了，你一看就能明白了。其中，RuleConfigParserFactoryMap 类是创建工厂对象的工厂类，`getParserFactory()` 返回的是缓存好的单例工厂对象。

```java

public class RuleConfigSource {
  public RuleConfig load(String ruleConfigFilePath) {
    String ruleConfigFileExtension = getFileExtension(ruleConfigFilePath);

    IRuleConfigParserFactory parserFactory = RuleConfigParserFactoryMap.getParserFactory(ruleConfigFileExtension);
    if (parserFactory == null) {
      throw new InvalidRuleConfigException("Rule config file format is not supported: " + ruleConfigFilePath);
    }
    IRuleConfigParser parser = parserFactory.createParser();

    String configText = "";
    //从ruleConfigFilePath文件中读取配置文本到configText中
    RuleConfig ruleConfig = parser.parse(configText);
    return ruleConfig;
  }

  private String getFileExtension(String filePath) {
    //...解析文件名获取扩展名，比如rule.json，返回json
    return "json";
  }
}

//因为工厂类只包含方法，不包含成员变量，完全可以复用，
//不需要每次都创建新的工厂类对象，所以，简单工厂模式的第二种实现思路更加合适。
public class RuleConfigParserFactoryMap { //工厂的工厂
  private static final Map<String, IRuleConfigParserFactory> cachedFactories = new HashMap<>();

  static {
    cachedFactories.put("json", new JsonRuleConfigParserFactory());
    cachedFactories.put("xml", new XmlRuleConfigParserFactory());
    cachedFactories.put("yaml", new YamlRuleConfigParserFactory());
  }

  public static IRuleConfigParserFactory getParserFactory(String type) {
    if (type == null || type.isEmpty()) {
      return null;
    }
    IRuleConfigParserFactory parserFactory = cachedFactories.get(type.toLowerCase());
    return parserFactory;
  }
}
```

当我们需要添加新的规则配置解析器的时候，我们只需要创建新的 `parser` 类和 `parser factory` 类，并且在 RuleConfigParserFactoryMap 类中，将新的 `parser factory` 对象添加到 `cachedFactories` 中即可。代码的改动非常少，基本上符合开闭原则。

实际上，对于规则配置文件解析这个应用场景来说，工厂模式需要额外创建诸多 Factory 类，也会增加代码的复杂性，而且，每个 Factory 类只是做简单的 `new` 操作，功能非常单薄 *（只有一行代码）*，也没必要设计成独立的类，所以，在这个应用场景下，简单工厂模式简单好用，比工厂方法模式更加合适。

### 什么时候该用工厂方法模式呢？

我们前面提到，之所以将某个代码块剥离出来，独立为函数或者类，原因是这个代码块的逻辑过于复杂，剥离之后能让代码更加清晰，更加可读、可维护。但是，如果代码块本身并不复杂，就几行代码而已，我们完全没必要将它拆分成单独的函数或者类。

所以让我们有足够理由使用工厂方法模式的情况大概有以下两点：

- 当对象的创建逻辑比较复杂，不只是简单的 `new` 一下就可以，而是要组合其他类对象，做各种初始化操作的时候；
- 避免烦人的 `if-else` 分支逻辑时；

## 抽象工厂(Abstract Factory)

在简单工厂和工厂方法中，类只有一种分类方式。比如，在规则配置解析那个例子中，解析器类只会根据配置文件格式 *(Json、Xml、Yaml……)* 来分类。但是，如果类有两种分类方式，比如，我们既可以按照配置文件格式来分类，也可以按照解析的对象 *(Rule 规则配置还是 System 系统配置)* 来分类，那就会对应下面这 `6` 个 `parser` 类。

```text
针对规则配置的解析器：基于接口IRuleConfigParser
JsonRuleConfigParser
XmlRuleConfigParser
YamlRuleConfigParser

针对系统配置的解析器：基于接口ISystemConfigParser
JsonSystemConfigParser
XmlSystemConfigParser
YamlSystemConfigParser
```

针对这种特殊的场景，如果还是继续用工厂方法来实现的话，我们要针对每个 parser 都编写一个工厂类，也就是要编写 `6` 个工厂类。如果我们未来还需要增加针对业务配置的解析器 *(比如 IBizConfigParser)*，那就要再对应地增加 `4` 个工厂类。而我们知道，过多的类也会让系统难维护。这个问题该怎么解决呢？

抽象工厂就是针对这种非常特殊的场景而诞生的。我们可以让一个工厂负责创建多个不同类型的对象 *(IRuleConfigParser、ISystemConfigParser 等)*，而不是只创建一种 `parser` 对象。这样就可以有效地减少工厂类的个数。具体的代码实现如下所示：

```java
public interface IConfigParserFactory {
  IRuleConfigParser createRuleParser();
  ISystemConfigParser createSystemParser();
  //此处可以扩展新的parser类型，比如IBizConfigParser
}

public class JsonConfigParserFactory implements IConfigParserFactory {
  @Override
  public IRuleConfigParser createRuleParser() {
    return new JsonRuleConfigParser();
  }

  @Override
  public ISystemConfigParser createSystemParser() {
    return new JsonSystemConfigParser();
  }
}

public class XmlConfigParserFactory implements IConfigParserFactory {
  @Override
  public IRuleConfigParser createRuleParser() {
    return new XmlRuleConfigParser();
  }

  @Override
  public ISystemConfigParser createSystemParser() {
    return new XmlSystemConfigParser();
  }
}

// 省略YamlConfigParserFactory代码
```

# 重点回顾

1. 接口的概念 / 接口的定义 / 接口的实现 / 接口的属性；
2. 接口的静态和私有方法 / 如何解决默认方法的冲突；
3. 接口和工厂模式；

# 练习

## 练习 1：实现一个图形绘制工具

> 创建一个可以绘制不同形状的绘图工具，可以绘制圆形、矩形、三角形，每个图形都会有一个 `draw()` 方法用于绘图，而绘图工具也有一个 `draw()` 方法，根据传入类型的不同调用不同的方法。

**创建 IShape 接口：**

```java
public interface IShape {
    void draw();
}
```

**继承 IShape 接口创建圆形、矩形、三角形：**

```java
// 圆形
public class Circle implements IShape {
    @Override
    public void draw() { System.out.println("Draw Circle..."); }
}
// 矩形
public class Rectangle implements IShape {
    @Override
    public void draw() { System.out.println("Draw Rectangle..."); }
}
// 三角形
public class Triangle implements IShape {
    @Override
    public void draw() { System.out.println("Draw Triangle..."); }
}
```

**图形绘制工具：**

```java
public class Paint {
    public static void draw(IShape shape) {
        shape.draw();
    }
}
```

**测试类：**

```java
public class Tester {
    public static void main(String[] args) {
        Paint.draw(new Circle());
        Paint.draw(new Rectangle());
        Paint.draw(new Triangle());
    }
}
```

**程序输出：**

```text
Draw Circle...
Draw Rectangle...
Draw Triangle...
```

*(ps：说实话这一篇文章虽然写了两天.. 但感觉总体质量挺差的.. 原因有许多，一来是发现存在很多知识点交叉的情况——也就是说知识是互相联系的，想要说清楚不容易——而且常常组织起来非常庞大。二来是发现光说清楚一个知识点也挺不容易的..所以在考虑新的组织形式.. 最近有接触到一些双向链接的工具.. 探索探索..)*

# 参考资料

1. 《Java 核心技术 卷 I》
2. 《Java 编程思想》
3. Introduction to Computer Science using Java - http://programmedlessons.org/Java9/index.html
4. 极客时间 | 设计模式之美 - https://time.geekbang.org/column/article/177110

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！

*(另外这些基础的知识体系我打算自己偷偷慢慢在博客搭建啦.. 等有确实的成果之后再分享吧.. 公众号还是希望分享更多能对小伙伴们有用的实际的东西.. Respect~)*

