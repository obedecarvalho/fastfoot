package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fastfoot.player.model.EstrategiaHabilidadePosicaoJogador;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.HabilidadeTipo;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.entity.Jogador;

public class JogadorFactoryImplDesenRegular extends JogadorFactory {
	
	private static JogadorFactory INSTANCE;
	
	protected JogadorFactoryImplDesenRegular() {

	}
	
	public static JogadorFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new JogadorFactoryImplDesenRegular();
		}
		return INSTANCE;
	}

	@Override
	protected void sortearHabilidadeValor(Jogador jogador, EstrategiaHabilidadePosicaoJogador estrategia, Integer potencial) {
		List<Habilidade> habEspecificas = new ArrayList<Habilidade>();
		List<Habilidade> habComuns = new ArrayList<Habilidade>();
		List<Habilidade> habOutros = new ArrayList<Habilidade>();
		
		List<Double> valorHabilidadesEspecificas = new ArrayList<Double>();
		List<Double> valorHabilidadesEspecificasPot = new ArrayList<Double>();
		List<Double> valorHabilidadesEspecificasPotEfetiva = new ArrayList<Double>();
		
		sortearEletivas(estrategia, habEspecificas, habComuns);
		sortearHabComunsEletivas(estrategia, habComuns, habOutros);
		sortearHabCoringa(estrategia, habEspecificas, habComuns, habOutros);
		
		Double ajusteForca = getAjusteForca(jogador.getJogadorDetalhe().getModoDesenvolvimentoJogador(), jogador.getIdade());
		Double ajusteForcaProx = getAjusteForca(jogador.getJogadorDetalhe().getModoDesenvolvimentoJogador(), jogador.getIdade() + 1);
		Double potencialSorteado = null;
		Double forca = null;
		Double passoProx = null;
		//Double forcaDecimal = null;
		
		//Comuns
		habComuns.addAll(estrategia.getHabilidadesComum());
		for (Habilidade h : habComuns) {
			potencialSorteado = gerarValorHabilidadeComum(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / QTDE_DESENVOLVIMENTO_ANO_JOGADOR;
			//forcaDecimal = forca - forca.intValue();
			addHabilidade(jogador, h, forca.intValue(), forca, HabilidadeTipo.COMUM, potencialSorteado,
					potencialSorteado, passoProx);
		}
		
		//Especificas
		habEspecificas.addAll(estrategia.getHabilidadesEspecificas());
		for (Habilidade h : habEspecificas) {
			potencialSorteado = gerarValorHabilidadeEspecifico(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / QTDE_DESENVOLVIMENTO_ANO_JOGADOR;
			//forcaDecimal = forca - forca.intValue();
			addHabilidade(jogador, h, forca.intValue(), forca, HabilidadeTipo.ESPECIFICA, potencialSorteado,
					potencialSorteado, passoProx);
			
			valorHabilidadesEspecificasPot.add(potencialSorteado);
			valorHabilidadesEspecificas.add(forca);
			valorHabilidadesEspecificasPotEfetiva.add(potencialSorteado);
		}
		jogador.setForcaGeral(
				(new Double(valorHabilidadesEspecificas.stream().mapToDouble(v -> v).average().getAsDouble())).intValue());
		jogador.setForcaGeralPotencial(
				(new Double(valorHabilidadesEspecificasPot.stream().mapToDouble(v -> v).average().getAsDouble()))
						.intValue());
		jogador.setForcaGeralPotencialEfetiva(
				(valorHabilidadesEspecificasPotEfetiva.stream().mapToDouble(v -> v).average().getAsDouble()));
		
		//Outros
		habOutros.addAll(estrategia.getHabilidadesOutros());
		for (Habilidade h : habOutros) {
			potencialSorteado = gerarValorHabilidadeOutros(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / QTDE_DESENVOLVIMENTO_ANO_JOGADOR;
			//forcaDecimal = forca - forca.intValue();
			addHabilidade(jogador, h, forca.intValue(), forca, HabilidadeTipo.OUTRO, potencialSorteado,
					potencialSorteado, passoProx);
		}
	}
	
	public void ajustarPassoDesenvolvimento(Jogador j) {

		Double ajusteForcaProx = JogadorFactory.getAjusteForca(j.getJogadorDetalhe().getModoDesenvolvimentoJogador(),
				j.getIdade() + 1);
		Double passoProx = null;
		
		for (HabilidadeValor hv : j.getHabilidades()) {
			passoProx = ((hv.getPotencialDesenvolvimentoEfetivo() * ajusteForcaProx) - hv.getValorTotal())
					/ JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR;
			hv.setPassoDesenvolvimento(passoProx);
		}
	}

	@Override
	public void ajustarPassoDesenvolvimento(Jogador j, HabilidadeEstatisticaPercentil habilidadeEstatisticaPercentil,
			Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticaGrupoMap) {

		//TODO: Limitar desenvolvimento para não passar do MAXIMO (100)

		Double ajusteForcaProx = JogadorFactory.getAjusteForca(j.getJogadorDetalhe().getModoDesenvolvimentoJogador(),
				j.getIdade() + 1);
		Double passoProx = null, passo = null, peso = null;
		
		Double variacao = null;
		List<Double> valorHabilidadesEspecificasPotEfetiva = new ArrayList<Double>();
		
		for (HabilidadeValor hv : j.getHabilidades()) {
			passoProx = ((hv.getPotencialDesenvolvimentoEfetivo() * ajusteForcaProx) - hv.getValorTotal())
					/ JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR;

			peso = getPesoHabilidadeValor(habilidadeEstatisticaPercentil, estatisticaGrupoMap.get(hv));
			passo = passoProx + (passoProx * peso * getPercDesenvolvimentoMaximo(j.getIdade()));
			variacao = passo - passoProx;

			hv.setPassoDesenvolvimento(passo);

			hv.setPotencialDesenvolvimentoEfetivo(hv.getPotencialDesenvolvimentoEfetivo()
					+ variacao * JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR);
			if (hv.isHabilidadeEspecifica()) {
				valorHabilidadesEspecificasPotEfetiva.add(hv.getPotencialDesenvolvimentoEfetivo());
			}

		}
		
		j.setForcaGeralPotencialEfetiva(
				(valorHabilidadesEspecificasPotEfetiva.stream().mapToDouble(v -> v).average().getAsDouble()));
	}
	
	private static final Double PESO_Q1 = 1.00;
	private static final Double PESO_Q2 = 0.67;
	private static final Double PESO_Q3 = 0.33;
	private static final Double PESO_Q4 = 0.00;
	
	private Double getPesoHabilidadeValor(HabilidadeEstatisticaPercentil habilidadeEstatisticaPercentil,
			HabilidadeValorEstatisticaGrupo habilidadeValorEstatisticaGrupo) {
		
		double pesoUso = PESO_Q4, pesoUsoVencedor = PESO_Q4, pesoPorcAcerto = PESO_Q4;
		
		if (habilidadeValorEstatisticaGrupo != null) {
			if (habilidadeEstatisticaPercentil.getQuantidadeUsoQ1() <= habilidadeValorEstatisticaGrupo.getQuantidadeUso()) {
				pesoUso = PESO_Q1;
			} else if (habilidadeEstatisticaPercentil.getQuantidadeUsoQ1() > habilidadeValorEstatisticaGrupo
					.getQuantidadeUso()
					&& habilidadeEstatisticaPercentil.getQuantidadeUsoQ2() <= habilidadeValorEstatisticaGrupo
							.getQuantidadeUso()) {
				pesoUso = PESO_Q2;
			} else if (habilidadeEstatisticaPercentil.getQuantidadeUsoQ2() > habilidadeValorEstatisticaGrupo
					.getQuantidadeUso()
					&& habilidadeEstatisticaPercentil.getQuantidadeUsoQ3() <= habilidadeValorEstatisticaGrupo
							.getQuantidadeUso()) {
				pesoUso = PESO_Q3;
			}
			
			
			if (habilidadeEstatisticaPercentil.getQuantidadeUsoVencedorQ1() <= habilidadeValorEstatisticaGrupo
					.getQuantidadeUsoVencedor()) {
				pesoUsoVencedor = PESO_Q1;
			} else if (habilidadeEstatisticaPercentil.getQuantidadeUsoVencedorQ1() > habilidadeValorEstatisticaGrupo
					.getQuantidadeUsoVencedor()
					&& habilidadeEstatisticaPercentil.getQuantidadeUsoVencedorQ2() <= habilidadeValorEstatisticaGrupo
							.getQuantidadeUsoVencedor()) {
				pesoUsoVencedor = PESO_Q2;
			} else if (habilidadeEstatisticaPercentil.getQuantidadeUsoVencedorQ2() > habilidadeValorEstatisticaGrupo
					.getQuantidadeUsoVencedor()
					&& habilidadeEstatisticaPercentil.getQuantidadeUsoVencedorQ3() <= habilidadeValorEstatisticaGrupo
							.getQuantidadeUsoVencedor()) {
				pesoUsoVencedor = PESO_Q3;
			}
			
			if (habilidadeEstatisticaPercentil.getPorcAcertoQ1() <= habilidadeValorEstatisticaGrupo.getPorcAcerto()) {
				pesoPorcAcerto = PESO_Q1;
			} else if (habilidadeEstatisticaPercentil.getPorcAcertoQ1() > habilidadeValorEstatisticaGrupo
					.getPorcAcerto()
					&& habilidadeEstatisticaPercentil.getPorcAcertoQ2() <= habilidadeValorEstatisticaGrupo
							.getPorcAcerto()) {
				pesoPorcAcerto = PESO_Q2;
			} else if (habilidadeEstatisticaPercentil.getPorcAcertoQ2() > habilidadeValorEstatisticaGrupo
					.getPorcAcerto()
					&& habilidadeEstatisticaPercentil.getPorcAcertoQ3() <= habilidadeValorEstatisticaGrupo
							.getPorcAcerto()) {
				pesoPorcAcerto = PESO_Q3;
			}
		}

		return pesoUso * pesoPorcAcerto;
	}
	
	private Double getPercDesenvolvimentoMaximo(Integer idade) {
		Double x = 0.00d;
		
		if (idade >= 17 && idade <= 22) {//if (idade >= FASE_1_IDADE_MIN && idade <= FASE_1_IDADE_MAX) {
			x = 0.15d;
		} else if (idade >= 23 && idade <= 26) {//} else if (idade >= FASE_2_IDADE_MIN && idade <= FASE_2_IDADE_MAX) {
			x = 0.10d;
		} else if (idade >= 27 && idade <= 31) {//} else if (idade >= FASE_3_IDADE_MIN && idade <= FASE_3_IDADE_MAX) {
			x = 0.05d;
		//} else if (idade >= FASE_4_IDADE_MIN && idade <= FASE_4_IDADE_MAX) {
		//	x = 1.00d;
		//} else if (idade >= FASE_5_IDADE_MIN && idade <= FASE_5_IDADE_MAX) {
		//	x = 1.00d;
		//} else if (IDADE_MAX == idade) {
		//	x = 1.00d;
		}

		return x;
	}

}
