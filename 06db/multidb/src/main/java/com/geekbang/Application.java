package com.geekbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.geekbang")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
