package com.fastfoot.match.model.factory;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaImpl;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.match.model.JogadorApoioCriacao;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;

public class EsquemaFactoryImpl extends EsquemaFactory {
	
	private static final EsquemaFactory INSTANCE = new EsquemaFactoryImpl();
	
	private EsquemaFactoryImpl() {

	}
	
	public static EsquemaFactory getInstance() {
		return INSTANCE;//TODO: melhorar
	}
	
	public EscalacaoPosicao[] getEscalacaoPosicaoByJogadorApoioToLinha3(JogadorApoioCriacao jogadorApoioCriacao, Boolean mandante) {
		
		EscalacaoPosicao[] posicoes = null;
		
		if (mandante) {
			
			if (JogadorApoioCriacao.LATERAL_DIREITO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_VD, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_LE};
			} else if (JogadorApoioCriacao.LATERAL_DIREITO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_VD, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_LE};
			} else if (JogadorApoioCriacao.LATERAL_ESQUERDO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LD, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_VE};
			} else if (JogadorApoioCriacao.LATERAL_ESQUERDO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LD, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_VE};
			} else if (JogadorApoioCriacao.VOLANTE_DIREITO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LD, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_LE};
			} else if (JogadorApoioCriacao.VOLANTE_DIREITO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LD, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_LE};
			} else if (JogadorApoioCriacao.VOLANTE_ESQUERDO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LD, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_LE};
			} else if (JogadorApoioCriacao.VOLANTE_ESQUERDO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LD, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_LE};
			}
			
		} else {
			
			if (JogadorApoioCriacao.LATERAL_DIREITO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] { EscalacaoPosicao.P_ME, EscalacaoPosicao.P_MD, EscalacaoPosicao.P_LD};
			} else if (JogadorApoioCriacao.LATERAL_DIREITO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] { EscalacaoPosicao.P_ME, EscalacaoPosicao.P_LD, EscalacaoPosicao.P_MD};
			} else if (JogadorApoioCriacao.LATERAL_ESQUERDO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] { EscalacaoPosicao.P_LE, EscalacaoPosicao.P_ME, EscalacaoPosicao.P_MD};
			} else if (JogadorApoioCriacao.LATERAL_ESQUERDO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] { EscalacaoPosicao.P_ME, EscalacaoPosicao.P_LE, EscalacaoPosicao.P_MD};
			} else if (JogadorApoioCriacao.VOLANTE_DIREITO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] { EscalacaoPosicao.P_ME, EscalacaoPosicao.P_MD, EscalacaoPosicao.P_VD};
			} else if (JogadorApoioCriacao.VOLANTE_DIREITO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] { EscalacaoPosicao.P_ME, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_MD};
			} else if (JogadorApoioCriacao.VOLANTE_ESQUERDO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] { EscalacaoPosicao.P_VE, EscalacaoPosicao.P_ME, EscalacaoPosicao.P_MD};
			} else if (JogadorApoioCriacao.VOLANTE_ESQUERDO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] { EscalacaoPosicao.P_ME, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_MD};
			}
			
		}

		return posicoes;
	}
	
	public EscalacaoPosicao[] getEscalacaoPosicaoByJogadorApoioToLinha4(JogadorApoioCriacao jogadorApoioCriacao, Boolean mandante) {
		
		EscalacaoPosicao[] posicoes = null;
		
		if (mandante) {
			
			if (JogadorApoioCriacao.LATERAL_DIREITO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LD, EscalacaoPosicao.P_MD, EscalacaoPosicao.P_ME};
			} else if (JogadorApoioCriacao.LATERAL_DIREITO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_MD, EscalacaoPosicao.P_LD, EscalacaoPosicao.P_ME};
			} else if (JogadorApoioCriacao.LATERAL_ESQUERDO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_MD, EscalacaoPosicao.P_ME, EscalacaoPosicao.P_LE};
			} else if (JogadorApoioCriacao.LATERAL_ESQUERDO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_MD, EscalacaoPosicao.P_LE, EscalacaoPosicao.P_ME};
			} else if (JogadorApoioCriacao.VOLANTE_DIREITO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_VD, EscalacaoPosicao.P_MD, EscalacaoPosicao.P_ME};
			} else if (JogadorApoioCriacao.VOLANTE_DIREITO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_MD, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_ME};
			} else if (JogadorApoioCriacao.VOLANTE_ESQUERDO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_MD, EscalacaoPosicao.P_ME, EscalacaoPosicao.P_VE};
			} else if (JogadorApoioCriacao.VOLANTE_ESQUERDO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_MD, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_ME};
			}
			
		} else {
			
			if (JogadorApoioCriacao.LATERAL_DIREITO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LE, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_VD};
			} else if (JogadorApoioCriacao.LATERAL_DIREITO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LE, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_VD};
			} else if (JogadorApoioCriacao.LATERAL_ESQUERDO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_VE, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_LD};
			} else if (JogadorApoioCriacao.LATERAL_ESQUERDO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_VE, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_LD};
			} else if (JogadorApoioCriacao.VOLANTE_DIREITO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LE, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_LD};
			} else if (JogadorApoioCriacao.VOLANTE_DIREITO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LE, EscalacaoPosicao.P_VE, EscalacaoPosicao.P_LD};
			} else if (JogadorApoioCriacao.VOLANTE_ESQUERDO_PELO_LADO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LE, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_LD};
			} else if (JogadorApoioCriacao.VOLANTE_ESQUERDO_PELO_MEIO.equals(jogadorApoioCriacao)) {
				posicoes = new EscalacaoPosicao[] {EscalacaoPosicao.P_LE, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_LD};
			}

		}

		return posicoes;
	}

	
	/*
	*--------------GOV--------------* --> Goleiro Visitante
	*-------------------------------*
	*-------ATE-----------ATD-------* --> Ataque
	*-------------------------------*
	*----MOE-------MOC-------MOD----* --> Meia Ofensivo
	*-------------------------------*
	*----MDE-------MDC-------MDD----* --> Meia Defensivo
	*-------------------------------*
	*-------DEE-----------DED-------* --> Defesa
	*-------------------------------*
	*--------------GOM--------------* --> Goleiro Mandante
	 */

	@Override
	public Esquema gerarEsquemaEscalacao(EscalacaoClube mandantes, EscalacaoClube visitantes,
			JogadorApoioCriacao jogadorApoioCriacaoMandante, JogadorApoioCriacao jogadorApoioCriacaoVisitante) {
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> mandantesMap = mandantes.getListEscalacaoJogadorPosicao()
				.stream().collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));

		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> visitantesMap = visitantes.getListEscalacaoJogadorPosicao()
				.stream().collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));

		EsquemaImpl esquema = new EsquemaImpl();

		int i = 1;
		
		//Goleiro Mandante
		EsquemaPosicao gom = new EsquemaPosicao();
		gom.setNumero(i++);
		gom.setGoleiro(mandantesMap.get(EscalacaoPosicao.P_GOL).getJogador());
		
		//#2 Linha
		EsquemaPosicao ded = new EsquemaPosicao();
		ded.setNumero(i++);
		ded.setMandante(mandantesMap.get(EscalacaoPosicao.P_ZD).getJogador());
		ded.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_2);
		ded.setVisitante(visitantesMap.get(EscalacaoPosicao.P_AE).getJogador());
		ded.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_5);
		
		
		EsquemaPosicao dee = new EsquemaPosicao();
		dee.setNumero(i++);
		dee.setMandante(mandantesMap.get(EscalacaoPosicao.P_ZE).getJogador());
		dee.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_2);
		dee.setVisitante(visitantesMap.get(EscalacaoPosicao.P_AD).getJogador());
		dee.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_5);
		
		//#3 Linha
		
		EscalacaoPosicao[] escalacaoPosicaoMandante = getEscalacaoPosicaoByJogadorApoioToLinha3(jogadorApoioCriacaoMandante, true);
		EscalacaoPosicao[] escalacaoPosicaoVisitante = getEscalacaoPosicaoByJogadorApoioToLinha3(jogadorApoioCriacaoVisitante, false);
		
		EsquemaPosicao mdd = new EsquemaPosicao();
		mdd.setNumero(i++);
		mdd.setMandante(mandantesMap.get(escalacaoPosicaoMandante[0]).getJogador());
		mdd.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_3);
		mdd.setVisitante(visitantesMap.get(escalacaoPosicaoVisitante[0]).getJogador());
		mdd.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_4);
		
		EsquemaPosicao mdc = new EsquemaPosicao();
		mdc.setNumero(i++);
		mdc.setMandante(mandantesMap.get(escalacaoPosicaoMandante[1]).getJogador());
		mdc.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_3);
		mdc.setVisitante(visitantesMap.get(escalacaoPosicaoVisitante[1]).getJogador());
		mdc.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_4);
		
		EsquemaPosicao mde = new EsquemaPosicao();
		mde.setNumero(i++);
		mde.setMandante(mandantesMap.get(escalacaoPosicaoMandante[2]).getJogador());
		mde.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_3);
		mde.setVisitante(visitantesMap.get(escalacaoPosicaoVisitante[2]).getJogador());
		mde.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_4);
		
		//#4 Linha
		
		EscalacaoPosicao[] escalacaoPosicaoMandanteL4 = getEscalacaoPosicaoByJogadorApoioToLinha4(jogadorApoioCriacaoMandante, true);
		EscalacaoPosicao[] escalacaoPosicaoVisitanteL4 = getEscalacaoPosicaoByJogadorApoioToLinha4(jogadorApoioCriacaoVisitante, false);
		
		EsquemaPosicao mod = new EsquemaPosicao();
		mod.setNumero(i++);
		mod.setMandante(mandantesMap.get(escalacaoPosicaoMandanteL4[0]).getJogador());
		mod.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_4);
		mod.setVisitante(visitantesMap.get(escalacaoPosicaoVisitanteL4[0]).getJogador());
		mod.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_3);
		
		EsquemaPosicao moc = new EsquemaPosicao();
		moc.setNumero(i++);
		moc.setMandante(mandantesMap.get(escalacaoPosicaoMandanteL4[1]).getJogador());
		moc.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_4);
		moc.setVisitante(visitantesMap.get(escalacaoPosicaoVisitanteL4[1]).getJogador());
		moc.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_3);
		
		EsquemaPosicao moe = new EsquemaPosicao();
		moe.setNumero(i++);
		moe.setMandante(mandantesMap.get(escalacaoPosicaoMandanteL4[2]).getJogador());
		moe.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_4);
		moe.setVisitante(visitantesMap.get(escalacaoPosicaoVisitanteL4[2]).getJogador());
		moe.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_3);
		
		//#5 Linha
		EsquemaPosicao atd = new EsquemaPosicao();
		atd.setNumero(i++);
		atd.setMandante(mandantesMap.get(EscalacaoPosicao.P_AD).getJogador());
		atd.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_5);
		atd.setVisitante(visitantesMap.get(EscalacaoPosicao.P_ZE).getJogador());
		atd.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_2);
		
		
		EsquemaPosicao ate = new EsquemaPosicao();
		ate.setNumero(i++);
		ate.setMandante(mandantesMap.get(EscalacaoPosicao.P_AE).getJogador());
		ate.setProbabilidadeArremateForaMandante(PROBABILIDADE_ARREMATE_FORA_LINHA_5);
		ate.setVisitante(visitantesMap.get(EscalacaoPosicao.P_ZD).getJogador());
		ate.setProbabilidadeArremateForaVisitante(PROBABILIDADE_ARREMATE_FORA_LINHA_2);

		//Goleiro Visitante
		EsquemaPosicao gov = new EsquemaPosicao();
		gov.setNumero(i++);
		gov.setGoleiro(visitantesMap.get(EscalacaoPosicao.P_GOL).getJogador());

		addTransicaoMandante(ded, dee, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(mdd, mdc, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(mdc, mde, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(mod, moc, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(moc, moe, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(atd, ate, PESO_LATERAL, PESO_LATERAL);

		addTransicaoMandante(ded, mdd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ded, mdc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ded, mde, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoMandante(dee, mdd, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoMandante(dee, mdc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(dee, mde, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoMandante(mdd, mod, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(mdd, moc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(mdd, moe, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoMandante(mdc, mod, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(mdc, moc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(mdc, moe, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(mde, moc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(mde, moe, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(mde, mod, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		
		addTransicaoMandante(mod, atd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(mod, ate, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoMandante(moc, atd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(moc, ate, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(moe, atd, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoMandante(moe, ate, PESO_AVANCAR, PESO_RECUAR);
		


		//------------------

		addTransicaoVisitante(dee, ded, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(mdc, mdd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(mde, mdc, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(moc, mod, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(moe, moc, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ate, atd, PESO_LATERAL, PESO_LATERAL);

		addTransicaoVisitante(mdd, ded, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(mdc, ded, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(mde, ded, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoVisitante(mdd, dee, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoVisitante(mdc, dee, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(mde, dee, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoVisitante(mod, mdd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(moc, mdd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(moe, mdd, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoVisitante(mod, mdc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(moc, mdc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(moe, mdc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(moc, mde, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(moe, mde, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(mod, mde, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);

		addTransicaoVisitante(atd, mod, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ate, mod, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoVisitante(atd, moc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ate, moc, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(atd, moe, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoVisitante(ate, moe, PESO_AVANCAR, PESO_RECUAR);


		esquema.setPosicoes(Arrays.asList(gom, ded, dee, mdd, mdc, mde, mod, moc, moe, atd, ate, gov));
		
		esquema.setPosicaoAtual(ded);
		
		esquema.setPosseBolaMandante(true);
		
		esquema.setGoleiroMandante(gom);
		
		esquema.setGoleiroVisitante(gov);
		
		//print(Arrays.asList(ded, dee, mdd, mdc, mde, mod, moc, moe, atd, ate));
		
		esquema.setEscalacaoClubeMandante(mandantes);
		esquema.setEscalacaoClubeVisitante(visitantes);

		return esquema;
	}

}
