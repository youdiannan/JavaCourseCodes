package io.kimmking.javacourse.kafkademo;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class KafkaDemoApplication {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            AtomicInteger count = new AtomicInteger(0);
            while (true) {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()) {
                    String message = "generated message = "
                            + UUID.randomUUID() + ", id=" + count.getAndIncrement();
                    kafkaTemplate.send("spring-kafka-test",  message);
                    System.out.println("message send: " + message);
                    scanner.next();
                }
            }
        };
    }

    @KafkaListener(topics = "spring-kafka-test")
    public void consume(String message) {
        System.out.println("message received: " + message);
    }

}
