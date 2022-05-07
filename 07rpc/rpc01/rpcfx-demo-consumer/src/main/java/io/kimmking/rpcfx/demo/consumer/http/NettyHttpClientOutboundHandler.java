package io.kimmking.rpcfx.demo.consumer.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

import java.nio.charset.StandardCharsets;

public class NettyHttpClientOutboundHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private String result;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpResponse response) throws Exception {
        if (response != null) {
            ByteBuf content = response.content();
            result = content.toString(StandardCharsets.UTF_8);
            System.out.println("result: " + result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("forward error!");
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

    public String getResult() {
        return result;
    }
}