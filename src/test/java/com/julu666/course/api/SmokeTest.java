package com.julu666.course.api;

import com.julu666.course.api.web.admin.AdminController;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

    @Autowired
    private AdminController controller;

    @Test
    public void contexLoads() {
        Assertions.assertThat(controller).isNotNull();

    }
}
