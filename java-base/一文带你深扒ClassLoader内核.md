![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200827234848003.png)

- **「MoreThanJava」** 宣扬的是 **「学习，不止 CODE」**。
- 如果觉得 **「不错」** 的朋友，欢迎 **「关注 + 留言 + 分享」**，文末有完整的获取链接，您的支持是我前进的最大的动力！

# 前言

ClassLoader 可以说是 Java 最为神秘的功能之一了，好像大家都知道怎么回事儿 *(双亲委派模型好像都都能说得出来...)*，又都说不清楚具体是怎么一回事 *(为什么需要需要有什么实际用途就很模糊了...)*。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/1598311905-57783.jpg)

今天，我们就来深度扒一扒，揭开它神秘的面纱！

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/006qOO1Xly1ghftzela9eg308c05sald.gif)

# Part 1. 类加载是做什么的？

首先，我们知道，Java 为了实现 **「一次编译，到处运行」** 的目标，采用了一种特别的方案：先 **编译** 为 **与任何具体及其环境及操作系统环境无关的中间代码**（也就是 `.class` 字节码文件），然后交由各个平台特定的 Java 解释器（也就是 JVM）来负责 **解释** 运行。

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/%E3%80%8CMoreThanJava%E3%80%8D%E6%9C%BA%E5%99%A8%E6%8C%87%E4%BB%A4%E5%88%B0%E6%B1%87%E7%BC%96%E5%86%8D%E5%88%B0%E9%AB%98%E7%BA%A7%E7%BC%96%E7%A8%8B%E8%AF%AD%E8%A8%80/7896890-81a0bce7dbea21a5.png)

ClassLoader *(顾名思义就是类加载器)* 就是那个把字节码交给 JVM 的搬运工 *（加载进内存）*。它负责将 **字节码形式** 的 Class 转换成 JVM 中 **内存形式** 的 Class 对象。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200826073719796.png)

字节码可以是来自于磁盘上的 `.class` 文件，也可以是 `jar` 包里的 `*.class`，甚至是来自远程服务器提供的字节流。**字节码的本质其实就是一个有特定复杂格式的字节数组 `byte[]`。** *(从后面解析 ClassLoader 类中的方法时更能体会)*

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200825080559568.png)

另外，类加载器不光可以把 Class 加载到 JVM 之中并解析成 JVM 统一要求的对象格式，还有一个重要的作用就是 **审查每个类应该由谁加载**。

而且，这些 Java 类不会一次全部加载到内存，而是在应用程序需要时加载，这也是需要类加载器的地方。

# Part 2. ClassLoader 类结构分析

以下就是 ClassLoader 的主要方法了：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200827083357060.png)

- `defineClass()` 用于将 `byte` 字节流解析成 JVM 能够识别的 Class 对象。有了这个方法意味着我们不仅可以通过 `.class` 文件实例化对象，还可以通过其他方式实例化对象，例如通过网络接收到一个类的字节码。

  *（注意，如果直接调用这个方法生成类的 Class 对象，这个类的 Class 对象还没有 `resolve`，JVM 会在这个对象真正实例化时才调用 `resolveClass()` 进行链接）*

- `findClass()` 通常和 `defineClass()` 一起使用，我们需要直接覆盖 ClassLoader 父类的 `findClass()` 方法来实现类的加载规则，从而取得要加载类的字节码。*(以下是 ClassLoader 源码)*

  ```java
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    throw new ClassNotFoundException(name);
  }
  ```

  如果你不想重新定义加载类的规则，也没有复杂的处理逻辑，只想在运行时能够加载自己制定的一个类，那么你可以用 `this.getClass().getClassLoader().loadClass("class")` 调用 ClassLoader 的 `loadClass()` 方法来获取这个类的 Class 对象，这个 `loadClass()` 还有重载方法，你同样可以决定再什么时候解析这个类。

- `loadClass()` 用于接受一个全类名，然后返回一个 Class 类型的对象。*（该方法源码蕴含了著名的双亲委派模型）*

- `resolveClass()` 用于对 Class 进行 **链接**，也就是把单一的 Class 加入到有继承关系的类树中。如果你想在类被加载到 JVM 中时就被链接（Link），那么可以在调用 `defineClass()` 之后紧接着调用一个 `resolveClass()` 方法，当然你也可以选择让 JVM 来解决什么时候才链接这个类（通常是真正被实实例化的时候）。

ClassLoader 是个抽象类，它还有很多子类，如果我们要实现自己的 ClassLoader，一般都会继承 **URLClassLoader** 这个子类，因为这个类已经帮我们实现了大部分工作。

例如，我们来看一下 `java.net.URLClassLoader.findClass()` 方法的实现：

```java
// 入参为 Class 的 binary name，如 java.lang.String
protected Class<?> findClass(final String name) throws ClassNotFoundException {
    // 以上代码省略
  
    // 通过 binary name 生成包路径，如 java.lang.String -> java/lang/String.class
    String path = name.replace('.', '/').concat(".class");
    // 根据包路径，找到该 Class 的文件资源
    Resource res = ucp.getResource(path, false);
    if (res != null) {
        try {
           // 调用 defineClass 生成 java.lang.Class 对象
            return defineClass(name, res);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    } else {
        return null;
    }
  
    // 以下代码省略
}
```

# Part 3. Java 类加载流程详解

以下就是 ClassLoader 加载一个 class 文件到 JVM 时需要经过的步骤。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200827092015305.png)

事实上，我们每一次在 IDEA 中点击运行时，IDE 都会默认替我们执行以下的命令：

- `javac Xxxx.java` ➡️ 找到源文件中的 `public class`，再找 `public class` 引用的其他类，Java 编译器会根据每一个类生成一个字节码文件；
- `java Xxxx` ➡️ 找到文件中的唯一主类 `public class`，并根据 `public static` 关键字找到跟主类关联可执行的 `main` 方法 *（这也是为什么 `main` 方法需要被定义为 `public static void` 的原因了——我们需要在类没有加载时访问）*，开始执行。

在真正的运行 `main` 方法之前，JVM 需要 **加载、链接** 以及 **初始化** 上述的 Xxxx 类。

## 第一步：加载（Loading）

这一步是读取到类文件产生的二进制流（`findClass()`），并转换为特定的数据结构（`defineClass()`），初步校验 `cafe babe` 魔法数 *（二进制中前四个字节为 `0xCAFEBABE` 用来标识该文件是 Java 文件，这是很多软件的做法，比如 `zip压缩文件`）*、常量池、文件长度、是否有父类等，然后在 Java **堆** 中创建对应类的 `java.lang.Class` 实例，类中存储的各部分信息也需要对应放入 **运行时数据区** 中（例如静态变量、类信息等放入方法区）。

>以下是一个 Class 文件具有的基本结构的简单图示：
>
>![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200827093649056.png)
>
>如果对 Class 文件更多细节感兴趣的可以进一步阅读：https://juejin.im/post/6844904199617003528

这里我们可能会有一个疑问，**为什么 JVM 允许还没有进行验证、准备和解析的类信息放入方法区呢？**

答案是加载阶段和链接阶段的部分动作（比如一部分字节码文件格式验证动作）是 **交叉进行** 的，也就是说 **加载阶段还没完成，链接阶段可能已经开始了**。但这些夹杂在加载阶段的动作（验证文件格式等）仍然属于链接操作。

## 第二步：链接（Linking）

Link 阶段包括验证、准备、解析三个步骤。下面👇我们来详细说说。

### 验证：确保被加载的类的正确性

验证是连接阶段的第一步，这一阶段的目的是 **为了确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全**。验证阶段大致会完成 `4` 个阶段的检验动作：

- **文件格式验证：** 验证字节流是否符合 Class 文件格式的规范；例如：是否以 `0xCAFEBABE` 开头、主次版本号是否在当前虚拟机的处理范围之内、常量池中的常量是否有不被支持的类型。
- **元数据验证：** 对字节码描述的信息进行语义分析（注意：对比 `javac` 编译阶段的语义分析），以保证其描述的信息符合 Java 语言规范的要求；例如：这个类是否有父类，除了 ` java.lang.Object` 之外。
- **字节码验证：** 通过数据流和控制流分析，确定程序语义是合法的、符合逻辑的。
- **符号引用验证：** 确保解析动作能正确执行。

**验证阶段是非常重要的，但不是必须的**，它对程序运行期没有影响，如果所引用的类经过反复验证，那么可以考虑采用 `-Xverifynone` 参数来关闭大部分的类验证措施，以缩短虚拟机类加载的时间。

### 准备：为类的静态变量分配内存，并将其初始化为默认值

准备阶段是正式为类变量分配内存并设置类变量初始值的阶段，这些内存都将在 **方法区** 中分配。对于该阶段有以下几点需要注意：

- 1️⃣ 这时候进行内存分配的 **仅包括类变量**（static），而不包括实例变量，实例变量会在对象实例化时随着对象一块分配在 Java 堆中。

- 2️⃣ 这里所设置的 **初始值通常情况下是数据类型默认的零值**（如 `0`、`0L`、`null`、`false`等），而不是被在 Java 代码中被显式地赋予的值。
- 3️⃣ 如果类字段的字段属性表中存在 ConstantValue 属性，即 **同时被 `final` 和 `static` 修饰**，那么在准备阶段变量 `value` 就会被初始化为 ConstValue 属性所指定的值。

➡️ 例如，假设这里有一个类变量 `public static int value = 666;`，在准备阶段时初始值是 `0` 而不是 `666`，在 **初始化阶段** 才会被真正赋值为 `666`。

➡️ 假设是一个静态类变量 `public static final int value = 666;`，则再准备阶段 JVM 就已经赋值为 `666` 了。

### 解析：把类中的符号引用转换为直接引用（重要）

解析阶段是虚拟机将常量池内的 **符号引用** 替换为 **直接引用** 的过程，解析动作主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用点限定符 `7` 类符号引用进行。

➡️ **符号引用** 的作用是在编译的过程中，JVM 并不知道引用的具体地址，所以用符号引用进行代替，而在解析阶段将会将这个符号引用转换为真正的内存地址。

➡️ **直接引用** 可以理解为指向 **类、变量、方法** 的指针，指向 **实例** 的指针和一个 **间接定位** 到对象的对象句柄。 

为了理解👆上面两种概念的区别，来看一个实际的例子吧：

```java
public class Tester {

    public static void main(String[] args) {
        String str = "关注【我没有三颗心脏】，关注更多精彩";
        System.out.println(str);
    }
}
```

我们先在该类同级目录下运行 `javac Tester` 编译成 `.class` 文件然后再利用 `javap -verbose Tester` 查看类的详细信息 *（为了节省篇幅只截取了 `main` 方法反编译后的代码）*：

```bash
// 上面是类的详细信息省略...
{
	// .....
  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=2, args_size=1
         0: ldc           #7                  // String 关注【我没有三颗心脏】，关注更多精彩
         2: astore_1
         3: getstatic     #9                  // Field java/lang/System.out:Ljava/io/PrintStream;
         6: aload_1
         7: invokevirtual #15                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        10: return
      LineNumberTable:
        line 4: 0
        line 5: 3
        line 6: 10
}
SourceFile: "Tester.java"
```

可以看到，上面👆定义的 `str` 变量在编译阶段会被解析称为 **符号引用**，符号引用的标志是 `astore_<n>`，这里就是 `astore_1`。

`store_1`的含义是将操作数栈顶的 `关注【我没有三颗心脏】，关注更多精彩` 保存回索引为 `1` 的局部变量表中，此时访问变量 `str` 就会读取局部变量表索引值为 `1` 中的数据。所以局部变量 `str` 就是一个符号引用。

再来看另外一个例子：

```java
public class Tester {

    public static void main(String[] args) {
        System.out.println("关注【我没有三颗心脏】，关注更多精彩");
    }
}
```

这一段代码反编译之后得到如下的代码：

```bash
// 上面是类的详细信息省略...
{
  // ......
  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #13                 // String 关注【我没有三颗心脏】，关注更多精彩
         5: invokevirtual #15                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 4: 0
        line 5: 8
}
SourceFile: "Tester.java"

```

我们可以看到这里直接使用了 `ldc` 指令将 `关注【我没有三颗心脏】，关注更多精彩` 推送到了栈，紧接着就是调用指令 `invokevirtual`，并没有将字符串存入局部变量表中，这里的字符串就是一个 **直接引用**。

## 第三步：初始化（Initialization）

初始化，为类的静态变量赋予正确的初始值，JVM 负责对类进行初始化，主要对类变量进行初始化。在 Java 中对类变量进行初始值设定有两种方式：

- 1️⃣ 声明类变量是指定初始值；
- 2️⃣ 使用静态代码块为类变量指定初始值；

**JVM 初始化步骤：**

- 1️⃣ 假如这个类还没有被加载和连接，则程序先加载并连接该类
- 2️⃣ 假如该类的直接父类还没有被初始化，则先初始化其直接父类
- 3️⃣ 假如类中有初始化语句，则系统依次执行这些初始化语句

**类初始化时机**：只有当对类的主动使用的时候才会导致类的初始化，类的主动使用包括以下几种：

- 创建类的实例，也就是 `new` 的方式
- 访问某个类或接口的静态变量，或者对该静态变量赋值
- 调用类的静态方法
- 反射（如 `Class.forName("com.wmyskxz.Tester")`）
- 初始化某个类的子类，则其父类也会被初始化
- Java 虚拟机启动时被标明为启动类的类，直接使用 `java.exe` 命令来运行某个主类
- 使用 JDK 7 新加入的动态语言支持时，如果一个 `java.lang.invoke.MethodHanlde` 实例最后的解析结果为 `REF_getstatic`、`REF_putstatic`、`REF_invokeStatic`、`REF_newInvokeSpecial` 四种类型的方法句柄时，都需要先初始化该句柄对应的类
- 接口中定义了 JDK 8 新加入的默认方法（`default`修饰符），**实现类在初始化之前需要先初始化其接口**

# Part 4. 深入理解双亲委派模型

我们在上面👆已经了解了一个类是如何被加载进 JVM 的——依靠类加载器——在 Java 语言中自带有三个类加载器：

- **Bootstrap ClassLoader** 最顶层的加载类，主要加载  **核心类库**，`%JRE_HOME%\lib` 下的`rt.jar`、`resources.jar`、`charsets.jar` 和 `class` 等。
- **Extention ClassLoader** 扩展的类加载器，加载目录 `%JRE_HOME%\lib\ext` 目录下的 `jar` 包和 `class` 文件。
- **Appclass Loader 也称为 SystemAppClass** 加载当前应用的 `classpath` 的所有类。

我们可以通过一个简单的例子来简单了解 Java 中这些自带的类加载器：

```java
public class PrintClassLoader {

    public static void main(String[] args) {
        printClassLoaders();
    }

    public static void printClassLoaders() {
        System.out.println("Classloader of this class:"
            + PrintClassLoader.class.getClassLoader());
        System.out.println("Classloader of Logging:"
            + com.sun.javafx.util.Logging.class.getClassLoader());
        System.out.println("Classloader of ArrayList:"
            + java.util.ArrayList.class.getClassLoader());
    }
}
```

**上方程序打印输出如下：**

```bash
Classloader of this class:sun.misc.Launcher$AppClassLoader@18b4aac2
Classloader of Logging:sun.misc.Launcher$ExtClassLoader@60e53b93
Classloader of ArrayList:null
```

如我们所见，这里分别对应三种不同类型的类加载器：AppClassLoader、ExtClassLoader 和 BootstrapClassLoader（显示为 `null`）。

一个很好的问题是：**Java 类是由 `java.lang.ClassLoader` 实例加载的，但类加载器本身也是类，那么谁来加载类加载器呢？**

我们假装不知道，先来跟着源码一步一步来看。

## 先来看看 Java 虚拟机入口代码

在 JDK 源码 `sun.misc.Launcher` 中，蕴含了 Java 虚拟机的入口方法：

```java
public class Launcher {
    private static Launcher launcher = new Launcher();
    private static String bootClassPath =
        System.getProperty("sun.boot.class.path");

    public static Launcher getLauncher() {
        return launcher;
    }

    private ClassLoader loader;

    public Launcher() {
        // Create the extension class loader
        ClassLoader extcl;
        try {
            extcl = ExtClassLoader.getExtClassLoader();
        } catch (IOException e) {
            throw new InternalError(
                "Could not create extension class loader", e);
        }

        // Now create the class loader to use to launch the application
        try {
            loader = AppClassLoader.getAppClassLoader(extcl);
        } catch (IOException e) {
            throw new InternalError(
                "Could not create application class loader", e);
        }

        // 设置 AppClassLoader 为线程上下文类加载器，这个文章后面部分讲解
        Thread.currentThread().setContextClassLoader(loader);
    }
    /*
     * Returns the class loader used to launch the main application.
     */
    public ClassLoader getClassLoader() {
        return loader;
    }
    /*
     * The class loader used for loading installed extensions.
     */
    static class ExtClassLoader extends URLClassLoader {}
		/**
     * The class loader used for loading from java.class.path.
     * runs in a restricted security context.
     */
    static class AppClassLoader extends URLClassLoader {}
}
```

源码有精简，但是我们可以得到以下信息：

1️⃣ Launcher 初始化了 ExtClassLoader 和 AppClassLoader。

2️⃣ Launcher 没有看到 Bootstrap ClassLoader 的影子，但是有一个叫做 `bootClassPath` 的变量，大胆一猜就是 Bootstrap ClassLoader 加载的 `jar` 包的路径。

*(ps: 可以自己尝试输出一下 `System.getProperty("sun.boot.class.path")` 的内容，它正好对应了 JDK 目录 `lib` 和 `classes` 目录下的 `jar` 包——也就是通常你配置环境变量时设置的 `%JAVA_HOME/lib` 的目录了——同样的方式你也可以看看 Ext 和 App 的源码)*

3️⃣ ExtClassLoader 和 AppClassLoader 都继承自 URLClassLoader，进一步查看 ClassLoader 的继承树，传说中的双亲委派模型也并没有出现。*（甚至看不到 Bootstrap ClassLoader 的影子，Ext 也没有直接继承自 App 类加载器）*

![ClassLoader 继承树](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200827155234783.png)

*（⚠️注意，这里可以明确看到每一个 ClassLoader 都有一个 `parent` 变量，用于标识自己的父类，下面👇详细说）*

4️⃣ 注意以下代码：

```java
ClassLoader extcl;
        
extcl = ExtClassLoader.getExtClassLoader();

loader = AppClassLoader.getAppClassLoader(extcl);
```

分别跟踪查看到这两个 ClassLoader 初始化时的代码：

```java
// 一直追踪到最顶层的 ClassLoader 定义，构造器的第二个参数标识了类加载器的父类
private ClassLoader(Void unused, ClassLoader parent) {
  this.parent = parent;
  // 代码省略.....
}
// Ext 设置自己的父类为 null
public ExtClassLoader(File[] var1) throws IOException {
  super(getExtURLs(var1), (ClassLoader)null, Launcher.factory);
  SharedSecrets.getJavaNetAccess().getURLClassPath(this).initLookupCache(this);
}
// 手动把 Ext 设置为 App 的 parent（这里的 var2 是传进来的 extc1）
AppClassLoader(URL[] var1, ClassLoader var2) {
  super(var1, var2, Launcher.factory);
  this.ucp.initLookupCache(this);
}
```

由此，我们得到了这样一个类加载器的关系图：

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200827164809547.png)

## 类加载器的父类都来自哪里？

奇怪，为什么 ExtClassLoader 的 `parent` 明明是 `null`，我们却一般地认为 Bootstrap ClassLoader 才是 ExtClassLoader 的父加载器呢？

答案的一部分就藏在 `java.lang.ClassLoader.loadClass()` 方法里面：**（这也就是著名的「双亲委派模型」现场了）**

```java
protected Class<?> loadClass(String name, boolean resolve)
  throws ClassNotFoundException
{
  synchronized (getClassLoadingLock(name)) {
    // 首先检查是否已经加载过了
    Class<?> c = findLoadedClass(name);
    if (c == null) {
      long t0 = System.nanoTime();
      try {
        if (parent != null) {
					// 父加载器不为空则调用父加载器的 loadClass 方法
          c = parent.loadClass(name, false);
        } else {
          // 父加载器为空则调用 Bootstrap ClassLoader
          c = findBootstrapClassOrNull(name);
        }
      } catch (ClassNotFoundException e) {
        // ClassNotFoundException thrown if class not found
        // from the non-null parent class loader
      }

      if (c == null) {
        // If still not found, then invoke findClass in order
        // to find the class.
        long t1 = System.nanoTime();
        // 父加载器没有找到，则调用 findclass
        c = findClass(name);

        // this is the defining class loader; record the stats
        sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
        sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
        sun.misc.PerfCounter.getFindClasses().increment();
      }
    }
    if (resolve) {
      // 调用 resolveClass()
      resolveClass(c);
    }
    return c;
  }
}
```

代码逻辑很好地解释了双亲委派的原理。

1️⃣ 当前 ClassLoader 首先从 **自己已经加载的类中查询是否此类已经加载**，如果已经加载则直接返回原来已经加载的类。(每个类加载器都有自己的加载缓存，当一个类被加载了以后就会放入缓存，等下次加载的时候就可以直接返回了。)

2️⃣ 当前 ClassLoader 的缓存中没有找到被加载的类的时候，**委托父类加载器去加载**，父类加载器采用同样的策略，首先查看自己的缓存，然后委托父类的父类去加载，一直到 Bootstrap ClassLoader。（当所有的父类加载器都没有加载的时候，再由当前的类加载器加载，并将其放入它自己的缓存中，以便下次有加载请求的时候直接返回。）

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200827213241812.png)

所以，答案的另一部分是因为最高一层的类加载器 Bootstrap 是通过 C/C++ 实现的，并不存在于 JVM 体系内 *（不是一个 Java 类，没办法直接表示为 ExtClassLoader 的父加载器）*，所以输出为 `null`。

*（我们可以很轻易跟踪到 `findBootstrapClass()` 方法被 `native` 修饰：`private native Class<?> findBootstrapClass(String name);`）*

➡️ OK，我们理解了为什么 ExtClassLoader 的父加载器为什么是表示为 `null` 的 Bootstrap 加载器，那我们 **自己实现的 ClassLoader 父加载器应该是谁呢？**

观察一下 ClassLoader 的源码就知道了：

```java
protected ClassLoader(ClassLoader parent) {
    this(checkCreateClassLoader(), parent);
}
protected ClassLoader() {
    this(checkCreateClassLoader(), getSystemClassLoader());
}
```

类加载器的 `parent` 的赋值是在 ClassLoader 对象的构造方法中，它有两个情况：

1️⃣ 由外部类创建 ClassLoader 时直接指定一个 ClassLoader 为 `parent`；

2️⃣ 由 `getSystemClassLoader()` 方法生成，也就是在 `sun.misc.Laucher` 通过 `getClassLoader()` 获取，也就是 AppClassLoader。直白的说，一个 ClassLoader 创建时如果没有指定 `parent`，那么它的 `parent` 默认就是 AppClassLoader。（建议去看一下源码）

## 为什么这样设计呢？

简单来说，主要是为了 **安全性**，避免用户自己编写的类动态替换 Java 的一些核心类，比如 String，同时也 **避免了重复加载**，因为 JVM 中区分不同类，不仅仅是根据类名，**相同的 class 文件被不同的 ClassLoader 加载就是不同的两个类**，如果相互转型的话会抛 `java.lang.ClassCaseException`。

如果我们要实现自己的类加载器，不管你是直接实现抽象类 ClassLoader，还是继承 URLClassLoader 类，或者其他子类，它的父加载器都是 AppClassLoader。

因为不管调用哪个父类构造器，创建的对象都必须最终调用 `getSystemClassLoader()` 作为父加载器 *（我们已经从上面👆的源码中看到了）*。而该方法最终获取到的正是 AppClassLoader *（别称 SystemClassLoader）*。

这也就是我们熟知的最终的双亲委派模型了。

![](https://cdn.jsdelivr.net/gh/wmyskxz/BlogImage01/一文带你深扒神秘ClassLoader内核/image-20200827215439538.png)

# Part 5. 实现自己的类加载器

## 什么情况下需要自定义类加载器

在学习了类加载器的实现机制之后，我们知道了双亲委派模型并非强制模型，用户可以自定义类加载器，在什么情况下需要自定义类加载器呢？

1️⃣ **隔离加载类**。在某些框架内进行中间件与应用的模块隔离，把类加载器到不同的环境。比如，阿里内某容器框架通过自定义类加载器确保应用中依赖的 `jar` 包不会影响到中间件运行时使用的 `jar` 包。

2️⃣ **修改类加载方式**。类的加载模型并非强制，除了 Bootstrap 外，其他的加载并非一定要引入，或者根据实际情况在某个时间点进行按需的动态加载。

3️⃣ **扩展加载源**。比如从数据库、网络，甚至是电视机顶盒进行加载。（下面👇我们会编写一个从网络加载类的例子）

4️⃣ **防止源码泄露**。Java 代码容易被编译和篡改，可以进行编译加密。那么类加载器也需要自定义，还原加密的字节码。

## 一个常规的例子

实现一个自定义的类加载器比较简单：继承 ClassLoader，重写 `findClass()` 方法，调用 `defineClass()` 方法，就差不多行了。

### Tester.java

我们先来编写一个测试用的类文件：

```java
public class Tester {

    public void say() {
        System.out.println("关注【我没有三颗心脏】，解锁更多精彩！");
    }
}
```

在同级目录下执行 `javac Tester.java` 命令，并把编译后的 `Tester.class` 放到指定的目录下（我这边为了方便就放在桌面上啦 `/Users/wmyskxz/Desktop`）

### MyClassLoader.java

我们编写自定义 ClassLoader 代码：

```java
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {

    private final String mLibPath;

    public MyClassLoader(String path) {
        // TODO Auto-generated constructor stub
        mLibPath = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // TODO Auto-generated method stub

        String fileName = getFileName(name);

        File file = new File(mLibPath, fileName);

        try {
            FileInputStream is = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = bos.toByteArray();
            is.close();
            bos.close();

            return defineClass(name, data, 0, data.length);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    // 获取要加载的 class 文件名
    private String getFileName(String name) {
        // TODO Auto-generated method stub
        int index = name.lastIndexOf('.');
        if (index == -1) {
            return name + ".class";
        } else {
            return name.substring(index + 1) + ".class";
        }
    }
}
```

我们在 `findClass()` 方法中定义了查找 class 的方法，然后数据通过 `defineClass()` 生成了 Class 对象。

### ClassLoaderTester 测试类

我们需要删除刚才在项目目录创建的 `Tester.java` 和编译后的 `Tester.class` 文件来观察效果：

```java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderTester {

    public static void main(String[] args) {
        // 创建自定义的 ClassLoader 对象
        MyClassLoader myClassLoader = new MyClassLoader("/Users/wmyskxz/Desktop");
        try {
            // 加载class文件
            Class<?> c = myClassLoader.loadClass("Tester");

            if(c != null){
                try {
                    Object obj = c.newInstance();
                    Method method = c.getDeclaredMethod("say",null);
                    //通过反射调用Test类的say方法
                    method.invoke(obj, null);
                } catch (InstantiationException | IllegalAccessException
                    | NoSuchMethodException
                    | SecurityException |
                    IllegalArgumentException |
                    InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
```

**运行测试，正常输出：**

```bash
关注【我没有三颗心脏】，解锁更多精彩！
```

## 加密解密类加载器

突破了 JDK 系统内置加载路径的限制之后，我们就可以编写自定义的 ClassLoader。你完全可以按照自己的意愿进行业务的定制，将 ClassLoader 玩出花样来。

例如，一个加密解密的类加载器。（不涉及完整代码，我们可以来说一下思路和关键代码）

首先，在编译之后的字节码文件中动一动手脚，例如，给文件每一个 `byte` 异或一个数字 2：（这就算是模拟加密过程）

```java
File file = new File(path);
try {
  FileInputStream fis = new FileInputStream(file);
  FileOutputStream fos = new FileOutputStream(path+"en");
  int b = 0;
  int b1 = 0;
  try {
    while((b = fis.read()) != -1){
      // 每一个 byte 异或一个数字 2
      fos.write(b ^ 2);
    }
    fos.close();
    fis.close();
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
} catch (FileNotFoundException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

然后我们再在 `findClass()` 中自己解密：

```java
File file = new File(mLibPath,fileName);

try {
  FileInputStream is = new FileInputStream(file);

  ByteArrayOutputStream bos = new ByteArrayOutputStream();
  int len = 0;
  byte b = 0;
  try {
    while ((len = is.read()) != -1) {
      // 将数据异或一个数字 2 进行解密
      b = (byte) (len ^ 2);
      bos.write(b);
    }
  } catch (IOException e) {
    e.printStackTrace();
  }

  byte[] data = bos.toByteArray();
  is.close();
  bos.close();

  return defineClass(name,data,0,data.length);

} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

*（代码几乎与上面👆一个例子等同，所以只说一下思路和完整代码）*

## 网络类加载器

其实非常类似，也不做过多讲解，直接上代码：

```java
import java.io.ByteArrayOutputStream;  
import java.io.InputStream;  
import java.net.URL;  
  
public class NetworkClassLoader extends ClassLoader {  
  
    private String rootUrl;  
  
    public NetworkClassLoader(String rootUrl) {  
        // 指定URL  
        this.rootUrl = rootUrl;  
    }  
  
    // 获取类的字节码  
    @Override  
    protected Class<?> findClass(String name) throws ClassNotFoundException {  
        byte[] classData = getClassData(name);  
        if (classData == null) {  
            throw new ClassNotFoundException();  
        } else {  
            return defineClass(name, classData, 0, classData.length);  
        }  
    }  
  
    private byte[] getClassData(String className) {  
        // 从网络上读取的类的字节  
        String path = classNameToPath(className);  
        try {  
            URL url = new URL(path);  
            InputStream ins = url.openStream();  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            int bufferSize = 4096;  
            byte[] buffer = new byte[bufferSize];  
            int bytesNumRead = 0;  
            // 读取类文件的字节  
            while ((bytesNumRead = ins.read(buffer)) != -1) {  
                baos.write(buffer, 0, bytesNumRead);  
            }  
            return baos.toByteArray();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    private String classNameToPath(String className) {  
        // 得到类文件的URL  
        return rootUrl + "/"  
                + className.replace('.', '/') + ".class";  
    }  
}  
```

*(代码来自：https://blog.csdn.net/justloveyou_/article/details/72217806)*

# Part 6. 必要的扩展阅读

学习到这里，我们对 ClassLoader 已经不再陌生了，但是仍然有一些必要的知识点需要去掌握 *（限于篇幅和能力这里不扩展了）*，希望您能认真阅读以下的材料：*（可能排版上面层次不齐，但内容都是有质量的，并用 ♨️ 标注了更加重点一些的内容）*

1️⃣ ♨️**能不能自己写一个类叫 `java.lang.System` 或者 `java.lang.String`？** - https://blog.csdn.net/tang9140/article/details/42738433

2️⃣ 深入理解 Java 之 JVM 启动流程 - https://cloud.tencent.com/developer/article/1038435

3️⃣ ♨️**真正理解线程上下文类加载器（多案例分析）** - https://blog.csdn.net/yangcheng33/article/details/52631940

4️⃣ ♨️**曹工杂谈：Java 类加载器还会死锁？这是什么情况？** - https://www.cnblogs.com/grey-wolf/p/11378747.html#_label2

5️⃣ 谨防JDK8重复类定义造成的内存泄漏 - https://segmentfault.com/a/1190000022837543

7️⃣ ♨️**Tomcat 类加载器的实现** - https://juejin.im/post/6844903945496690695

8️⃣ ♨️**Spring 中的类加载机制** - https://www.shuzhiduo.com/A/gVdnwgAlzW/

# 参考资料

1. 《深入分析 Java Web 技术内幕》 | 许令波 著
2. Java 类加载机制分析 - https://www.jianshu.com/p/3615403c7c84
3. Class 文件解析实战 - https://juejin.im/post/6844904199617003528
4. 图文兼备看懂类加载机制的各个阶段，就差你了！ - https://juejin.im/post/6844904119258316814
5. Java面试知识点解析（三）——JVM篇 - https://www.wmyskxz.com/2018/05/16/java-mian-shi-zhi-shi-dian-jie-xi-san-jvm-pian/
6. 一看你就懂，超详细Java中的ClassLoader详解 - https://blog.csdn.net/briblue/article/details/54973413

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://cdn.jsdelivr.net/gh/wmyskxz/img/img/common/qrcode.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！