package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaTransicao;
import com.fastfoot.match.model.JogadorApoioCriacao;
import com.fastfoot.match.model.PartidaJogadorEstatisticaDTO;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.factory.EsquemaFactoryImpl;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticaSemana;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class JogarPartidaService {
	
	/*
	 * TODO:
	 * 
	 * Inserir FALTA (estilo HabilidadeValor(Habilidade.FORA) na roleta) e CARTOES
	 * Inserir ERRO (estilo HabilidadeValor(Habilidade.FORA) na roleta)
	 * Substituicoes
	 * 
	 */

	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private DisputarPenaltsService disputarPenaltsService;
	
	/*@Autowired
	private ParametroService parametroService;*/
	
	@Autowired
	private EscalarClubeService escalarClubeService;

	private static final Double NUM_LANCES_POR_MINUTO = 1d;
	
	/*private static final Double PESO_FORCA_GERAL = 1.1d;*/
	
	private static final Integer MINUTOS = 90;

	private static final float MIN_FORA = 0.2f;

	private static final Boolean IMPRIMIR = false;
	
	private static final Boolean LANCE_A_LANCE = true;

	private PartidaLance criarPartidaLance(List<PartidaLance> lances, PartidaResultadoJogavel partida, Jogador jogador,
			Habilidade habilidade, Boolean vencedor, Integer ordem, Boolean acao) {
		PartidaLance pl = new PartidaLance();
		if (partida instanceof PartidaResultado) {
			pl.setPartidaResultado((PartidaResultado) partida);
		} else if (partida instanceof PartidaAmistosaResultado) {
			pl.setPartidaAmistosaResultado((PartidaAmistosaResultado) partida);
		} else if (partida instanceof PartidaEliminatoriaResultado) {
			pl.setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partida);
		}
		pl.setJogador(jogador);
		pl.setVencedor(vencedor);
		pl.setHabilidadeUsada(habilidade);
		pl.setOrdem(ordem);
		pl.setAcao(acao);
		lances.add(pl);
		return pl;
	}
	
	private void atualizarEstatisticasHabilidade(HabilidadeValor habilidadeValor, Boolean vencedor) {
		habilidadeValor.getHabilidadeValorEstatistica().incrementarQuantidadeUso();
		if (vencedor) {
			habilidadeValor.getHabilidadeValorEstatistica().incrementarQuantidadeUsoVencedor();
		}
	}

	private void inicializarEstatisticas(List<Jogador> jogadores, Semana semana, PartidaResultadoJogavel partidaResultado) {
		for (Jogador j : jogadores) {
			for (HabilidadeValor hv : j.getHabilidades()) {
				hv.setHabilidadeValorEstatistica(new HabilidadeValorEstatistica(hv, semana, partidaResultado.isAmistoso()/*, partidaResultado*/));
			}
		}
	}
	
	private void salvarEstatisticas(List<Jogador> jogadores, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		List<HabilidadeValorEstatistica> estatisticas = new ArrayList<HabilidadeValorEstatistica>();
		
		for (Jogador j : jogadores) {
			//estatisticas.addAll(j.getHabilidades().stream().map(hv -> hv.getHabilidadeValorEstatistica()).collect(Collectors.toList()));
			estatisticas.addAll(j.getHabilidades().stream().map(hv -> hv.getHabilidadeValorEstatistica())
					.filter(hve -> hve.getQuantidadeUso() > 0).collect(Collectors.toList()));
		}

		//partidaJogadorEstatisticaDTO.adicionarHabilidadeValorEstatistica(estatisticas.stream().filter(hve -> hve.getQuantidadeUso() > 0).collect(Collectors.toList()));
		
		partidaJogadorEstatisticaDTO.adicionarHabilidadeValorEstatistica(estatisticas);
	}

	/*public void jogar(PartidaResultadoJogavel partidaResultado, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		List<EscalacaoJogadorPosicao> escalacaoMandante = escalacaoJogadorPosicaoRepository
				.findByClubeAndAtivoFetchJogadorHabilidades(partidaResultado.getClubeMandante(), true);

		List<EscalacaoJogadorPosicao> escalacaoVisitante = escalacaoJogadorPosicaoRepository
				.findByClubeAndAtivoFetchJogadorHabilidades(partidaResultado.getClubeVisitante(), true);

		List<Jogador> jogadoresMandante = escalacaoMandante.stream().map(e -> e.getJogador()).collect(Collectors.toList());
		List<Jogador> jogadoresVisitante = escalacaoVisitante.stream().map(e -> e.getJogador()).collect(Collectors.toList());

		inicializarEstatisticasJogador(escalacaoMandante, partidaResultado.getRodada().getSemana().getTemporada(), partidaResultado);
		inicializarEstatisticasJogador(escalacaoVisitante, partidaResultado.getRodada().getSemana().getTemporada(), partidaResultado);
		
		inicializarEstatisticas(jogadoresMandante, partidaResultado.getRodada().getSemana(), partidaResultado);
		inicializarEstatisticas(jogadoresVisitante, partidaResultado.getRodada().getSemana(), partidaResultado);

		Esquema esquema = getEsquemaFactory().gerarEsquemaEscalacao(escalacaoMandante, escalacaoVisitante);
		
		partidaResultado.setPartidaEstatisticas(new PartidaEstatisticas());
		jogar(esquema, partidaResultado);
		
		if (partidaResultado.isDisputarPenalts() && partidaResultado.isResultadoEmpatado()) {
			disputarPenaltsService.disputarPenalts(partidaResultado, esquema);
		}
		
		salvarEstatisticas(jogadoresMandante, partidaJogadorEstatisticaDTO);
		salvarEstatisticas(jogadoresVisitante, partidaJogadorEstatisticaDTO);
		salvarEstatisticasJogador(jogadoresMandante, partidaJogadorEstatisticaDTO, partidaResultado);
		salvarEstatisticasJogador(jogadoresVisitante, partidaJogadorEstatisticaDTO, partidaResultado);
	}*/
	
	/*private EsquemaFactory getEsquemaFactory() {

		EsquemaFactory factory = null;
		String formacao = parametroService.getParametroString(ParametroConstantes.ESCALACAO_PADRAO);

		if (ParametroConstantes.ESCALACAO_PADRAO_PARAM_41212.equals(formacao)) {
			factory = new EsquemaFactoryImplQuatroUmDoisUmDois();
		} else if (ParametroConstantes.ESCALACAO_PADRAO_PARAM_4222.equals(formacao)) {
			factory = new EsquemaFactoryImplQuatroDoisDoisDois();
		} else if (ParametroConstantes.ESCALACAO_PADRAO_PARAM_4132.equals(formacao)) {
			factory = new EsquemaFactoryImplDoisTresTresDois();
		}

		return factory;
	}*/
	
	public void jogar(PartidaResultadoJogavel partidaResultado, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		
		List<Jogador> jogadoresMandante = jogadorRepository
				.findByClubeAndStatusJogadorFetchHabilidades(partidaResultado.getClubeMandante(), StatusJogador.ATIVO);
		List<Jogador> jogadoresVisitante = jogadorRepository
				.findByClubeAndStatusJogadorFetchHabilidades(partidaResultado.getClubeVisitante(), StatusJogador.ATIVO);

		List<EscalacaoJogadorPosicao> escalacaoMandante = escalarClubeService
				.gerarEscalacaoInicial(partidaResultado.getClubeMandante(), jogadoresMandante);
		List<EscalacaoJogadorPosicao> escalacaoVisitante = escalarClubeService
				.gerarEscalacaoInicial(partidaResultado.getClubeVisitante(), jogadoresVisitante);

		//inicializarEstatisticasJogador(escalacaoMandante, partidaResultado.getRodada().getSemana().getTemporada(), partidaResultado);
		//inicializarEstatisticasJogador(escalacaoVisitante, partidaResultado.getRodada().getSemana().getTemporada(), partidaResultado);
		inicializarEstatisticasJogador2(escalacaoMandante, partidaResultado.getRodada().getSemana(), partidaResultado);
		inicializarEstatisticasJogador2(escalacaoVisitante, partidaResultado.getRodada().getSemana(), partidaResultado);
		
		inicializarEstatisticas(jogadoresMandante, partidaResultado.getRodada().getSemana(), partidaResultado);
		inicializarEstatisticas(jogadoresVisitante, partidaResultado.getRodada().getSemana(), partidaResultado);

		Esquema esquema = EsquemaFactoryImpl.getInstance().gerarEsquemaEscalacao(escalacaoMandante, escalacaoVisitante,
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()),
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()));
		
		partidaResultado.setPartidaEstatisticas(new PartidaEstatisticas());
		jogar(esquema, partidaResultado);
		
		if (partidaResultado.isDisputarPenalts() && partidaResultado.isResultadoEmpatado()) {
			disputarPenaltsService.disputarPenalts(partidaResultado, esquema);
		}
		
		salvarEstatisticas(jogadoresMandante, partidaJogadorEstatisticaDTO);
		salvarEstatisticas(jogadoresVisitante, partidaJogadorEstatisticaDTO);
		salvarEstatisticasJogador2(jogadoresMandante, partidaJogadorEstatisticaDTO);
		salvarEstatisticasJogador2(jogadoresVisitante, partidaJogadorEstatisticaDTO);
	}
	
	private void inicializarEstatisticasJogador2(List<EscalacaoJogadorPosicao> escalacao, Semana semana,
			PartidaResultadoJogavel partidaResultado) {
		// TODO: apenas titular e substituicoes

		if (!partidaResultado.isAmistoso()) {// Partidas Oficiais

			escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
					.forEach(j -> j
							.setJogadorEstatisticaSemana(new JogadorEstatisticaSemana(j, semana, j.getClube(), partidaResultado, false)));

		} else {

			escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
					.forEach(j -> j
							.setJogadorEstatisticaSemana(new JogadorEstatisticaSemana(j, semana, j.getClube(), null, true)));

		}

		escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
				.forEach(j -> j.getJogadorEstatisticaSemana().setNumeroJogos(1));

		escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
				.forEach(j -> j.getJogadorEstatisticaSemana().setNumeroMinutosJogados(90)); // TODO: implementar logica substituicao
	}
	
	/*@Deprecated
	private void inicializarEstatisticasJogador(List<EscalacaoJogadorPosicao> escalacao, Temporada temporada, PartidaResultadoJogavel partidaResultado) {
		//TODO: apenas titular e substituicoes
		
		if (!partidaResultado.isAmistoso()) {//Partidas Oficiais
			/*jogadores.stream().forEach(j -> j.getJogadorEstatisticasTemporadaAtual()
					.setNumeroJogos(j.getJogadorEstatisticasTemporadaAtual().getNumeroJogos() + 1));* /

			escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
					.forEach(j -> j.getJogadorEstatisticasTemporadaAtual()
							.setNumeroJogos(j.getJogadorEstatisticasTemporadaAtual().getNumeroJogos() + 1));
			
			escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
					.forEach(j -> j.getJogadorEstatisticasTemporadaAtual().setNumeroMinutosJogados(
							j.getJogadorEstatisticasTemporadaAtual().getNumeroMinutosJogados() + 90));//TODO: implementar logica substituicao

		} else {
			escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
					.forEach(j -> j.getJogadorEstatisticasAmistososTemporadaAtual()
							.setNumeroJogos(j.getJogadorEstatisticasAmistososTemporadaAtual().getNumeroJogos() + 1));
			
			escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
					.forEach(j -> j.getJogadorEstatisticasAmistososTemporadaAtual().setNumeroMinutosJogados(
							j.getJogadorEstatisticasAmistososTemporadaAtual().getNumeroMinutosJogados() + 90));//TODO: implementar logica substituicao
		}
	}*/
	
	/*@Deprecated
	private void salvarEstatisticasJogador(List<Jogador> jogadores,
			PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO, PartidaResultadoJogavel partidaResultado) {
		//TODO: salvar so de jogadores que jogaram?
		if (!partidaResultado.isAmistoso()) {
			partidaJogadorEstatisticaDTO.adicionarJogadorEstatisticasTemporada(
					jogadores.stream().map(Jogador::getJogadorEstatisticasTemporadaAtual).collect(Collectors.toList()));
		} else {
			partidaJogadorEstatisticaDTO.adicionarJogadorEstatisticasTemporada(jogadores.stream()
					.map(Jogador::getJogadorEstatisticasAmistososTemporadaAtual).collect(Collectors.toList()));
		}
	}*/
	
	private void salvarEstatisticasJogador2(List<Jogador> jogadores,
			PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		// TODO: salvar so de jogadores que jogaram?
		partidaJogadorEstatisticaDTO.adicionarJogadorEstatisticaSemana(
				jogadores.stream().filter(j -> j.getJogadorEstatisticaSemana() != null)
						.map(Jogador::getJogadorEstatisticaSemana).collect(Collectors.toList()));
	}

	private void jogar(Esquema esquema, PartidaResultadoJogavel partidaResultado) {

		List<PartidaLance> lances = new ArrayList<PartidaLance>();
		Integer ordemJogada = 1;
		Integer golMandante = 0, golVisitante = 0;

		HabilidadeValor habilidadeVencedorAnterior = null;
		HabilidadeValor habilidadeValorAcao = null;
		HabilidadeValor habilidadeValorReacao = null;
		HabilidadeValor habilidadeFora = null;
		HabilidadeValor habilidadeVencedora = null;
		
		Jogador jogadorAssistencia = null;
		
		Boolean jogadorAcaoVenceu = null, goleiroVenceu = null;
		
		for (int i = 0; i < (NUM_LANCES_POR_MINUTO * MINUTOS); i++) {
			
			do {
			
				if (habilidadeVencedorAnterior != null && habilidadeVencedorAnterior.getHabilidadeAcao().contemAcoesSubsequentes()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidades(
									habilidadeVencedorAnterior.getHabilidadeAcao().getAcoesSubsequentes()));
				} else {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesAcaoMeioFimJogadorPosicaoAtualPosse());
				}
				
				habilidadeValorReacao = (HabilidadeValor) RoletaUtil
						.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesJogadorPosicaoAtualSemPosse(
								habilidadeValorAcao.getHabilidadeAcao().getPossiveisReacoes()));
				
				jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedorN(habilidadeValorAcao, habilidadeValorReacao);

				if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidade(), jogadorAcaoVenceu, ordemJogada, true);
				atualizarEstatisticasHabilidade(habilidadeValorAcao, jogadorAcaoVenceu);
				if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorSemPosse(), habilidadeValorReacao.getHabilidade(), !jogadorAcaoVenceu, ordemJogada, false);
				atualizarEstatisticasHabilidade(habilidadeValorReacao, !jogadorAcaoVenceu);
				ordemJogada++;
				
				partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				
				//
				if (IMPRIMIR) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
				//
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			
			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeAcao().isAcaoInicial());//Dominio
			
			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeAcao().isAcaoMeio() || habilidadeValorAcao.getHabilidadeAcao().isAcaoInicioMeio()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesAcaoFimJogadorPosicaoAtualPosse());
					if (IMPRIMIR) System.err.println(String.format("\t\t-> %s", habilidadeValorAcao.getHabilidade().getDescricao()));
					if (!habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()){
						if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidade(), true, ordemJogada, true);
						atualizarEstatisticasHabilidade(habilidadeValorAcao, true);
					}
					ordemJogada++;
					partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				}
				
				if (habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()) {//FINALIZACAO, CABECEIO
					habilidadeValorReacao = (HabilidadeValor) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getGoleiroSemPosse().getGoleiro()
									.getHabilidades(
											Arrays.asList(habilidadeValorAcao.getHabilidadeAcao().getReacaoGoleiro())));
					
					//fora
					//habilidadeFora = new HabilidadeValor(Habilidade.NULL, (habilidadeValorAcao.getValor() + habilidadeValorReacao.getValor())/2);
					habilidadeFora = new HabilidadeValor(Habilidade.FORA, (int) Math.round(Math.max(
							((habilidadeValorAcao.getJogador().getForcaGeral() * esquema.getProbabilidadeArremateForaPosicaoPosse()) - habilidadeValorAcao.getValor()),
							(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral()))));
					//System.err.println(String.format("\t\t\tJ:%d A:%d F:%d", habilidadeValorAcao.getJogador().getForcaGeral(), habilidadeValorAcao.getValor(), habilidadeFora.getValor()));

					//jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedor(habilidadeValorAcao, habilidadeValorReacao);
					habilidadeVencedora = (HabilidadeValor) RoletaUtil.sortearN(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao);
					goleiroVenceu = habilidadeVencedora.equals(habilidadeValorReacao);
					//System.err.println("\t\tFORA!!!!");
					
					//
					if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(),
							habilidadeValorAcao.getHabilidade(), jogadorAcaoVenceu, ordemJogada, true);
					atualizarEstatisticasHabilidade(habilidadeValorAcao, jogadorAcaoVenceu);
					
					if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getGoleiroSemPosse().getGoleiro(),
							habilidadeValorReacao.getHabilidade(), goleiroVenceu, ordemJogada, false);
					atualizarEstatisticasHabilidade(habilidadeValorReacao, goleiroVenceu);
					ordemJogada++;
					
					partidaResultado.incrementarLance(esquema.getPosseBolaMandante());

					//
					if (IMPRIMIR) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
					//
					
					if (jogadorAcaoVenceu) {
						if (IMPRIMIR) System.err.println("\t\tGOLLL");
						if (esquema.getPosseBolaMandante()) {
							golMandante++;
						} else {
							golVisitante++;
						}
						partidaResultado.incrementarGol(esquema.getPosseBolaMandante());
						/*if (!partidaResultado.isAmistoso()) {
							habilidadeValorAcao.getJogador().getJogadorEstatisticaSemana()
									.incrementarGolsMarcados();
							if (jogadorAssistencia != null) {
								jogadorAssistencia.getJogadorEstatisticaSemana().incrementarAssistencias();
							}
							esquema.getGoleiroSemPosse().getGoleiro().getJogadorEstatisticaSemana()
									.incrementarGolsSofridos();
						} else {*/
							habilidadeValorAcao.getJogador().getJogadorEstatisticaSemana()
									.incrementarGolsMarcados();
							if (jogadorAssistencia != null) {
								jogadorAssistencia.getJogadorEstatisticaSemana()
										.incrementarAssistencias();
							}
							esquema.getGoleiroSemPosse().getGoleiro().getJogadorEstatisticaSemana()
									.incrementarGolsSofridos();
						//}
					} else if (goleiroVenceu) {
						if (IMPRIMIR) System.err.println("\t\tGOLEIRO DEFENDEU");
						partidaResultado.incrementarFinalizacaoDefendida(esquema.getPosseBolaMandante());
						/*if (!partidaResultado.isAmistoso()) {
							habilidadeValorAcao.getJogador().getJogadorEstatisticaSemana()
									.incrementarFinalizacoesDefendidas();
							esquema.getGoleiroSemPosse().getGoleiro().getJogadorEstatisticaSemana()
									.incrementarGoleiroFinalizacoesDefendidas();
						} else {*/
							habilidadeValorAcao.getJogador().getJogadorEstatisticaSemana()
									.incrementarFinalizacoesDefendidas();
							esquema.getGoleiroSemPosse().getGoleiro().getJogadorEstatisticaSemana()
									.incrementarGoleiroFinalizacoesDefendidas();
						//}
					} else if (habilidadeVencedora.equals(habilidadeFora)) {
						if (IMPRIMIR) System.err.println("\t\tFORA!!!!");
						partidaResultado.incrementarFinalizacaoFora(esquema.getPosseBolaMandante());
						/*if (!partidaResultado.isAmistoso()) {
							habilidadeValorAcao.getJogador().getJogadorEstatisticaSemana()
									.incrementarFinalizacoesFora();
						} else {*/
							habilidadeValorAcao.getJogador().getJogadorEstatisticaSemana()
									.incrementarFinalizacoesFora();
						//}
					}
					esquema.inverterPosse();//TODO: iniciar posse em qual jogador???
					jogadorAssistencia = null;
				} else {
					//PASSE, CRUZAMENTO, ARMACAO
					jogadorAssistencia = esquema.getJogadorPosse();
					EsquemaTransicao t = (EsquemaTransicao) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getTransicoesPosse());
					if (IMPRIMIR) System.err.println(String.format("%d ==> %d (%s)", esquema.getPosicaoAtual().getNumero(), t.getPosFinal().getNumero(), esquema.getPosseBolaMandante() ? "M" : "V"));
					esquema.setPosicaoAtual(t.getPosFinal());
				}
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			} else {
				esquema.inverterPosse();
				if (IMPRIMIR) System.err.println("\t\tPOSSE INVERTIDA");
				//habilidadeVencedorAnterior = habilidadeValorReacao;
				habilidadeVencedorAnterior = null;
				jogadorAssistencia = null;
			}
		}

		//JogadorAgruparGrupoEstatisticasUtil.agruparEstatisticas(lances);

		if (IMPRIMIR) System.err.println(String.format("\n\t\t%d x %d", golMandante, golVisitante));
		
		partidaResultado.setPartidaJogada(true);
	}
	
	//###	TESTE	###
	
	public void print(Esquema esquema, HabilidadeValor habilidadeValorAcao, HabilidadeValor habilidadeValorReacao, boolean jogadorAcaoVenceu) {
		System.err.println(String.format("\t\t%s (%d) x %s (%d) [%s] [%s] [%s]", habilidadeValorAcao.getHabilidade().getDescricao(),
				habilidadeValorAcao.getValor(), habilidadeValorReacao.getHabilidade().getDescricao(),
				habilidadeValorReacao.getValor(), esquema.getPosseBolaMandante() ? "M" : "V",
				jogadorAcaoVenceu ? "Venceu" : "Perdeu", habilidadeValorAcao.getJogador().getNumero()));
	}
}
