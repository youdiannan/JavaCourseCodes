package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HeaderHttpRequestFilter;
import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.outbound.netty4.NettyHttpClient;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.github.kimmking.gateway.router.RandomHttpEndpointRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.http.impl.bootstrap.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final List<String> proxyServer;
    private HttpRequestFilter requestFilter = new HeaderHttpRequestFilter();
    private HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            logger.info("channelRead流量接口请求开始，时间为{}", System.currentTimeMillis());
            FullHttpRequest fullRequest = (FullHttpRequest) msg;

            preFilter(fullRequest, ctx);

            forward(fullRequest, ctx);

        } catch(Exception e) {
            // 兜底
            e.printStackTrace();
            ctx.writeAndFlush(createDefaultExceptionHttpResponse());
        }
    }

    private void preFilter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        // TODO: 2022/3/20 chain
        requestFilter.filter(fullRequest, ctx);
    }


    private void forward(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws Exception {
        List<String> backendUrls = fetchServiceUrls(fullRequest.uri());
        String route = router.route(backendUrls);

        // TODO: 2022/3/20 修改请求路径
        // 防止多个线程共用连接，todo
        NettyHttpClient httpClient = new NettyHttpClient();
        httpClient.doRequest(route, fullRequest, ctx);
    }

    private List<String> fetchServiceUrls(String uri) {
        return this.proxyServer;
    }


    private FullHttpResponse createDefaultExceptionHttpResponse() {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }

//    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
//        FullHttpResponse response = null;
//        try {
//            String value = "hello,kimmking";
//            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
//            response.headers().set("Content-Type", "application/json");
//            response.headers().setInt("Content-Length", response.content().readableBytes());
//
//        } catch (Exception e) {
//            logger.error("处理测试接口出错", e);
//            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
//        } finally {
//            if (fullRequest != null) {
//                if (!HttpUtil.isKeepAlive(fullRequest)) {
//                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
//                } else {
//                    response.headers().set(CONNECTION, KEEP_ALIVE);
//                    ctx.write(response);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }

}
