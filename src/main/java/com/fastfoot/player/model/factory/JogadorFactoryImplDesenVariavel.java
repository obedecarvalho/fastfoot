package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fastfoot.player.model.EstrategiaHabilidadePosicaoJogador;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.entity.Jogador;

public class JogadorFactoryImplDesenVariavel extends JogadorFactory {
	
	private static JogadorFactory INSTANCE;
	
	protected JogadorFactoryImplDesenVariavel() {

	}
	
	public static JogadorFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new JogadorFactoryImplDesenVariavel();
		}
		return INSTANCE;
	}
	
	private Double getPercVariacaoPassoDesenvolvimento(Integer idade) {
		Double x = 0d;
		
		if (idade >= FASE_1_IDADE_MIN && idade <= FASE_1_IDADE_MAX) {
			x = 0.15d;//15%
		} else if (idade >= FASE_2_IDADE_MIN && idade <= FASE_2_IDADE_MAX) {
			x = 0.10d;//10%
		} else if (idade >= FASE_3_IDADE_MIN && idade <= FASE_3_IDADE_MAX) {
			x = 0.07d;//7%
		//} else if (idade >= FASE_4_IDADE_MIN && idade <= FASE_4_IDADE_MAX) {
		//	x = 0.05d;
		//} else if (idade >= FASE_5_IDADE_MIN && idade <= FASE_5_IDADE_MAX) {
		//	x = 0.03d;
		//} else if (IDADE_MAX == idade) {
		//	x = 0.00d;
		}
		
		/**
		 * Se multiplicar x por:
		 * 
		 * 		1.000 - 70% estaram no range [-x, x]
		 * 		0.850 - 76% estaram no range [-x, x]
		 * 		0.800 - 80% estaram no range [-x, x]
		 * 		0.666 - 85% estaram no range [-x, x]
		 * 		0.600 - 90% estaram no range [-x, x]
		 * 		0.500 - 96% estaram no range [-x, x]
		 * 		0.333 - 99% estaram no range [-x, x]
		 * 
		 */
		return x * 0.6d;
	}
	
	private Double gerarVariacaoPassoDesenvolvimento(Double passo, Integer idade) {
		Double stdev = getPercVariacaoPassoDesenvolvimento(idade) * passo;
		return R.nextGaussian() * stdev;
	}

	@Override
	protected void sortearHabilidadeValor(Jogador jogador, EstrategiaHabilidadePosicaoJogador estrategia, Integer potencial) {
		List<Habilidade> habEspecificas = new ArrayList<Habilidade>();
		List<Habilidade> habComuns = new ArrayList<Habilidade>();
		
		List<Double> valorHabilidadesEspecificas = new ArrayList<Double>();
		List<Double> valorHabilidadesEspecificasPot = new ArrayList<Double>();
		List<Double> valorHabilidadesEspecificasPotEfetiva = new ArrayList<Double>();
		
		sortearEletivas(estrategia, habEspecificas, habComuns);
		
		Double ajusteForca = getAjusteForca(jogador.getIdade());
		Double ajusteForcaProx = getAjusteForca(jogador.getIdade() + 1);
		Double potencialSorteado = null;
		Double forca = null;
		Double passoProx = null;
		Double forcaDecimal = null;
		
		Double variacao = null;//Remover essa variavel para Passo desenvolvimento REGULAR
		
		//Comuns
		habComuns.addAll(estrategia.getHabilidadesComum());
		for (Habilidade h : habComuns) {
			potencialSorteado = gerarValorHabilidadeComum(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			variacao = gerarVariacaoPassoDesenvolvimento(passoProx, jogador.getIdade());
			forcaDecimal = forca - forca.intValue();
			//addHabilidade(jogador, h, forca.intValue(), potencialSorteado, false, passoProx + variacao, forcaDecimal, potencialSorteado + variacao);
			addHabilidade(jogador, h, forca.intValue(), forcaDecimal, false, potencialSorteado, potencialSorteado + variacao * NUMERO_DESENVOLVIMENTO_ANO_JOGADOR, passoProx + variacao);
		}
		
		//Especificas
		habEspecificas.addAll(estrategia.getHabilidadesEspecificas());
		for (Habilidade h : habEspecificas) {
			potencialSorteado = gerarValorHabilidadeEspecifico(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			variacao = gerarVariacaoPassoDesenvolvimento(passoProx, jogador.getIdade());
			forcaDecimal = forca - forca.intValue();
			valorHabilidadesEspecificasPot.add(potencialSorteado);
			valorHabilidadesEspecificas.add(forca);
			valorHabilidadesEspecificasPotEfetiva.add(potencialSorteado + variacao * NUMERO_DESENVOLVIMENTO_ANO_JOGADOR);
			//addHabilidade(jogador, h, forca.intValue(), potencialSorteado, true, passoProx + variacao, forcaDecimal, potencialSorteado + variacao);
			addHabilidade(jogador, h, forca.intValue(), forcaDecimal, true, potencialSorteado, potencialSorteado + variacao * NUMERO_DESENVOLVIMENTO_ANO_JOGADOR, passoProx + variacao);
		}
		jogador.setForcaGeral(
				(new Double(valorHabilidadesEspecificas.stream().mapToDouble(v -> v).average().getAsDouble())).intValue());
		jogador.setForcaGeralPotencial(
				(new Double(valorHabilidadesEspecificasPot.stream().mapToDouble(v -> v).average().getAsDouble()))
						.intValue());
		jogador.setForcaGeralPotencialEfetiva(
				(valorHabilidadesEspecificasPotEfetiva.stream().mapToDouble(v -> v).average().getAsDouble()));
		
		//Outros
		for (Habilidade h : estrategia.getHabilidadesOutros()) {
			potencialSorteado = gerarValorHabilidadeOutros(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			variacao = gerarVariacaoPassoDesenvolvimento(passoProx, jogador.getIdade());
			forcaDecimal = forca - forca.intValue();
			//addHabilidade(jogador, h, forca.intValue(), potencialSorteado, false, passoProx + variacao, forcaDecimal, potencialSorteado + variacao);
			addHabilidade(jogador, h, forca.intValue(), forcaDecimal, false, potencialSorteado, potencialSorteado + variacao * NUMERO_DESENVOLVIMENTO_ANO_JOGADOR, passoProx + variacao);
		}
	}
	
	public void ajustarPassoDesenvolvimento(Jogador j) {

		Double ajusteForcaProx = JogadorFactory.getAjusteForca(j.getIdade() + 1);
		Double passoProx = null;
		
		Double variacao = null;
		List<Double> valorHabilidadesEspecificasPotEfetiva = new ArrayList<Double>();
		
		for (HabilidadeValor hv : j.getHabilidades()) {
			//passoProx = ((hv.getPotencialDesenvolvimento() * ajusteForcaProx) - forca) / JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			passoProx = ((hv.getPotencialDesenvolvimentoEfetivo() * ajusteForcaProx) - hv.getValorTotal()) / JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			variacao = gerarVariacaoPassoDesenvolvimento(passoProx, j.getIdade());
			hv.setPassoDesenvolvimento(passoProx + variacao);
			hv.setPotencialDesenvolvimentoEfetivo(hv.getPotencialDesenvolvimentoEfetivo() + variacao * JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR);
			if (hv.getHabilidadeEspecifica()) {
				valorHabilidadesEspecificasPotEfetiva.add(hv.getPotencialDesenvolvimentoEfetivo());
			}
		}
		
		j.setForcaGeralPotencialEfetiva(
				(valorHabilidadesEspecificasPotEfetiva.stream().mapToDouble(v -> v).average().getAsDouble()));
	}

	@Override
	public void ajustarPassoDesenvolvimento(Jogador j, HabilidadeEstatisticaPercentil habilidadeEstatisticaPercentil,
			Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticaGrupoMap) {
		// TODO Auto-generated method stub
		
	}

}
