package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.match.model.PartidaJogadorEstatisticaDTO;
import com.fastfoot.match.model.dto.PartidaTorcidaSalvarDTO;
import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.repository.PartidaEstatisticasRepository;
import com.fastfoot.match.model.repository.PartidaLanceRepository;
import com.fastfoot.match.model.repository.PartidaTorcidaRepository;
import com.fastfoot.match.service.CalcularTorcidaPartidaService;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.repository.HabilidadeValorEstatisticaRepository;
import com.fastfoot.player.model.repository.JogadorEstatisticaSemanaRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
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
	
	@Autowired
	private HabilidadeValorEstatisticaRepository habilidadeValorEstatisticaRepository;

	/*@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;*/
	
	@Autowired
	private JogadorEstatisticaSemanaRepository jogadorEstatisticaSemanaRepository;
	
	@Autowired
	private PartidaEstatisticasRepository partidaEstatisticasRepository;

	@Autowired
	private PartidaTorcidaRepository partidaTorcidaRepository;

	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

	//###	SERVICE	###
	@Autowired
	private PartidaResultadoService partidaResultadoService;
	
	@Autowired
	private CalcularTorcidaPartidaService calcularTorcidaPartidaService;

	private static final Boolean SALVAR_LANCES = false;

	@Async("defaultExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(Rodada rodada) {
		
		PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO = new PartidaJogadorEstatisticaDTO();
		PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO = new PartidaTorcidaSalvarDTO();
		
		List<String> mensagens = new ArrayList<String>();
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio, fim;

		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		carregarPartidas(rodada);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#carregarPartidas:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularTorcidaPartida:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		jogarPartidas(rodada, partidaJogadorEstatisticaDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#jogarPartidas:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		salvarPartidas(rodada);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#salvarPartidas:" + (fim - inicio));

		if (rodada.isUltimaRodadaPontosCorridos()){
			carregarClassificacao(rodada);
			classificarComDesempate(rodada);
			salvarClassificacao(rodada);
		}
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		salvarPartidaJogadorEstatisticas(partidaJogadorEstatisticaDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#salvarPartidaJogadorEstatisticas:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		salvarPartidaTorcida(partidaTorcidaSalvarDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#salvarPartidaTorcida:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		//System.err.println(mensagens);

		return CompletableFuture.completedFuture(rodada);
	}
	
	private void calcularTorcidaPartida(RodadaJogavel rodada, PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
	}

	private void jogarPartidas(Rodada rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		if(rodada.getCampeonato() != null) {
			partidaResultadoService.jogarRodada(rodada, partidaJogadorEstatisticaDTO,
					rodada.getCampeonato().getClassificacao());
			
			if (!rodada.isUltimaRodadaPontosCorridos()) {
				ClassificacaoUtil.ordernarClassificacao(rodada.getCampeonato().getClassificacao(), false);
			}
		} else if (rodada.getGrupoCampeonato() != null) {
			partidaResultadoService.jogarRodada(rodada, partidaJogadorEstatisticaDTO,
					rodada.getGrupoCampeonato().getClassificacao());
			
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
			rodada.getGrupoCampeonato()
					.setClassificacao(classificacaoRepository.findByGrupoCampeonato(rodada.getGrupoCampeonato()));
		}
	}

	private void salvarPartidas(Rodada r) {
		
		partidaEstatisticasRepository.saveAll(
				r.getPartidas().stream().map(PartidaResultado::getPartidaEstatisticas).collect(Collectors.toList()));

		partidaRepository.saveAllAndFlush(r.getPartidas());

		if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }

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
			r.getGrupoCampeonato()
					.setClassificacao(classificacaoRepository.findByGrupoCampeonato(r.getGrupoCampeonato()));
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

	@Async("defaultExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(RodadaEliminatoria rodada) {
		
		PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO = new PartidaJogadorEstatisticaDTO();
		PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO = new PartidaTorcidaSalvarDTO();
		
		List<String> mensagens = new ArrayList<String>();
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio, fim;

		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		carregarPartidas(rodada);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#carregarPartidas:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularTorcidaPartida:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		jogarPartidas(rodada, partidaJogadorEstatisticaDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#jogarPartidas:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		salvarPartidas(rodada);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#salvarPartidas:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		salvarPartidaJogadorEstatisticas(partidaJogadorEstatisticaDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#salvarPartidaJogadorEstatisticas:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		salvarPartidaTorcida(partidaTorcidaSalvarDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#salvarPartidaTorcida:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal(RE):" + stopWatch.getTime());
		//System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(rodada);
	}

	private void carregarPartidas(RodadaEliminatoria rodada) {
		rodada.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada));
	}

	private void jogarPartidas(RodadaEliminatoria rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		partidaResultadoService.jogarRodada(rodada, partidaJogadorEstatisticaDTO);
	}

	private void salvarPartidas(RodadaEliminatoria r) {
		
		partidaEstatisticasRepository.saveAll(r.getPartidas().stream()
				.map(PartidaEliminatoriaResultado::getPartidaEstatisticas).collect(Collectors.toList()));

		partidaEliminatoriaRepository.saveAllAndFlush(r.getPartidas());
		/*for (PartidaEliminatoriaResultado per : r.getPartidas()) {
			if (per.getProximaPartida() != null) partidaEliminatoriaRepository.saveAndFlush(per.getProximaPartida());
		}*/
		partidaEliminatoriaRepository.saveAll(r.getPartidas().stream().filter(p -> p.getProximaPartida() != null)
				.map(p -> p.getProximaPartida()).collect(Collectors.toList()));

		if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }
	}

	private void salvarLances(List<? extends PartidaResultadoJogavel> partidas) {//TODO: salvar PartidaLance
		List<PartidaLance> lances = null;

		for (PartidaResultadoJogavel partida : partidas) {
			lances = null;//partida.getPartidaLances();
			partidaLanceRepository.saveAll(lances);//TODO: agrupar lances e fazer apenas um saveAll??
		}
	}

	@Async("defaultExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(RodadaAmistosa rodada) {
		
		PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO = new PartidaJogadorEstatisticaDTO();
		PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO = new PartidaTorcidaSalvarDTO();

		carregarPartidas(rodada);
		calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
		jogarPartidas(rodada, partidaJogadorEstatisticaDTO);
		salvarPartidas(rodada);

		salvarPartidaJogadorEstatisticas(partidaJogadorEstatisticaDTO);
		salvarPartidaTorcida(partidaTorcidaSalvarDTO);

		return CompletableFuture.completedFuture(rodada);
	}
	
	private void salvarPartidaJogadorEstatisticas(PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		
		List<String> mensagens = new ArrayList<String>();
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio, fim;

		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		habilidadeValorEstatisticaRepository.saveAll(partidaJogadorEstatisticaDTO.getHabilidadeValorEstatistica());
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#habilidadeValorEstatisticaRepository:" + (fim - inicio));
		
		/*stopWatch.split();
		inicio = stopWatch.getSplitTime();
		//TODO:melhorar aqui
		jogadorEstatisticasTemporadaRepository.saveAll(partidaJogadorEstatisticaDTO.getJogadorEstatisticasTemporada());
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#jogadorEstatisticasTemporadaRepository:" + (fim - inicio));*/
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		jogadorEstatisticaSemanaRepository.saveAll(partidaJogadorEstatisticaDTO.getJogadorEstatisticasSemana());
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#jogadorEstatisticaSemanaRepository:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		//System.err.println(mensagens);
	}

	private void carregarPartidas(RodadaAmistosa rodada) {
		rodada.setPartidas(partidaAmistosaResultadoRepository.findByRodada(rodada));
	}

	private void jogarPartidas(RodadaAmistosa rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		partidaResultadoService.jogarRodada(rodada, partidaJogadorEstatisticaDTO);
	}

	private void salvarPartidas(RodadaAmistosa r) {
		
		partidaEstatisticasRepository.saveAll(r.getPartidas().stream()
				.map(PartidaAmistosaResultado::getPartidaEstatisticas).collect(Collectors.toList()));

		partidaAmistosaResultadoRepository.saveAllAndFlush(r.getPartidas());
		
		if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }
	}
	
	private void salvarPartidaTorcida(PartidaTorcidaSalvarDTO dto) {
		partidaTorcidaRepository.saveAll(dto.getPartidaTorcidaList());
		movimentacaoFinanceiraRepository.saveAll(dto.getMovimentacaoFinanceira());
	}
}
