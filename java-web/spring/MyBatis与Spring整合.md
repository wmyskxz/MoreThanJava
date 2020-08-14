![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/MyBatis-与-Spring-整合/7896890-f534c3282f1f6192.png)

## MyBatis—Spring 项目 

目前大部分的 Java 互联网项目，都是用 Spring MVC + Spring + MyBatis 搭建平台的。

使用 [Spring IoC](https://www.jianshu.com/p/20cea9170110) 可以有效的管理各类的 Java 资源，达到即插即拔的功能；通过 [Spring AOP](https://www.jianshu.com/p/994027425b44) 框架，数据库事务可以委托给 Spring 管理，消除很大一部分的事务代码，配合 MyBatis 的高灵活、可配置、可优化 SQL 等特性，完全可以构建高性能的大型网站。 

毫无疑问，MyBatis 和 Spring 两大框架已经成了 Java 互联网技术主流框架组合，它们经受住了大数据量和大批量请求的考验，在互联网系统中得到了广泛的应用。使用 MyBatis-Spring 使得业务层和模型层得到了更好的分离，与此同时，在 Spring 环境中使用 MyBatis 也更加简单，节省了不少代码，甚至可以不用 SqlSessionFactory、 SqlSession 等对象，因为 MyBatis-Spring 为我们封装了它们。

> 摘自：《Java EE 互联网轻量级框架整合开发》

#### 第一步：创建测试工程

第一步，首先在 IDEA 中新建一个名为【MybatisAndSpring】的 WebProject 工程：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/MyBatis-与-Spring-整合/7896890-7c8242b9afa48492.png)

然后在【src】中创建 4 个空包：

- cn.wmyskxz.dao（放置 DAO 数据交互层处理类）
- cn.wmyskxz.mapper（放置 Mapper 代理接口）
- cn.wmyskxz.pojo（放置 Java 实体类）
- cn.wmyskxz.test（放置测试类）

接着新建源文件夹【config】，用于放置各种资源配置文件：

- 在【config / mybatis】下创建一个空的名为 “SqlMapConfig.xml” 的 MyBatis 全局配置文件
- 在【config / spring】下创建一个空的名为 “applicationContext.xml” 的 Spring 资源配置文件
- 在【config / sqlmap】下创建一个空的名为 “UserMapper.xml” 的 Mapper 映射文件。
- 在【config】下创建两个 properties 属性文件，分别为 “db.properties” 和 “log4j.properties”，用于数据库连接和日志系统参数设置。

再在【web】文件夹下新建一个【WEB-INF】默认安全文件夹，并在其下创建一个【classes】和【lib】，并将项目的输出位置，改在【classes】下：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/MyBatis-与-Spring-整合/7896890-f33c52b56b56ad91.png)


工程的完整初始结构如下：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/MyBatis-与-Spring-整合/7896890-2ea182a1f1e9bd9a.png)


#### 第二步：引入依赖 jar 包

第二步，就是要准备项目的依赖 jar 包：

- MyBatis 的包（[MyBatis 3.4.6](https://github.com/mybatis/mybatis-3/releases)）
- Spring 的 jar 包（[Spring 4.3.15](http://repo.spring.io/release/org/springframework/spring/)）
- MyBatis 与 Spring 的整合 jar 包（[mybatis-spring 1.3.2](http://mvnrepository.com/artifact/org.mybatis/mybatis-spring)）
- mysql-connector-java-5.1.21.jar
- junit-4.12.jar

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/MyBatis-与-Spring-整合/7896890-9b60a2c6d0cde57d.png)


在【WEB-INF】文件夹下的【lib】文件夹中放置上面列举的 jar 包，然后添加依赖。

#### 第三步：编写 Spring 配置文件

第三步，编写 Spring 的配置文件：

- 加载数据库连接文件 “db.properties” 中的数据，建立数据源
- 配置 sqlSessionFactory 会话工厂对象

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 加载配置文件 -->
    <context:property-placeholder location="classpath:db.properties"/>

    <!-- 配置数据源 -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!-- sqlSessionFactory -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 加载 MyBatis 的配置文件 -->
        <property name="configLocation" value="mybatis/SqlMapConfig.xml"/>
        <!-- 数据源 -->
        <property name="dataSource" ref="dataSource"/>
    </bean>
</beans>
```

- **头部的信息就是声明 xml 文档配置标签的规则的限制与规范。**
- **“context:property-placeholder”** 配置是**用于读取工程中的静态属性文件**，然后在其他配置中使用时，就**可以采用 “${属性名}” 的方式获取该属性文件中的配置参数值。**
- 配置了一个名为 **“dataSrouce”** 的 bean 的信息，实际上是**连接数据库的数据源。**
- 设置 **sqlSessionFactory** 的 bean 实现类为 MyBatis 与 Spring 整合 jar 包中的 **SqlSessionFactoryBean** 类，**在其中只需要注入两个参数：一个是 MyBatis 的全局配置文件，一个是上面配置的数据源 bean**

#### 第四步：编写 MyBatis 配置文件

第四步，在【mybatis】包下编写 MyBatis 的全局配置文件 SqlMapConfig.xml ：

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!-- settings -->
    <settings>
        <!-- 打开延迟加载的开关 -->
        <setting name="lazyLoadingEnabled" value="true"/>
        <!-- 将积极加载改为消极加载（即按需加载） -->
        <setting name="aggressiveLazyLoading" value="false"/>
        <!-- 打开全局缓存开关（二级缓存）默认值就是 true -->
        <setting name="cacheEnabled" value="true"/>
    </settings>

    <!-- 别名定义 -->
    <typeAliases>
        <package name="cn.wmyskxz.pojo"/>
    </typeAliases>

    <!-- 加载映射文件 -->
    <mappers>
        <!-- 通过 resource 方法一次加载一个映射文件 -->
        <mapper resource="sqlmap/UserMapper.xml"/>
        <!-- 批量加载mapper -->
        <package name="cn.wmyskxz.mapper"/>
    </mappers>
</configuration>
```

在该配置文件中：

- 通过 **settings 配置了一些延迟加载和缓存的开关信息**
- 在 **typeAliases 中设置了一个 package 的别名扫描路径**，在该路径下的 Java 实体类都可以拥**有一个别名（即首字母小写的类名）**
- 在 mappers 配置中，使用 mapper 标签配置了即将要加载的 Mapper 映射文件的资源路径，当然也可以使用 package 标签，配置 mapper 代理接口所在的包名，以批量加载 mapper 代理对象。
- **注意：** 有了 Spring 托管数据源，在 MyBatis 配置文件中仅仅需要关注性能化配置。

#### 第五步：编写 Mapper 以及其他配置文件

第五步，编写 Mapper 映射文件，这里依然定义 Mapper 映射文件的名字为 “UserMapper.xml” （与 SqlMapConfig.xml 中配置一致），为了测试效果，只配置了一个查询类 SQL 映射：

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="test">
    <select id="findUserById" parameterType="_int" resultType="user">
    SELECT * FROM USER WHERE id = #{id}
</select>
</mapper>
```

在该配置中，输出参数的映射为 “user” ，这是因为之前在 SqlMapConfig.xml 中配置了 “cn.wmyskxz.pojo” 包下的实体类使用别名（即首字母小写的类名），所以这里只需在 “cn.wmyskxz.pojo” 包下，创建 “finduserById” 对应的 Java 实体类 User：

```
package cn.wmyskxz.pojo;

import java.io.Serializable;

public class User implements Serializable {
	private int id;
	private String username;

	/* getter and setter */
}
```

- 实现 Serializable 接口是为之后使用 Mapper 动态代理做准备，这里没有使用动态代理。

在数据库资源 “db.properties” 中配置了数据库的连接信息，以 “key=value” 的形式配置，String 正是使用 “${}” 获取其中 key 对应的 value 配置的：
```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatis?characterEncoding=UTF-8
jdbc.username=root
jdbc.password=root
```

另外日志配置和[之前的配置](https://www.jianshu.com/p/76d35d939539)一样，我就直接黏贴了：

```
# Global logging configuration
# 在开发环境下日志级别要设置成 DEBUG ，生产环境设为 INFO 或 ERROR
log4j.rootLogger=DEBUG, stdout
# Console output...
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

#### 第六步：编写 DAO 层

第六步，进行数据库交互（Data Access Object）层的编写。

由于该项目只对 User 用户查询，所以 DAO 层就只有一个类，在 “cn.wmyskxz” 包下创建 DAO 层的 interface 接口，其中定义了 findUserById 方法，参数为用户的 id 值（int 类型）：

``` 
package cn.wmyskxz.dao;

import cn.wmyskxz.pojo.User;

public interface UserDAO {

	// 根据 id 查询用户信息
	public User findUserById(int id) throws Exception;
}
```

然后在同一个包下创建 UserDAO 接口的实现类 UserDAOImpl:

```
package cn.wmyskxz.dao;

import cn.wmyskxz.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

public class UserDAOImpl extends SqlSessionDaoSupport implements UserDAO {

	@Override
	public User findUserById(int id) throws Exception {
		// 继承 SqlSessionDaoSupport 类，通过 this.getSqlSession() 得到 sqlSession
		SqlSession sqlSession = this.getSqlSession();
		User user = sqlSession.selectOne("test.findUserById", id);
		return user;
	}
}
```

- **有几点解释：**
- **UserDAOImpl 不仅实现了 UserDAO 接口，而且继承了 SqlSessionDaoSupport 类。**
- SqlSessionDaoSupport 类是 MyBatis 与 Spring 整合的 jar 包中提供的，**在该类中已经包含了 sqlSessionFactory 对象作为其成员变量**，而且对外提供 get 和 set 方法，方便 Spring 从外部注入 sqlSessionFactory 对象。
- UserDAOImpl 类要成功获取 sqlSessionFactory 对象，还**需要在 Spring 配置文件 applicationContext.xml 中添加 userDAO 的 bean 配置，将其中定义的 sqlSessionFactory 对象当做参数注入进去**，这样 UserDAOImpl 继承 SqlSessionDaoSupport 类才会起到作用：

```
<!-- 原始 DAO 接口 -->
<bean id="userDAO" class="cn.wmyskxz.dao.UserDAOImpl">
    <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
</bean>
```

- **注意：** DAO 实现类继承了 SqlSessionDaoSupport 父类后，就无须自己定义获取 SqlSession 会话实例类方法了，该父类会默认加载数据源信息并提供获取 SqlSession 类的方法。

#### 第七步：编写 Service 测试类

在 “cn.wmyskxz.test” 包下创建【UserServiceTest】测试类：

```
package cn.wmyskxz.test;

import cn.wmyskxz.dao.UserDAO;
import cn.wmyskxz.pojo.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceTest {

	private ApplicationContext applicationContext;

	// 在执行测试方法之前首先获取 Spring 配置文件对象
	// 注解@Before在执行本类所有测试方法之前先调用这个方法
	@Before
	public void setup() throws Exception {
		applicationContext = new
				ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
	}

	@Test
	public void testFindUserById() throws Exception {
		// 通过配置资源对象获取 userDAO 对象
		UserDAO userDAO = (UserDAO) applicationContext.getBean("userDAO");
		// 调用 UserDAO 的方法
		User user = userDAO.findUserById(1);
		// 输出用户信息
		System.out.println(user.getId() + ":" + user.getUsername());
	}
}
```

运行测试方法，输出结果如下：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/MyBatis-与-Spring-整合/7896890-3aa5986831a67175.png)

---

## 动态代理 + 注解实现

上面的实例程序并没有使用 Mapper 动态代理和注解来完成，下面我们就来试试如何用动态代理和注解：

#### 第一步：编写 UserQueryMapper

在【mapper】下新建一个【UserQueryMapper】代理接口，并使用注解：

```
package cn.wmyskxz.mapper;

import cn.wmyskxz.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserQueryMapper {

	@Select("SELECT * FROM USER WHERE id = #{id}")
	public User findUserById(int id) throws Exception;
}
```

- **注意：** 在默认情况下，该 bean 的名字为 userQueryMapper（即首字母小写）

现在有了代理类，我们需要通知 Spring 在这里来扫描到该类，Mapper 扫描配置对象需要用专门的扫描器：

```
<!-- Mapper 扫描器 -->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <!-- 扫描 cn.wmyskxz.mapper 包下的组件 -->
    <property name="basePackage" value="cn.wmyskxz.mapper"/>
</bean>
```

#### 第二步：编写测试类

这一次我们获取的不再是 userDAO 对象，而是定义的 Mapper 代理对象 userQueryMapper：

```
package cn.wmyskxz.test;

import cn.wmyskxz.mapper.UserQueryMapper;
import cn.wmyskxz.pojo.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceTest {

	private ApplicationContext applicationContext;

	// 在执行测试方法之前首先获取 Spring 配置文件对象
	// 注解@Before在执行本类所有测试方法之前先调用这个方法
	@Before
	public void setup() throws Exception {
		applicationContext = new
				ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
	}

	@Test
	public void testFindUserById() throws Exception {
		// 通过配置资源对象获取 userDAO 对象
		UserQueryMapper userQueryMapper = (UserQueryMapper) applicationContext.getBean("userQueryMapper");
		// 调用 UserDAO 的方法
		User user = userQueryMapper.findUserById(1);
		// 输出用户信息
		System.out.println(user.getId() + ":" + user.getUsername());
	}
}
```

运行测试方法，得到正确结果：

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/MyBatis-与-Spring-整合/7896890-8543b70d3880b5ac.png)

可以看到，查询结果和之前非 Mapper 代理的查询结果一样。

- **原理：** 在 applicationContext.xml 配置文件中配置的 **mapper 批量扫描器类，会从 mapper 包中扫描出 Mapper 接口，自动创建代理对象并且在 Spring 容器中注入。**自动扫描出来的 Mapper 的 bean 的 id 为 mapper 类名（首字母小写），所以这里获取的就是名为 “userQueryMapper” 的 mapper 代理对象。

#### 参考资料：
- 《Java EE 互联网轻量级框架整合开发》
- 《Spring MVC + MyBatis开发从入门到项目实战》
- 全能的百度和万能的大脑


> 欢迎转载，转载请注明出处！ 
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)
> github：[wmyskxz](https://github.com/wmyskxz/)
> 欢迎关注公众微信号：wmyskxz_javaweb
> 分享自己的Java Web学习之路以及各种Java学习资料
