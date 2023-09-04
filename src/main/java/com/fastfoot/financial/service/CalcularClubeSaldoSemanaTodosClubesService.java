package com.fastfoot.financial.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.FastfootApplication;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
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
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

	@Autowired
	private SemanaRepository semanaRepository;
	
	@Autowired
	private CalcularClubeSaldoSemanaService calcularClubeSaldoSemanaService;

	public void calcularClubeSaldoSemana() {
		
		List<Semana> semanas = semanaRepository.findAll();
		
		Collections.sort(semanas, COMPARATOR);
		
		List<MovimentacaoFinanceira> movimentaceosFinanceiras = movimentacaoFinanceiraRepository.findAll();

		Map<Long, Map<Clube, List<MovimentacaoFinanceira>>> movimentacoesClube = movimentaceosFinanceiras.stream()
				.collect(Collectors.groupingBy(mf -> mf.getClube().getId() % FastfootApplication.NUM_THREAD,
						Collectors.groupingBy(MovimentacaoFinanceira::getClube)));//TODO: mudar de getClube().getId() % FastfootApplication.NUM_THREAD para liga + primeirosId
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		for (long i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			completableFutures
					.add(calcularClubeSaldoSemanaService.calcularClubeSaldoSemana(semanas, movimentacoesClube.get(i)));
		}
		
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}
}
