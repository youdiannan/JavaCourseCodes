# netty-gateway

## 作业完成情况

单纯利用HttpClient实现转发：GET请求正常，POST请求在填充BODY时有问题，还没找到问题在哪

**自己实现Netty的HttpClient**：目前只简单测试了GET请求，看着是正常的

## 遇到的问题

### 连接过早关闭
启动NettyServerApplication，预期是按照自己写的简单的NettyHttpClient去请求对应的服务。

在转发之后，相应的testServer可以收到转发的消息，从日志看testServer也执行了发送消息的动作。但是NettyHttpClientOutboundHandler没有触发channelRead0方法。

同时，在testServer的日志可以看到如下信息：

```
java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
	at sun.nio.ch.SocketDispatcher.read0(Native Method)
	at sun.nio.ch.SocketDispatcher.read(SocketDispatcher.java:43)
	at sun.nio.ch.IOUtil.readIntoNativeBuffer(IOUtil.java:223)
	at sun.nio.ch.IOUtil.read(IOUtil.java:192)
	at sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:378)
	at io.netty.buffer.PooledByteBuf.setBytes(PooledByteBuf.java:253)
	at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1133)
	at io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:350)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:148)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:748)
```

看着是向外转发的连接被过早关闭了。

#### 解决办法

NettyHttpClient中添加一行代码，等待服务端主动发起关闭

```java
f.channel().closeFuture().sync();
```

### 请求转发成功，但将响应回传给用户时出现了Empty Result，响应体为空

参见NettyHttpClientOutboundHandler，原因估计为原先直接将转发的服务端传回的FullHttpResponse直接写入到用户的ChannelHandlerContext中，但是由于SimpleChannelInboundHandler会自动执行ReferenceCountUtil.release()的操作，所以导致FullHttpResponse中的ByteBuf被回收，传给用户的响应中就没有响应体了。

在使用了retain/retainedDuplicate后就能正常返回。

