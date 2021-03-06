package com.julu666.course.api;

import com.julu666.course.api.configuration.StorageProperties;
import com.julu666.course.api.interceptor.TokenInterceptor;
import com.julu666.course.api.interfaces.Storage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableTransactionManagement
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean

    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/*").allowedOrigins("*");
            }

//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//                registry.addInterceptor(new TokenInterceptor());
//            }
        };
    }


    @Bean
    CommandLineRunner init(Storage storage) {
        return (args) -> {
            storage.init();
        };
    }
}

