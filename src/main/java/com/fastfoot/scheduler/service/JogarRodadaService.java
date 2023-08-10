package com.fastfoot.scheduler.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.match.model.PartidaJogadorEstatisticaDTO;
import com.fastfoot.match.model.dto.PartidaTorcidaSalvarDTO;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.repository.PartidaEstatisticasRepository;
import com.fastfoot.match.model.repository.PartidaLanceRepository;
import com.fastfoot.match.model.repository.PartidaTorcidaRepository;
import com.fastfoot.match.service.CalcularTorcidaPartidaService;
import com.fastfoot.match.service.CarregarEscalacaoJogadoresPartidaService;
import com.fastfoot.match.service.JogarPartidaService;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEnergia;
import com.fastfoot.player.model.repository.HabilidadeValorEstatisticaRepository;
import com.fastfoot.player.model.repository.JogadorEnergiaRepository;
import com.fastfoot.player.model.repository.JogadorEstatisticasSemanaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Classificacao;
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
import com.fastfoot.scheduler.service.util.PromotorEliminatoria;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class JogarRodadaService {
	
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
	private JogadorEstatisticasSemanaRepository jogadorEstatisticasSemanaRepository;
	
	@Autowired
	private PartidaEstatisticasRepository partidaEstatisticasRepository;

	@Autowired
	private PartidaTorcidaRepository partidaTorcidaRepository;

	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	@Autowired
	private JogadorEnergiaRepository jogadorEnergiaRepository;

	@Autowired
	private JogadorRepository jogadorRepository;

	//###	SERVICE	###
	/*@Autowired
	private PartidaResultadoService partidaResultadoService;*/
	
	@Autowired
	private CalcularTorcidaPartidaService calcularTorcidaPartidaService;
	
	@Autowired
	private JogarPartidaService jogarPartidaService;

	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;

	private static final Boolean SALVAR_LANCES = false;
	
	private static final Boolean SALVAR_HABILIDADE_VALOR_ESTATISTICAS = false;//#DEVEL

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
		carregarEscalacao(rodada.getPartidas());
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#carregarEscalacao:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
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
		
		if (SALVAR_LANCES) {
			stopWatch.split();
			inicio = stopWatch.getSplitTime();
			partidaLanceRepository.saveAll(partidaJogadorEstatisticaDTO.getPartidaLances());
			stopWatch.split();
			fim = stopWatch.getSplitTime();
			mensagens.add("\t#partidaLanceRepository:" + (fim - inicio));
		}
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		//System.err.println(mensagens);

		return CompletableFuture.completedFuture(rodada);
	}
	
	/*private void calcularTorcidaPartida(RodadaJogavel rodada, PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
	}*/

	private void jogarPartidas(Rodada rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		if(rodada.getCampeonato() != null) {
			jogarRodada(rodada, partidaJogadorEstatisticaDTO,
					rodada.getCampeonato().getClassificacao());
			
			if (!rodada.isUltimaRodadaPontosCorridos()) {
				ClassificacaoUtil.ordernarClassificacao(rodada.getCampeonato().getClassificacao(), false);
			}
		} else if (rodada.getGrupoCampeonato() != null) {
			jogarRodada(rodada, partidaJogadorEstatisticaDTO,
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

		//if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }

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
		carregarEscalacao(rodada.getPartidas());
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#carregarEscalacao:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularTorcidaPartida:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		jogarRodada(rodada, partidaJogadorEstatisticaDTO);
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
		
		if (SALVAR_LANCES) {
			stopWatch.split();
			inicio = stopWatch.getSplitTime();
			partidaLanceRepository.saveAll(partidaJogadorEstatisticaDTO.getPartidaLances());
			stopWatch.split();
			fim = stopWatch.getSplitTime();
			mensagens.add("\t#partidaLanceRepository:" + (fim - inicio));
		}
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal(RE):" + stopWatch.getTime());
		//System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(rodada);
	}

	private void carregarPartidas(RodadaEliminatoria rodada) {
		rodada.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada));
	}

	/*private void jogarPartidas(RodadaEliminatoria rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		jogarRodada(rodada, partidaJogadorEstatisticaDTO);
	}*/

	private void salvarPartidas(RodadaEliminatoria r) {
		
		partidaEstatisticasRepository.saveAll(r.getPartidas().stream()
				.map(PartidaEliminatoriaResultado::getPartidaEstatisticas).collect(Collectors.toList()));

		partidaEliminatoriaRepository.saveAllAndFlush(r.getPartidas());
		/*for (PartidaEliminatoriaResultado per : r.getPartidas()) {
			if (per.getProximaPartida() != null) partidaEliminatoriaRepository.saveAndFlush(per.getProximaPartida());
		}*/
		partidaEliminatoriaRepository.saveAll(r.getPartidas().stream().filter(p -> p.getProximaPartida() != null)
				.map(p -> p.getProximaPartida()).collect(Collectors.toList()));

		//if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }
	}

	@Async("defaultExecutor")
	public CompletableFuture<RodadaJogavel> executarRodada(RodadaAmistosa rodada) {
		
		PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO = new PartidaJogadorEstatisticaDTO();
		PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO = new PartidaTorcidaSalvarDTO();

		carregarPartidas(rodada);
		carregarEscalacao(rodada.getPartidas());
		calcularTorcidaPartidaService.calcularTorcidaPartida(rodada, partidaTorcidaSalvarDTO);
		jogarRodada(rodada, partidaJogadorEstatisticaDTO);
		salvarPartidas(rodada);

		salvarPartidaJogadorEstatisticas(partidaJogadorEstatisticaDTO);
		salvarPartidaTorcida(partidaTorcidaSalvarDTO);
		
		if (SALVAR_LANCES) {
			partidaLanceRepository.saveAll(partidaJogadorEstatisticaDTO.getPartidaLances());
		}

		return CompletableFuture.completedFuture(rodada);
	}
	
	private void salvarPartidaJogadorEstatisticas(PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		
		List<String> mensagens = new ArrayList<String>();
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio, fim;

		if (SALVAR_HABILIDADE_VALOR_ESTATISTICAS) {
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		habilidadeValorEstatisticaRepository.saveAll(partidaJogadorEstatisticaDTO.getHabilidadeValorEstatistica());
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#habilidadeValorEstatisticaRepository:" + (fim - inicio));
		}
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		jogadorEstatisticasSemanaRepository.saveAll(partidaJogadorEstatisticaDTO.getJogadorEstatisticasSemana());
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#jogadorEstatisticaSemanaRepository:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		jogadorEnergiaRepository.saveAll(partidaJogadorEstatisticaDTO.getJogadorEnergias());
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#jogadorEnergiaRepository:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		//System.err.println(mensagens);
	}

	private void carregarPartidas(RodadaAmistosa rodada) {
		rodada.setPartidas(partidaAmistosaResultadoRepository.findByRodada(rodada));
	}

	/*private void jogarPartidas(RodadaAmistosa rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		jogarRodada(rodada, partidaJogadorEstatisticaDTO);
	}*/

	private void salvarPartidas(RodadaAmistosa r) {
		
		partidaEstatisticasRepository.saveAll(r.getPartidas().stream()
				.map(PartidaAmistosaResultado::getPartidaEstatisticas).collect(Collectors.toList()));

		partidaAmistosaResultadoRepository.saveAllAndFlush(r.getPartidas());
		
		//if (SALVAR_LANCES) { salvarLances(r.getPartidas()); }
	}
	
	private void salvarPartidaTorcida(PartidaTorcidaSalvarDTO dto) {
		partidaTorcidaRepository.saveAll(dto.getPartidaTorcidaList());
		movimentacaoFinanceiraRepository.saveAll(dto.getMovimentacaoFinanceira());
	}

	/*private void jogarPartida(PartidaResultadoJogavel partida, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		jogarPartidaService.jogar(partida, partidaJogadorEstatisticaDTO);
	}*/

	private void jogarRodada(Rodada rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO, List<Classificacao> classificacao) {

		for (PartidaResultado p : rodada.getPartidas()) {
			jogarPartidaService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);
		}
		
		ClassificacaoUtil.atualizarClassificacao(classificacao, rodada.getPartidas());
	}

	private void jogarRodada(RodadaEliminatoria rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {

		for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
			jogarPartidaService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);

			if (p.getProximaPartida() != null) {
				PromotorEliminatoria.promoverProximaPartidaEliminatoria(p);
			}
		}
	}

	private void jogarRodada(RodadaAmistosa rodada, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		for (PartidaAmistosaResultado p : rodada.getPartidas()) {
			jogarPartidaService.jogar(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante(), partidaJogadorEstatisticaDTO);
		}
	}

	private void carregarEscalacao(List<? extends PartidaResultadoJogavel> partidas) {

		List<Clube> clubes = new ArrayList<Clube>();

		clubes.addAll(partidas.stream().map(p -> p.getClubeMandante()).collect(Collectors.toList()));
		clubes.addAll(partidas.stream().map(p -> p.getClubeVisitante()).collect(Collectors.toList()));

		List<Jogador> jogadores = jogadorRepository.findByClubesAndStatusJogadorFetchHabilidades(clubes,
				StatusJogador.ATIVO);

		carregarJogadorEnergia(jogadores);

		Map<Clube, List<Jogador>> jogadoresPorClube = jogadores.stream()
				.collect(Collectors.groupingBy(Jogador::getClube));

		EscalacaoClube escalacaoClubeMandante, escalacaoClubeVisitante;

		for (PartidaResultadoJogavel p : partidas) {
			escalacaoClubeMandante = carregarEscalacaoJogadoresPartidaService
					.carregarJogadoresPartida(p.getClubeMandante(), jogadoresPorClube.get(p.getClubeMandante()), p);
			escalacaoClubeVisitante = carregarEscalacaoJogadoresPartidaService
					.carregarJogadoresPartida(p.getClubeVisitante(), jogadoresPorClube.get(p.getClubeVisitante()), p);
			p.setEscalacaoMandante(escalacaoClubeMandante);
			p.setEscalacaoVisitante(escalacaoClubeVisitante);
		}
	}

	private void carregarJogadorEnergia(List<Jogador> jogadores) {
		List<Map<String, Object>> jogEnergia = jogadorEnergiaRepository.findEnergiaJogador();

		Map<Jogador, Map<String, Object>> x = jogEnergia.stream()
				.collect(Collectors.toMap(ej -> new Jogador(((BigInteger) ej.get("id_jogador")).longValue()), Function.identity()));

		JogadorEnergia je = null;
		Map<String, Object> jes = null;
		Integer energia = null;

		for (Jogador j : jogadores) {
			je = new JogadorEnergia();
			je.setJogador(j);
			je.setEnergia(0);//Variacao de energia

			jes = x.get(j);
			if (!ValidatorUtil.isEmpty(jes)) {
				energia = ((BigInteger) jes.get("energia")).intValue();
				if (energia > Constantes.ENERGIA_INICIAL) {
					je.setEnergiaInicial(Constantes.ENERGIA_INICIAL);
				} else {
					je.setEnergiaInicial(energia);
				}
			} else {
				je.setEnergiaInicial(Constantes.ENERGIA_INICIAL);
			}

			j.setJogadorEnergia(je);
		}

	}
}
