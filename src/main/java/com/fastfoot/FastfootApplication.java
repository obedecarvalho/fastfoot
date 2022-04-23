package com.fastfoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
public class FastfootApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastfootApplication.class, args);
	}

}
