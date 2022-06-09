package com.fastfoot.probability.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.probability.model.entity.ClubeProbabilidadePosicao;
import com.fastfoot.probability.model.repository.ClubeProbabilidadePosicaoRepository;
import com.fastfoot.probability.model.repository.ClubeProbabilidadeRepository;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.service.ParametroService;

@Service
public class CalcularProbabilidadeService {
	
	private static final Integer NUM_SIMULACOES_SEM_22 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_23 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_24 = 10000;
	
	private static final Random R = new Random();
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaRepository;

	@Autowired
	private ClubeProbabilidadeRepository clubeProbabilidadeRepository;

	@Autowired
	private ClubeProbabilidadePosicaoRepository clubeProbabilidadePosicaoRepository;

	@Autowired
	private ParametroService parametroService;
	
	@Async("probabilidadeExecutor")
	public CompletableFuture<Boolean> calcularProbabilidadesCampeonato(Semana semana, Campeonato c) {

		c.setClassificacao(classificacaoRepository.findByCampeonato(c));
		c.setRodadas(rodadaRepository.findByCampeonato(c));

		for (Rodada r : c.getRodadas()) {
			r.setPartidas(partidaRepository.findByRodada(r));
		}

		Collection<ClubeProbabilidade> probabilidades = calcularClubeProbabilidade(semana, c);
		
		salvarProbabilidades(probabilidades);
		
		return CompletableFuture.completedFuture(true);
	}

	private void salvarProbabilidades(Collection<ClubeProbabilidade> probabilidades) {
		
		clubeProbabilidadeRepository.saveAll(probabilidades);
		
		for (ClubeProbabilidade cp : probabilidades) {
			clubeProbabilidadePosicaoRepository.saveAll(cp.getClubeProbabilidadePosicao().values());
		}
	}

	private Integer getNumeroSimulacoes(Semana semana) {
		if (semana.getNumero() == 22)
			return NUM_SIMULACOES_SEM_22;
		if (semana.getNumero() == 23)
			return NUM_SIMULACOES_SEM_23;
		if (semana.getNumero() == 24)
			return NUM_SIMULACOES_SEM_24;
		return -1;
	}
	
	public Collection<ClubeProbabilidade> calcularClubeProbabilidade(Semana semana, Campeonato campeonato) {
		
		Map<Clube, ClubeProbabilidade> clubeProbabilidades = new HashMap<Clube, ClubeProbabilidade>();
		
		inicializarClubeProbabilidade(clubeProbabilidades,
				campeonato.getClassificacao().stream().map(c -> c.getClube()).collect(Collectors.toList()), semana,
				campeonato);

		for (int i = 0; i < getNumeroSimulacoes(semana); i++) {
		
			Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades = campeonato.getClassificacao().stream()
					.map(c -> ClassificacaoProbabilidade.criar(c))
					.collect(Collectors.toMap(ClassificacaoProbabilidade::getClube, Function.identity()));
		
		
			for (int r = campeonato.getRodadaAtual(); r < campeonato.getRodadas().size(); r++) {
				jogarRodada(campeonato.getRodadas().get(r), classificacaoProbabilidades);
			}
			
			List<ClassificacaoProbabilidade> classificacaoProbabilidadesList = new ArrayList<ClassificacaoProbabilidade>(
					classificacaoProbabilidades.values());
			
			ordernarClassificacao(classificacaoProbabilidadesList, true);
			
			
			/*List<Integer> x = classificacaoProbabilidadesList.stream().map(c -> c.getPosicao()).sorted().collect(Collectors.toList());
			
			if (x.size() != 16) {
				System.err.println("--------------------\n\t" + x);
			}*/
			
			agruparClubeProbabilidade(clubeProbabilidades, classificacaoProbabilidadesList);

		}
		
		calcularProbabilidadesEspecificas(clubeProbabilidades, campeonato.getNivelCampeonato());
		
		//printClubeProbabilidade(clubeProbabilidades.values());
		
		return clubeProbabilidades.values();

	}
	
	private void calcularProbabilidadesEspecificas(Map<Clube, ClubeProbabilidade> clubeProbabilidades, NivelCampeonato nivelCampeonato) {
		
		Integer numeroRebaixados = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS);

		ClubeProbabilidadePosicao cpp = null;
		
		for (ClubeProbabilidade cp : clubeProbabilidades.values()) {
			
			cpp = cp.getClubeProbabilidadePosicao().get(1);
			if (cpp != null) {
				cp.setProbabilidadeCampeao(cpp.getProbabilidade());
			}
			
			if (nivelCampeonato.isNacional()) {
				int probabilidadeRebaixamento = 0;
				for (int i = Constantes.NRO_CLUBE_CAMP_NACIONAL; i > (Constantes.NRO_CLUBE_CAMP_NACIONAL - numeroRebaixados); i-- ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeRebaixamento += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeRebaixamento(probabilidadeRebaixamento);
			}
			
			if (nivelCampeonato.isNacionalII()) {
				int probabilidadeAcesso = 0;
				for (int i = 1; i <= numeroRebaixados; i++ ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeAcesso += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeAcesso(probabilidadeAcesso);
			}
			
			if (nivelCampeonato.isNacional()) {
				int probabilidadeClassificacaoContinental = 0;
				for (int i = 1; i <= getPosicoesClassificamContinental(); i++ ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeClassificacaoContinental += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeClassificacaoContinental(probabilidadeClassificacaoContinental);
			}
			
			if (nivelCampeonato.isNacional()) {
				int probabilidadeClassificacaoCopaNacional = 0;
				for (int i = 1; i <= getPosicoesClassificamCopaNacional(); i++ ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeClassificacaoCopaNacional += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeClassificacaoCopaNacional(probabilidadeClassificacaoCopaNacional);
			}
			
			if (nivelCampeonato.isNacionalII()) {
				int probabilidadeClassificacaoCopaNacional = 0;
				for (int i = 1; i <= getPosicoesClassificamCopaNacionalNII(); i++ ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeClassificacaoCopaNacional += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeClassificacaoCopaNacional(probabilidadeClassificacaoCopaNacional);
			}

		}

	}
	
	private Integer getPosicoesClassificamContinental() {

		Integer nroCompeticoesContinentais = parametroService
				.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);

		Boolean cnIIIReduzida = false; // parametroService.getParametro... TODO

		if (nroCompeticoesContinentais == 3) {
			if (cnIIIReduzida) {
				return 6;
			} else {
				return 8;
			}
		} else if (nroCompeticoesContinentais == 2) {
			return 4;
		}
		
		return -1;
	}
	
	private Integer getPosicoesClassificamCopaNacional() {
		
		Integer nroCompeticoesContinentais = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		
		String numeroRodadasCopaNacional = parametroService.getParametroString(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);
		
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R.equals(numeroRodadasCopaNacional)) {
			return 11;
		}
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoesContinentais == 3) {//20
				return 13;
			} else if (nroCompeticoesContinentais == 2) {//24
				return 16;
			}
		}
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoesContinentais == 3) {//28
				return 16;
			} else if (nroCompeticoesContinentais == 2) {//32
				return 16;
			}
		}

		return -1;
	}
	
	private Integer getPosicoesClassificamCopaNacionalNII() {
		
		Integer nroCompeticoesContinentais = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		
		String numeroRodadasCopaNacional = parametroService.getParametroString(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);
		
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R.equals(numeroRodadasCopaNacional)) {
			return 0;
		}
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoesContinentais == 3) {//20
				return 2;
			} else if (nroCompeticoesContinentais == 2) {//24
				return 3;
			}
		}
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoesContinentais == 3) {//28
				return 7;
			} else if (nroCompeticoesContinentais == 2) {//32
				return 16;
			}
		}

		return -1;
	}
	
	private void inicializarClubeProbabilidade(Map<Clube, ClubeProbabilidade> clubeProbabilidades,
			List<Clube> clubes, Semana semana, Campeonato campeonato) {
		if (clubeProbabilidades.isEmpty()) {
			for (Clube clube : clubes) {
				ClubeProbabilidade clup = new ClubeProbabilidade();
				
				clup.setClube(clube);
				clup.setCampeonato(campeonato);
				clup.setSemana(semana);

				clup.setClubeProbabilidadePosicao(new HashMap<Integer, ClubeProbabilidadePosicao>());
				
				clubeProbabilidades.put(clube, clup);
			}
		}
	}

	private void agruparClubeProbabilidade(Map<Clube, ClubeProbabilidade> clubeProbabilidades,
			List<ClassificacaoProbabilidade> classificacaoProbabilidades) {

		for (ClassificacaoProbabilidade clasp : classificacaoProbabilidades) {
			ClubeProbabilidade clup = clubeProbabilidades.get(clasp.getClube());
			
			
			ClubeProbabilidadePosicao cpp = clup.getClubeProbabilidadePosicao().get(clasp.getPosicao());
			
			if (cpp == null) {
				cpp = new ClubeProbabilidadePosicao();
				
				cpp.setPosicao(clasp.getPosicao());
				cpp.setProbabilidade(1);
				cpp.setClubeProbabilidade(clup);
				
				cpp.setPontuacaoMaxima(clasp.getPontos());
				cpp.setPontuacaoMedia(clasp.getPontos().doubleValue());
				cpp.setPontuacaoMinima(clasp.getPontos());
				
				clup.getClubeProbabilidadePosicao().put(clasp.getPosicao(), cpp);
			} else {
				
				if (clasp.getPontos() > cpp.getPontuacaoMaxima()) {
					cpp.setPontuacaoMaxima(clasp.getPontos());
				}
				
				if (clasp.getPontos() < cpp.getPontuacaoMinima()) {
					cpp.setPontuacaoMinima(clasp.getPontos());
				}
				
				double media = (cpp.getPontuacaoMedia() * cpp.getProbabilidade() + clasp.getPontos().byteValue())/(cpp.getProbabilidade() + 1);
				cpp.setPontuacaoMedia(media);
				
				cpp.setProbabilidade(cpp.getProbabilidade() + 1);
			}

		}
		
	}
	
	private void jogarRodada(Rodada r, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades) {

		for (PartidaResultado p : r.getPartidas()) {
			jogarPartida(p, classificacaoProbabilidades);
		}

	}

	private void jogarPartida(PartidaResultado p, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades) {

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


	public static void ordernarClassificacao(List<ClassificacaoProbabilidade> classificacao, boolean desempatar) throws RuntimeException {

		Collections.sort(classificacao, new Comparator<ClassificacaoProbabilidade>() {

			@Override
			public int compare(ClassificacaoProbabilidade o1, ClassificacaoProbabilidade o2) {
				return o1.compareTo(o2);
			}
		});
		
		//Setar posicao inicial
		for (int i = 0; i < classificacao.size(); i++) {
			if (i > 0 && classificacao.get(i-1).compareTo(classificacao.get(i)) == 0) {
				
				List<ClassificacaoProbabilidade> empatados = new ArrayList<ClassificacaoProbabilidade>();
				
				empatados.add(classificacao.get(i));
				
				empatados.add(classificacao.get(i - 1));
				
				int j = i - 1;
				
				while (j > 0 && classificacao.get(j - 1).compareTo(classificacao.get(j)) == 0) {
					empatados.add(classificacao.get(j - 1));
					j--;
				}
				
				//Se entrou aqui, o clube[i] está empatado com clube[i-1]
				//if (desempatar) {
					sortearPosicao(empatados, j + 1);
				//} else {
					//Para manter varios clubes com a mesma classificacao em Caso de empate
					//classificacao.get(i).setPosicao(classificacao.get(i-1).getPosicao()); 
				//}
			} else {
				classificacao.get(i).setPosicao(i+1);
			}
		}

	}

	private static void sortearPosicao(List<ClassificacaoProbabilidade> classificacao, Integer posInicial) {
		Collections.shuffle(classificacao);
		for (ClassificacaoProbabilidade c : classificacao) {
			c.setPosicao(posInicial++);
		}
	}
	
	//########	TESTE	##############3333
	
	@SuppressWarnings("unused")
	private synchronized void printClubeProbabilidade(Collection<ClubeProbabilidade> clubeProbabilidades) {
		for (ClubeProbabilidade cp : clubeProbabilidades) {
			System.err.println("\t" + cp.getClube().getNome());
			System.err.println("\t\tCampeao: " + cp.getProbabilidadeCampeao());
			System.err.println("\t\tRebaixamento: " + cp.getProbabilidadeRebaixamento());
			System.err.println("\t\tAcesso: " + cp.getProbabilidadeAcesso());
			System.err.println("\t\tContinental: " + cp.getProbabilidadeClassificacaoContinental());
			System.err.println("\t\tCopa Nacional: " + cp.getProbabilidadeClassificacaoCopaNacional());
			/*for (Integer pos : cp.getClubeProbabilidadePosicao().keySet()) {
				System.err.println(String.format("\t\t\t%d:%d", pos, cp.getClubeProbabilidadePosicao().get(pos).getProbabilidade()));
				//System.err.println(String.format("\t\t\tmax:%d, min:%d, avg:%f", cp.getClubeProbabilidadePosicao().get(pos).getPontuacaoMaxima(), cp.getClubeProbabilidadePosicao().get(pos).getPontuacaoMinima(), cp.getClubeProbabilidadePosicao().get(pos).getPontuacaoMedia()) );
			}*/
		}
	}
}
