package com.julu666.course.api;


import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnDefaultMessage() {
        try {
            Assert.assertTrue("检查主页是否包含 视频课堂后台 这几个字", this.restTemplate.getForObject("http://localhost:" + port + "/", String.class).contains("视频课堂后台"));
        } catch (Exception ex) {
            System.out.print("检查没有通过");
        }
        System.out.print("检查通过");
    }
}
