package com.fastfoot.probability.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.probability.model.CampeonatoClubeProbabilidadePosicao;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.ClubeRankingPosicaoProbabilidade;
import com.fastfoot.probability.model.ClubeRankingProbabilidade;
import com.fastfoot.probability.model.TipoCampeonatoClubeProbabilidade;
import com.fastfoot.probability.model.entity.CampeonatoClubeProbabilidade;
import com.fastfoot.probability.service.util.ClassificacaoProbabilidadeUtil;
import com.fastfoot.probability.service.util.ClubeRankingProbabilidadeUtil;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.Semana;

public abstract class CalcularProbabilidadeService {
	
	protected abstract Integer getNumeroSimulacoes(Semana semana);
	
	protected abstract Integer getPosicoesClassificamCIMin();

	protected abstract Integer getPosicoesClassificamCIMax();

	protected abstract Integer getPosicoesClassificamCIIMin();

	protected abstract Integer getPosicoesClassificamCIIMax();

	protected abstract Integer getPosicoesClassificamCIIIMin(Jogo jogo);

	protected abstract Integer getPosicoesClassificamCIIIMax(Jogo jogo);

	protected abstract Integer getPosicoesClassificamCNIMin();

	protected abstract Integer getPosicoesClassificamCNIMax(Jogo jogo);
	
	protected abstract void jogarPartida(PartidaResultado p, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades,
			PartidaProbabilidadeResultado partidaProbabilidade);

	protected void agruparClubeProbabilidade(Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades,
			List<ClassificacaoProbabilidade> classificacaoProbabilidades) {

		for (ClassificacaoProbabilidade clasp : classificacaoProbabilidades) {
			CampeonatoClubeProbabilidade clup = clubeProbabilidades.get(clasp.getClube());
			
			
			CampeonatoClubeProbabilidadePosicao cpp = clup.getClubeProbabilidadePosicao().get(clasp.getPosicao());
			
			if (cpp == null) {
				cpp = new CampeonatoClubeProbabilidadePosicao();
				
				cpp.setPosicao(clasp.getPosicao());
				cpp.setProbabilidade(1);
				cpp.setClubeProbabilidade(clup);
				
				/*cpp.setPontuacaoMaxima(clasp.getPontos());
				cpp.setPontuacaoMedia(clasp.getPontos().doubleValue());
				cpp.setPontuacaoMinima(clasp.getPontos());*/
				
				clup.getClubeProbabilidadePosicao().put(clasp.getPosicao(), cpp);
			} else {
				
				/*if (clasp.getPontos() > cpp.getPontuacaoMaxima()) {
					cpp.setPontuacaoMaxima(clasp.getPontos());
				}
				
				if (clasp.getPontos() < cpp.getPontuacaoMinima()) {
					cpp.setPontuacaoMinima(clasp.getPontos());
				}
				
				double media = (cpp.getPontuacaoMedia() * cpp.getProbabilidade() + clasp.getPontos().byteValue())/(cpp.getProbabilidade() + 1);
				cpp.setPontuacaoMedia(media);*/
				
				cpp.setProbabilidade(cpp.getProbabilidade() + 1);
			}

		}
		
	}

	protected void agruparClubeRankingProbabilidade(Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades, List<ClubeRankingProbabilidade> ranking) {

		ClubeRankingPosicaoProbabilidade crpp = null;
		
		for (ClubeRankingProbabilidade crp : ranking) {
			if (clubeProbabilidades.get(crp.getClube()) != null) {
				CampeonatoClubeProbabilidade clubeProb = clubeProbabilidades.get(crp.getClube());
				crpp = clubeProb.getClubeProbabilidadePosicaoGeral().get(crp.getPosicaoGeral());
					
				if (crpp == null) {
					crpp = new ClubeRankingPosicaoProbabilidade();
					
					crpp.setPosicaoGeral(crp.getPosicaoGeral());
					crpp.setProbabilidade(1);
					crpp.setClubeProbabilidade(clubeProb);
	
					clubeProb.getClubeProbabilidadePosicaoGeral().put(crp.getPosicaoGeral(), crpp);
					
				} else {
					
					crpp.setProbabilidade(crpp.getProbabilidade() + 1);
				}
			}
		}		
	}

	protected void inicializarClubeProbabilidade(Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades,
			List<Clube> clubes, Semana semana, Campeonato campeonato, TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {
		for (Clube clube : clubes) {
			CampeonatoClubeProbabilidade clup = new CampeonatoClubeProbabilidade();
			
			clup.setClube(clube);
			clup.setCampeonato(campeonato);
			clup.setSemana(semana);
			clup.setTipoClubeProbabilidade(tipoClubeProbabilidade);

			clup.setClubeProbabilidadePosicao(new HashMap<Integer, CampeonatoClubeProbabilidadePosicao>());
			
			clup.setClubeProbabilidadePosicaoGeral(new HashMap<Integer, ClubeRankingPosicaoProbabilidade>());
			
			clubeProbabilidades.put(clube, clup);
		}
	}

	protected void calcularProbabilidadesEspecificas(Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades, Semana semana, Integer numeroRebaixados) {

		CampeonatoClubeProbabilidadePosicao cpp = null;

		ClubeRankingPosicaoProbabilidade crpp = null;
		
		for (CampeonatoClubeProbabilidade cp : clubeProbabilidades.values()) {
			
			//Campeao
			cpp = cp.getClubeProbabilidadePosicao().get(1);
			if (cpp != null) {
				cp.setProbabilidadeCampeao(cpp.getProbabilidade().doubleValue()/getNumeroSimulacoes(semana));
				//cp.setQtdeCampeao(cpp.getProbabilidade());
			}
			
			//Rebaixamento
			if (cp.getCampeonato().getNivelCampeonato().isNacional()) {
				Integer probabilidadeRebaixamento = 0;
				for (int i = Constantes.NRO_CLUBE_CAMP_NACIONAL; i > (Constantes.NRO_CLUBE_CAMP_NACIONAL - numeroRebaixados); i-- ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeRebaixamento += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeRebaixamento(probabilidadeRebaixamento.doubleValue()/getNumeroSimulacoes(semana));
				//cp.setQtdeRebaixamento(probabilidadeRebaixamento);
			}
			
			//Acesso
			if (cp.getCampeonato().getNivelCampeonato().isNacionalII()) {
				Integer probabilidadeAcesso = 0;
				for (int i = 1; i <= numeroRebaixados; i++ ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeAcesso += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeAcesso(probabilidadeAcesso.doubleValue()/getNumeroSimulacoes(semana));
				//cp.setQtdeAcesso(probabilidadeAcesso);
			}
			
			if (semana.getNumero() >= 22) {
				//CI
				Integer probabilidadeClassificacaoCI = 0;
				for (int i = getPosicoesClassificamCIMin(); i <= getPosicoesClassificamCIMax(); i++ ) {
					crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
					if (crpp != null) {
						probabilidadeClassificacaoCI += crpp.getProbabilidade();
					}
				}
				//cp.setQtdeClassificacaoCI(probabilidadeClassificacaoCI);
				cp.setProbabilidadeClassificacaoCI(probabilidadeClassificacaoCI.doubleValue()/getNumeroSimulacoes(semana));
	
				//CII
				Integer probabilidadeClassificacaoCII = 0;
				for (int i = getPosicoesClassificamCIIMin(); i <= getPosicoesClassificamCIIMax(); i++ ) {
					crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
					if (crpp != null) {
						probabilidadeClassificacaoCII += crpp.getProbabilidade();
					}
				}
				//cp.setQtdeClassificacaoCII(probabilidadeClassificacaoCII);
				cp.setProbabilidadeClassificacaoCII(probabilidadeClassificacaoCII.doubleValue()/getNumeroSimulacoes(semana));
	
				//CIII
				Integer probabilidadeClassificacaoCIII = 0;
				for (int i = getPosicoesClassificamCIIIMin(semana.getTemporada().getJogo()); i <= getPosicoesClassificamCIIIMax(semana.getTemporada().getJogo()); i++ ) {
					crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
					if (crpp != null) {
						probabilidadeClassificacaoCIII += crpp.getProbabilidade();
					}
				}
				//cp.setQtdeClassificacaoCIII(probabilidadeClassificacaoCIII);
				cp.setProbabilidadeClassificacaoCIII(probabilidadeClassificacaoCIII.doubleValue()/getNumeroSimulacoes(semana));
	
				//CNI
				Integer probabilidadeClassificacaoCNI = 0;
				for (int i = getPosicoesClassificamCNIMin(); i <= getPosicoesClassificamCNIMax(semana.getTemporada().getJogo()); i++ ) {
					crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
					if (crpp != null) {
						probabilidadeClassificacaoCNI += crpp.getProbabilidade();
					}
				}
				//cp.setQtdeClassificacaoCNI(probabilidadeClassificacaoCNI);
				cp.setProbabilidadeClassificacaoCNI(probabilidadeClassificacaoCNI.doubleValue()/getNumeroSimulacoes(semana));
			}

		}

	}

	protected void jogarRodada(Rodada r, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades,
			Map<PartidaResultado, PartidaProbabilidadeResultado> partidaProbabilidade) {

		for (PartidaResultado p : r.getPartidas()) {
			if (!p.getPartidaJogada()) {
				jogarPartida(p, classificacaoProbabilidades, partidaProbabilidade != null ? partidaProbabilidade.get(p) : null);
			}
		}

	}
	
	protected Collection<CampeonatoClubeProbabilidade> calcularClubeProbabilidade(Semana semana, Campeonato nacional,
			Campeonato nacionalII, Map<PartidaResultado, PartidaProbabilidadeResultado> partidaProbabilidade,
			TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade, Map<Integer, Clube> clubesCampeoes,
			Integer numeroRebaixados) {
		
		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();
		
		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitDuration().toMillis();
		
		Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades = new HashMap<Clube, CampeonatoClubeProbabilidade>();
		
		List<Clube> clubesLiga = nacional.getClassificacao().stream().map(c -> c.getClube()).collect(Collectors.toList());
		
		List<Clube> clubesLigaII = nacionalII.getClassificacao().stream().map(c -> c.getClube()).collect(Collectors.toList());
		
		inicializarClubeProbabilidade(clubeProbabilidades, clubesLiga, semana, nacional, tipoClubeProbabilidade);

		inicializarClubeProbabilidade(clubeProbabilidades, clubesLigaII, semana, nacionalII, tipoClubeProbabilidade);
		
		clubesLiga.addAll(clubesLigaII);
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#carregar:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
		
		//Map<Integer, Clube> clubesCampeoes = consultarClubeCampeaoService.getCampeoes(semana.getTemporada(), nacional.getLiga());

		for (int i = 0; i < getNumeroSimulacoes(semana); i++) {

			//NI
			Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidadesI = nacional.getClassificacao().stream()
					.map(c -> ClassificacaoProbabilidade.criar(c))
					.collect(Collectors.toMap(ClassificacaoProbabilidade::getClube, Function.identity()));
		
		
			for (int r = nacional.getRodadaAtual(); r < nacional.getRodadas().size(); r++) {
				jogarRodada(nacional.getRodadas().get(r), classificacaoProbabilidadesI, partidaProbabilidade);
			}
			
			List<ClassificacaoProbabilidade> classificacaoProbabilidadesListI = new ArrayList<ClassificacaoProbabilidade>(
					classificacaoProbabilidadesI.values());
			
			ClassificacaoProbabilidadeUtil.ordernarClassificacao(classificacaoProbabilidadesListI, true);
			
			agruparClubeProbabilidade(clubeProbabilidades, classificacaoProbabilidadesListI);
			
			//NII
			Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidadesII = nacionalII.getClassificacao().stream()
					.map(c -> ClassificacaoProbabilidade.criar(c))
					.collect(Collectors.toMap(ClassificacaoProbabilidade::getClube, Function.identity()));
		
		
			for (int r = nacionalII.getRodadaAtual(); r < nacionalII.getRodadas().size(); r++) {
				jogarRodada(nacionalII.getRodadas().get(r), classificacaoProbabilidadesII, partidaProbabilidade);
			}
			
			List<ClassificacaoProbabilidade> classificacaoProbabilidadesListII = new ArrayList<ClassificacaoProbabilidade>(
					classificacaoProbabilidadesII.values());
			
			ClassificacaoProbabilidadeUtil.ordernarClassificacao(classificacaoProbabilidadesListII, true);

			agruparClubeProbabilidade(clubeProbabilidades, classificacaoProbabilidadesListII);
			
			//Rankear
			List<ClubeRankingProbabilidade> ranking = ClubeRankingProbabilidadeUtil.rankearClubesTemporada(
					clubesLiga, classificacaoProbabilidadesListI, classificacaoProbabilidadesListII, clubesCampeoes);
			
			agruparClubeRankingProbabilidade(clubeProbabilidades, ranking);
			
		}
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#simulacao:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		calcularProbabilidadesEspecificas(clubeProbabilidades, semana, numeroRebaixados);
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#calcularProbabilidadesEspecificas:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
		
		//Finalizar
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getDuration().toMillis());//Tempo total
		
		//System.err.println(mensagens);
		
		//if (imprimir) printClubeProbabilidade(clubeProbabilidades.values());

		return clubeProbabilidades.values();

	}
}
