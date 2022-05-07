package io.github.kimmking.gateway.outbound.netty4;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

public class NettyHttpClientOutboundHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private ChannelHandlerContext inboundCtx;

    public NettyHttpClientOutboundHandler(ChannelHandlerContext ctx) {
        this.inboundCtx = ctx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpResponse response) throws Exception {
        if (response != null) {
            // 应该是要去copy一份或者retain给计数加1，不然响应里的byteBuf被回收就拿不到响应体了
            response.retain();
            if (!HttpUtil.isKeepAlive(response)) {
                inboundCtx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                //response.headers().set(CONNECTION, KEEP_ALIVE);
                inboundCtx.write(response);
            }
        }
        inboundCtx.flush();
        inboundCtx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("forward error!");
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}