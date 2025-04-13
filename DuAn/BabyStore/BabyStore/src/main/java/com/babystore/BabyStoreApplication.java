package com.babystore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BabyStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(BabyStoreApplication.class, args);
    }
}
