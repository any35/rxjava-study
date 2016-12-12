RxJava2 响应式编程介绍
====================

<!-- TOC -->

- [响应式编程介绍](#%E5%93%8D%E5%BA%94%E5%BC%8F%E7%BC%96%E7%A8%8B%E4%BB%8B%E7%BB%8D)
    - [什么是响应式编程(Reactive Programming)](#%E4%BB%80%E4%B9%88%E6%98%AF%E5%93%8D%E5%BA%94%E5%BC%8F%E7%BC%96%E7%A8%8Breactive-programming)
    - [响应式宣言](#%E5%93%8D%E5%BA%94%E5%BC%8F%E5%AE%A3%E8%A8%80)
    - [响应式流规范](#%E5%93%8D%E5%BA%94%E5%BC%8F%E6%B5%81%E8%A7%84%E8%8C%83)
    - [响应式扩展](#%E5%93%8D%E5%BA%94%E5%BC%8F%E6%89%A9%E5%B1%95)
    - [基本概念](#%E5%9F%BA%E6%9C%AC%E6%A6%82%E5%BF%B5)
- [RxJava 基础](#rxjava-%E5%9F%BA%E7%A1%80)
    - [RxJava现状](#rxjava%E7%8E%B0%E7%8A%B6)
    - [RxJava2 优势](#rxjava2-%E4%BC%98%E5%8A%BF)
        - [与其它编程模式/库相比](#%E4%B8%8E%E5%85%B6%E5%AE%83%E7%BC%96%E7%A8%8B%E6%A8%A1%E5%BC%8F%E5%BA%93%E7%9B%B8%E6%AF%94)
        - [RxJava 1 vs RxJava 2](#rxjava-1-vs-rxjava-2)
    - [RxJava2中的响应式类](#rxjava2%E4%B8%AD%E7%9A%84%E5%93%8D%E5%BA%94%E5%BC%8F%E7%B1%BB)
        - [RxJava2 主要类关系图](#rxjava2-%E4%B8%BB%E8%A6%81%E7%B1%BB%E5%85%B3%E7%B3%BB%E5%9B%BE)
        - [Flowable & Observable](#flowable--observable)
        - [2.3.2  Single & Completable & Maybe](#232--single--completable--maybe)
- [RxJava2 的主要操作](#rxjava2-%E7%9A%84%E4%B8%BB%E8%A6%81%E6%93%8D%E4%BD%9C)
    - [创建一个Flowable](#%E5%88%9B%E5%BB%BA%E4%B8%80%E4%B8%AAflowable)
    - [转换、过滤与聚合操作](#%E8%BD%AC%E6%8D%A2%E8%BF%87%E6%BB%A4%E4%B8%8E%E8%81%9A%E5%90%88%E6%93%8D%E4%BD%9C)
    - [异步与并发（Asynchronized & Concurrency）](#%E5%BC%82%E6%AD%A5%E4%B8%8E%E5%B9%B6%E5%8F%91asynchronized--concurrency)
        - [observeOn & subscribeOn & Scheduler](#observeon--subscribeon--scheduler)
        - [多线程并发示例](#%E5%A4%9A%E7%BA%BF%E7%A8%8B%E5%B9%B6%E5%8F%91%E7%A4%BA%E4%BE%8B)
        - [阻塞与非阻塞示例](#%E9%98%BB%E5%A1%9E%E4%B8%8E%E9%9D%9E%E9%98%BB%E5%A1%9E%E7%A4%BA%E4%BE%8B)
    - [错误处理 (Error Handling)](#%E9%94%99%E8%AF%AF%E5%A4%84%E7%90%86-error-handling)
    - [冷热数据流](#%E5%86%B7%E7%83%AD%E6%95%B0%E6%8D%AE%E6%B5%81)
        - [ConnectableFlowable & publish & connect](#connectableflowable--publish--connect)
        - [replay](#replay)
        - [cache](#cache)
    - [RxJava 测试](#rxjava-%E6%B5%8B%E8%AF%95)
- [Reference](#reference)

<!-- /TOC -->

##  响应式编程介绍

###  什么是响应式编程(Reactive Programming)

- 响应式编程是一种面向数据流和变化传播的编程范式。这意味着可以在编程语言中很方便地表达静态或动态的数据流，而相关的计算模型会自动将变化的值通过数据流进行传播。

### 响应式宣言

响应式宣言,[Reactive Manifesto](https://github.com/reactivemanifesto/reactivemanifesto)：
来自不同领域的组织正在不约而同地发现一些看起来如出一辙的软件构建模式。它们的系统更加稳健，更加有可回复性，更加灵活，并且以更好的定位来满足现代的需求。

响应式宣言针对一个系统而言，并不等同于 响应式编程规范，响应式系统应该满足如下特点：

- 反应灵敏的[Responsive]：只要有可能，系统就会及时响应。 
- 有回复性的[Resilient]：系统在面临故障时也能保持及时响应。
- 可伸缩的[Elastic]：系统在变化的工作负载下保持及时响应。
- 消息驱动的[Message Driven]：响应式系统依赖异步消息传递来建立组件之间的界限，这一界限确保了松耦合，隔离，位置透明性等特性的实现，还提供了以消息的形式把故障委派出去的手段。

![reactive-traits.svg](http://www.reactivemanifesto.org/images/reactive-traits.svg)

### 响应式流规范

- [Reactive Streams](http://www.reactive-streams.org/) 规范提供一个非堵塞的异步流处理的抗压(breakpressure)标准；Reactive Streams的目标是增加抽象层，而不是进行底层的流处理，规范将这些问题留给了库实现来解决。

- 对于JVM，目前已经有多个库实现该标准，RxJava2, akka-streams,[Reactor](https://github.com/reactor/reactor) 等；
- 统一标准的好处就是 各个实现产生的数据可以方便的转换和消费；

> 示例

```java 
    Path filePath = Paths.get("build.gradle");
    // RxJava2 to Reactor
    Flowable<String> flowable = Flowable
            .fromCallable(() -> Files.readAllLines(filePath))
            .flatMap(x -> Flowable.fromIterable(x));
    Flux.from(flowable).count().subscribe(System.out::println);

    // Reactor to RxJava2
    try
    {
        Flux<String> flux = Flux.fromIterable(Files.readAllLines(filePath));
        Flowable.fromPublisher(flux).count()
                .subscribe(System.out::println);
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
```
- Reactive Streams JVM接口由以下四个interface 组成：
    - Publisher ： 消息发布者

    - Subscriber ： 消息订阅者

    - Subscription ： 一个订阅

    - Processor ： Publisher + Subscriber 的结合体

- Reactive Streams 规范[主要目标](https://www.infoq.com/news/2015/09/reactive-streams-introduction)：
    
    - 通过异步边界([Asynchronous Boundary](https://github.com/reactive-streams/reactive-streams-jvm/issues/46))来解耦系统组件。 解偶的先决条件，分离事件/数据流的发送方和接收方的资源使用;
    - 为背压（ back pressure ) 处理定义一种模型。流处理的理想范式是将数据从发布者推送到订阅者，这样发布者就可以快速发布数据，同时通过压力处理来确保速度更快的发布者不会对速度较慢的订阅者造成过载。背压处理通过使用流控制来确保操作的稳定性并能实现优雅降级，从而提供弹性能力。

- 该规范将包含在 JDK 9 的[java.util.concurrent.Flow](http://gee.cs.oswego.edu/dl/jsr166/dist/docs/java/util/concurrent/Flow.html) 类中，包含四个接口类。 

- [适用范围](https://medium.com/@kvnwbbr/a-journey-into-reactive-streams-5ee2a9cd7e29)： 适合于流处理的系统有ETL（Extract、Transform、Load）与复杂事件处理（CEP）系统，此外还有报表与分析系统。

### 响应式扩展
- 响应式扩展(Reactive Extensions, ReactiveX), 一般简写为Rx，最初是LINQ的一个扩展，由微软的架构师Erik Meijer领导的团队开发，在2012年11月开源[[1]](http://download.microsoft.com/download/4/E/4/4E4999BA-BC07-4D85-8BB1-4516EC083A42/Rx%20Design%20Guidelines.pdf)；

- Rx是一个编程模型，目标是提供一致的编程接口，帮助开发者更方便的处理异步数据流，Rx库支持.NET、JavaScript和C++，Rx近几年越来越流行了，现在已经支持几乎全部的流行编程语言了，Rx的大部分语言库由ReactiveX这个组织负责维护，比较流行的有RxJava/RxJS/Rx.NET，社区网站是 reactivex.io。

- Rx = Observables + LINQ + Schedulers。Rx 让开发者可以利用可观察序列和LINQ风格查询操作符来编写异步和基于事件的程序;

- RxJava 是 在Java虚拟机上实现的Reactive Extensions（响应式扩展)库;  

### 基本概念
- 同步/异步： 关注的是消息通信机制，同步是指 发出一个*调用*，在没有得到结果之前，该*调用*就不返回，但是一旦调用返回，就得到返回值了；
而异步是指 *调用*发出后，调用直接返回，但不会立刻得到调用的结果。而是在*调用*发出后，*被调用者*通过状态、通知来通知调用者，或通过回调函数处理这个调用；
异步强调被动通知。
- 阻塞/非阻塞：关注的是程序在等待调用结果（消息，返回值）时的状态；阻塞调用是指调用结果返回之前，当前线程会被挂起。调用线程只有在得到结果之后才会返回。
非阻塞调用指在不能立刻得到结果之前，该调用不会阻塞当前线程。如java8 stream为阻塞式，Future为非阻塞式的；
非阻塞强调状态主动轮询。

- 并发(Concurrent)与并行(Parallel)：并发性，又称共行性，是指能处理多个同时性活动的能力；并行是指同时发生的两个并发事件，具有并发的含义，而并发则不一定并行，也亦是说并发事件之间不一定要同一时刻发生; `并行`是`并发`的子集。

- 其它名词： https://github.com/reactivemanifesto/reactivemanifesto/blob/master/glossary.zh-cn.md

## RxJava 基础

### RxJava现状
- RxJava 项目地址 <https://github.com/ReactiveX/RxJava>
- RxJava 1.x 先于 Reactive Streams 规范出现,部分接口支持Reactive Streams 规范；
- RxJava 2.0 于 2016.10.29 [正式发布](https://github.com/ReactiveX/RxJava/releases/tag/v2.0.0)，已经按照Reactive-Streams specification规范完全的重写, 基于Java8+;
- RxJava 2.0已经独立于RxJava 1.x而存在，即 RxJava2(io.reactivex.*)  使用与RxJava1（rx.*） 不同的包名。
- RxJava 目前在Android 开发上应用较多；

### RxJava2 优势

#### 与其它编程模式/库相比

- Rx扩展了观察者模式用于支持数据和事件序列，通过一些的操作符号，统一风格，用户无需关注底层的实现：如线程、同步、线程安全、并发数据结构和非阻塞IO；

|| 单个数据 	|  多个数据 | 
|:--|:--|:--|
| 同步	|`T getData()`        |	`Iterable<T> getData()`   |
| 异步	|`Future<T> getData()`|	`Observable<T> getData()` |

- 对于单条数据可以选择Future 模式，但是多条异步数据组合，Future就相对不方便；

- 对于同步的多条数据，Observable/Flowable 和 Java8 Stream 都要比Iterable更加方便。

- 对比 Java 8 Stream,都是属于函数式编程(Monad)，Stream主要是对数据集的阻塞处理， 而Rx 是非阻塞的，并且在时间纬度上处理发射的数据 ，RxJava2提供更加丰富的操作集。

- 对比 Reactor-core, 都遵循响应式流规范，Reactor(Flux)和RxJava2(Flowable)可以相互转换, Reactor更多的依赖java8的函数式接口，RxJava2 所有函数式接口都提供异常抛出，在写代码时更加便利。

- RxJava 函数式风格,简化代码(Rx的操作符通通常可以将复杂的难题简化为很少的几行代码), 异步错误处理,轻松使用并发;



#### RxJava 1 vs RxJava 2

- RxJava 2x 不再支持 null 值，如果传入一个null会抛出 NullPointerException

```java
    Observable.just(null);
    Single.just(null);
    Flowable.just(null);
    Maybe.just(null);
    Observable.fromCallable(() -> null)
            .subscribe(System.out::println, Throwable::printStackTrace);
    Observable.just(1).map(v -> null)
            .subscribe(System.out::println, Throwable::printStackTrace);

```
 - RxJava2 所有的函数接口(Function/Action/Consumer)均设计为可抛出Exception，解决编译异常需要转换问题。

 - RxJava1 中Observable不能很好支持背压，在RxJava2 中将Oberservable实现成不支持背压，而新增Flowable 来支持背压。
 - 详细参考请参考[5]

### RxJava2中的响应式类

#### RxJava2 主要类关系图
如下图所示，为RxJava2中的主要类关系图，可清晰知道各响应式类的联系和区别。后面无特别说明均以Flowable说明。
![Publisher-Subscriber-class-relation.png](Publisher-Subscriber-class-relation.png)


#### Flowable & Observable

- **Observable**: 不支持背压；

- **Flowable** : Observable新的实现，支持背压，同时实现Reactive Streams 的 Publisher 接口。

- 什么时候用 Observable:
    - 一般处理最大不超过1000条数据，并且几乎不会出现内存溢出； 
    - 如果式GUI 鼠标事件，频率不超过1000 Hz,基本上不会背压（可以结合 sampling/debouncing 操作）；
    - 如果处理的式同步流而你的Java平台又不支持Java Stream（如果有异常处理，Observable 比Stream也更适合）;

- 什么时候用 Flowable: 
    - 处理以某种方式产生超过10K的元素；
    - 文件读取与分析，例如 读取指定行数的请求；
    - 通过JDBC 读取数据库记录， 也是一个阻塞的和基于拉取模式，并且由ResultSet.next() 控制；
    - 网络IO流;
    - 有很多的阻塞和/或 基于拉取的数据源，但是又想得到一个响应式非阻塞接口的。

#### 2.3.2  Single & Completable & Maybe

- **Single**: 可以发射一个单独onSuccess 或 onError消息。它现在按照Reactive-Streams规范被重新设计,并遵循协议 onSubscribe (onSuccess | onError)? .SingleObserver改成了如下的接口;

```java
interface SingleObserver<T> {
    void onSubscribe(Disposable d);
    void onSuccess(T value);
    void onError(Throwable error);
}
```

- **Completable**: 可以发送一个单独的成功或异常的信号，按照Reactive-Streams规范被重新设计,并遵循协议onSubscribe (onComplete | onError)?

```java
    Completable.create(new CompletableOnSubscribe()
    {
        @Override
        public void subscribe(CompletableEmitter e) throws Exception
        {
            Path filePath = Paths.get("build.gradle");
            Files.readAllLines(filePath);
            e.onComplete();
        }
    }).subscribe(() -> System.out.println("OK!"),
            Throwable::printStackTrace);
```
- **Maybe**:从概念上来说，它是Single 和 Completable 的结合体。它可以发射0个或1个通知或错误的信号, 遵循协议 onSubscribe (onSuccess | onError | onComplete)?。

```java
Maybe.just(1)
        .map(v -> v + 1)
        .filter(v -> v == 1)
        .defaultIfEmpty(2)
        .test()
        .assertResult(21);
//        java.lang.AssertionError: Values at position 0 differ; Expected: 21 (class: Integer), Actual: 2 (class: Integer) (latch = 0, values = 1, errors = 0, completions = 1)
//
//        at io.reactivex.observers.BaseTestConsumer.fail(BaseTestConsumer.java:133)
//        ....
```

## RxJava2 的主要操作

我们已经知道 RxJava主要特性为为一个扩展的观察者模式、流式操作和异步编程，支持ReactiveX 规范给出的一些操作， 同时RxJava2 符合响应式流规范，接下来以Flowable为例，按照功能分类讲解RxJava2中的重要操作[[9]](http://reactivex.io/documentation/operators.html);

### 创建一个Flowable

- fromArray & fromIterable & just,直接从数组或迭代器中产生；

```java
    List<String> list = Arrays.asList(
            "blue", "red", "green", "yellow", "orange", "cyan", "purple"
    );
    Flowable.fromIterable(list).skip(2).subscribe(System.out::println);
    Flowable.fromArray(list.toArray()).subscribe(System.out::println);
    Flowable.just("blue").subscribe(System.out::println);
```

- fromFuture & fromCallable：
>fromFuture, 事件从非主线程中产生；
fromCallable, 事件从主线程中产生， 在需要消费时生产；

```java
    ExecutorService executor = Executors.newFixedThreadPool(2);
    System.out.println("MAIN: " + Thread.currentThread().getId());
    Callable<String> callable = () -> {
        System.out.println("callable [" + Thread.currentThread().getId() + "]: ");
        Path filePath = Paths.get("build.gradle");
        return Files.readAllLines(filePath).stream().flatMap(s -> Arrays.stream(s.split
                (""))).count() + "";
    };

    Future<String> future = executor.submit(callable);

    Consumer<String> onNext = v -> System.out
            .println("consumer[" + Thread.currentThread().getId() + "]:" + v);

    Flowable.fromCallable(callable).subscribe(onNext);
    Flowable.fromFuture(future).subscribe(onNext);
    System.out.println("END");
```
- fromPublisher ，从标准(Reactive Streams)的发布者中产生；

- 自定义创建(generate & create)
> 下面以斐波那契数列产生为例说明 generate & create的使用， generate为RxJava2新增的创建方式。 

```java
    class Fib
    {
        long a;
        long b;

        public Fib(long a, long b)
        {
            this.a = a;
            this.b = b;
        }

        public long fib()
        {
            return a + b;
        }
    }

    //斐波那契数列
    Flowable.create(new FlowableOnSubscribe<Fib>()
    {
        @Override
        public void subscribe(FlowableEmitter<Fib> e) throws Exception
        {
            Fib start = new Fib(1L, 1L);

            while (!e.isCancelled()) {
                e.onNext(start);
                start = new Fib(start.b, start.fib());
            }
            e.onComplete();
        }
    }, BackpressureStrategy.BUFFER).map(x -> x.fib()).take(10).subscribe(System.out::println);

    Flowable.generate(() -> new Fib(1L, 1L), (x, y) -> {
        Fib fib = new Fib(x.b, x.fib());
        y.onNext(fib);
        return fib;
    }).ofType(Fib.class).map(x -> x.fib()).take(10).subscribe(System.out::println);
```

- amb & concat & merge, 由多个Flowable产生结合;

   -  **amb**: 给定两个或多个Flowable，只发射最先发射数据的Flowable，如下面示例中的f1被发射； 

   - **concat**: 给定多个Flowable， 按照Flowable数组顺序,依次发射数据，不会交错，下面示例中f1,f2中数据依次发射;
   - **merge**: 给定多个Flowable， 按照Flowable数组中数据发射的顺序组合成新的Flowable，各Flowable数据可能会交错；
   - **switchOnNext**：给定能发射多个Flowable的Flowable,顺序发射各子Flowable,最新发射的子Flowable覆盖当前子Flowable中还未发射的元素。
    ![zip](https://raw.githubusercontent.com/wiki/ReactiveX/RxJava/images/rx-operators/switchDo.png)

```java
    Flowable<String> f1 = Flowable.intervalRange(1, 10, 1, 1, TimeUnit.SECONDS).map(index -> "f1-" + index);
    Flowable<String> f2 = Flowable.intervalRange(1, 3, 2, 2, TimeUnit.SECONDS).map(index -> "f2-" + index);

    Flowable.ambArray(f1, f2).map(x -> "amb: " + x).subscribe(System.out::println);
    System.out.println("----------concat-----------");
    Flowable.concat(f1, f2).map(x -> "concat: " + x).subscribe(System.out::println);

    System.out.println("----------merge-----------");
    Flowable.merge(f1, f2).map(x -> "merge: " + x).subscribe(System.out::println);

    Flowable<String>[] flowables = new Flowable[]{f1, f2};
    Flowable.switchOnNext(Flowable.intervalRange(0, 2, 0, 3, TimeUnit.SECONDS).map(i -> flowables[i.intValue()]))
            .map(x -> "switchOnNext-" + x).subscribe(System.out::println);
    Flowable.intervalRange(0, 2, 0, 3, TimeUnit.SECONDS).map(i -> flowables[i.intValue()])
            .switchMap((io.reactivex.functions.Function) Functions.identity())
            .map(x -> "switchMap-" + x).subscribe(System.out::println);
```
- zip & combineLatest, 多Flowable中元素结合变换
    - **zip** ：每个Flowable中的元素都按顺序结合变换，直到元素最少Flowable的已经发射完毕；
    ![zip](https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/zip.png)

    - **combineLatest**: 每个Flowable中的发射的元素都与其他Flowable最近发射的元素结合变换，知道所有的Flowable的元素发射完毕；
    ![zip](https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/combineLatest.png)


### 转换、过滤与聚合操作
在Java8中Stream也有包含这些功能的操作，由于多了时间这个维度，在 RxJava 中操作相对更加丰富。
这里主要介绍一些重点操作。

- buffer & groupBy & window
>buffer 和 window 都可以按时间或者元素数量窗口，buffer是直接转换成元素集，window是将元素集转换成另一个Flowable，
>groupBy,按照key 来分组，需要元素发射完成才能消费，如果只是对数据处理使用Java8 groupBy更方便；

```java
    Flowable<String> f1 = Flowable.intervalRange(1, 10, 1, 1, TimeUnit.SECONDS).delay((t) ->
            Flowable.timer(t % 3 + new Random().nextLong() % 3, TimeUnit.SECONDS))
            .map(index -> index % 3 + "-f1-" + index);
    f1.buffer(5, TimeUnit.SECONDS).map(x -> "buffer-" + x).subscribe(System.out::println);

    f1.window(5, TimeUnit.SECONDS).map(x -> x.toList())
            .subscribe(x -> x.subscribe(System.out::println));

    Disposable b = f1.groupBy((x) -> x.split("-", 2)[0])
            .subscribe(x -> x.toList().subscribe(System.out::println));
    Map<String, List<String>> map = f1.toList().blockingGet().stream()
                .collect(Collectors.groupingBy((x) -> x.split
                        ("-", 2)[0]));
    System.out.println(map);

    while (!b.isDisposed()) {
    }

```
- debounce & throttleFirst & sample 按照时间区间采集数据

>debounce 防抖动，两元素发射间隔，在设定的超时时间内将不被发射， 在前端APP应用较多。
![debounce](https://raw.githubusercontent.com/wiki/ReactiveX/RxJava/images/rx-operators/debounce.png)

> throttle 限流操作，对于 throttleFirst是 取发射后元素，经过间隔时间后的第一个元素进行发射。
![throttleFirst](https://raw.githubusercontent.com/wiki/ReactiveX/RxJava/images/rx-operators/throttleFirst.s.png)

> sample 数据采样, 对于源数据，发射间隔时间内的最后出现的元素。
![sample](https://raw.githubusercontent.com/wiki/ReactiveX/RxJava/images/rx-operators/sample.s.png)

- take & skip & first & emlmentAt,精确获取数据(集)
> take, 类似java8 limit 操作，但是这里支持更多的操作(take/takeLast/takeUntil/takeWhen)，同时支持在时间区间上获取数据集；
> skip, 类似java8 skip 操作,但是这里的可以扩展到时间区间上
> first/firstElement/last/lastElement, 由 Flowable -> Single/Maybe.

```java
        Flowable<String> f1 = Flowable
                .fromArray("blue", "red", "green", "yellow11", "orange", "cyan", "purple"
                );

        f1.elementAt(4, "hello").subscribe(System.out::println);
        //out: orange
        f1.takeUntil(x -> x.length() > 5).map(x -> "takeUntil-" + x).toList()
                .subscribe(System.out::println);
        //out: [takeUntil-blue, takeUntil-red, takeUntil-green, takeUntil-yellow11]
        f1.takeWhile(x -> x.length() <= 5).map(x -> "takeWhile-" + x).toList()
                .subscribe(System.out::println);
        //out: [takeWhile-blue, takeWhile-red, takeWhile-green]

        f1.skipWhile(x -> x.length() <= 5).map(x -> "skipWhile-" + x).toList()
                .subscribe(System.out::println);
        //[skipWhile-yellow11, skipWhile-orange, skipWhile-cyan, skipWhile-purple]

        Disposable d = f1.delay(v -> Flowable.timer(v.length(), TimeUnit.SECONDS))
                .skipUntil(Flowable.timer(5, TimeUnit.SECONDS)).map(x -> "skipUntil-" + x)
                .subscribe(System.out::println);
//        skipUntil-green
//        skipUntil-orange
//        skipUntil-purple
//        skipUntil-yellow11
        while (!d.isDisposed()) {
        }

```

### 异步与并发（Asynchronized & Concurrency）
RxJava 通过一些操作统一了 同步和异步，阻塞与非阻塞，并行与并发编程。


#### observeOn & subscribeOn & Scheduler

- subscribeOn 和 observeOn 都是用来切换线程用的,都需要参数 Scheduler.
- Scheduler ,调度器, 是RxJava 对线程控制器 的 一个抽象,RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：
   - trampoline, 直接在当前线程运行（继续上一个操作中，最后处理完成的数据源所处线程，并不一定是主线程），相当于不指定线程;
   - computation, 这个 Scheduler 使用的固定的线程池(FixedSchedulerPool)，大小为 CPU 核数, 适用于CPU 密集型计算。
   - io,I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率;
   - newThread, 总是启用新线程，并在新线程中执行操作；
   - single， 使用定长为1 的线程池（newScheduledThreadPool(1)），重复利用这个线程;
   - Schedulers.from， 将java.util.concurrent.Executor 转换成一个调度器实例。
```java
    java.util.function.Consumer<Object> pc = x -> System.out
            .println("Thread[" + Thread.currentThread().getName() + " ," + Thread
                    .currentThread().getId() + "] :" + x);
    Executor executor = Executors.newFixedThreadPool(2);
    Schedulers.from(executor).scheduleDirect(() -> pc.accept("executor one"));
    Schedulers.from(executor).scheduleDirect(() -> pc.accept("executor two"));
    Schedulers.trampoline().scheduleDirect(() -> pc.accept("trampoline"), 1, TimeUnit.SECONDS);
    Schedulers.single().scheduleDirect(() -> pc.accept("single one DONE"));
    Schedulers.single().scheduleDirect(() -> pc.accept("single two DONE"));
    Schedulers.computation()
            .scheduleDirect(() -> pc.accept("computation one DONE"), 1, TimeUnit.SECONDS);
    Schedulers.computation()
            .scheduleDirect(() -> pc.accept("computation two DONE"), 1, TimeUnit.SECONDS);
    Schedulers.io().scheduleDirect(() -> pc.accept("io one DONE"));
    Schedulers.io().scheduleDirect(() -> pc.accept("io two DONE"), 1, TimeUnit.SECONDS);
    Schedulers.io().scheduleDirect(() -> pc.accept("io tree DONE"), 1, TimeUnit.SECONDS);
    Schedulers.newThread().scheduleDirect(() -> pc.accept("newThread tree DONE"));
```   

- subscribeOn 将Flowable 的数据发射 切换到 Scheduler 所定义的线程， 只有第一个 subscribeOn 操作有效 ；

- observeOn 指定 observeOn 后续操作所在线程，可以联合多个 observeOn 将切换多次 线程 ；
    > 示例
    > Schedulers.newThread() 定义的线程发送数据；
    > 
    >Schedulers.computation() 定义的线程 执行doOnNext；
    >
    >Schedulers.single() 执行 subscribe

    ```java
    Consumer<Object> threadConsumer = x -> System.out
            .println("Thread[" + Thread.currentThread().getName() + " ," + Thread
                    .currentThread().getId() + "] :" + x);

    Flowable<Path> f1 = Flowable.create((FlowableEmitter<Path> e) -> {
        Path dir = Paths.get("/home/clouder/berk/workspaces/cattle").toRealPath();
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
            Iterator<Path> iter = dirStream.iterator();
            while (iter.hasNext() && !e.isCancelled()) {
                e.onNext(iter.next());
            }
            e.onComplete();
        }
    }, BackpressureStrategy.BUFFER);
    f1.subscribeOn(Schedulers.newThread()).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation()).take(5).doOnNext(consumer).observeOn(Schedulers
            .single()).subscribe(consumer);
    ```

#### 多线程并发示例
上小节给出示例 发射元素都会经过同样的线程切换，元素间不会产生并行执行的效果。
如果需要达到 类似 Java8 parallel 执行效果。可以采用FlatMap 变换 自定义并发操作，在返回的Flowable进行线程操作，如下示例所示：

 - f1 中元素会在Schedulers.newThread()中发射；

 - 读取文本内容的操作(Files::readAllLines, Collection::size) 会在 Schedulers.io() 所指定的线程池执行；

 - sorted 操作会在  Schedulers.computation() 所指定的线程池中执行；
 
 - subscribe() 同样会在 Schedulers.computation() 所指定的线程池中执行；
 
```java
    f1.filter(Files::isRegularFile).doOnNext(consumer).subscribeOn(Schedulers.newThread())
            .flatMap(y -> Flowable.just(y).subscribeOn(Schedulers.io())
                    .map(Files::readAllLines)).map(Collection::size)
            .observeOn(Schedulers.computation()).doOnNext(consumer)
            .sorted(Comparator.naturalOrder())
            .observeOn(Schedulers.trampoline()).subscribe(consumer);
```

#### 阻塞与非阻塞示例

 - 从阻塞到非阻塞 我们可以通过 subscribeOn() 来达到；
```java
    //f1 为 主线程发射数据的Flowable
    //会阻塞主线程知道消费完成
    f1.subscribe(System.out::println);
    
    // d会理解返回.
    Disposable d = f1.subscribeOn(Schedulers.newThread())
            .subscribe(System.out::println);
    while (!d.isDisposed()) {
    }
```
- 从非阻塞到阻塞，可以通过blocking* 相关操作来实现

```java
    Flowable<Path> f2 = f1.subscribeOn(Schedulers.newThread());
    //f2 为非阻塞flowable
    // 可以通过 blockingSubscribe 变为在主线程上消费
    f2.blockingSubscribe(System.out::println);
    // 也可以通过下面操作返回结果。
    List<Path> list = f2.toList().blockingGet();
    Iterable<Path> iterator = f2.blockingIterable();

```

### 错误处理 (Error Handling)

- 无需显示的catch 编译异常，RxJava2 已经支持所有函数接口抛出Exception；
> 如下示例 会打印第一个异常；

```java
    Flowable<Long> f1 = Flowable.interval(500, TimeUnit.MILLISECONDS).map(index -> {
        throw new IOException(index + "");
    }).map(index -> {
        throw new IllegalArgumentException(index + "");
    });
    Disposable d = f1.subscribe(System.out::println, Throwable::printStackTrace);
    while (!d.isDisposed()) {
    }
```
- 异常可以被转换,源数据发射终止
    - 当出现异常时，可以通过 onErrorReturn* 转换成一个正常值返回；
    - 当出现异常时，通过 onErrorResumeNext 自定义一个Publisher返回，意味着可以转换一个异常类型；

```java
    Flowable<Long> f1 = Flowable.interval(500, TimeUnit.MILLISECONDS).map(index -> {
        throw new IOException(index + "");
    }).map(index -> {
        throw new IllegalArgumentException(index + "");
    });
    f1.onErrorReturnItem(-1L).take(5)
            .subscribe(System.out::println, Throwable::printStackTrace);
    // 打印 -1 
    Disposable d = f1.onErrorResumeNext(e -> {
        if (e instanceof IOException) {
            return Flowable.error(new UncheckedIOException((IOException) e));
        }
        return Flowable.error(e);
    }).subscribe(System.out::println, Throwable::printStackTrace);
    // 打印 UncheckedIOException 异常
    while (!d.isDisposed()) {
    }
```

- Flowable,map抛出异常,但数据继续发射
 >暂没有找到直接方法可以达到，但可以采取如下两种方法达到

```java
    Function<Long, Long> exceptionMap = x -> {
        if (new Random().nextInt(5) > 2) {
            throw new IOException(x + "");
        }
        return x;
    };
    
    // 使用flatMap + onErrorReturnItem
    Flowable<Long> f1 = Flowable.interval(500, TimeUnit.MILLISECONDS);
    f1.flatMap(index -> Flowable.just(index).map(exceptionMap).onErrorReturnItem(-1L))
    .take(5).subscribe(System.out::println);
   
   //直接封装lift 操作
   public class ErrorResumeOperator<D, U> implements FlowableOperator<D, U>
    {
        private final Function<U, D> function;
        private final D defaultValue;

        public ErrorResumeOperator(Function<U, D> function, D defaultValue)
        {
            this.function = function;
            this.defaultValue = defaultValue;
        }

        @Override
        public Subscriber<? super U> apply(Subscriber<? super D> observer) throws Exception
        {
            Subscriber<U> subscriber = new Subscriber<U>()
            {
                @Override
                public void onSubscribe(Subscription s)
                {
                    observer.onSubscribe(s);
                }

                @Override
                public void onNext(U onNext)
                {
                    try {
                        observer.onNext(function.apply(onNext));
                    }
                    catch (Exception e) {
                        observer.onNext(defaultValue);
                    }
                }

                @Override
                public void onError(Throwable t)
                {
                    observer.onError(t);
                }

                @Override
                public void onComplete()
                {
                    observer.onComplete();
                }
            };
            return subscriber;
        }
    }
    Disposable d = f1.lift(new ErrorResumeOperator<>(exceptionMap, -1L)).take(5)
            .subscribe(System.out::println);

    while (!d.isDisposed()) {
    }
```

- 出错重试(retry)
>RxJava 提供了retry以及相关的多个操作，提供出错后重新发射数据功能；

```java

    Function<Long, Long> exceptionMap = x -> {
        if (new Random().nextInt(5) > 3) {
            throw new IOException(x + "");
        }
        if (new Random().nextInt(6) < 1) {
            throw new SQLException(x + "");
        }
        return x;
    };
    Flowable<Long> f1 = Flowable.interval(500, TimeUnit.MILLISECONDS);
// 仅为 IOException 异常时最多重试3次,其它异常立即打印异常
    Disposable d = f1.map(exceptionMap).retry(3, e -> e instanceof IOException)
            .subscribe(System.out::println, Throwable::printStackTrace);
    while (!d.isDisposed()) {
    }
```

### 冷热数据流

####  ConnectableFlowable & publish & connect 
 - ConnectableFlowable 可连接的Flowable， 不管是否消费，只有调用了connect， 数据就一直在发射，不受消费影响 ('冷' 的Flowable 变成'热'的)
 - publish 将 普通 Flowable，变成 ConnectableFlowable ；
 
![publishConnect](https://raw.githubusercontent.com/wiki/ReactiveX/RxJava/images/rx-operators/publishConnect.png);

```java
    ConnectableFlowable<String> f1 = Flowable.generate(() -> new BufferedReader(new InputStreamReader(System.in))
            , (reader, e) -> {
                while (true) {
                    String line = reader.readLine();
                    if (line == null || line.equalsIgnoreCase("exit")) {
                        break;
                    }
                    e.onNext(line);
                }
                e.onComplete();
            }).ofType(String.class).subscribeOn(Schedulers.io()).publish();

    TimeUnit.SECONDS.sleep(5);
    f1.connect(System.out::println);
    TimeUnit.SECONDS.sleep(5);
    f1.observeOn(Schedulers.newThread()).map(x -> "s0- " + x).subscribe(System.out::println);
    TimeUnit.SECONDS.sleep(5);
    f1.map(x -> "s1- " + x).subscribe(System.out::println);
    TimeUnit.SECONDS.sleep(50);
```
#### replay
replay 将Flowable变成 ConnectableFlowable, 在connect之后，确保每次消费都使用相同数据。

```java
    java.util.function.Function<String, Consumer<Object>> m = s -> v -> System.out
            .println("[" + System.currentTimeMillis() / 100 + "] " + s + "-" + v);
    ConnectableFlowable<Long> f1 = Flowable.intervalRange(1, 100, 0, 1, TimeUnit.SECONDS)
            .onBackpressureBuffer().replay();
    m.apply("").accept("start");
    TimeUnit.SECONDS.sleep(5);
    f1.connect();
    TimeUnit.SECONDS.sleep(5);
    f1.subscribe(m.apply("o1"));

    TimeUnit.SECONDS.sleep(5);
    f1.subscribe(m.apply("o2"));
    TimeUnit.SECONDS.sleep(20);
```
#### cache
缓存功能，将Flowable进行缓存

```java
    java.util.function.Function<String, Consumer<Object>> m = s -> v -> System.out
            .println("[" + System.currentTimeMillis() / 100 + "] " + s + "-" + v);

    Flowable<Path> f1 = Flowable.create((FlowableEmitter<Path> e) -> {
        Path dir = Paths.get("/home/clouder/berk/workspaces/cattle").toRealPath();
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
            Iterator<Path> iter = dirStream.iterator();
            while (iter.hasNext() && !e.isCancelled()) {
                Path path = iter.next();
                m.apply("-----create").accept(path);
                e.onNext(path);
            }
            e.onComplete();
        }
    }, BackpressureStrategy.BUFFER).cache();

    f1.count().subscribe(m.apply("count"));
    f1.filter(Files::isDirectory).subscribe(m.apply("filter"));
```


问题描述： 在rxjava中会经常遇到一种情况就是被观察者发送消息太快以至于它的操作符或者订阅者不能及时处理相关的消息。那么随之而来的就是如何处理这些未处理的消息。

>如下示例： f1 比 f2 元素发射速度快一倍。而zip是按照发射顺序结合，所以出现f1的产生速度快于其消费速度，因此会有背压问题产生（当发射到一定数量时会有异常抛出）。

```java
    Consumer<Object> consumer = v -> System.out.println("[" + System.currentTimeMillis() / 100 + "] " + v);
    Flowable<Long> f1 = Flowable.interval(100, TimeUnit.MILLISECONDS);
    Flowable<Long> f2 = Flowable.interval(200, TimeUnit.MILLISECONDS);

    Flowable<Long> f3 = Flowable.zip(f1, f2, (x, y) -> x * 10000 + y);

    f3.subscribe(consumer);
```
对于出现的背压问题：
 - Flowable默认队列大小为128，并且规范要求，所有的操作符强制支持背压。
 - 通过操作节流(Throttling)相关操作(sample 、throttleLast、throttleFirst、throttleWithTimeout、debounce等)来改变Flowable的发射数率；

 - 通过设置缓冲区和窗口(buffer,window)操作,来缓存过剩的数据，然后发送特定数据。

 - 设置背压策略（onBackpressurebuffer & onBackpressureDrop & onBackpressureLatest）  
    

### RxJava 测试
RxJava2 支持test() 操作符，将Flowable转变为 TestSubscriber,从而支持多种断言操作。
```java
    List<String> list = Arrays.asList(
            "orange", "blue", "red", "green", "yellow", "cyan", "purple");

    Flowable.fromIterable(list).subscribeOn(Schedulers.newThread()).sorted().test().assertValues(list.stream().sorted().toArray(String[]::new));
    Flowable.fromIterable(list).count().test().assertValue(Integer.valueOf(list.size()).longValue());
    List<String> out1 = Flowable.fromIterable(list).sorted().test().values();
```

## Reference

1. 响应式宣言.https://github.com/reactivemanifesto/reactivemanifesto/blob/master/README.zh-cn.md

2. RxJava 2.0 Released with Support for Reactive Streams Specification. https://www.infoq.com/news/2016/11/rxjava-2-with-reactive-streams

3. https://www.lightbend.com/blog/7-ways-washing-dishes-and-message-driven-reactive-systems

4. Use reactive streams API to combine akka-streams with rxJava. http://www.smartjava.org/content/use-reactive-streams-api-combine-akka-streams-rxjava

5. What's different in 2.0.https://github.com/ReactiveX/RxJava/wiki/What%27s-different-in-2.0