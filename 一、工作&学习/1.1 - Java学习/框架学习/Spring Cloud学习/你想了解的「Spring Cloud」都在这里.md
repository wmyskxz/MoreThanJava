![](https://upload-images.jianshu.io/upload_images/7896890-1bb9ab18d3b811d8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> **前言：** 之前我们已经了解了[「什么是微服务？」](https://www.jianshu.com/p/5368af76a0f8)，现在我们开始了解「微服务」关键字下比较热门的「Spring Cloud」...

# 一、传统架构发展史

---

> 部分引用自：[从架构演进的角度聊聊Spring Cloud都做了些什么？ - 纯洁的微笑](http://www.ityouknow.com/springcloud/2017/11/02/framework-and-springcloud.html)

## 单体架构

单体架构在小微企业比较常见，典型代表就是一个应用、一个数据库、一个web容器就可以跑起来。

在两种情况下可能会选择单体架构：一是在企业发展的初期，为了保证快速上线，采用此种方案较为简单灵活；二是传统企业中垂直度较高，访问压力较小的业务。在这种模式下对技术要求较低，方便各层次开发人员接手，也能满足客户需求。

下面是单体架构的架构图：

![](https://upload-images.jianshu.io/upload_images/7896890-97c4c079986d5325.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在单体架构中，技术选型非常灵活，优先满足快速上线的要求，也便于快速跟进市场。

## 垂直架构

在单体架构发展一段时间后，公司的业务模式得到了认可，交易量也慢慢的大起来，这时候有些企业为了应对更大的流量，就会对原有的业务进行拆分，比如说：后台系统、前端系统、交易系统等。

在这一阶段往往会将系统分为不同的层级，每个层级有对应的职责，UI层负责和用户进行交互、业务逻辑层负责具体的业务功能、数据库层负责和上层进行数据交换和存储。

下面是垂直架构的架构图：

![](https://upload-images.jianshu.io/upload_images/7896890-bed88a6d69bf4346.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 服务化架构

如果公司进一步的做大，垂直子系统会变的越来越多，系统和系统之间的调用关系呈指数上升的趋势。在这样的背景下，很多公司都会考虑服务的 SOA 化。**SOA 代表面向服务的架构，将应用程序根据不同的职责划分为不同的模块**，不同的模块直接通过特定的协议和接口进行交互。这样使整个系统切分成很多单个组件服务来完成请求，当流量过大时通过水平扩展相应的组件来支撑，所有的组件通过交互来满足整体的业务需求。

SOA服务化的优点是，它可以根据需求通过网络对松散耦合的粗粒度应用组件进行分布式部署、组合和使用。服务层是SOA的基础，可以直接被应用调用，从而有效控制系统中与软件代理交互的人为依赖性。

服务化架构是一套松耦合的架构，服务的拆分原则是服务内部高内聚，服务之间低耦合。

下面是服务化架构图：

![](https://upload-images.jianshu.io/upload_images/7896890-9d1b9a96a112b4b7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在这个阶段可以使用 WebService 或者 Dubbo 来服务治理。

我们发现从单体架构到服务化架构，应用数量都在不断的增加，慢慢的下沉的就成了基础组建，上浮的就成为业务系统。从上述也可以看出**架构的本质就是不断的拆分重构**：分的过程是把系统拆分为各个子系统/模块/组件，拆的时候，首先要解决每个组件的定位问题，然后才能划分彼此的边界，实现合理的拆分。合就是根据最终要求，把各个分离的组件有机整合在一起。拆分的结果使开发人员能够做到业务聚焦、技能聚焦，实现开发敏捷，合的结果是系统变得柔性，可以因需而变，实现业务敏捷。

## 微服务架构

**微服务是一种软件架构风格，它是以专注于单一责任与功能的小型功能区块为基础**，利用模组化的方式组合出复杂的大型应用程序，各功能区块使用与语言无关的 API（例如 REST）集相互通讯，且每个服务可以被单独部署，它具备以下三个核心特点：

- **微服务为大型系统而生。**随着业务的快速增长，会带来系统流量压力和复杂度的上升，系统的可维护性和可扩展性成为架构设计的主要考虑因素，微服务架构设计理念通过小而美的业务拆分，通过分而自治来实现复杂系统的优雅设计实现。
- **微服务架构是面向结果的。**微服务架构设计风格的产生并非是出于学术或为标准而标准的设计，而是在软件架构设计领域不断演进过程中，面对实际工业界所遇到问题，而出现的面向解决实际问题的架构设计风格。
- **专注于服务的可替代性来设计。**微服务架构设计风格核心要解决的问题之一便是如何便利地在大型系统中进行系统组件的维护和替换，且不影响整体系统稳定性。

![](https://upload-images.jianshu.io/upload_images/7896890-edd16c96b17343b5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


**SOA** 与 **微服务** 的不同在于：

- **服务拆分粒度更细。**微服务可以说是更细维度的服务化，小到一个子子模块，只要该模块依赖的资源与其他模块都没有关系，那么就可以拆分成一个微服务。
- **服务独立部署。**每个服务都严格遵循独立打包部署的准则，互不影响。比如一台物理机上可以部署多个 Docker 实例，每个 Docker 实例可以部署一个微服务的代码。
- **服务独立维护。**每个微服务都可以交由一个小团队甚至个人来开发、测试、发布和运维，并对整个生命周期负责。
- **服务治理能力要求高。**因为拆分为微服务之后，服务的数量变多，因此需要有统一的服务治理平台，来对各个服务进行管理。

# 二、引入 Spring Cloud

---

## 什么是 Spring Cloud?

Spring 全家桶在 Java 开发中拥有举足轻重的地位，其中的一系列产品不仅仅大大简化和方便了 Java 的开发，其中的 AOP 和 IoC 等一系列的理念也深刻地影响着 Java 程序员们。

Spring 全家桶产品众多，总结起来大概就是：

- **Spring** 通常指 Spring IOC。
- **Spring Framework** 包含了 Spring IOC，同时包含了 Spring AOP，并实现与其它 J2EE 框架的整合。
- **Spring Boot** 是对 Spring Framework 的补充，让框架的集成变得更简单，致力于快速开发 独立的 Spring 应用。
- **Spring Cloud** 是基于 Spring Boot 设计的一套微服务规范，并增强了应用上下文。

我们也不妨来看看官网的介绍：

![](https://upload-images.jianshu.io/upload_images/7896890-3267323e6d3b94bf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**总结起来就是： Spring Cloud 是一系列框架的有序集合。**我们能够使用基于 Spring Boot 设计的 Spring Cloud 方便快速的搭建起自己的可靠、协调一致的分布式系统。

## 为什么是 Spring Cloud？

微服务的框架那么多比如：Dubbo、Kubernetes，为什么就要使用 Spring Cloud 的呢？

- 产出于 Spring 大家族，Spring 在企业级开发框架中无人能敌，来头很大，可以保证后续的更新、完善。比如 Dubbo 现在就差不多死了
- 有 Spring Boot 这个独立干将可以省很多事，大大小小的活 Spring Boot 都搞的挺不错。
- 作为一个微服务治理的大家伙，考虑的很全面，几乎服务治理的方方面面都考虑到了，方便开发开箱即用。
- Spring Cloud 活跃度很高，教程很丰富，遇到问题很容易找到解决方案。
- 轻轻松松几行代码就完成了熔断、均衡负载、服务中心的各种平台功能。

# 三、Spring Cloud 能够帮我们做什么？

---

前面我们说到了，「Spring Cloud」是一系列框架的集合，可以帮助我们解决分布式/微服务的各种问题，那么「Spring Cloud」究竟能帮助我们做什么呢？

SpringCloud的基础功能包括：

- **服务治理：** Spring  Cloud Eureka
- **客户端负载均衡：** Spring Cloud Ribbon
- **服务容错保护：** Spring  Cloud Hystrix
- **声明式服务调用：** Spring  Cloud Feign
- **API网关服务：** Spring Cloud Zuul
- **分布式配置中心：** Spring Cloud Config

当然 Spring Cloud 还包括一些高级的功能：

- **消息总线：** Spring Cloud Bus
- **消息驱动的微服务：** Spring Cloud Stream
- **分布式服务跟踪：** Spring Cloud Sleuth

##  服务治理：Eureka

微服务很重要的一点就是「无状态」，也就是说每一个服务之间应该是独立的，所以当微服务架构搭起来之后各个独立的「微服务」之间应该如何**通讯**成了首要的问题。

假设我们的 A服务 需要访问 B服务，那么我们首先需要知道对方的 **ip地址**，所以我们调用起来可能就像：

![](https://upload-images.jianshu.io/upload_images/7896890-d736e62854e9f949.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

似乎并没有什么问题，但是如果 B服务 的 **ip地址** 变更了，那么我们就只能手动的去更改 A服务 的配置，如果我们的服务有很多，并且不止 A服务 调用了 B服务，那么手动更改这些配置将会是一场噩梦。

Eureka 是 Netflix 开源的一款提供服务注册和发现的产品，它提供了完整的 Service Registry 和 Service Discovery 实现。也是 Spring Cloud 体系中最重要最核心的组件之一。

用大白话讲，Eureka 就是一个服务中心，将所有的可以提供的服务都注册到它这里来管理，其它各调用者需要的时候去注册中心获取，然后再进行调用，避免了服务之间的直接调用，方便后续的水平扩展、故障转移等。如下图：

![](https://upload-images.jianshu.io/upload_images/7896890-44cc612bd6a8a064.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

当然服务中心这么重要的组件一但挂掉将会影响全部服务，因此需要搭建 Eureka 集群来保持高可用性，生产中建议最少两台。随着系统的流量不断增加，需要根据情况来扩展某个服务，Eureka 内部已经提供均衡负载的功能，只需要增加相应的服务端实例既可。那么在系统的运行期间某个实例挂了怎么办？**Eureka 内容有一个心跳检测机制，** 如果某个实例在规定的时间内没有进行通讯则会自动被剔除掉，避免了某个实例挂掉而影响服务。

因此使用了Eureka就自动具有了注册中心、负载均衡、故障转移的功能。如果想对Eureka进一步了解可以参考这篇文章：[注册中心Eureka](http://www.ityouknow.com/springcloud/2017/05/10/springcloud-eureka.html)

## 客户端负载均衡： Ribbon

Ribbon 是一个基于 HTTP 和 TCP 客户端的负载均衡器。Ribbon 可以在通过客户端中配置的 ribbonServerList 服务端列表去**轮询访问**以达到均衡负载的作用。

当 Ribbon 与 Eureka 联合使用时，ribbonServerList 会被 DiscoveryEnabledNIWSServerList 重写，扩展成**从 Eureka 注册中心中获取服务端列表。**同时它也会用 NIWSDiscoveryPing 来取代 IPing，**它将职责委托给 Eureka 来确定服务端是否已经启动。**

- 实战：

[Spring Cloud构建微服务架构（二）服务消费者 - http://blog.didispace.com/springcloud2/](http://blog.didispace.com/springcloud2/)

## 服务容错保护： Hystrix

在微服务架构中通常会有多个服务层调用，基础服务的故障可能会导致级联故障，进而造成整个系统不可用的情况，这种现象被称为**服务雪崩效应。**服务雪崩效应是一种因“服务提供者”的不可用导致“服务消费者”的不可用,并将不可用逐渐放大的过程。

如下图所示：A作为服务提供者，B为A的服务消费者，C和D是B的服务消费者。A不可用引起了B的不可用，并将不可用像滚雪球一样放大到C和D时，雪崩效应就形成了。

![](https://upload-images.jianshu.io/upload_images/7896890-2c62b2ffa85e86e7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在这种情况下就需要整个服务机构具有**故障隔离**的功能，避免某一个服务挂掉影响全局。在 Spring Cloud 中 Hystrix 组件就扮演这个角色。

Hystrix 会在某个服务连续调用 N 次不响应的情况下，立即通知调用端调用失败，避免调用端持续等待而影响了整体服务。Hystrix 间隔时间会再次检查此服务，如果服务恢复将继续提供服务。

继续了解Hystrix可以参考：[熔断器Hystrix](http://www.ityouknow.com/springcloud/2017/05/10/springcloud-eureka.html)

## Hystrix Dashboard 和 Turbine

当熔断发生的时候需要迅速的响应来解决问题，避免故障进一步扩散，那么对熔断的监控就变得非常重要。熔断的监控现在有两款工具：Hystrix-dashboard 和 Turbine

Hystrix-dashboard 是一款针对Hystrix进行实时监控的工具，通过 Hystrix Dashboard 我们可以直观地看到各 Hystrix Command 的请求响应时间, 请求成功率等数据。但是只使用 Hystrix Dashboard 的话, 你只能看到单个应用内的服务信息, 这明显不够. 我们需要一个工具能让我们汇总系统内多个服务的数据并显示到 Hystrix Dashboard 上, 这个工具就是 Turbine. 监控的效果图如下：

![](https://upload-images.jianshu.io/upload_images/7896890-de80387262f335c4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

想了解具体都监控了哪些指标，以及如何监控可以参考这篇文章：[熔断监控Hystrix Dashboard和Turbine](http://www.ityouknow.com/springcloud/2017/05/18/hystrix-dashboard-turbine.html)

## 声明式服务调用：Feign

上面我们介绍了 Ribbon 和 Hystrix 了，可以发现：这两个可以作为基础工具类广泛的嵌入到各个微服务中。为了**简化我们的开发**，Spring Cloud Feign 出现了！它基于 Netflix Feign 实现，整合了 Spring Cloud Ribbon 与 Spring Cloud Hystrix,  除了整合这两者的强大功能之外，它还提供了**声明式的服务调用**(不再通过RestTemplate)。

> Feign 是一种声明式、模板化的HTTP客户端。在 Spring Cloud 中使用 Feign, 我们可以做到使用HTTP请求远程服务时能与调用本地方法一样的编码体验，开发者完全感知不到这是远程方法，更感知不到这是个 HTTP 请求。

下面就简单看看Feign是怎么优雅地实现远程调用的：

**服务绑定：**

```java
// value --->指定调用哪个服务
// fallbackFactory--->熔断器的降级提示
@FeignClient(value = "MICROSERVICECLOUD-DEPT", fallbackFactory = DeptClientServiceFallbackFactory.class)
public interface DeptClientService {

    // 采用Feign我们可以使用SpringMVC的注解来对服务进行绑定！
    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") long id);

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list();

    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    public boolean add(Dept dept);
}
```

**Feign 中使用熔断器：**

```java
/**
 * Feign中使用断路器
 * 这里主要是处理异常出错的情况(降级/熔断时服务不可用，fallback就会找到这里来)
 */
@Component // 不要忘记添加，不要忘记添加
public class DeptClientServiceFallbackFactory implements FallbackFactory<DeptClientService> {
    @Override
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService() {
            @Override
            public Dept get(long id) {
                return new Dept().setDeptno(id).setDname("该ID：" + id + "没有没有对应的信息,Consumer客户端提供的降级信息,此刻服务Provider已经关闭")
                        .setDb_source("no this database in MySQL");
            }

            @Override
            public List<Dept> list() {
                return null;
            }

            @Override
            public boolean add(Dept dept) {
                return false;
            }
        };
    }
}
```

**调用：**

![](https://upload-images.jianshu.io/upload_images/7896890-151920f51a61863b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 引用自：
[外行人都能看懂的 Spring Cloud - https://juejin.im/post/5b83466b6fb9a019b421cecc#heading-12](https://juejin.im/post/5b83466b6fb9a019b421cecc#heading-12)

## API 网关服务：Zuul

在微服务架构模式下，**后端服务的实例数一般是动态的，**对于客户端而言很难发现动态改变的服务实例的访问地址信息。因此在基于微服务的项目中为了简化前端的调用逻辑，通常会引入 API Gateway 作为轻量级网关，同时 API Gateway 中也会实现相关的认证逻辑从而简化内部服务之间相互调用的复杂度。

![](https://upload-images.jianshu.io/upload_images/7896890-e3e021be95edbf77.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Spring Cloud 体系中支持 API Gateway 落地的技术就是 Zuul。Spring Cloud Zuul 路由是微服务架构中不可或缺的一部分，提供动态路由，监控，弹性，安全等的边缘服务。Zuul 是 Netflix 出品的一个基于 JVM 路由和服务端的负载均衡器。

它的具体作用就是**服务转发，接收并转发所有内外部的客户端调用。**使用 Zuul 可以作为资源的统一访问入口，同时也可以在网关做一些权限校验等类似的功能。

具体使用参考这篇文章：[服务网关zuul](http://www.ityouknow.com/springcloud/2017/06/01/gateway-service-zuul.html)

## 分布式配置中心：Config

随着业务的不断发展，我们的「微服务」可能会越来越多，而**每一个微服务都会有自己的配置文件，**在研发过程中有测试环境、UAT环境、生产环境，因此每个微服务又对应至少三个不同环境的配置文件。这么多的配置文件，如果需要修改某个公共服务的配置信息，如：缓存、数据库等，难免会产生混乱，这个时候就需要引入 Spring Cloud 另外一个组件：Spring Cloud Config。

**Spring Cloud Config 是一个解决分布式系统的配置管理方案。**它包含了 Client 和 Server 两个部分，Server 提供配置文件的存储、以接口的形式将配置文件的内容提供出去，Client 通过接口获取数据、并依据此数据初始化自己的应用。

其实就是 Server 端将所有的配置文件服务化，需要配置文件的服务实例去 Config Server 获取对应的数据。**将所有的配置文件统一整理，避免了配置文件碎片化。**配置中心git实例参考：[配置中心git示例](http://www.ityouknow.com/springcloud/2017/05/22/springcloud-config-git.html)；

如果服务运行期间改变配置文件，服务是不会得到最新的配置信息，需要解决这个问题就需要引入 Refresh。可以在服务的运行期间重新加载配置文件，具体可以参考这篇文章：[配置中心svn示例和refresh](http://www.ityouknow.com/springcloud/2017/05/23/springcloud-config-svn-refresh.html)

当所有的配置文件都存储在配置中心的时候，配置中心就成为了一个非常重要的组件。**如果配置中心出现问题将会导致灾难性的后果，因此在生产中建议对配置中心做集群，来支持配置中心高可用性。**具体参考：[配置中心服务化和高可用](http://www.ityouknow.com/springcloud/2017/05/25/springcloud-config-eureka.html)

## 消息总线：Bus

上面的 Refresh 方案虽然可以解决单个微服务运行期间重载配置信息的问题，但是在真正的实践生产中，可能会有 N 多的服务需要更新配置，如果每次依靠手动 Refresh 将是一个巨大的工作量，这时候 Spring Cloud 提出了另外一个解决方案：Spring Cloud Bus

**Spring Cloud Bus 通过轻量消息代理连接各个分布的节点。**这会用在**广播状态**的变化（例如配置变化）或者其它的消息指令中。Spring Cloud Bus 的一个核心思想是**通过分布式的启动器对Spring Boot应用进行扩展，也可以用来建立一个或多个应用之间的通信频道。**目前唯一实现的方式是用 AMQP 消息代理作为通道。

Spring Cloud Bus 是轻量级的通讯组件，也可以用在其它类似的场景中。有了 Spring Cloud Bus 之后，当我们改变配置文件提交到版本库中时，会自动的触发对应实例的 Refresh，具体的工作流程如下：

![](https://upload-images.jianshu.io/upload_images/7896890-ff35d8cb86a33733.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

也可以参考这篇文章来了解：[配置中心和消息总线](http://www.ityouknow.com/springcloud/2017/05/26/springcloud-config-eureka-bus.html)

## 消息驱动的微服务：Stream

Spring Cloud Stream 是一个用来为微服务应用构建消息驱动能力的框架。它可以基于 Spring Boot 来创建独立的、可用于生产的 Spring 应用程序。它通过使用 Spring Integration 来**连接消息代理中间件以实现消息事件驱动的微服务应用。**

下图是官方文档中对于 Spring Cloud Stream 应用模型的结构图。从中我们可以看到，Spring Cloud Stream 构建的应用程序与消息中间件之间是通过绑定器 Binder 相关联的，绑定器对于应用程序而言起到了隔离作用，它使得不同消息中间件的实现细节对应用程序来说是透明的。所以对于每一个 Spring Cloud Stream 的应用程序来说，它不需要知晓消息中间件的通信细节，它只需要知道 Binder 对应用程序提供的概念去实现即可。如下图案例，在应用程序和 Binder 之间定义了两条输入通道和三条输出通道来传递消息，而绑定器则是作为这些通道和消息中间件之间的桥梁进行通信。

![](https://upload-images.jianshu.io/upload_images/7896890-07f3ff1255dfc531.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Spring Cloud Stream 为一些供应商的消息中间件产品提供了个性化的自动化配置实现，并且引入了发布-订阅、消费组以及消息分区这三个核心概念。简单的说，**Spring Cloud Stream 本质上就是整合了 Spring Boot 和 Spring Integration，实现了一套轻量级的消息驱动的微服务框架。**通过使用 Spring Cloud Stream，可以有效地简化开发人员对消息中间件的使用复杂度，让系统开发人员可以有更多的精力关注于核心业务逻辑的处理。由于 Spring Cloud Stream 基于 Spring Boot 实现，所以它秉承了 Spring Boot 的优点，实现了自动化配置的功能帮忙我们可以快速的上手使用，但是目前为止 Spring Cloud Stream 只支持 **RabbitMQ** 和 **Kafka** 两个著名的消息中间件的自动化配置：

- 实战：
[Spring Cloud构建微服务架构：消息驱动的微服务（入门）【Dalston版】 - http://blog.didispace.com/spring-cloud-starter-dalston-7-1/](http://blog.didispace.com/spring-cloud-starter-dalston-7-1/)

## 分布式服务跟踪：Sleuth

随着服务的越来越多，对调用链的分析会越来越复杂，如服务之间的调用关系、某个请求对应的调用链、调用之间消费的时间等，**对这些信息进行监控就成为一个问题。**在实际的使用中我们需要监控服务和服务之间通讯的各项指标，这些数据将是我们改进系统架构的主要依据。因此分布式的链路跟踪就变的非常重要，Spring Cloud 也给出了具体的解决方案：Spring Cloud Sleuth 和 Zipkin

![](https://upload-images.jianshu.io/upload_images/7896890-c68c2ef12cd4adae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Spring Cloud Sleuth 为服务之间调用提供链路追踪。通过 Sleuth 可以很清楚的了解到一个服务请求经过了哪些服务，每个服务处理花费了多长时间。从而让我们可以很方便的理清各微服务间的调用关系。

Zipkin 是 Twitter 的一个开源项目，允许开发者收集 Twitter 各个服务上的监控数据，并提供查询接口

分布式链路跟踪需要 Sleuth + Zipkin 结合来实现，具体操作参考这篇文章：[分布式链路跟踪(Sleuth)](http://www.jianshu.com/p/c3d191663279)

## 总结

我们从整体上来看一下Spring Cloud各个组件如何来配套使用：

![](https://upload-images.jianshu.io/upload_images/7896890-eda069329a8b386d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

从上图可以看出 Spring Cloud 各个组件相互配合，合作支持了一套完整的微服务架构。

- 其中 **Eureka** 负责服务的注册与发现，很好将各服务连接起来
- **Hystrix** 负责监控服务之间的调用情况，连续多次失败进行熔断保护。
- **Hystrix dashboard,Turbine** 负责监控 Hystrix 的熔断情况，并给予图形化的展示
- **Spring Cloud Config** 提供了统一的配置中心服务
- 当配置文件发生变化的时候，**Spring Cloud Bus** 负责通知各服务去获取最新的配置信息
- 所有对外的请求和服务，我们都通过 **Zuul** 来进行转发，起到 API 网关的作用
- 最后我们使用 **Sleuth + Zipkin** 将所有的请求数据记录下来，方便我们进行后续分析

Spring Cloud 从设计之初就考虑了绝大多数互联网公司架构演化所需的功能，如服务发现注册、配置中心、消息总线、负载均衡、断路器、数据监控等。这些功能都是以插拔的形式提供出来，方便我们系统架构演进的过程中，可以合理的选择需要的组件进行集成，从而在架构演进的过程中会更加平滑、顺利。

微服务架构是一种趋势，Spring Cloud 提供了标准化的、全站式的技术方案，意义可能会堪比当前 Servlet 规范的诞生，有效推进服务端软件系统技术水平的进步。

> 引用自：[从架构演进的角度聊聊Spring Cloud都做了些什么？ - http://www.ityouknow.com/springcloud/2017/11/02/framework-and-springcloud.html](http://www.ityouknow.com/springcloud/2017/11/02/framework-and-springcloud.html)

# 四、Spring Cloud 版本

---

刚接触的「Spring Cloud」的童鞋可能会对它的版本感到奇怪，什么 `Angle`、`Brixton`、`Finchley`，这些都是啥啊？「为什么会有这么多种看起来不同的 Spring Cloud？」

从上面我们可以知道：**Spring Cloud 是一个拥有诸多子项目的大型综合项目**（功能不止上面的介绍），原则上其子项目也都维护着自己的发布版本号。那么每一个Spring Cloud的版本都会包含不同的子项目版本，**为了要管理每个版本的子项目清单，避免版本名与子项目的发布号混淆，所以没有采用版本号的方式，而是通过命名的方式。**

这些版本名字采用了伦敦地铁站的名字，**根据字母表的顺序来对应版本时间顺序，**比如：最早的Release版本：Angel，第二个Release版本：Brixton，以此类推……

当一个项目到达发布临界点或者解决了一个严重的 BUG 后就会发布一个 "service Release" 版本， 简称 SR（X）版本，x 代表一个递增数字。

- 引用自：
[聊聊Spring Cloud版本的那些事儿 - http://blog.didispace.com/springcloud-version/](http://blog.didispace.com/springcloud-version/)

## Spring Cloud & Spring Boot 版本对照表

通过查阅官网：[https://spring.io/projects/spring-cloud](https://spring.io/projects/spring-cloud)，我们可以看到一个「Release train Spring Boot compatibility」表：

**Release Train** | **Boot Version**
:-- | :--
Greenwich | 2.1.x
Finchley | 2.0.x
Edgware | 1.5.x
Dalston | 1.5.x

上表可以看出，最新的「Spring Cloud」版本已经出到了 Greenwich... 每个版本都能查阅到当前版本所包含的子项目，以及子项目的版本号，我们可以通过此来决定需要选择怎么样的版本。

# 参考资料

---

[1. 外行人都能看懂的SpringCloud，错过了血亏！ - https://juejin.im/post/5b83466b6fb9a019b421cecc#heading-19](https://juejin.im/post/5b83466b6fb9a019b421cecc#heading-19)
[2. 从架构演进的角度聊聊Spring Cloud都做了些什么？ - http://www.ityouknow.com/springcloud/2017/11/02/framework-and-springcloud.html](http://www.ityouknow.com/springcloud/2017/11/02/framework-and-springcloud.html)
[3. 聊聊Spring Cloud版本的那些事儿 - http://blog.didispace.com/springcloud-version/](http://blog.didispace.com/springcloud-version/)
[4. Spring Cloud 从入门到精通 - http://blog.didispace.com/spring-cloud-learning/](http://blog.didispace.com/spring-cloud-learning/)
[5. Spring Cloud 中文网 - https://springcloud.cc/](https://springcloud.cc/)

---

按照惯例黏一个尾巴：


> 欢迎转载，转载请注明出处！   
> 简书ID：[@我没有三颗心脏](https://www.jianshu.com/u/a40d61a49221)  
> github：[wmyskxz](https://github.com/wmyskxz/)  
> 欢迎关注公众微信号：wmyskxz
> 分享自己的学习 & 学习资料 & 生活
> 想要交流的朋友也可以加qq群：3382693
