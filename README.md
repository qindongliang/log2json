将输出的log转换为json格式。

#### 项目背景
这个小项目的是为了统一我们各种应用日志的输出可以直接转成json，功能比较简单仅仅定义了Log4j的Layout，并且支持一些扩展功能，比如自定义的一些字段，
json是一种比较灵活的格式，我们只需要把输出日志转成json，然后这些数据日志就可以直接推送到Kafka中，最后通过flume或者logstash等其他收集工具，将其收集到hive仓库或者elasticsearch和
solr就可以直接检索快速使用了或者使用spark sql分析，我们应用的日志主要收集到es里面，然后通过kibana就可以快速探索了。

题外话：可以配合KafkaLog4jAppender这个开源包，直接把spark sql项目，spark streaming项目里面日志直接输出到kafka里面。


#### 使用方法

（1）拷贝src/main/resource/template_log4j.properties文件重命名为log4j.properties

（2）模板里面是是一个配置好的内容，直接去test目录下执行准备好的三个case

case 1：最简单的log记录，输出如下

```
{
    "logType": "work1",
    "@timestamp": "2018-08-24T09:25:18.220Z",
    "source_host": "DESKTOP-S44QJNI",
    "method": "testBasicLog",
    "level": "INFO",
    "thread_name": "main",
    "line_number": "21",
    "message": "测试信息",
    "class": "net.test.JsonTest"
}
```

case 2：自定义的两个字段log，支持嵌套，内部用的fastjson来转的json

```
{
    "logType": "work1",
    "@timestamp": "2018-08-24T09:27:36.183Z",
    "source_host": "DESKTOP-S44QJNI",
    "cost": 124,
    "method": "testCustomFiled",
    "level": "INFO",
    "thread_name": "main",
    "line_number": "37",
    "description": "测试自定义的字段",
    "class": "net.test.JsonTest"
}
```

case 3: 针对异常信息日志输出：
```
{
    "logType": "work1",
    "exception": "java.lang.NullPointerException
	at net.test.JsonTest.exceptionLogTest(JsonTest.java:46)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:117)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:42)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:253)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:84)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:147)
",
    "trace_id": 100025,
    "@timestamp": "2018-08-24T09:28:49.276Z",
    "source_host": "DESKTOP-S44QJNI",
    "method": "exceptionLogTest",
    "level": "ERROR",
    "thread_name": "main",
    "line_number": "52",
    "message": "发生异常",
    "class": "net.test.JsonTest"
}
```

（3）一些说明

@timestamp字段是为elasticsearch存储专门定制的，使用logstash可以直接收集到es集群中使用

logType字段在log4j配置的，这个字段固定的值是为了区分不同的业务线的log来源，比如我们
把log统一收集到kafka集群，或者ELK的kibana中，可以通过这个字段来过滤不同类型的数据源。


（4）打包命令

```
mvn clean package
```
编译后的jar包拷贝到项目中就可以直接使用，或者你把源码下载下来把编译后的jar包发布到你们的maven私服，
这样更方便内部使用。



