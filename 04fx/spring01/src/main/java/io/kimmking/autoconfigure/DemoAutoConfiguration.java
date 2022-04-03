package io.kimmking.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@ImportResource({"classpath:applicationContext.xml", "classpath:springjms-receiver.xml", "classpath:springjms-sender.xml"})
public class DemoAutoConfiguration {

}
