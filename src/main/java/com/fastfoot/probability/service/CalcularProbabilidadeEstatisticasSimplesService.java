package com.fastfoot.probability.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.service.ConsultarClubeCampeaoService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
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
public class CalcularProbabilidadeEstatisticasSimplesService extends CalcularProbabilidadeService {
	
	private static final Integer NUM_SIMULACOES_SEM_17 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_19 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_21 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_22 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_23 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_24 = 10000;
	
	private static final Random R = new Random();
	
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

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularProbabilidadesCampeonato(Semana semana, Campeonato nacional, Campeonato nacionalII) {

		nacional.setClassificacao(classificacaoRepository.findByCampeonato(nacional));
		nacional.setRodadas(rodadaRepository.findByCampeonato(nacional));

		nacionalII.setClassificacao(classificacaoRepository.findByCampeonato(nacionalII));
		nacionalII.setRodadas(rodadaRepository.findByCampeonato(nacionalII));

		List<Rodada> rodadas = new ArrayList<Rodada>();
		rodadas.addAll(nacional.getRodadas());
		rodadas.addAll(nacionalII.getRodadas());
		carregarPartidas(rodadas);
		
		Collection<CampeonatoClubeProbabilidade> probabilidades = calcularClubeProbabilidade(semana, nacional,
				nacionalII, null, TipoCampeonatoClubeProbabilidade.ESTATISTICAS_SIMPLES,
				consultarClubeCampeaoService.getCampeoes(semana.getTemporada(), nacional.getLiga()),
				carregarParametroService.getParametroInteger(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS));
		
		salvarProbabilidades(probabilidades);
		
		return CompletableFuture.completedFuture(true);
	}

	private void carregarPartidas(List<Rodada> rodadas) {
		List<PartidaResultado> partidas = partidaRepository.findByRodadas(rodadas);
		Map<Rodada, List<PartidaResultado>> partidasRodada = partidas.stream()
				.collect(Collectors.groupingBy(PartidaResultado::getRodada));
		rodadas.stream().forEach(r -> r.setPartidas(partidasRodada.get(r)));
	}

	protected Integer getNumeroSimulacoes(Semana semana) {

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

	protected void jogarPartida(PartidaResultado p, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades, PartidaProbabilidadeResultado partidaProbabilidade) {

		ClassificacaoProbabilidade clasMandante = classificacaoProbabilidades.get(p.getClubeMandante());

		ClassificacaoProbabilidade clasVisitante = classificacaoProbabilidades.get(p.getClubeVisitante());

		double vitoriaMandante = clasMandante.getProbabilidadeVitoria() + clasVisitante.getProbabilidadeDerrota();

		double empate = clasMandante.getProbabilidadeEmpate() + clasVisitante.getProbabilidadeEmpate();

		//double vitoriaVisitante = clasMandante.getProbabilidadeDerrota() + clasVisitante.getProbabilidadeVitoria();

		double resultado = R.nextDouble() * 2;

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

		clasMandante.calcularProbabilidades();
		clasVisitante.calcularProbabilidades();
	}

	protected Integer getPosicoesClassificamCIMin() { return 1; }
	
	protected Integer getPosicoesClassificamCIMax() { return 4; }
	
	protected Integer getPosicoesClassificamCIIMin() { return 5; }
	
	protected Integer getPosicoesClassificamCIIMax() { return 8; }
	
	protected Integer getPosicoesClassificamCIIIMin() {

		Integer nroCompeticoesContinentais = carregarParametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		
		if (nroCompeticoesContinentais != 3) {
			return -1;
		}

		return 9;
	}

	protected Integer getPosicoesClassificamCIIIMax() {

		Integer nroCompeticoesContinentais = carregarParametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);

		Boolean cIIIReduzido = carregarParametroService.getParametroBoolean(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);

		if (nroCompeticoesContinentais != 3) {
			return -1;
		}

		if (cIIIReduzido) {
			return 10;
		}

		return 12;
	}
	
	protected Integer getPosicoesClassificamCNIMin() { return 1; }
	
	protected Integer getPosicoesClassificamCNIMax() {
		
		return carregarParametroService.getNumeroTimesParticipantesCopaNacional();
	}
	
	//########	TESTE	##############

	/*private synchronized void printClubeProbabilidade(Collection<CampeonatoClubeProbabilidade> clubeProbabilidades) {
		for (CampeonatoClubeProbabilidade cp : clubeProbabilidades) {
			System.err.println(cp);
		}
	}*/
}
