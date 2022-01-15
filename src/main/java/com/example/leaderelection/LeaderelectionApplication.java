package com.example.leaderelection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LeaderelectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaderelectionApplication.class, args);
	}



}
