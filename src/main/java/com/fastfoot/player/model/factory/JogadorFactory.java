package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

public abstract class JogadorFactory {
	
	//########	SUPER CLASSE ABSTRACT	########################
	
	protected static final Random R = new Random();
	
	protected static final Double STDEV = 3.5; 
	
	protected static final Double PESO_HABILIDADE_COMUM = 0.8;
	
	protected static final Double PESO_HABILIDADE_ESPECIFICO = 1.0;
	
	protected static final Double PESO_HABILIDADE_OUTROS = 0.4;
	
	protected static final Integer VALOR_HABILIDADE_MAX = 100;
	
	protected static final Integer VALOR_HABILIDADE_MIN = 1;
	
	public static final Integer IDADE_MIN = 17;
	public static final Integer IDADE_MAX = 38;
	
	protected static final Integer IDADE_MIN_TITULAR = 25;
	protected static final Integer IDADE_MAX_TITULAR = 33;
	
	protected static final Double POT_DES_PORC_INICIAL = 23d;
	
	//6 anos
	//crescimento I - 7.0
	protected static final Integer FASE_1_IDADE_MIN = 17;
	protected static final Integer FASE_1_IDADE_MAX = 22;
	protected static final Double FASE_1_POT_DES_PORC = 65d;
	protected static final Double FASE_1_POT_DES_PASSO = (FASE_1_POT_DES_PORC - POT_DES_PORC_INICIAL)
			/ (FASE_1_IDADE_MAX - FASE_1_IDADE_MIN + 1);
	
	//4 anos
	//crescimento II - 5.0
	protected static final Integer FASE_2_IDADE_MIN = 23;
	protected static final Integer FASE_2_IDADE_MAX = 26;
	protected static final Double FASE_2_POT_DES_PORC = 85d;
	protected static final Double FASE_2_POT_DES_PASSO = (FASE_2_POT_DES_PORC - FASE_1_POT_DES_PORC)
			/ (FASE_2_IDADE_MAX - FASE_2_IDADE_MIN + 1);
	
	//5 anos
	//crescimento III - 3.0
	protected static final Integer FASE_3_IDADE_MIN = 27;
	protected static final Integer FASE_3_IDADE_MAX = 31;
	protected static final Double FASE_3_POT_DES_PORC = 100d;
	protected static final Double FASE_3_POT_DES_PASSO = (FASE_3_POT_DES_PORC - FASE_2_POT_DES_PORC)
			/ (FASE_3_IDADE_MAX - FASE_3_IDADE_MIN + 1);

	//3 anos
	//decrescimento I - -5.0
	protected static final Integer FASE_4_IDADE_MIN = 32;
	protected static final Integer FASE_4_IDADE_MAX = 34;
	protected static final Double FASE_4_POT_DES_PORC = 85d;
	protected static final Double FASE_4_POT_DES_PASSO = (FASE_4_POT_DES_PORC - FASE_3_POT_DES_PORC)
			/ (FASE_4_IDADE_MAX - FASE_4_IDADE_MIN + 1);//Negativo
	
	//3 anos
	//decrescimento I - -8.0
	protected static final Integer FASE_5_IDADE_MIN = 35;
	protected static final Integer FASE_5_IDADE_MAX = 37;
	protected static final Double FASE_5_POT_DES_PORC = 61d;
	protected static final Double FASE_5_POT_DES_PASSO = (FASE_5_POT_DES_PORC - FASE_4_POT_DES_PORC)
			/ (FASE_5_IDADE_MAX - FASE_5_IDADE_MIN + 1);//Negativo
	
	public static final Double NUMERO_DESENVOLVIMENTO_ANO_JOGADOR = 5d;

	//17 a 38 anos
	public static final List<Double> VALOR_AJUSTE = Arrays.asList(0.23d, 0.30d, 0.37d, 0.44d, 0.51d, 0.58d, 0.65d, 0.70d, 0.75d, 0.80d, 0.85d, 0.88d, 0.91d, 0.94d, 0.97d, 1.00d, 0.95d, 0.90d, 0.85d, 0.77d, 0.69d, 0.61d);

	protected Integer gerarValorHabilidadeEspecifico(Integer media, List<Integer> valorHabilidadesEspecificas) {
		Integer valor = gerarValorHabilidade(media, PESO_HABILIDADE_ESPECIFICO);
		valorHabilidadesEspecificas.add(valor);
		return valor;
	}
	
	protected Integer gerarValorHabilidadeEspecifico(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_ESPECIFICO);
	}
	
	protected Integer gerarValorHabilidadeComum(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_COMUM);
	}
	
	protected Integer gerarValorHabilidadeOutros(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_OUTROS);
	}

	protected Integer gerarValorHabilidade(Integer media, Double peso) {
		return Long
				.valueOf(Math.max(Math.min(Math.round(peso * (R.nextGaussian() * STDEV + media)), VALOR_HABILIDADE_MAX),
						VALOR_HABILIDADE_MIN))
				.intValue();
	}

	protected void addHabilidade(Jogador jogador, Habilidade habilidade, Integer valor, Integer potencial,
			Boolean especifica) {
		HabilidadeValor hv = new HabilidadeValor(habilidade, valor, jogador, potencial, especifica);
		jogador.addHabilidade(hv);
	}
	
	protected void addHabilidade(Jogador jogador, Habilidade habilidade, Integer valor, Integer potencial,
			Boolean especifica, Double passoDesenvolvimentoAno, Double valorDecimal) {
		HabilidadeValor hv = new HabilidadeValor(habilidade, valor, jogador, potencial, especifica,
				passoDesenvolvimentoAno, valorDecimal);
		jogador.addHabilidade(hv);
	}

	protected Integer sortearIdade(Boolean titular) {
		if (titular != null && titular) return sortearIdadeTitular();
		return sortearIdade(); 
	}

	protected Integer sortearIdade() {
		return IDADE_MIN + R.nextInt(IDADE_MAX - IDADE_MIN);
	}
	
	protected Integer sortearIdadeTitular() {
		return IDADE_MIN_TITULAR + R.nextInt(IDADE_MAX_TITULAR - IDADE_MIN_TITULAR);
	}

	public static Double getAjusteForca(Integer idade) {
		Double x = 0d;
		
		if (idade >= FASE_1_IDADE_MIN && idade <= FASE_1_IDADE_MAX) {
			x = POT_DES_PORC_INICIAL + FASE_1_POT_DES_PASSO * (idade - FASE_1_IDADE_MIN);
		} else if (idade >= FASE_2_IDADE_MIN && idade <= FASE_2_IDADE_MAX) {
			x = FASE_1_POT_DES_PORC + FASE_2_POT_DES_PASSO * (idade - FASE_2_IDADE_MIN);
		} else if (idade >= FASE_3_IDADE_MIN && idade <= FASE_3_IDADE_MAX) {
			x = FASE_2_POT_DES_PORC + FASE_3_POT_DES_PASSO * (idade - FASE_3_IDADE_MIN);
		} else if (idade >= FASE_4_IDADE_MIN && idade <= FASE_4_IDADE_MAX) {
			x = FASE_3_POT_DES_PORC + FASE_4_POT_DES_PASSO * (idade - FASE_4_IDADE_MIN);
		} else if (idade >= FASE_5_IDADE_MIN && idade <= FASE_5_IDADE_MAX) {
			x = FASE_4_POT_DES_PORC + FASE_5_POT_DES_PASSO * (idade - FASE_5_IDADE_MIN);
		} else if (IDADE_MAX == idade) {
			x = FASE_5_POT_DES_PORC;
		}
		
		return x/100d;
	}
	
	protected void sortearHabilidadeValor(Jogador jogador, Integer potencial) {
		List<Habilidade> habEspecificas = new ArrayList<Habilidade>();
		List<Habilidade> habComuns = new ArrayList<Habilidade>();
		
		List<Integer> valorHabilidadesEspecificas = new ArrayList<Integer>();
		List<Integer> valorHabilidadesEspecificasPot = new ArrayList<Integer>();
		
		sortearEletivas(habEspecificas, habComuns);
		
		Double ajusteForca = getAjusteForca(jogador.getIdade());
		Double ajusteForcaProx = getAjusteForca(jogador.getIdade() + 1);
		Integer potencialSorteado = null;
		Double forca = null;
		Double passoProx = null;
		Double forcaDecimal = null;
		
		//Comuns
		habComuns.addAll(getHabilidadesComum());
		for (Habilidade h : habComuns) {
			potencialSorteado = gerarValorHabilidadeComum(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			forcaDecimal = forca - forca.intValue();
			addHabilidade(jogador, h, forca.intValue(), potencialSorteado, false, passoProx, forcaDecimal);
		}
		
		//Especificas
		habEspecificas.addAll(getHabilidadesEspecificas());
		for (Habilidade h : habEspecificas) {
			potencialSorteado = gerarValorHabilidadeEspecifico(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			forcaDecimal = forca - forca.intValue();
			valorHabilidadesEspecificasPot.add(potencialSorteado);
			valorHabilidadesEspecificas.add(forca.intValue());
			addHabilidade(jogador, h, forca.intValue(), potencialSorteado, true, passoProx, forcaDecimal);
		}
		jogador.setForcaGeral(
				(new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		jogador.setForcaGeralPotencial(
				(new Double(valorHabilidadesEspecificasPot.stream().mapToInt(v -> v).average().getAsDouble()))
						.intValue());
		
		//Outros
		for (Habilidade h : getHabilidadesOutros()) {
			potencialSorteado = gerarValorHabilidadeOutros(potencial);
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			forcaDecimal = forca - forca.intValue();
			addHabilidade(jogador, h, forca.intValue(), potencialSorteado, false, passoProx, forcaDecimal);
		}
	}
	
	protected void sortearEletivas(List<Habilidade> habEspecificas, List<Habilidade> habComuns) {
		List<Integer> posicoesElet = sortearPosicoesHabilidadesEletivas();
		
		for (int i = 0; i < getHabilidadesEspecificasEletivas().size(); i++) {
			if (posicoesElet.contains(i)) {
				habEspecificas.add(getHabilidadesEspecificasEletivas().get(i));
			} else {
				habComuns.add(getHabilidadesEspecificasEletivas().get(i));
			}
		}
	}
	
	protected List<Integer> sortearPosicoesHabilidadesEletivas() {
		int totalHabEspEle = getHabilidadesEspecificasEletivas().size();
		
		List<Integer> posicoes = new ArrayList<Integer>();
		
		int qtde = 0;
		
		Integer sorteado = null; 
		
		while (qtde < getNumHabEspEletivas()) {
			
			sorteado = R.nextInt(totalHabEspEle);
			
			if (!posicoes.contains(sorteado)) {
				posicoes.add(sorteado);
				qtde++;
			}

		}
		
		return posicoes;
	}
	
	protected abstract List<Habilidade> getHabilidadesEspecificas();

	protected abstract List<Habilidade> getHabilidadesEspecificasEletivas();

	protected abstract List<Habilidade> getHabilidadesComum();

	protected abstract List<Habilidade> getHabilidadesOutros();

	protected abstract Integer getNumHabEspEletivas();

	protected abstract Jogador gerarJogador(Clube clube, Integer numero, Boolean titular);
	
	protected abstract Jogador gerarJogador(Clube clube, Integer numero, Integer idade);
	
	//########	/SUPER CLASSE ABSTRACT	########################
	
	public static Jogador gerarJogador(Clube clube, Posicao posicao, Integer numero) {		
		return gerarJogador(clube, posicao, numero, false);
	}

	public static Jogador gerarJogador(Clube clube, Posicao posicao, Integer numero, Boolean titular) {		
		JogadorFactory jogadorFactory = getFactory(posicao);
		return jogadorFactory.gerarJogador(clube, numero, titular);
	}
	
	public static Jogador gerarJogador(Clube clube, Posicao posicao, Integer numero, Integer idade) {		
		JogadorFactory jogadorFactory = getFactory(posicao);
		return jogadorFactory.gerarJogador(clube, numero, idade);
	}
	
	protected static JogadorFactory getFactory(Posicao posicao) {
		
		if (Posicao.GOLEIRO.equals(posicao)) {
			return JogadorGoleiroFactory.getInstance();
		} else if (Posicao.LATERAL.equals(posicao)) {
			return JogadorLateralFactory.getInstance();
		} else if (Posicao.ZAGUEIRO.equals(posicao)) {
			return JogadorZagueiroFactory.getInstance();
		} else if (Posicao.VOLANTE.equals(posicao)) {
			return JogadorVolanteFactory.getInstance();
		} else if (Posicao.MEIA.equals(posicao)) {
			return JogadorMeiaFactory.getInstance();
		} else if (Posicao.ATACANTE.equals(posicao)) {
			return JogadorAtacanteFactory.getInstance();
		}
		
		return null;
	}

}
