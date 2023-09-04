package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.FastfootApplication;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;

@Service
public class CalcularTrajetoriaForcaClubeTodosClubesService {

	//###	REPOSITORY	###

	@Autowired
	private ClubeRepository clubeRepository;

	//###	SERVICE	###

	@Autowired
	private CalcularTrajetoriaForcaClubeService calcularTrajetoriaForcaClubeService;
	
	@Autowired
	private SemanaCRUDService semanaCRUDService;
	
	public void calcularTrajetoriaForcaClube(Jogo jogo) {
		List<Clube> clubes = clubeRepository.findByJogo(jogo);
		Semana s = semanaCRUDService.getProximaSemana(jogo);

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				completableFutures.add(calcularTrajetoriaForcaClubeService
						.calcularTrajetoriaForcaClube(clubes.subList(i * offset, clubes.size()), s));
			} else {
				completableFutures.add(calcularTrajetoriaForcaClubeService
						.calcularTrajetoriaForcaClube(clubes.subList(i * offset, (i + 1) * offset), s));
			}
		}
		
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();

	}

}
