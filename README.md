# spring-boot-sleuth-rabbit-demo-rest

This demo shows a problem when using `spring-cloud-sleuth-amqp-starter` with a combination of a AMQP message and a REST call in the same flow

1. start RabbitMQ
2. start the app
3. call http://localhost:8888/start
4. this will trigger a rabbit message, which will be handled by `MyMessageHandler.onMessage(MyMessage)`
5. this will trigger a GET REST call to http://localhost:8888/test-rest, which will be handled by `MyMessageHandler.testRest()`


With the version 0.9 of `spring-cloud-sleuth-amqp-starter` used, the log output looks like this:

```

2018-02-06 13:04:05.068  INFO [-,764044ad51077954,764044ad51077954,false] 7860 --- [nio-8888-exec-1] com.example.demo.DemoApplication         : Handling home
2018-02-06 13:04:05.068  INFO [-,764044ad51077954,764044ad51077954,false] 7860 --- [nio-8888-exec-1] com.example.demo.MyMessageSender         : just about to send
2018-02-06 13:04:05.538  INFO [-,764044ad51077954,09f756d816b478b4,false] 7860 --- [cTaskExecutor-1] com.example.demo.MyMessageHandler        : got a message: Hello World!. Testing rest
2018-02-06 13:04:05.612  INFO [-,cb8c3218f6e2fbb2,cb8c3218f6e2fbb2,false] 7860 --- [nio-8888-exec-2] com.example.demo.DemoApplication         : Handling test-rest
```

With a modification of the mentioned version of the same library with commented out tracer.detach line (`com/netshoes/springframework/cloud/sleuth/instrument/amqp/DefaultAmqpMessagingSpanManager.java:51`) used, the log output looks like this:

```
2018-02-06 13:07:54.905  INFO [-,efda1e9e2dfac778,efda1e9e2dfac778,false] 15828 --- [nio-8888-exec-1] com.example.demo.DemoApplication         : Handling home
2018-02-06 13:07:54.906  INFO [-,efda1e9e2dfac778,efda1e9e2dfac778,false] 15828 --- [nio-8888-exec-1] com.example.demo.MyMessageSender         : just about to send
2018-02-06 13:07:55.251  INFO [-,efda1e9e2dfac778,6db2bbfb93a6a3f1,false] 15828 --- [cTaskExecutor-1] com.example.demo.MyMessageHandler        : got a message: Hello World!. Testing rest
2018-02-06 13:07:55.331  INFO [-,efda1e9e2dfac778,6668f1eb67789b66,false] 15828 --- [nio-8888-exec-2] com.example.demo.DemoApplication         : Handling test-rest
```

Please notice the difference in traceId in two paragraphs.
The second paragraph has the shared trace id (`efda1e9e2dfac778`) for all the calls made by the given flow.

The second paragraph traces the calls in the manner of ![spring_docs_image](https://raw.githubusercontent.com/spring-cloud/spring-cloud-sleuth/master/docs/src/main/asciidoc/images/trace-id.png)
as shown on [spring_docs](https://github.com/spring-cloud/spring-cloud-sleuth)

In the given image, it should not matter whether the REQUEST arrow is done through AMQP, REST or any other kind of communication.
Therefore, should `DefaultAmqpMessagingSpanManager.afterHandle()` really call the `tracer.detach` when it finishes processing tracing information via AMQP.
This seems to prevent reuse of the existing tracing information received via AMQP headers, in this case.

In the given scenario, in the first paragraph, since `CURRENT_SPAN` is null in `SpanContextHolder`, a new traceId and spanId will be created after a REST call is received in `MyMessageHandler.testRest()`, effectively resulting in the original traceId being lost.  




