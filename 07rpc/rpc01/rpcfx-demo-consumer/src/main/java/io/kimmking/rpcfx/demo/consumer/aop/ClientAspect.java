package io.kimmking.rpcfx.demo.consumer.aop;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.annotation.RpcFacade;
import io.kimmking.rpcfx.api.Filter;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.demo.consumer.http.NettyHttpClient;
import io.kimmking.rpcfx.exception.RpcfxException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;

@Component
@Aspect
public class ClientAspect {

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    @Pointcut("@within(io.kimmking.rpcfx.annotation.RpcFacade)")
    public void facade() {}

    // todo 尝试将客户端动态代理改成 AOP，添加异常处理；
    @Around("facade()")
    // bean definition
    public Object rpcAround(ProceedingJoinPoint joinPoint) throws Throwable{
         MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> clazz = joinPoint.getTarget().getClass().getInterfaces()[0];

        RpcFacade annotation = joinPoint.getTarget().getClass().getAnnotation(RpcFacade.class);
        String url = annotation.url();

        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(clazz.getName());
        request.setMethod(method.getName());
        request.setParams(joinPoint.getArgs());

        RpcfxResponse response = post(request, url);

        // 加filter地方之三
        // Student.setTeacher("cuijing");

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException
        if (!response.isStatus()) {
            throw new RpcfxException(response.getException());
        }

        return JSON.parse(response.getResult().toString());
    }

    private RpcfxResponse post(RpcfxRequest req, String url) throws Exception {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: "+reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
//        OkHttpClient client = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(url)
//                .post(RequestBody.create(JSONTYPE, reqJson))
//                .build();
//        String respJson = client.newCall(request).execute().body().string();
//        System.out.println("resp json: "+respJson);
        // todo 尝试使用 Netty+HTTP 作为 client 端传输方式。
        String respJson = NettyHttpClient.doPost(url, reqJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
