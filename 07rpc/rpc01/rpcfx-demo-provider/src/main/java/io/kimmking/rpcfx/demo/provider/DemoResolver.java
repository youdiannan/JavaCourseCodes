package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.server.service.GenericService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.el.MethodNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class DemoResolver implements RpcfxResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object resolve(String serviceClass) {
        return this.applicationContext.getBean(serviceClass);
    }

    @Override
    public GenericService resolveGeneric(String serviceClass) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(serviceClass);
        // spi?
        Object bean = this.applicationContext.getBean(clazz);
        return (methodName, params) -> {
            Optional<Method> methodOptional = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(m -> m.getName().equals(methodName)).findFirst();
            if (!methodOptional.isPresent()) {
                throw new MethodNotFoundException("method not exists");
            }

            Method method = methodOptional.get();
            return method.invoke(bean, params);
        };
    }
}
