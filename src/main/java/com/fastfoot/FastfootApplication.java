package com.fastfoot;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class FastfootApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastfootApplication.class, args);
	}

	@Bean(name = "partidaExecutor")
	public Executor asyncExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(8);
		executor.setQueueCapacity(32);
		executor.setThreadNamePrefix("PartidaExecutor-");
		executor.initialize();
		return executor;
    }

}
