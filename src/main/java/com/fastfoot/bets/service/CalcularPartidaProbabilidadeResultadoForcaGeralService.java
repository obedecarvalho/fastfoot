package com.fastfoot.bets.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.bets.model.repository.PartidaProbabilidadeResultadoRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;

@Service
public class CalcularPartidaProbabilidadeResultadoForcaGeralService implements CalcularPartidaProbabilidadeResultadoService {
	
	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Autowired
	private PartidaProbabilidadeResultadoRepository partidaProbabilidadeResultadoRepository;

	@Override
	public PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(
			PartidaResultadoJogavel partidaResultado) {
		
		
		PartidaProbabilidadeResultado partidaProbabilidadeResultado = new PartidaProbabilidadeResultado();
		
		partidaResultado.setPartidaProbabilidadeResultado(partidaProbabilidadeResultado);
		partidaProbabilidadeResultado.setPartida(partidaResultado);
		
		partidaProbabilidadeResultado.setTipoProbabilidadeResultadoPartida(TipoProbabilidadeResultadoPartida.FORCA_GERAL);
		
		double probVitMandante = Math.pow(partidaResultado.getClubeMandante().getForcaGeralAtual(), 2);

		double probVitVisitante = Math.pow(partidaResultado.getClubeVisitante().getForcaGeralAtual(), 2);

		double probEmpate = partidaResultado.getClubeMandante().getForcaGeralAtual() * partidaResultado.getClubeVisitante().getForcaGeralAtual();
		
		double total = probVitMandante + probVitVisitante + probEmpate;
				
		partidaProbabilidadeResultado.setProbabilidadeVitoriaMandante(probVitMandante/total);
		
		partidaProbabilidadeResultado.setProbabilidadeVitoriaVisitante(probVitVisitante/total);
		
		partidaProbabilidadeResultado.setProbabilidadeEmpate(probEmpate/total);

		return partidaProbabilidadeResultado;
	}

	@Override
	public CompletableFuture<Boolean> calcularPartidaProbabilidadeResultado(RodadaJogavel rodada) {

		List<PartidaProbabilidadeResultado> probabilidades = new ArrayList<PartidaProbabilidadeResultado>();
		
		//List<? extends PartidaResultadoJogavel> partidas = null;
		
		if (rodada instanceof Rodada) {
			List<PartidaResultado> partidas = partidaResultadoRepository.findByRodada((Rodada) rodada);
			
			for (PartidaResultadoJogavel p : partidas) {
				probabilidades.add(calcularPartidaProbabilidadeResultado(p));
			}

			partidaProbabilidadeResultadoRepository.saveAll(probabilidades);
			partidaResultadoRepository.saveAll(partidas);
		} else if (rodada instanceof RodadaEliminatoria) {
			List<PartidaEliminatoriaResultado> partidas = partidaEliminatoriaResultadoRepository.findByRodada((RodadaEliminatoria) rodada);
			
			for (PartidaResultadoJogavel p : partidas) {
				probabilidades.add(calcularPartidaProbabilidadeResultado(p));
			}

			partidaProbabilidadeResultadoRepository.saveAll(probabilidades);
			partidaEliminatoriaResultadoRepository.saveAll(partidas);
		}
		
		return CompletableFuture.completedFuture(Boolean.TRUE);

	}

}
