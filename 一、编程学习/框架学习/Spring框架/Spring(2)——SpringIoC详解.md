![](https://upload-images.jianshu.io/upload_images/7896890-34e6864b15c793ec.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## Spring IoC 概述
#### IoC：Inverse of Control（控制反转）
- 读作 **“反转控制”**，更好理解，不是什么技术，而是一种**设计思想**，就是**将原本在程序中手动创建对象的控制权，交由Spring框架来管理。**
- **正控：**若要使用某个对象，需要**自己去负责对象的创建**
- **反控：**若要使用某个对象，只需要**从 Spring 容器中获取需要使用的对象，不关心对象的创建过程**，也就是把**创建对象的控制权反转给了Spring框架**
- **好莱坞法则：** Don’t call me ,I’ll call you

#### 一个例子

控制反转显然是一个抽象的概念，我们举一个鲜明的例子来说明。

在现实生活中，人们要用到一样东西的时候，第一反应就是去找到这件东西，比如想喝新鲜橙汁，在没有饮品店的日子里，最直观的做法就是：买果汁机、买橙子，然后准备开水。值得注意的是：这些都是你自己**“主动”创造**的过程，也就是说一杯橙汁需要你自己创造。

![](https://upload-images.jianshu.io/upload_images/7896890-e460070aba0d8ab0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然而到了今时今日，由于饮品店的盛行，当我们想喝橙汁时，第一想法就转换成了找到饮品店的联系方式，通过电话等渠道描述你的需要、地址、联系方式等，下订单等待，过一会儿就会有人送来橙汁了。

![](https://upload-images.jianshu.io/upload_images/7896890-5cebd72ddc461d18.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**请注意你并没有“主动”去创造橙汁**，橙汁是由饮品店创造的，而不是你，然而也完全达到了你的要求，甚至比你创造的要好上那么一些。

#### Spring IoC 阐述

这就是一种控制反转的理念，上述的例子已经很好的说明了问题，我们再来描述一下控制反转的概念：**控制反转是一种通过描述（在 Java 中可以是 XML 或者注解）并通过第三方（Spring）去产生或获取特定对象的方式。** 

- **好处：**
降低对象之间的耦合
我们不需要理解一个类的具体实现，只需要知道它有什么用就好了（直接向 IoC 容器拿）

主动创建的模式中，责任归于开发者，而在被动的模式下，责任归于 IoC 容器，**基于这样的被动形式，我们就说对象被控制反转了。（也可以说是反转了控制）**

---

## Spring IoC 容器

Spring 会提供 **IoC 容器**来管理和容纳我们所开发的各种各样的 Bean，并且我们可以从中获取各种发布在 Spring IoC 容器里的 Bean，并且**通过描述**可以得到它。

![](https://upload-images.jianshu.io/upload_images/7896890-5b59278f85cd6086.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### Spring IoC 容器的设计
Spring IoC 容器的设计主要是基于以下两个接口：

- **BeanFactory**
- **ApplicationContext** 

其中 ApplicationContext 是 BeanFactory 的子接口之一，换句话说：**BeanFactory 是 Spring IoC 容器所定义的最底层接口，**而 ApplicationContext 是其最高级接口之一，并对 BeanFactory 功能做了许多的扩展，所以在**绝大部分的工作场景下**，都会使用 ApplicationContext 作为 Spring IoC 容器。

![ApplicationContext 继承关系](https://upload-images.jianshu.io/upload_images/7896890-539d3860abb6b3f7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### BeanFactory
从上图中我们可以几乎看到， BeanFactory 位于设计的最底层，它提供了 Spring IoC 最底层的设计，为此，我们先来看看该类中提供了哪些方法：

![](https://upload-images.jianshu.io/upload_images/7896890-ec7f8eb4cc563216.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

由于这个接口的重要性，所以有必要在这里作一下简短的说明：

- 【getBean】 对应了多个方法来获取配置给 Spring IoC 容器的 Bean。
① 按照类型拿 bean：
`bean = (Bean) factory.getBean(Bean.class);`
**注意：** 要求在 Spring 中只配置了一个这种类型的实例，否则报错。（如果有多个那 Spring 就懵了，不知道该获取哪一个）
② 按照 bean 的名字拿 bean:
`bean = (Bean) factory.getBean("beanName");`
**注意：** 这种方法不太安全，IDE 不会检查其安全性（关联性）
③ 按照名字和类型拿 bean：**（推荐）**
`bean = (Bean) factory.getBean("beanName", Bean.class);`
- 【isSingleton】 用于判断是否单例，如果判断为真，其意思是该 Bean 在容器中是作为一个唯一单例存在的。而【isPrototype】则相反，如果判断为真，意思是当你从容器中获取 Bean，容器就为你生成一个新的实例。
**注意：** 在默认情况下，【isSingleton】为 ture，而【isPrototype】为 false
- 关于 type 的匹配，这是一个按 Java 类型匹配的方式
- 【getAliases】方法是获取别名的方法

这就是 Spring IoC 最底层的设计，所有关于 Spring IoC 的容器将会遵守它所定义的方法。

#### ApplicationContext

根据 ApplicationContext 的类继承关系图，可以看到 ApplicationContext 接口扩展了许许多多的接口，因此它的功能十分强大，所以在实际应用中常常会使用到的是 ApplicationContext 接口，因为 BeanFactory 的方法和功能较少，而 ApplicationContext 的方法和功能较多。

通过[上一篇 IoC 的例子](https://www.jianshu.com/p/1af66a499f49)，我们来认识一个 ApplicationContext 的子类——ClassPathXmlApplicationContext。

1. 先在【src】目录下创建一个 【bean.xml】 文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 通过 xml 方式装配 bean -->
    <bean name="source" class="pojo.Source">
        <property name="fruit" value="橙子"/>
        <property name="sugar" value="多糖"/>
        <property name="size" value="超大杯"/>
    </bean>
</beans>
```

2. 这里定义了一个 bean ，这样 Spring IoC 容器在初始化的时候就能找到它们，然后使用 ClassPathXmlApplicationContext 容器就可以将其初始化：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
Source source = (Source) context.getBean("source", Source.class);

System.out.println(source.getFruit());
System.out.println(source.getSugar());
System.out.println(source.getSize());
```

这样就会使用 Application 的实现类 ClassPathXmlApplicationContext 去初始化 Spring IoC 容器，然后开发者就可以通过 IoC 容器来获取资源了啦！

> 关于 Spring Bean 的装配以及一些细节，会在下一篇文章中讲到

#### ApplicationContext 常见实现类：
1.**ClassPathXmlApplicationContext：**
读取classpath中的资源

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
```

2:**FileSystemXmlApplicationContext:-**
读取指定路径的资源

```java
ApplicationContext ac = new FileSystemXmlApplicationContext("c:/applicationContext.xml");
```

3.**XmlWebApplicationContext:**
需要在Web的环境下才可以运行

```java
XmlWebApplicationContext ac = new XmlWebApplicationContext(); // 这时并没有初始化容器
ac.setServletContext(servletContext); // 需要指定ServletContext对象
ac.setConfigLocation("/WEB-INF/applicationContext.xml"); // 指定配置文件路径，开头的斜线表示Web应用的根目录
ac.refresh(); // 初始化容器
```

#### BeanFactory 和 ApplicationContext 的区别：

- **BeanFactory：** 是Spring中最底层的接口，只提供了最简单的IoC功能,负责配置，创建和管理bean。
在应用中，一般不使用 BeanFactory，而推荐使用ApplicationContext（应用上下文），原因如下。
- **ApplicationContext：**
1.继承了 BeanFactory，拥有了基本的 IoC 功能；
2.除此之外，ApplicationContext 还提供了以下功能：
① 支持国际化；
② 支持消息机制；
③ 支持统一的资源加载；
④ 支持AOP功能；

---

## Spring IoC 的容器的初始化和依赖注入
虽然 Spring IoC 容器的生成十分的复杂，但是大体了解一下 Spring IoC 初始化的过程还是必要的。这对于理解 Spring 的一系列行为是很有帮助的。

**注意：** Bean 的定义和初始化在 Spring IoC 容器是两大步骤，它是先定义，然后初始化和依赖注入的。

- **Bean 的定义分为 3 步：**
1.Resource 定位
Spring IoC 容器先根据开发者的配置，进行资源的定位，在 Spring 的开发中，通过 XML 或者注解都是十分常见的方式，定位的内容是由开发者提供的。
2.BeanDefinition 的载入
这个时候只是将 Resource 定位到的信息，保存到 Bean 定义（BeanDefinition）中，此时并不会创建 Bean 的实例
3.BeanDefinition 的注册
这个过程就是将 BeanDefinition 的信息发布到 Spring IoC 容器中
**注意：**此时仍然没有对应的 Bean 的实例。

做完了以上 3 步，Bean 就在 Spring IoC 容器中被定义了，而没有被初始化，更没有完成依赖注入，也就是没有注入其配置的资源给 Bean，那么它还不能完全使用。

对于初始化和依赖注入，Spring Bean 还有一个配置选项——**【lazy-init】**，其含义就是**是否初始化 Spring Bean**。在没有任何配置的情况下，它的默认值为 default，实际值为 false，也就是 **Spring IoC 默认会自动初始化 Bean**。如果将其设置为 true，那么只有当我们使用 Spring IoC 容器的 getBean 方法获取它时，它才会进行 Bean 的初始化，完成依赖注入。

---

## IoC 是如何实现的
最后我们简单说说IoC是如何实现的。想象一下如果我们自己来实现这个依赖注入的功能，我们怎么来做？ 无外乎：

1.  读取标注或者配置文件，看看JuiceMaker依赖的是哪个Source，拿到类名
2.  使用反射的API，基于类名实例化对应的对象实例
3.  将对象实例，通过构造函数或者 setter，传递给 JuiceMaker

我们发现其实自己来实现也不是很难，Spring实际也就是这么做的。这么看的话其实IoC就是一个工厂模式的升级版！当然要做一个成熟的IoC框架，还是非常多细致的工作要做，Spring不仅提供了一个已经成为业界标准的Java IoC框架，还提供了更多强大的功能，所以大家就别去造轮子啦！希望了解IoC更多实现细节不妨通过学习Spring的源码来加深理解！

> 引用地址：[这里](https://link.jianshu.com/?t=https%3A%2F%2Fwww.tianmaying.com%2Ftutorial%2Fspring-ioc)
> 【参考资料】:《Java EE 互联网轻量级框架整合开发》、《Spring 实战（第四版）》
> 【好文推荐】：[①Spring 的本质系列(1) -- 依赖注入](https://mp.weixin.qq.com/s?__biz=MzAxOTc0NzExNg==&mid=2665513179&idx=1&sn=772226a5be436a0d08197c335ddb52b8&scene=21#wechat_redirect)、 [②Spring的IoC原理](https://www.tianmaying.com/tutorial/spring-ioc)

---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693
