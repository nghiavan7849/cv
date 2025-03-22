package com.snackviet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SnackVietApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnackVietApplication.class, args);
	}

}
