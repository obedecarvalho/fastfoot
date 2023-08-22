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
	
	/*
		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();
		
		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#carregar:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		//Finalizar
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total
	 */
	
	public static final Integer NUM_THREAD = 8;

	public static void main(String[] args) {
		SpringApplication.run(FastfootApplication.class, args);
	}

	@Bean(name = "defaultExecutor")
	public Executor asyncDefaultExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(NUM_THREAD);
		//executor.setMaxPoolSize(8);
		//executor.setQueueCapacity(32);
		executor.setThreadNamePrefix("defaultExecutor-");
		executor.initialize();
		return executor;
    }

}
