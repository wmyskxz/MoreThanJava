![](https://imgkr.cn-bj.ufileos.com/7f21a249-22a1-4983-842f-58dd4ca25ed7.png)

# 执行注释

大多数开发人员认为 **注释** 永远不会在程序中执行，并用于帮助代码理解。但是，它们却 **可以被执行**：

```java
public class Main {

    public static void main(String[] args) {
        // \u000d System.out.println("wmyskxz is awesome!");
    }
}
```

程序输出：

```java
wmyskxz is awesome!
```


## 说明

Java 允许使用 Unicode 字符而不进行编码。这里的 Unicode 字符 `\u000d` 被 Java 编译器解析为新行，因此可以理解为该 Unicode 字符后面出现的语句会在下一行被执行。

---

# 双括号初始化集合

在 Java 中，`Set/ List/ Map` 等集合对象没有在生命期间初始化值的简单方法 *(Java 11 支持了该类操作)*。开发人员要么将值显式地传送到集合内，要么为常量集合创建一个静态块。

使用双括号初始化，可以在声明过程中以更少的精力和时间初始化集合。例如：

```java
Set<String> set = new HashSet<String>() {{
    add("wmyskxz");
    add("is");
    add("awesome");
    add("!");
} };
System.out.println(set);
```

程序输出：

```java
[awesome, !, wmyskxz, is]
```

> 在 `Java 11` 中，你可以使用 `Set.of("wmyskxz", "is", "awesome", "!")` 代替，这里仅仅讨论 `Java 8`
>
> 另外对于 `ArrayList` 集合类型还是有简单初始化方法的：`Arrays.asList()`

---

# 获取数组插入数字元素的下标

有一个很酷的技巧，可以找到可以在数组中插入所请求元素的位置：

```java
int[] arr = new int[] { 1, 3, 4, 5, 6 };

// 2 has to be inserted
int pos = Arrays.binarySearch(arr, 2);
System.out.print("Element has to be inserted at: "
    + ~pos);
```

程序输出：

```java
Element has to be inserted at: 1
```

## 说明

`Arrays.binarySearch()` 是 JDK 自己实现的二分查找方法，局限就是目标数组必须是排序好的 *(可以使用 `Arrays.sort()` 进行排序)*。

---

# 判断数字是偶数还是奇数

通常我们会使用 `num % 2 == 0` *(一定记住要使用 [偶判断](https://www.wmyskxz.com/2017/11/15/bian-xie-gao-zhi-liang-dai-ma-xue-xi-bi-ji-1/#toc-heading-6) 而不是奇判断，原因是负数会出错..)* 来判断数字是奇数还是偶数。下面的技巧尽管并不比上述方法好多少，但在考虑大数时，效率会高很多：

```java
System.out.println((num & 1) == 0 ?  "EVEN" : "ODD" );
```

示例：

```java
int num = 2;
System.out.println((num & 1) == 0 ? "EVEN" : "ODD");
// 输出 EVEN

num = -1;
System.out.println((num & 1) == 0 ? "EVEN" : "ODD");
// 输出 ODD
```

---

# 快速乘或除 2

二进制中，乘以 `2` 表示将所有位向左移动，除以 `2` 表示向右移动。

```java
n = n << 1;   // Multiply n with 2 
n = n >> 1;   // Divide n by 2 
```

---

# 判断素数

Java 在 `BigInteger` 类中内置了 `isProbablePrime()` 方法。如果此 BigInteger 可能是质数（可以肯定），则返回 `true`，如果它肯定是复合的，则返回 `false`。

```java
BigInteger.valueOf(1235).isProbablePrime(1) 
```

# 参考资料

1. Interesting and Cool Tricks in Java - https://www.geeksforgeeks.org/interesting-and-cool-tricks-in-java/?ref=leftbar-rightbar
1. Java tricks for competitive programming (for Java 8) - https://www.geeksforgeeks.org/java-tricks-competitive-programming-java-8/?ref=rp

> - 本文已收录至我的 Github 程序员成长系列 **【More Than Java】，学习，不止 Code，欢迎 star：[https://github.com/wmyskxz/MoreThanJava](https://github.com/wmyskxz/MoreThanJava)**
> - **个人公众号** ：wmyskxz，**个人独立域名博客**：wmyskxz.com，坚持原创输出，下方扫码关注，2020，与您共同成长！

![](https://imgkr.cn-bj.ufileos.com/ace97ed9-3cfd-425f-85e5-c1a1e5ca7d3f.png)

非常感谢各位人才能 **看到这里**，如果觉得本篇文章写得不错，觉得 **「我没有三颗心脏」有点东西** 的话，**求点赞，求关注，求分享，求留言！**

创作不易，各位的支持和认可，就是我创作的最大动力，我们下篇文章见！