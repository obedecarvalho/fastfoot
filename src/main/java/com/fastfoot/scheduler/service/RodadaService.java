package com.fastfoot.scheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.repository.PartidaResumoRepository;
import com.fastfoot.match.model.repository.PartidaLanceRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.repository.JogadorGrupoEstatisticasRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
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
	private PartidaResumoRepository partidaEstatisticasRepository;
	
	@Autowired
	private PartidaLanceRepository jogadorEstatisticasRepository;

	@Autowired
	private JogadorGrupoEstatisticasRepository jogadorGrupoEstatisticasRepository;
	
	//###	SERVICE	###
	@Autowired
	private PartidaResultadoService partidaResultadoService;

	//@Async
	public void executarRodada(Rodada rodada) {
		carregarPartidas(rodada);
		jogarPartidas(rodada);
		salvarPartidas(rodada);

		if (rodada.isUltimaRodadaPontosCorridos()){
			carregarClassificacao(rodada);
			classificarComDesempate(rodada);
			salvarClassificacao(rodada);
		}
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
		boolean salvarEstatisticas = false;

		partidaRepository.saveAll(r.getPartidas());
		if (salvarEstatisticas) { salvarEstatisticas(r.getPartidas()); }
		if(r.getCampeonato() != null) {
			classificacaoRepository.saveAll(r.getCampeonato().getClassificacao());
		} else if (r.getGrupoCampeonato() != null) {
			classificacaoRepository.saveAll(r.getGrupoCampeonato().getClassificacao());
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
			classificacaoRepository.saveAll(r.getCampeonato().getClassificacao());
		} else if (r.getGrupoCampeonato() != null) {
			classificacaoRepository.saveAll(r.getGrupoCampeonato().getClassificacao());
		}
	}

	//@Async
	public void executarRodada(RodadaEliminatoria rodada) {
		carregarPartidas(rodada);
		jogarPartidas(rodada);
		salvarPartidas(rodada);
	}

	private void carregarPartidas(RodadaEliminatoria rodada) {
		rodada.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada));
	}

	private void jogarPartidas(RodadaEliminatoria rodada) {
		partidaResultadoService.jogarRodada(rodada);
	}

	private void salvarPartidas(RodadaEliminatoria r) {
		boolean salvarEstatisticas = false;

		partidaEliminatoriaRepository.saveAll(r.getPartidas());
		if (salvarEstatisticas) { salvarEstatisticas(r.getPartidas()); }
	}

	private void salvarEstatisticas(List<? extends PartidaResultadoJogavel> partidas) {
		for (PartidaResultadoJogavel partida : partidas) {
			salvarEstatisticas(partida);
		}
	}

	private void salvarEstatisticas(PartidaResultadoJogavel partida) {
		partidaEstatisticasRepository.save(partida.getPartidaResumo());
		jogadorEstatisticasRepository.saveAll(partida.getPartidaResumo().getPartidaLances());
		jogadorGrupoEstatisticasRepository.saveAll(partida.getPartidaResumo().getGrupoEstatisticas());
	}
}
