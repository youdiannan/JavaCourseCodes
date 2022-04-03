package com.demo;

import io.kimmking.homework.IoCDemo;
import io.kimmking.spring02.School;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AutoConfigureTest implements InitializingBean {

    @Autowired
    private IoCDemo ioCDemo;

    @Autowired
    private School school;

    @Autowired

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("AutoConfigure success, iocDemo: {}", ioCDemo);
        school.ding();
    }

}
