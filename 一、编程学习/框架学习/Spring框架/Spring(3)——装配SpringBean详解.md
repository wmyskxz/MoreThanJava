![](https://upload-images.jianshu.io/upload_images/7896890-34e6864b15c793ec.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 装配 Bean 的概述

前面已经介绍了 Spring IoC 的理念和设计，这一篇文章将介绍的是如何将自己开发的 Bean 装配到 Spring IoC 容器中。

大部分场景下，我们都会使用 ApplicationContext 的具体实现类，因为对应的 Spring IoC 容器功能相对强大。

而在 Spring 中提供了 3 种方法进行配置：

- 在 XML 文件中显式配置
- 在 Java 的接口和类中实现配置
- 隐式 Bean 的发现机制和自动装配原则

#### 方式选择的原则
在现实的工作中，这 3 种方式都会被用到，并且在学习和工作之中常常混合使用，所以这里给出一些关于这 3 种优先级的建议：

1.**最优先：通过隐式 Bean 的发现机制和自动装配的原则。**
基于约定由于配置的原则，这种方式应该是最优先的

- **好处：** 减少程序开发者的决定权，简单又不失灵活。

2.**其次：Java 接口和类中配置实现配置**
在没有办法使用自动装配原则的情况下应该优先考虑此类方法

- **好处：** 避免 XML 配置的泛滥，也更为容易。
- **典型场景：** 一个父类有多个子类，比如学生类有两个子类，一个男学生类和女学生类，通过 IoC 容器初始化一个学生类，容器将无法知道使用哪个子类去初始化，这个时候可以使用 Java 的注解配置去指定。

3.**最后：XML 方式配置**
在上述方法都无法使用的情况下，那么也只能选择 XML 配置的方式。
- **好处：** 简单易懂（当然，特别是对于初学者）
- **典型场景：** 当使用第三方类的时候，有些类并不是我们开发的，我们无法修改里面的代码，这个时候就通过 XML 的方式配置使用了。

---

## 通过 XML 配置装配 Bean

使用 XML 装配 Bean 需要定义对应的 XML，这里需要引入对应的 XML 模式（XSD）文件，这些文件会定义配置 Spring Bean 的一些元素，当我们在 IDEA 中创建 XML 文件时，会有友好的提示：

![](https://upload-images.jianshu.io/upload_images/7896890-d5d95a897f81f022.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

一个简单的 XML 配置文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

</beans>
```

这就只是一个格式文件，引入了一个 beans 的定义，引入了 xsd 文件，它是一个根元素，这样它所定义的元素将可以定义对应的 Spring Bean

#### 装配简易值

先来一个最简单的装配：

```xml
<bean id="c" class="pojo.Category">
    <property name="name" value="测试" />
</bean>
```

简单解释一下：

- `id` 属性是 Spring 能找到当前 Bean 的一个依赖的编号，**遵守 XML 语法的 ID 唯一性约束。必须以字母开头，**可以使用*字母、数字、连字符、下划线、句号、冒号*，**不能以 `/` 开头**。
不过 `id` 属性**不是一个必需的属性**，`name` 属性也可以定义 bean 元素的名称，能以逗号或空格隔开**起多个别名**，并且可以**使用很多的特殊字符**，比如在 Spring 和 Spring MVC 的整合中，就得使用 `name` 属性来定义 bean 的名称，并且使用 `/` 开头。
**注意：** 从 Spring 3.1 开始，`id` 属性也可以是 String 类型了，也就是说 `id` 属性也可以使用 `/` 开头，而 bean 元素的 id 的唯一性由容器负责检查。
如果 `id` 和 `name` 属性都没有声明的话，那么 Spring 将会采用 **“全限定名#{number}”** 的格式生成编号。 例如这里，如果没有声明 “`id="c"`” 的话，那么 Spring 为其生成的编号就是 “`pojo.Category#0`”，当它第二次声明没有 `id` 属性的 Bean 时，编号就是 “`pojo.Category#1`”，以此类推。
- `class` 属性显然就是一个类的全限定名
- `property` 元素是定义类的属性，其中的 `name` 属性定义的是属性的名称，而 `value` 是它的值。

这样的定义很简单，但是有时候需要注入一些自定义的类，比如之前饮品店的例子，JuickMaker 需要用户提供原料信息才能完成 juice 的制作：

```xml
<!-- 配置 srouce 原料 -->
<bean name="source" class="pojo.Source">
    <property name="fruit" value="橙子"/>
    <property name="sugar" value="多糖"/>
    <property name="size" value="超大杯"/>
</bean>

<bean name="juickMaker" class="pojo.JuiceMaker">
    <!-- 注入上面配置的id为srouce的Srouce对象 -->
    <property name="source" ref="source"/>
</bean>
```

这里先定义了一个 `name` 为 source 的 Bean，然后再制造器中**通过 `ref` 属性**去引用对应的 Bean，而 source 正是之前定义的 Bean 的 `name` ，这样就可以相互引用了。

- **注入对象：**使用 `ref` 属性

#### 装配集合

有些时候我们需要装配一些复杂的东西，比如 Set、Map、List、Array 和 Properties 等，为此我们在 Packge【pojo】下新建一个 ComplexAssembly 类：

```java
package pojo;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ComplexAssembly {
	
	private Long id;
	private List<String> list;
	private Map<String, String> map;
	private Properties properties;
	private Set<String> set;
	private String[] array;

    /* setter and getter */
}
```

这个 Bean 没有任何的实际意义，知识为了介绍如何装配这些常用的集合类：

```xml
<bean id="complexAssembly" class="pojo.ComplexAssembly">
    <!-- 装配Long类型的id -->
    <property name="id" value="1"/>
    
    <!-- 装配List类型的list -->
    <property name="list">
        <list>
            <value>value-list-1</value>
            <value>value-list-2</value>
            <value>value-list-3</value>
        </list>
    </property>
    
    <!-- 装配Map类型的map -->
    <property name="map">
        <map>
            <entry key="key1" value="value-key-1"/>
            <entry key="key2" value="value-key-2"/>
            <entry key="key3" value="value-key-2"/>
        </map>
    </property>
    
    <!-- 装配Properties类型的properties -->
    <property name="properties">
        <props>
            <prop key="prop1">value-prop-1</prop>
            <prop key="prop2">value-prop-2</prop>
            <prop key="prop3">value-prop-3</prop>
        </props>
    </property>
    
    <!-- 装配Set类型的set -->
    <property name="set">
        <set>
            <value>value-set-1</value>
            <value>value-set-2</value>
            <value>value-set-3</value>
        </set>
    </property>
    
    <!-- 装配String[]类型的array -->
    <property name="array">
        <array>
            <value>value-array-1</value>
            <value>value-array-2</value>
            <value>value-array-3</value>
        </array>
    </property>
</bean>
```

- **总结：**
- List 属性为对应的 `<list>` 元素进行装配，然后通过多个 `<value>` 元素设值
- Map 属性为对应的 `<map>` 元素进行装配，然后通过多个 `<entry>` 元素设值，只是 `entry` 包含一个键值对(key-value)的设置
- Properties 属性为对应的 `<properties>` 元素进行装配，通过多个 `<property>` 元素设值，只是 `properties` 元素有一个必填属性 `key` ，然后可以设置值
- Set 属性为对应的 `<set>` 元素进行装配，然后通过多个 `<value>` 元素设值
- 对于数组而言，可以使用 `<array>` 设置值，然后通过多个 `<value>` 元素设值。

上面看到了对简单 String 类型的各个集合的装载，但是有些时候可能需要更为复杂的装载，比如一个 List 可以是一个系列类的对象，为此需要定义注入的相关信息，其实跟上面的配置没什么两样，只不过加入了 `ref` 这一个属性而已：

- **集合注入总结：**
- List 属性使用 `<list>` 元素定义注入，使用多个 `<ref>` 元素的 Bean 属性去引用之前定义好的 Bean

```xml
<property name="list">
    <list>
        <ref bean="bean1"/>
        <ref bean="bean2"/>
    </list>
</property>
```

- Map 属性使用 `<map>` 元素定义注入，使用多个 `<entry>` 元素的 `key-ref` 属性去引用之前定义好的 Bean 作为键，而用 `value-ref` 属性引用之前定义好的 Bean 作为值

```xml
<property name="map">
    <map>
        <entry key-ref="keyBean" value-ref="valueBean"/>
    </map>
</property>
```

- Set 属性使用 `<set>` 元素定义注入，使用多个 `<ref>` 元素的 `bean` 去引用之前定义好的 Bean 

```xml
<property name="set">
    <set>
        <ref bean="bean"/>
    </set>
</property>
```

#### 命名空间装配

除了上述的配置之外， Spring 还提供了对应的命名空间的定义，只是在使用命名空间的时候要先引入对应的命名空间和 XML 模式（XSD）文件。

##### ——【① c-命名空间】——
c-命名空间是在 Spring 3.0 中引入的，它是在 XML 中更为简洁地描述构造器参数的方式，要使用它的话，必须要在 XML 的顶部声明其模式：

![](https://upload-images.jianshu.io/upload_images/7896890-80f5689d01764e9d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- **注意：是通过构造器参数的方式**

现在假设我们现在有这么一个类：

```java
package pojo;

public class Student {

	int id;
	String name;

	public Student(int id, String name) {
		this.id = id;
		this.name = name;
	}
    // setter and getter
}
```


在 c-命名空间和模式声明之后，我们就可以使用它来声明构造器参数了：

```xml
<!-- 引入 c-命名空间之前 -->
<bean name="student1" class="pojo.Student">
    <constructor-arg name="id" value="1" />
    <constructor-arg name="name" value="学生1"/>
</bean>

<!-- 引入 c-命名空间之后 -->
<bean name="student2" class="pojo.Student"
      c:id="2" c:name="学生2"/>
```

c-命名空间属性名**以 “`c:`” 开头**，也就是命名空间的前缀。接下来就是**要装配的构造器参数名**，在此之后如果需要注入对象的话则要跟上 `-ref`（如`c:card-ref="idCard1"`，则对 card 这个构造器参数注入之前配置的名为 idCard1 的 bean） 

很显然，使用 c-命名空间属性要比使用 `<constructor-arg>` 元素精简，并且会直接引用构造器之中参数的名称，这有利于我们使用的安全性。

我们有另外一种替代方式：

```xml
<bean name="student2" class="pojo.Student"
      c:_0="3" c:_1="学生3"/>
```

我们将参数的名称替换成了 “0” 和 “1” ，也就是参数的索引。因为在 XML 中不允许数字作为属性的第一个字符，因此必须要添加一个下划线来作为前缀。

##### ——【② p-命名空间】——
c-命名空间通过构造器注入的方式来配置 bean，p-命名空间则是用setter的注入方式来配置 bean ，同样的，我们需要引入声明：

![](https://upload-images.jianshu.io/upload_images/7896890-1f4edf39d05392c5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后我们就可以通过 p-命名空间来设置属性：

```xml
<!-- 引入p-命名空间之前 -->
<bean name="student1" class="pojo.Student">
    <property name="id" value="1" />
    <property name="name" value="学生1"/>
</bean>

<!-- 引入p-命名空间之后 -->
<bean name="student2" class="pojo.Student" 
      p:id="2" p:name="学生2"/>
```

我们需要先删掉 Student 类中的构造函数，不然 XML 约束会提示我们配置 `<constructor-arg>` 元素。

同样的，如果属性需要注入其他 Bean 的话也可以在后面跟上 `-ref`：

```xml
    <bean name="student2" class="pojo.Student"
          p:id="2" p:name="学生2" p:cdCard-ref="cdCard1"/>
```

##### ——【③ util-命名空间】——

工具类的命名空间，可以简化集合类元素的配置，同样的我们需要引入其声明（无需担心怎么声明的问题，IDEA会有很友好的提示）：

![](https://upload-images.jianshu.io/upload_images/7896890-f9dee3e8ab30990c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们来看看引入前后的变化：

```xml
<!-- 引入util-命名空间之前 -->
<property name="list">
    <list>
        <ref bean="bean1"/>
        <ref bean="bean2"/>
    </list>
</property>

<!-- 引入util-命名空间之后 -->
<util:list id="list">
    <ref bean="bean1"/>
    <ref bean="bean2"/>
</util:list>
```

`<util:list>` 只是 util-命名空间中的多个元素之一，下表提供了 util-命名空间提供的所有元素：

元素 | 描述
:-- | :--
`<util:constant>` | 引用某个类型的 `public static` 域，并将其暴露为 bean
`<util:list>` | 创建一个 `java.util.List` 类型的 bean，其中包含值或引用
`<util:map>` | 创建一个 `java.util.map` 类型的 bean，其中包含值或引用
`<util:properties>` | 创建一个 `java.util.Properties` 类型的 bean
`<util:property-path>` | 引用一个 bean 的属性（或内嵌属性），并将其暴露为 bean
`<util:set>` | 创建一个 `java.util.Set` 类型的 bean，其中包含值或引用


#### 引入其他配置文件
在实际开发中，随着应用程序规模的增加，系统中 `<bean>` 元素配置的数量也会大大增加，导致 applicationContext.xml 配置文件变得非常臃肿难以维护。

- **解决方案：**让 applicationContext.xml 文件包含其他配置文件即可
使用 `<import>` 元素引入其他配置文件

1.在【src】文件下新建一个 bean.xml 文件，写好基础的约束，把 applicationContext.xml 文件中配置的 `<bean>` 元素复制进去

2.在 applicationContext.xml 文件中写入：

```xml
<import resource="bean.xml" />
```

3.运行测试代码，仍然能正确获取到 bean:

![](https://upload-images.jianshu.io/upload_images/7896890-9636b07a81db1c16.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


---

## 通过注解装配 Bean

上面，我们已经了解了如何使用 XML 的方式去装配 Bean，但是更多的时候已经不再推荐使用 XML 的方式去装配 Bean，更多的时候回考虑使用注解（annotation） 的方式去装配 Bean。

- **优势：**
1.可以减少 XML 的配置，当配置项多的时候，臃肿难以维护
2.功能更加强大，既能实现 XML 的功能，也提供了自动装配的功能，采用了自动装配后，程序猿所需要做的决断就少了，更加有利于对程序的开发，这就是“约定由于配置”的开发原则

在 Spring 中，它提供了两种方式来让 Spring IoC 容器发现 bean：

- **组件扫描：**通过定义资源的方式，让 Spring IoC 容器扫描对应的包，从而把 bean 装配进来。
- **自动装配：**通过注解定义，使得一些依赖关系可以通过注解完成。

### 使用@Compoent 装配 Bean
我们把之前创建的 Student 类改一下：

``` java
package pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "student1")
public class Student {

	@Value("1")
	int id;
	@Value("student_name_1")
	String name;

    // getter and setter
}
```

解释一下：

- **@Component注解：** 
表示 Spring IoC 会把这个类扫描成一个 bean 实例，而其中的 `value` 属性代表这个类在 Spring 中的 `id`，这就相当于在 XML 中定义的 Bean 的 id：`<bean id="student1" class="pojo.Student" />`，也可以简写成 `@Component("student1")`，甚至直接写成 `@Component` ，对于不写的，Spring IoC 容器就默认以类名来命名作为 `id`，只不过首字母小写，配置到容器中。
- **@Value注解：**
表示值的注入，跟在 XML 中写 `value` 属性是一样的。

这样我们就声明好了我们要创建的一个 Bean，就像在 XML 中写下了这样一句话：

```xml
<bean name="student1" class="pojo.Student">
    <property name="id" value="1" />
    <property name="name" value="student_name_1"/>
</bean>
```

但是现在我们声明了这个类，并不能进行任何的测试，因为 Spring IoC 并不知道这个 Bean 的存在，这个时候我们可以使用一个 StudentConfig 类去告诉 Spring IoC ：

```java
package pojo;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class StudentConfig {
}
```

这个类十分简单，没有任何逻辑，但是需要说明两点：

- **该类和 Student 类位于同一包名下**
- **@ComponentScan注解：**
代表进行扫描，**默认是扫描当前包的路径**，扫描所有带有 `@Component` 注解的 POJO。

这样一来，我们就可以通过 Spring 定义好的 Spring IoC 容器的实现类——AnnotationConfigApplicationContext 去生成 IoC 容器了：

```java
ApplicationContext context = new AnnotationConfigApplicationContext(StudentConfig.class);
Student student = (Student) context.getBean("student1", Student.class);
student.printInformation();
```

这里可以看到使用了 AnnotationConfigApplicationContext 类去初始化 Spring IoC 容器，它的配置项是 StudentConfig 类，这样 Spring IoC 就会根据注解的配置去解析对应的资源，来生成 IoC 容器了。

- **明显的弊端：**
- 对于 `@ComponentScan` 注解，它只是扫描所在包的 Java 类，但是更多的时候我们希望的是可以扫描我们指定的类
- 上面的例子只是注入了一些简单的值，测试发现，通过 `@Value` 注解并不能注入对象

`@Component` 注解存在着两个配置项：

- **basePackages：**它是由 base 和 package 两个单词组成的，而 package 还是用了复数，意味着**它可以配置一个 Java 包的数组**，Spring 会根据它的配置扫描对应的包和子包，将配置好的 Bean 装配进来
- **basePackageClasses**：它由 base、package 和 class 三个单词组成，采用复数，意味着它可以配置多个类， Spring 会根据配置的类所在的包，为包和子包进行扫描装配对应配置的 Bean

我们来试着重构之前写的 StudentConfig 类来验证上面两个配置项：

```java
package pojo;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "pojo")
public class StudentConfig {
}

//  —————————————————— 【 宇宙超级无敌分割线】—————————————————— 
package pojo;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = pojo.Student.class)
public class StudentConfig {
}
```

验证都能通过，bingo！

- 对于 【basePackages】 和 【basePackageClasses】 的选择问题：
【basePackages】 的可读性会更好一些，所以在项目中会优先选择使用它，但是在需要大量重构的工程中，尽量不要使用【basePackages】，因为很多时候重构修改包名需要反复地配置，而 IDE 不会给你任何的提示，而采用【basePackageClasses】会有错误提示。

### 自动装配——@Autowired

上面提到的两个弊端之一就是没有办法注入对象，通过自动装配我们将解决这个问题。

所谓自动装配技术是一种**由 Spring 自己发现对应的 Bean，自动完成装配工作的方式，**它会应用到一个十分常用的注解 `@Autowired` 上，这个时候 **Spring 会根据类型去寻找定义的 Bean 然后将其注入**，听起来很神奇，让我们实际来看一看：

1.先在 Package【service】下创建一个 StudentService 接口：

```java
package service;

public interface StudentService {
	public void printStudentInfo();
}
```

使用接口是 Spring 推荐的方式，这样可以更为灵活，可以将定义和实现分离

2.为上面的接口创建一个 StudentServiceImp 实现类：

```java
package service;

import org.springframework.beans.factory.annotation.Autowired;
import pojo.Student;

@Component("studentService")
public class StudentServiceImp implements StudentService {

	@Autowired
	private Student student = null;

     // getter and setter

	public void printStudentInfo() {
		System.out.println("学生的 id 为：" + student.getName());
		System.out.println("学生的 name 为：" + student.getName());
	}
}
```

该实现类实现了接口的 printStudentInfo() 方法，打印出成员对象 student 的相关信息，这里的 `@Autowired` 注解，表示**在 Spring IoC 定位所有的 Bean 后，这个字段需要按类型注入**，这样 IoC 容器就会**寻找资源**，然后将其注入。

3.编写测试类：  

```java
// 第一步：修改 StudentConfig 类，告诉 Spring IoC 在哪里去扫描它：
package pojo;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"pojo", "service"})
public class StudentConfig {
}

// 或者也可以在 XML 文件中声明去哪里做扫描
<context:component-scan base-package="pojo" />
<context:component-scan base-package="service" />

// 第二步：编写测试类：
package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pojo.StudentConfig;
import service.StudentService;
import service.StudentServiceImp;

public class TestSpring {

	public static void main(String[] args) {
		// 通过注解的方式初始化 Spring IoC 容器
		ApplicationContext context = new AnnotationConfigApplicationContext(StudentConfig.class);
		StudentService studentService = context.getBean("studentService", StudentServiceImp.class);
		studentService.printStudentInfo();
	}
}
```

运行代码：

![](https://upload-images.jianshu.io/upload_images/7896890-abfe633e2b86f389.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- **再次理解：** `@Autowired` 注解表示在 Spring IoC 定位所有的 Bean 后，再根据类型寻找资源，然后将其注入。
- **过程：** 定义 Bean  ——》 初始化 Bean（扫描） ——》  根据属性需要从 Spring IoC 容器中搜寻满足要求的 Bean ——》 满足要求则注入
- **问题：** IoC 容器可能会寻找失败，此时会抛出异常（默认情况下，Spring IoC 容器会认为一定要找到对应的 Bean 来注入到这个字段，但有些时候并不是一定需要，比如日志）
- **解决：** 通过配置项 `required` 来改变，比如 `@Autowired(required = false)`

`@Autowired` 注解不仅仅能配置在属性之上，还允许方法配置，常见的 Bean 的 setter 方法也可以使用它来完成注入，总之一切需要 Spring IoC 去寻找 Bean 资源的地方都可以用到，例如：

```java
/* 包名和import */
public class JuiceMaker {
    ......
    @Autowired
    public void setSource(Source source) {
        this.source = source;
    }
}
```

在大部分的配置中都推荐使用这样的自动注入来完成，这是 Spring IoC 帮助我们自动装配完成的，这样使得配置大幅度减少，满足约定优于配置的原则，增强程序的健壮性。

#### 自动装配的歧义性（@Primary和@Qualifier）

在上面的例子中我们使用 `@Autowired` 注解来自动注入一个 Source 类型的 Bean 资源，但如果我们现在有两个 Srouce 类型的资源，Spring IoC 就会不知所措，不知道究竟该引入哪一个 Bean：

```xml
<bean name="source1" class="pojo.Source">
    <property name="fruit" value="橙子"/>
    <property name="sugar" value="多糖"/>
    <property name="size" value="超大杯"/>
</bean>
<bean name="source2" class="pojo.Source">
    <property name="fruit" value="橙子"/>
    <property name="sugar" value="少糖"/>
    <property name="size" value="小杯"/>
</bean>
```

我们可以会想到 Spring IoC 最底层的容器接口——BeanFactory 的定义，它存在一个按照类型获取 Bean 的方法，显然通过 Source.class 作为参数**无法判断使用哪个类实例进行返回**，这就是自动装配的歧义性。

为了消除歧义性，Spring 提供了两个注解：

- **@Primary 注解：**
代表首要的，当 Spring IoC 检测到有多个相同类型的 Bean 资源的时候，会优先注入使用该注解的类。
- **问题：**该注解只是解决了首要的问题，但是并没有选择性的问题
- **@Qualifier 注解：**
上面所谈及的歧义性，一个重要的原因是 Spring 在寻找依赖注入的时候是按照类型注入引起的。除了按类型查找 Bean，Spring IoC 容器最底层的接口 BeanFactory 还提供了按名字查找的方法，如果按照名字来查找和注入不就能消除歧义性了吗？
- **使用方法：** 指定注入名称为 "source1" 的 Bean 资源

```java
/* 包名和import */
public class JuiceMaker {
    ......
    @Autowired
    @Qualifier("source1")
    public void setSource(Source source) {
        this.source = source;
    }
}
```

#### 使用@Bean 装配 Bean

- **问题：** 以上都是通过 `@Component` 注解来装配 Bean ，并且只能注解在类上，当你需要引用第三方包的（jar 文件），而且往往并没有这些包的源码，这时候将无法为这些包的类加入 `@Component` 注解，让它们变成开发环境中的 Bean 资源。
- **解决方案：**
1.自己创建一个新的类来扩展包里的类，然后再新类上使用 `@Component` 注解，**但这样很 low**
2.**使用 `@Bean` 注解，注解到方法之上**，使其成为 Spring 中返回对象为 Spring 的 Bean 资源。

我们在 Package【pojo】 下新建一个用来测试 `@Bean` 注解的类：

```java
package pojo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanTester {

    @Bean(name = "testBean")
    public String test() {
        String str = "测试@Bean注解";
        return str;
    }
}
```

- **注意：** `@Configuration` 注解相当于 XML 文件的根元素，**必须要**，有了才能解析其中的 `@Bean` 注解

然后我们在测试类中编写代码，从 Spring IoC 容器中获取到这个 Bean ：

```java
// 在 pojo 包下扫描
ApplicationContext context = new AnnotationConfigApplicationContext("pojo");
// 因为这里获取到的 Bean 就是 String 类型所以直接输出
System.out.println(context.getBean("testBean"));
```

`@Bean` 的配置项中包含 4 个配置项：

- **name：** 是一个字符串数组，允许配置多个 BeanName
- **autowire：** 标志是否是一个引用的 Bean 对象，默认值是 Autowire.NO
- **initMethod：** 自定义初始化方法
- **destroyMethod：** 自定义销毁方法

使用 `@Bean` 注解的好处就是能够动态获取一个 Bean 对象，能够根据环境不同得到不同的 Bean 对象。或者说将 Spring 和其他组件分离（其他组件不依赖 Spring，但是又想 Spring 管理生成的 Bean）

#### Bean 的作用域

**在默认的情况下，Spring IoC 容器只会对一个 Bean 创建一个实例**，但有时候，我们希望能够通过 Spring IoC 容器获取多个实例，我们可以通过 `@Scope` 注解或者 `<bean>` 元素中的 `scope` 属性来设置，例如：

```xml
// XML 中设置作用域
<bean id="" class="" scope="prototype" />
// 使用注解设置作用域
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
```

Spring 提供了 5 种作用域，它会根据情况来决定是否生成新的对象：

作用域类别 | 描述
:-- | :--
singleton(单例)  | 在Spring IoC容器中仅存在一个Bean实例 （默认的scope）
prototype(多例) | 每次从容器中调用Bean时，都返回一个新的实例，即每次调用getBean()时 ，相当于执行new XxxBean()：不会在容器启动时创建对象
request(请求) | 用于web开发，将Bean放入request范围 ，request.setAttribute("xxx") ， 在同一个request 获得同一个Bean
session(会话) | 用于web开发，将Bean 放入Session范围，在同一个Session 获得同一个Bean 
globalSession(全局会话) | 一般用于 Porlet 应用环境 , 分布式系统存在全局 session 概念（单点登录），如果不是 porlet 环境，globalSession 等同于 Session  

在开发中主要使用 `scope="singleton"`、`scope="prototype"`，**对于MVC中的Action使用prototype类型，其他使用singleton**，Spring容器会管理 Action 对象的创建,此时把 Action 的作用域设置为 prototype.


> 扩展阅读：[@Profile 注解](https://blog.csdn.net/u013803262/article/details/62416880) 、 [条件化装配 Bean](https://blog.csdn.net/tinydolphin/article/details/76253771)

#### Spring 表达式语言简要说明

Spring 还提供了更灵活的注入方式，那就是 Spring 表达式，实际上 Spring EL 远比以上注入方式都要强大，它拥有很多功能：

- 使用 Bean 的 id 来引用 Bean
- 调用指定对象的方法和访问对象的属性
- 进行运算
- 提供正则表达式进行匹配
- 集合配置

我们来看一个简单的使用 Spring 表达式的例子：

``` java
package pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("elBean")
public class ElBean {
    // 通过 beanName 获取 bean，然后注入 
    @Value("#{role}")
    private Role role;
    
    // 获取 bean 的属性 id
    @Value("#{role.id}")
    private Long id;
    
    // 调用 bean 的 getNote 方法
    @Value("#{role.getNote().toString()}")
    private String note;
    /* getter and setter */
}
```

与属性文件中读取使用的 “`$`” 不同，在 Spring EL 中则使用 “`#`”

> 扩展阅读： [Spring 表达式语言](https://www.cnblogs.com/best/p/5748105.html)

#### 参考资料：
- 《Java EE 互联网轻量级框架整合开发》
- 《Java 实战（第四版）》
- 万能的百度 and 万能的大脑


---

> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693

