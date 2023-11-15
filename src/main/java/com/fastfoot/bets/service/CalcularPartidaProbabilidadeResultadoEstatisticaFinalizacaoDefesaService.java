package com.fastfoot.bets.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.bets.model.repository.PartidaProbabilidadeResultadoRepository;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.service.CalcularEstatisticasFinalizacaoDefesaService;
import com.fastfoot.probability.model.ClubeProbabilidadeDefesa;
import com.fastfoot.probability.model.ClubeProbabilidadeFinalizacao;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;

@Service
public class CalcularPartidaProbabilidadeResultadoEstatisticaFinalizacaoDefesaService implements CalcularPartidaProbabilidadeResultadoService {

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

	@Override
	//@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularPartidaProbabilidadeResultado(RodadaJogavel rodada) {

		List<PartidaProbabilidadeResultado> probabilidades = new ArrayList<PartidaProbabilidadeResultado>();
		
		//List<? extends PartidaResultadoJogavel> partidas = null;
		
		Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes = calcularEstatisticasFinalizacaoDefesaService
				.getEstatisticasFinalizacaoClube(rodada.getSemana().getTemporada());

		Map<Clube, ClubeProbabilidadeDefesa> clubesProbabilidadeDefesa = calcularEstatisticasFinalizacaoDefesaService
				.getEstatisticasDefesaClube(rodada.getSemana().getTemporada());
		
		if (rodada instanceof Rodada) {
			List<PartidaResultado> partidas = partidaResultadoRepository.findByRodada((Rodada) rodada);
			
			for (PartidaResultadoJogavel p : partidas) {
				probabilidades.add(calcularPartidaProbabilidadeResultado(p, clubeProbabilidadeFinalizacoes, clubesProbabilidadeDefesa));
			}

			partidaProbabilidadeResultadoRepository.saveAll(probabilidades);
			partidaResultadoRepository.saveAll(partidas);
		} else if (rodada instanceof RodadaEliminatoria) {
			List<PartidaEliminatoriaResultado> partidas = partidaEliminatoriaResultadoRepository.findByRodada((RodadaEliminatoria) rodada);
			
			for (PartidaResultadoJogavel p : partidas) {
				probabilidades.add(calcularPartidaProbabilidadeResultado(p, clubeProbabilidadeFinalizacoes, clubesProbabilidadeDefesa));
			}

			partidaProbabilidadeResultadoRepository.saveAll(probabilidades);
			partidaEliminatoriaResultadoRepository.saveAll(partidas);
		}
		
		return CompletableFuture.completedFuture(Boolean.TRUE);

	}
	
	public PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(PartidaResultadoJogavel partidaResultado,
			Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes,
			Map<Clube, ClubeProbabilidadeDefesa> clubesProbabilidadeDefesa) {
		
		ClubeProbabilidadeFinalizacao probFinalizacaoMandante = clubeProbabilidadeFinalizacoes
				.get(partidaResultado.getClubeMandante());
		
		ClubeProbabilidadeFinalizacao probFinalizacaoVisitante = clubeProbabilidadeFinalizacoes
				.get(partidaResultado.getClubeVisitante());
		
		ClubeProbabilidadeDefesa probDefesaMandante = clubesProbabilidadeDefesa
				.get(partidaResultado.getClubeMandante());

		ClubeProbabilidadeDefesa probDefesaVisitante = clubesProbabilidadeDefesa
				.get(partidaResultado.getClubeVisitante());
		
		PartidaProbabilidadeResultado partidaProbabilidadeResultado = new PartidaProbabilidadeResultado();
		partidaProbabilidadeResultado
				.setTipoProbabilidadeResultadoPartida(TipoProbabilidadeResultadoPartida.ESTATISTICAS_FINALIZACAO_DEFESA);

		partidaResultado.setPartidaProbabilidadeResultado(partidaProbabilidadeResultado);
		/*if (partidaResultado instanceof PartidaResultado) {
			partidaProbabilidadeResultado.setPartidaResultado((PartidaResultado) partidaResultado);
		} else if (partidaResultado instanceof PartidaEliminatoriaResultado) {
			partidaProbabilidadeResultado.setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partidaResultado);
		}*/
		
		double vitoriaMandante = 0, vitoriaVisitante = 0, empate = 0, resultado = 0d;
		
		int golsMandante = 0, golsVisitante = 0;
		
		for (int j = 0; j < NUM_SIMULACOES; j++) {
			
			golsMandante = 0; golsVisitante = 0;

			for (int i = 0; i < probFinalizacaoMandante.getFinalizacoesPartidas(); i++) {
				resultado = R.nextDouble();
				
				if (resultado <= probFinalizacaoMandante.getProbabilidadeFinalizacaoNoGol())  {
				
					resultado = R.nextDouble();
					if (resultado <= probDefesaVisitante.getProbabilidadeDefesa()) {
						//defesa
					} else {
						golsMandante++;
					}
				}
			}
			
			for (int i = 0; i < probFinalizacaoVisitante.getFinalizacoesPartidas(); i++) {
				resultado = R.nextDouble();
				
				if (resultado <= probFinalizacaoVisitante.getProbabilidadeFinalizacaoNoGol()) {
					
					resultado = R.nextDouble();
					if (resultado <= probDefesaMandante.getProbabilidadeDefesa()) {
						//defesa
					} else {
						golsVisitante++;
					}
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

	@Override
	public PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(
			PartidaResultadoJogavel partidaResultado) {
		// TODO Auto-generated method stub
		return null;
	}

}
