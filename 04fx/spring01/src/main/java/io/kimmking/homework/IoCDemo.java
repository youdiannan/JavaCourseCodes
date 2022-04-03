package io.kimmking.homework;

import io.kimmking.spring01.Student;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Data
public class IoCDemo implements InitializingBean {

    @Resource(name = "student123")
    private Student student;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("============== IocDemo ==============");
        System.out.println(student);
    }
}
