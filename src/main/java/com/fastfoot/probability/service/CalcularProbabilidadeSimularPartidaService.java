package com.fastfoot.probability.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoFacadeService;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.service.ConsultarClubeCampeaoService;
import com.fastfoot.match.service.CarregarEscalacaoJogadoresPartidaService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.player.service.CalcularEstatisticasFinalizacaoDefesaService;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.ClubeProbabilidadeDefesa;
import com.fastfoot.probability.model.ClubeProbabilidadeFinalizacao;
import com.fastfoot.probability.model.TipoCampeonatoClubeProbabilidade;
import com.fastfoot.probability.model.entity.CampeonatoClubeProbabilidade;
import com.fastfoot.probability.model.repository.CampeonatoClubeProbabilidadeRepository;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.service.CarregarParametroService;

@Service
public class CalcularProbabilidadeSimularPartidaService extends CalcularProbabilidadeService {
	
	private static final Integer NUM_SIMULACOES_SEM_17 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_19 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_21 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_22 = 5000;
	
	private static final Integer NUM_SIMULACOES_SEM_23 = 5000;
	
	private static final Integer NUM_SIMULACOES_SEM_24 = 5000;
	
	private static final SplittableRandom R = new SplittableRandom();
	
	//private static boolean imprimir = false;
	
	//###	REPOSITORY	###
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaRepository;

	@Autowired
	private CampeonatoClubeProbabilidadeRepository campeonatoClubeProbabilidadeRepository;

	//###	SERVICE	###

	@Autowired
	private CarregarParametroService carregarParametroService;

	@Autowired
	private ConsultarClubeCampeaoService consultarClubeCampeaoService;
	
	@Autowired
	private CalcularEstatisticasFinalizacaoDefesaService calcularEstatisticasFinalizacaoDefesaService;

	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;

	@Autowired
	private CalcularPartidaProbabilidadeResultadoFacadeService calcularPartidaProbabilidadeResultadoFacadeService;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularProbabilidadesCampeonato(Semana semana, Campeonato nacional, Campeonato nacionalII,
			TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {
		
		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();
		
		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitDuration().toMillis();

		nacional.setClassificacao(classificacaoRepository.findByCampeonato(nacional));
		nacional.setRodadas(rodadaRepository.findByCampeonato(nacional));

		nacionalII.setClassificacao(classificacaoRepository.findByCampeonato(nacionalII));
		nacionalII.setRodadas(rodadaRepository.findByCampeonato(nacionalII));
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#carregar:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		List<Rodada> rodadas = new ArrayList<Rodada>();
		rodadas.addAll(nacional.getRodadas());
		rodadas.addAll(nacionalII.getRodadas());
		carregarPartidas(semana.getTemporada().getJogo(), rodadas, tipoClubeProbabilidade);
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#carregarPartidas:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
		
		//
		Map<PartidaResultado, PartidaProbabilidadeResultado> partidaProbabilidade = new HashMap<PartidaResultado, PartidaProbabilidadeResultado>();
		
		Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes = null;

		Map<Clube, ClubeProbabilidadeDefesa> clubesProbabilidadeDefesa = null;

		if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO.equals(tipoClubeProbabilidade) ||
				TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_DEFESA.equals(tipoClubeProbabilidade) ||
				TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_ESTATISTICAS_FINALIZACAO_POISSON.equals(tipoClubeProbabilidade)) {
			clubeProbabilidadeFinalizacoes = calcularEstatisticasFinalizacaoDefesaService
					.getEstatisticasFinalizacaoClube(semana.getTemporada());
			clubesProbabilidadeDefesa = calcularEstatisticasFinalizacaoDefesaService
					.getEstatisticasDefesaClube(semana.getTemporada());
		}
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#getEstatisticas:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		for (int r = nacional.getRodadaAtual(); r < nacional.getRodadas().size(); r++) {			
			for (PartidaResultado p : nacional.getRodadas().get(r).getPartidas()) {
				partidaProbabilidade.put(p, calcularPartidaProbabilidadeResultadoFacadeService.calcularPartidaProbabilidadeResultado(p,
						clubeProbabilidadeFinalizacoes, clubesProbabilidadeDefesa, p.getEscalacaoMandante(),
						p.getEscalacaoVisitante(), tipoClubeProbabilidade));
			}
		}
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#calcularPartidaProbabilidadeResultado:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
		
		/*clubeProbabilidadeFinalizacoes = calcularEstatisticasFinalizacaoDefesaService
				.getEstatisticasFinalizacaoClube(nacionalII);
		clubesProbabilidadeDefesa = calcularEstatisticasFinalizacaoDefesaService.getEstatisticasDefesaClube(nacionalII);*/
		
		for (int r = nacionalII.getRodadaAtual(); r < nacionalII.getRodadas().size(); r++) {			
			for (PartidaResultado p : nacionalII.getRodadas().get(r).getPartidas()) {
				partidaProbabilidade.put(p, calcularPartidaProbabilidadeResultadoFacadeService.calcularPartidaProbabilidadeResultado(p,
						clubeProbabilidadeFinalizacoes, clubesProbabilidadeDefesa, p.getEscalacaoMandante(),
						p.getEscalacaoVisitante(), tipoClubeProbabilidade));
			}
		}
		//
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#calcularPartidaProbabilidadeResultadoII:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
		
		Collection<CampeonatoClubeProbabilidade> probabilidades = calcularClubeProbabilidade(semana, nacional,
				nacionalII, partidaProbabilidade, tipoClubeProbabilidade,
				consultarClubeCampeaoService.getCampeoes(semana.getTemporada(), nacional.getLigaJogo()),
				carregarParametroService.getParametroInteger(semana.getTemporada().getJogo(), ParametroConstantes.NUMERO_CLUBES_REBAIXADOS));
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#calcularClubeProbabilidade:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
		
		salvarProbabilidades(probabilidades);
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#salvarProbabilidades:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
		
		//Finalizar
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getDuration().toMillis());//Tempo total
		
		//System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(true);
	}

	private void carregarPartidas(Jogo jogo, List<Rodada> rodadas, TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {
		List<PartidaResultado> partidas = partidaRepository.findByRodadas(rodadas);
		if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA.equals(tipoClubeProbabilidade)) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacao(jogo, partidas);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_HABILIDADE_GRUPO.equals(tipoClubeProbabilidade)) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(jogo, partidas);
		}
		Map<Rodada, List<PartidaResultado>> partidasRodada = partidas.stream()
				.collect(Collectors.groupingBy(PartidaResultado::getRodada));
		rodadas.stream().forEach(r -> r.setPartidas(partidasRodada.get(r)));
	}

	protected Integer getNumeroSimulacoes(Semana semana) {
		//return 100;

		if (semana.getNumero() == 17)
			return NUM_SIMULACOES_SEM_17;
		if (semana.getNumero() == 19)
			return NUM_SIMULACOES_SEM_19;
		if (semana.getNumero() == 21)
			return NUM_SIMULACOES_SEM_21;
		if (semana.getNumero() == 22)
			return NUM_SIMULACOES_SEM_22;
		if (semana.getNumero() == 23)
			return NUM_SIMULACOES_SEM_23;
		if (semana.getNumero() == 24)
			return NUM_SIMULACOES_SEM_24;
		return -1;
	}
	
	private void salvarProbabilidades(Collection<CampeonatoClubeProbabilidade> probabilidades) {
		campeonatoClubeProbabilidadeRepository.saveAll(probabilidades);
	}

	protected void jogarPartida(PartidaResultado p, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades,
			PartidaProbabilidadeResultado partidaProbabilidade) {

		ClassificacaoProbabilidade clasMandante = classificacaoProbabilidades.get(p.getClubeMandante());

		ClassificacaoProbabilidade clasVisitante = classificacaoProbabilidades.get(p.getClubeVisitante());

		double vitoriaMandante = partidaProbabilidade.getProbabilidadeVitoriaMandante();

		double empate = partidaProbabilidade.getProbabilidadeEmpate();

		//double vitoriaVisitante = clasMandante.getProbabilidadeDerrota() + clasVisitante.getProbabilidadeVitoria();

		double resultado = R.nextDouble();

		if (resultado <= vitoriaMandante) {
			clasMandante.setPontos(clasMandante.getPontos() + Constantes.PTOS_VITORIA);
			clasMandante.setVitorias(clasMandante.getVitorias() + 1);
			clasMandante.setGolsPro(clasMandante.getGolsPro() + 1);
			clasMandante.setSaldoGols(clasMandante.getSaldoGols() + 1);

			clasVisitante.setSaldoGols(clasVisitante.getSaldoGols() - 1);
		} else if (resultado <= empate) {
			clasMandante.setPontos(clasMandante.getPontos() + Constantes.PTOS_EMPATE);
			clasMandante.setGolsPro(clasMandante.getGolsPro() + 0);

			clasVisitante.setPontos(clasVisitante.getPontos() + Constantes.PTOS_EMPATE);
			clasVisitante.setGolsPro(clasVisitante.getGolsPro() + 0);
		} else {
			clasVisitante.setPontos(clasVisitante.getPontos() + Constantes.PTOS_VITORIA);
			clasVisitante.setVitorias(clasVisitante.getVitorias() + 1);
			clasVisitante.setGolsPro(clasVisitante.getGolsPro() + 1);
			clasVisitante.setSaldoGols(clasVisitante.getSaldoGols() + 1);

			clasMandante.setSaldoGols(clasMandante.getSaldoGols() - 1);
		}

		//clasMandante.calcularProbabilidades();
		//clasVisitante.calcularProbabilidades();
	}

	protected Integer getPosicoesClassificamCIMin() { return 1; }
	
	protected Integer getPosicoesClassificamCIMax() { return 4; }
	
	protected Integer getPosicoesClassificamCIIMin() { return 5; }
	
	protected Integer getPosicoesClassificamCIIMax() { return 8; }
	
	protected Integer getPosicoesClassificamCIIIMin(Jogo jogo) {

		Integer nroCompeticoesContinentais = carregarParametroService.getParametroInteger(jogo, ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		
		if (nroCompeticoesContinentais != 3) {
			return -1;
		}

		return 9;
	}

	protected Integer getPosicoesClassificamCIIIMax(Jogo jogo) {

		Integer nroCompeticoesContinentais = carregarParametroService.getParametroInteger(jogo, ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);

		Boolean cIIIReduzido = carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);

		if (nroCompeticoesContinentais != 3) {
			return -1;
		}

		if (cIIIReduzido) {
			return 10;
		}

		return 12;
	}
	
	protected Integer getPosicoesClassificamCNIMin() { return 1; }
	
	protected Integer getPosicoesClassificamCNIMax(Jogo jogo) {
		
		return carregarParametroService.getNumeroTimesParticipantesCopaNacional(jogo);
	}
	
	//########	TESTE	##############

	/*private synchronized void printClubeProbabilidade(Collection<CampeonatoClubeProbabilidade> clubeProbabilidades) {
		for (CampeonatoClubeProbabilidade cp : clubeProbabilidades) {
			System.err.println(cp);
		}
	}*/
}
