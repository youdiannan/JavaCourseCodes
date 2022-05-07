package io.kimmking.rpcfx.demo.consumer.client;

import io.kimmking.rpcfx.annotation.RpcFacade;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.stereotype.Component;

@Component
@RpcFacade(url = "http://localhost:8080/")
public class UserServiceFacade implements UserService {

    @Override
    public User findById(int id) {
        return null;
    }
}
