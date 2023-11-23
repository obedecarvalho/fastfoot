package com.fastfoot.financial.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.FastfootApplication;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.repository.SemanaRepository;

@Service
public class CalcularClubeSaldoSemanaTodosClubesService {
	
	private static final Comparator<Semana> COMPARATOR;
	
	static {
		COMPARATOR = new Comparator<Semana>() {
			
			@Override
			public int compare(Semana o1, Semana o2) {
				
				int compare = o1.getTemporada().getAno().compareTo(o2.getTemporada().getAno());
				
				if (compare == 0) {
					compare = o1.getNumero().compareTo(o2.getNumero());
				}

				return compare;
			}
		};
	}
	
	/*@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;*/

	@Autowired
	private SemanaRepository semanaRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private CalcularClubeSaldoSemanaService calcularClubeSaldoSemanaService;

	/*
	public void calcularClubeSaldoSemana(Jogo jogo) {
		
		List<Semana> semanas = semanaRepository.findByJogo(jogo);
		
		Collections.sort(semanas, COMPARATOR);
		
		List<MovimentacaoFinanceira> movimentaceosFinanceiras = movimentacaoFinanceiraRepository.findByJogo(jogo);

		Map<Long, Map<Clube, List<MovimentacaoFinanceira>>> movimentacoesClube = movimentaceosFinanceiras.stream()
				.collect(Collectors.groupingBy(mf -> mf.getClube().getId() % FastfootApplication.NUM_THREAD,
						Collectors.groupingBy(MovimentacaoFinanceira::getClube)));
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		for (long i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			completableFutures
					.add(calcularClubeSaldoSemanaService.calcularClubeSaldoSemana(semanas, movimentacoesClube.get(i)));
		}
		
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}
	*/
	
	public void calcularClubeSaldoSemana(Jogo jogo) {

		List<Semana> semanas = semanaRepository.findByJogo(jogo);

		Collections.sort(semanas, COMPARATOR);

		List<Clube> clubes = clubeRepository.findByJogo(jogo);

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();

		int offset = clubes.size() / FastfootApplication.NUM_THREAD;

		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				completableFutures.add(calcularClubeSaldoSemanaService
						.calcularClubeSaldoSemana(clubes.subList(i * offset, clubes.size()), semanas));
			} else {
				completableFutures.add(calcularClubeSaldoSemanaService
						.calcularClubeSaldoSemana(clubes.subList(i * offset, (i + 1) * offset), semanas));
			}
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}
}
