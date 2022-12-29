package com.fastfoot.bets.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.bets.model.repository.PartidaProbabilidadeResultadoRepository;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.probability.model.ClubeProbabilidadeFinalizacao;
import com.fastfoot.probability.service.CalcularEstatisticasFinalizacaoDefesaService;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;

@Service
public class CalcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoService {

	private static final Integer NUM_SIMULACOES = 10000;

	private static final Random R = new Random();
	
	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Autowired
	private PartidaProbabilidadeResultadoRepository partidaProbabilidadeResultadoRepository;

	@Autowired
	private CalcularEstatisticasFinalizacaoDefesaService calcularEstatisticasFinalizacaoDefesaService;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> simularPartidas(RodadaJogavel rodada) {

		List<PartidaProbabilidadeResultado> probabilidades = new ArrayList<PartidaProbabilidadeResultado>();
		
		List<? extends PartidaResultadoJogavel> partidas = null;
		
		if (rodada instanceof Rodada) {
			partidas = partidaResultadoRepository.findByRodada((Rodada) rodada);
		} else if (rodada instanceof RodadaEliminatoria) {
			partidas = partidaEliminatoriaResultadoRepository.findByRodada((RodadaEliminatoria) rodada);
		}
		
		Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes = calcularEstatisticasFinalizacaoDefesaService
				.getEstatisticasFinalizacaoClube(rodada.getSemana().getTemporada());

		for (PartidaResultadoJogavel p : partidas) {
			probabilidades.add(simularPartida(p, clubeProbabilidadeFinalizacoes));
		}

		partidaProbabilidadeResultadoRepository.saveAll(probabilidades);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);

	}

	public PartidaProbabilidadeResultado simularPartida(PartidaResultadoJogavel partidaResultado,
			Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes) {
		
		ClubeProbabilidadeFinalizacao probFinalizacaoMandante = clubeProbabilidadeFinalizacoes
				.get(partidaResultado.getClubeMandante());
		
		ClubeProbabilidadeFinalizacao probFinalizacaoVisitante = clubeProbabilidadeFinalizacoes
				.get(partidaResultado.getClubeVisitante());
		
		PartidaProbabilidadeResultado partidaProbabilidadeResultado = new PartidaProbabilidadeResultado();
		partidaProbabilidadeResultado
				.setTipoProbabilidadeResultadoPartida(TipoProbabilidadeResultadoPartida.ESTATISTICAS_FINALIZACAO);

		if (partidaResultado instanceof PartidaResultado) {
			partidaProbabilidadeResultado.setPartidaResultado((PartidaResultado) partidaResultado);
		} else if (partidaResultado instanceof PartidaEliminatoriaResultado) {
			partidaProbabilidadeResultado.setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partidaResultado);
		}
		
		double vitoriaMandante = 0, vitoriaVisitante = 0, empate = 0, resultado = 0d;
		
		int golsMandante = 0, golsVisitante = 0;
		
		for (int j = 0; j < NUM_SIMULACOES; j++) {
			
			golsMandante = 0; golsVisitante = 0;

			for (int i = 0; i < probFinalizacaoMandante.getFinalizacoesPartidas(); i++) {
				resultado = R.nextDouble();
				
				if (resultado <= probFinalizacaoMandante.getProbabilidadeGolFinalizacao()) {
					golsMandante++;
				}
			}
			
			for (int i = 0; i < probFinalizacaoVisitante.getFinalizacoesPartidas(); i++) {
				resultado = R.nextDouble();
				
				if (resultado <= probFinalizacaoVisitante.getProbabilidadeGolFinalizacao()) {
					golsVisitante++;
				}
			}
			
			if (golsMandante > golsVisitante) {
				vitoriaMandante++;
			} else if (golsMandante < golsVisitante) {
				vitoriaVisitante++;
			} else if (golsMandante == golsVisitante) {
				empate++;
			}
			
		}
		
		partidaProbabilidadeResultado.setProbabilidadeVitoriaMandante(vitoriaMandante/NUM_SIMULACOES);

		partidaProbabilidadeResultado.setProbabilidadeVitoriaVisitante(vitoriaVisitante/NUM_SIMULACOES);

		partidaProbabilidadeResultado.setProbabilidadeEmpate(empate/NUM_SIMULACOES);

		return partidaProbabilidadeResultado;

	}

}