package com.fastfoot.probability.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.fastfoot.model.entity.Clube;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.ClubeProbabilidade;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;

@Service
public class CalcularProbabilidadeService {
	
	private static final Integer NUM_SIMULACOES = 100;//1000
	
	private static final Random R = new Random();
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaRepository;
	
	@Async("probabilidadeExecutor")
	public CompletableFuture<Boolean> calcularProbabilidadesCampeonato(Campeonato c) {

		c.setClassificacao(classificacaoRepository.findByCampeonato(c));
		c.setRodadas(rodadaRepository.findByCampeonato(c));

		for (Rodada r : c.getRodadas()) {
			r.setPartidas(partidaRepository.findByRodada(r));
		}

		calcularClubeProbabilidade(c);
		
		return CompletableFuture.completedFuture(true);
	}
	
	public void calcularClubeProbabilidade(Campeonato campeonato) {
		
		Map<Clube, ClubeProbabilidade> clubeProbabilidades = new HashMap<Clube, ClubeProbabilidade>();

		for (int i = 0; i < NUM_SIMULACOES; i++) {
		
			Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades = campeonato.getClassificacao().stream()
					.map(c -> ClassificacaoProbabilidade.criar(c))
					.collect(Collectors.toMap(ClassificacaoProbabilidade::getClube, Function.identity()));
		
		
			for (int r = campeonato.getRodadaAtual(); r < campeonato.getRodadas().size(); r++) {
				jogarRodada(campeonato.getRodadas().get(r), classificacaoProbabilidades);
			}
			
			List<ClassificacaoProbabilidade> classificacaoProbabilidadesX = new ArrayList<ClassificacaoProbabilidade>(
					classificacaoProbabilidades.values());
			
			ordernarClassificacao(classificacaoProbabilidadesX, true);
			
			agruparClubeProbabilidade(clubeProbabilidades, classificacaoProbabilidadesX);
		}
		
		print(clubeProbabilidades.values());
		
	}
	
	private void print(Collection<ClubeProbabilidade> clubeProbabilidades) {
		for (ClubeProbabilidade cp : clubeProbabilidades) {
			System.err.println("\t" + cp.getClube().getNome());
			for (Integer pos : cp.getPosicaoQtde().keySet()) {
				System.err.println(String.format("\t\t%d:%d", pos, cp.getPosicaoQtde().get(pos).size()));
				System.err.println("\t\t\t->" + cp.getPosicaoQtde().get(pos).stream().map(x -> x.getPontos()).collect(Collectors.toList()));
			}
		}
	}
	
	private void agruparClubeProbabilidade(Map<Clube, ClubeProbabilidade> clubeProbabilidades,
			List<ClassificacaoProbabilidade> classificacaoProbabilidades) {
		
		if (clubeProbabilidades.isEmpty()) {
			for (ClassificacaoProbabilidade clasp : classificacaoProbabilidades) {
				ClubeProbabilidade clup = new ClubeProbabilidade();
				clup.setPosicaoQtde(new HashMap<Integer, List<ClassificacaoProbabilidade>>());
				clup.setClube(clasp.getClube());
				clubeProbabilidades.put(clasp.getClube(), clup);
			}
		}
		
		for (ClassificacaoProbabilidade clasp : classificacaoProbabilidades) {
			ClubeProbabilidade clup = clubeProbabilidades.get(clasp.getClube());
			
			List<ClassificacaoProbabilidade> qtde = clup.getPosicaoQtde().get(clasp.getPosicao());
			
			if (qtde == null) {
				qtde = new ArrayList<ClassificacaoProbabilidade>();
			}
			qtde.add(clasp);

			clup.getPosicaoQtde().put(clasp.getPosicao(), qtde);
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
				//Se entrou aqui, o clube[i] está empatado com clube[i-1]
				if (desempatar) {
					sortearPosicao(Arrays.asList(classificacao.get(i-1), classificacao.get(i)), i);
				} else {
					//Para manter varios clubes com a mesma classificacao em Caso de empate
					classificacao.get(i).setPosicao(classificacao.get(i-1).getPosicao()); 
				}
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
}