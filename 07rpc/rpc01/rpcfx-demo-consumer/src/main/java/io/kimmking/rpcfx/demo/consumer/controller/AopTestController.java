package io.kimmking.rpcfx.demo.consumer.controller;

import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AopTestController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public User findById() {
        return userService.findById(1);
    }
}
