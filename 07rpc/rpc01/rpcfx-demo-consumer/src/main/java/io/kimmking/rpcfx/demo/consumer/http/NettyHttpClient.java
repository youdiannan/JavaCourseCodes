package io.kimmking.rpcfx.demo.consumer.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class NettyHttpClient {

    public static String doPost(String url, String content) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        URI uri = new URI(url);
        String host = uri.getHost();
        int port = uri.getPort();
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, url, byteBuf);
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().add(HttpHeaderNames.CONTENT_TYPE, "application/json");
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
        request.headers().set(HttpHeaderNames.ACCEPT, "*/*");
        // 请求一次后立即关闭连接
        request.headers().set(HttpHeaderNames.CONNECTION, "close");

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);

            NettyHttpClientOutboundHandler outboundHandler = new NettyHttpClientOutboundHandler();
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpClientCodec());
                    ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                    ch.pipeline().addLast(outboundHandler);
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            f.channel().writeAndFlush(request);
            f.channel().closeFuture().sync();
            return outboundHandler.getResult();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

}