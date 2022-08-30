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
	
	public static final Integer NUM_THREAD = 8;

	public static void main(String[] args) {
		SpringApplication.run(FastfootApplication.class, args);
	}

	@Bean(name = "partidaExecutor")
	public Executor asyncPartidaExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(NUM_THREAD);
		//executor.setMaxPoolSize(8);
		//executor.setQueueCapacity(32);
		executor.setThreadNamePrefix("partidaExecutor-");
		executor.initialize();
		return executor;
    }
	
	@Bean(name = "jogadorServiceExecutor")
	public Executor asyncJogadorServiceExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(NUM_THREAD);
		//executor.setMaxPoolSize(8);
		//executor.setQueueCapacity(32);
		executor.setThreadNamePrefix("jogadorServiceExecutor-");
		executor.initialize();
		return executor;
    }
	
	@Bean(name = "probabilidadeExecutor")
	public Executor asyncProbabilidadeExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(NUM_THREAD);
		//executor.setMaxPoolSize(8);
		//executor.setQueueCapacity(32);
		executor.setThreadNamePrefix("probabilidadeExecutor-");
		executor.initialize();
		return executor;
    }

}
