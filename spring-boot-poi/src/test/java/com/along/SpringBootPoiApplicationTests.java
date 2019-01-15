package com.along;

import com.along.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootPoiApplicationTests {

    @Autowired
    PersonService personService;

    @Test
    public void contextLoads() {
    }


    @Test
    public void exportPersons() throws Exception {
        personService.exportPersons();
    }

}

