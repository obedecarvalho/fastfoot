package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.repository.PartidaLanceRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.PartidaAmistosaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.service.util.ClassificacaoUtil;

@Service
public class RodadaService {
	
	//###	REPOSITORY	###
	@Autowired
	private PartidaResultadoRepository partidaRepository;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private PartidaLanceRepository partidaLanceRepository;

	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;
	
	//###	SERVICE	###
	@Autowired
	private PartidaResultadoService partidaResultadoService;

	private static final Boolean SALVAR_ESTATISTICAS = false;

	@Async("partidaExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(Rodada rodada) {

		carregarPartidas(rodada);
		jogarPartidas(rodada);
		salvarPartidas(rodada);

		if (rodada.isUltimaRodadaPontosCorridos()){
			carregarClassificacao(rodada);
			classificarComDesempate(rodada);
			salvarClassificacao(rodada);
		}

		return CompletableFuture.completedFuture(rodada);
	}

	private void jogarPartidas(Rodada rodada) {
		if(rodada.getCampeonato() != null) {
			partidaResultadoService.jogarRodada(rodada, rodada.getCampeonato().getClassificacao());
			
			if (!rodada.isUltimaRodadaPontosCorridos()) {
				ClassificacaoUtil.ordernarClassificacao(rodada.getCampeonato().getClassificacao(), false);
			}
		} else if (rodada.getGrupoCampeonato() != null) {
			partidaResultadoService.jogarRodada(rodada, rodada.getGrupoCampeonato().getClassificacao());
			
			if (!rodada.isUltimaRodadaPontosCorridos()) {
				ClassificacaoUtil.ordernarClassificacao(rodada.getGrupoCampeonato().getClassificacao(), false);
			}
		}
	}

	private void carregarPartidas(Rodada rodada) {
		rodada.setPartidas(partidaRepository.findByRodada(rodada));
		
		if(rodada.getCampeonato() != null) {
			rodada.getCampeonato().setClassificacao(classificacaoRepository.findByCampeonato(rodada.getCampeonato()));
		} else if (rodada.getGrupoCampeonato() != null) {
			rodada.getGrupoCampeonato().setClassificacao(classificacaoRepository.findByGrupoCampeonato(rodada.getGrupoCampeonato()));
		}
	}

	private void salvarPartidas(Rodada r) {

		partidaRepository.saveAllAndFlush(r.getPartidas());

		if (SALVAR_ESTATISTICAS) { salvarEstatisticas(r.getPartidas()); }

		if(r.getCampeonato() != null) {
			classificacaoRepository.saveAllAndFlush(r.getCampeonato().getClassificacao());
		} else if (r.getGrupoCampeonato() != null) {
			classificacaoRepository.saveAllAndFlush(r.getGrupoCampeonato().getClassificacao());
		}
	}

	private void carregarClassificacao(Rodada r) {
		if(r.getCampeonato() != null) {
			r.getCampeonato().setClassificacao(classificacaoRepository.findByCampeonato(r.getCampeonato()));
		} else if (r.getGrupoCampeonato() != null) {
			r.getGrupoCampeonato().setClassificacao(classificacaoRepository.findByGrupoCampeonato(r.getGrupoCampeonato()));
		}
	}

	/**
	 * Classifica os times com Desempate. Para ser executado na ultima rodada. 
	 * @param semana
	 */
	private void classificarComDesempate(Rodada r) {

		if(r.getCampeonato() != null) {
			if (r.getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {
				List<PartidaResultado> partidas = partidaRepository.findByCampeonato(r.getCampeonato());
				ClassificacaoUtil.ordernarClassificacao(r.getCampeonato().getClassificacao(), partidas);
			}
		} else if (r.getGrupoCampeonato() != null) {
			if (r.getNumero() == Constantes.NRO_PARTIDAS_FASE_GRUPOS) {
				List<PartidaResultado> partidas = partidaRepository.findByGrupoCampeonato(r.getGrupoCampeonato());
				ClassificacaoUtil.ordernarClassificacao(r.getGrupoCampeonato().getClassificacao(), partidas);
			}
		}
	}

	private void salvarClassificacao(Rodada r) {
		if(r.getCampeonato() != null) {
			classificacaoRepository.saveAllAndFlush(r.getCampeonato().getClassificacao());
		} else if (r.getGrupoCampeonato() != null) {
			classificacaoRepository.saveAllAndFlush(r.getGrupoCampeonato().getClassificacao());
		}
	}

	@Async("partidaExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(RodadaEliminatoria rodada) {

		carregarPartidas(rodada);
		jogarPartidas(rodada);
		salvarPartidas(rodada);
		
		return CompletableFuture.completedFuture(rodada);
	}

	private void carregarPartidas(RodadaEliminatoria rodada) {
		rodada.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada));
	}

	private void jogarPartidas(RodadaEliminatoria rodada) {
		partidaResultadoService.jogarRodada(rodada);
	}

	private void salvarPartidas(RodadaEliminatoria r) {

		partidaEliminatoriaRepository.saveAllAndFlush(r.getPartidas());
		for (PartidaEliminatoriaResultado per : r.getPartidas()) {
			if (per.getProximaPartida() != null) partidaEliminatoriaRepository.saveAndFlush(per.getProximaPartida());
		}

		if (SALVAR_ESTATISTICAS) { salvarEstatisticas(r.getPartidas()); }
	}

	private void salvarEstatisticas(List<? extends PartidaResultadoJogavel> partidas) {//TODO
		List<PartidaLance> lances = null;

		for (PartidaResultadoJogavel partida : partidas) {
			lances = null;//partida.getPartidaLances();
			partidaLanceRepository.saveAll(lances);
		}
	}

	@Async("partidaExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(RodadaAmistosa rodada) {

		carregarPartidas(rodada);
		jogarPartidas(rodada);
		salvarPartidas(rodada);

		return CompletableFuture.completedFuture(rodada);
	}

	private void carregarPartidas(RodadaAmistosa rodada) {
		rodada.setPartidas(partidaAmistosaResultadoRepository.findByRodada(rodada));
	}

	private void jogarPartidas(RodadaAmistosa rodada) {
		partidaResultadoService.jogarRodada(rodada);
	}

	private void salvarPartidas(RodadaAmistosa r) {

		partidaAmistosaResultadoRepository.saveAllAndFlush(r.getPartidas());
		
		if (SALVAR_ESTATISTICAS) { salvarEstatisticas(r.getPartidas()); }
	}
}
