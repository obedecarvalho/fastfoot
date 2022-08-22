package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.EstrategiaHabilidadeAtacante;
import com.fastfoot.player.model.EstrategiaHabilidadeGoleiro;
import com.fastfoot.player.model.EstrategiaHabilidadeLateral;
import com.fastfoot.player.model.EstrategiaHabilidadeMeia;
import com.fastfoot.player.model.EstrategiaHabilidadePosicaoJogador;
import com.fastfoot.player.model.EstrategiaHabilidadeVolante;
import com.fastfoot.player.model.EstrategiaHabilidadeZagueiro;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.HabilidadeTipo;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.service.util.NomeUtil;

public abstract class JogadorFactory {
	
	//########	SUPER CLASSE ABSTRACT	########################
	
	protected static final Random R = new Random();
	
	protected static final Double STDEV = 3.5; 
	
	protected static final Double PESO_HABILIDADE_COMUM = 0.75;
	
	protected static final Double PESO_HABILIDADE_ESPECIFICO = 1.0;
	
	protected static final Double PESO_HABILIDADE_OUTROS = 0.3;
	
	//protected static final Integer VALOR_HABILIDADE_MAX = 100;
	
	//protected static final Integer VALOR_HABILIDADE_MIN = 1;
	
	protected static final Double VALOR_HABILIDADE_MAX = 100d;
	
	protected static final Double VALOR_HABILIDADE_MIN = 1d;
	
	public static final Integer IDADE_MIN = 17;
	public static final Integer IDADE_MAX = 38;
	
	//protected static final Integer IDADE_MIN_TITULAR = 25;
	//protected static final Integer IDADE_MAX_TITULAR = 33;
	
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
	//decrescimento II - -8.0
	protected static final Integer FASE_5_IDADE_MIN = 35;
	protected static final Integer FASE_5_IDADE_MAX = 37;
	protected static final Double FASE_5_POT_DES_PORC = 61d;
	protected static final Double FASE_5_POT_DES_PASSO = (FASE_5_POT_DES_PORC - FASE_4_POT_DES_PORC)
			/ (FASE_5_IDADE_MAX - FASE_5_IDADE_MIN + 1);//Negativo
	
	public static final Double NUMERO_DESENVOLVIMENTO_ANO_JOGADOR = 5d;

	//17 a 38 anos
	public static final List<Double> VALOR_AJUSTE = Arrays.asList(0.23d, 0.30d, 0.37d, 0.44d, 0.51d, 0.58d, 0.65d,
			0.70d, 0.75d, 0.80d, 0.85d, 0.88d, 0.91d, 0.94d, 0.97d, 1.00d, 0.95d, 0.90d, 0.85d, 0.77d, 0.69d, 0.61d);

	/*protected Integer gerarValorHabilidadeEspecifico(Integer media, List<Integer> valorHabilidadesEspecificas) {
		Integer valor = gerarValorHabilidade(media, PESO_HABILIDADE_ESPECIFICO);
		valorHabilidadesEspecificas.add(valor);
		return valor;
	}*/
	
	protected Double gerarValorHabilidadeEspecifico(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_ESPECIFICO);
	}
	
	protected Double gerarValorHabilidadeComum(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_COMUM);
	}
	
	protected Double gerarValorHabilidadeOutros(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_OUTROS);
	}

	/*protected Integer gerarValorHabilidade(Integer media, Double peso) {
		return Long
				.valueOf(Math.max(Math.min(Math.round(peso * (R.nextGaussian() * STDEV + media)), VALOR_HABILIDADE_MAX),
						VALOR_HABILIDADE_MIN))
				.intValue();
	}*/

	protected Double gerarValorHabilidade(Integer media, Double peso) {
		return Math.max(Math.min(peso * (R.nextGaussian() * STDEV + media), VALOR_HABILIDADE_MAX),
				VALOR_HABILIDADE_MIN);
	}

	/*protected void addHabilidade(Jogador jogador, Habilidade habilidade, Integer valor, Integer potencial,
			Boolean especifica) {
		HabilidadeValor hv = new HabilidadeValor(habilidade, valor, jogador, potencial, especifica);
		jogador.addHabilidade(hv);
	}*/
	
	/*protected void addHabilidade(Jogador jogador, Habilidade habilidade, Integer valor, Integer potencial,
			Boolean especifica, Double passoDesenvolvimento, Double valorDecimal, Double potencialDesenvolvimentoEfetivo) {
		HabilidadeValor hv = new HabilidadeValor(habilidade, valor, jogador, potencial, especifica,
				passoDesenvolvimento, valorDecimal, potencialDesenvolvimentoEfetivo);
		jogador.addHabilidade(hv);
	}*/
	
	protected void addHabilidade(Jogador jogador, Habilidade habilidade, Integer valor, Double valorDecimal,
			/*Boolean especifica, Boolean habComum,*/
			HabilidadeTipo habilidadeTipo, Double potencial, Double potencialDesenvolvimentoEfetivo,
			Double passoDesenvolvimento) {
		HabilidadeValor hv = new HabilidadeValor(jogador, habilidade, valor, valorDecimal, /*especifica, habComum,*/
				habilidadeTipo, potencial,
				potencialDesenvolvimentoEfetivo, passoDesenvolvimento);
		jogador.addHabilidade(hv);
	}

	/*protected Integer sortearIdade(Boolean titular) {
		if (titular != null && titular) return sortearIdadeTitular();
		return sortearIdade(); 
	}*/

	protected Integer sortearIdade() {
		return IDADE_MIN + R.nextInt(IDADE_MAX - IDADE_MIN);
	}
	
	/*protected Integer sortearIdadeTitular() {
		return IDADE_MIN_TITULAR + R.nextInt(IDADE_MAX_TITULAR - IDADE_MIN_TITULAR);
	}*/

	protected static Double getAjusteForca(Integer idade) {
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

	/*private Double gerarVariacaoPassoDesenvolvimento(Double passo, Integer idade) {
		Double stdev = JogadorFactory.getPercVariacaoPassoDesenvolvimento(idade) * passo;
		return R.nextGaussian() * stdev;
	}*/
	
	/*protected void sortearHabilidadeValor(Jogador jogador, EstrategiaHabilidadePosicaoJogador estrategia, Integer potencial) {
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
	}*/
	
	public abstract void ajustarPassoDesenvolvimento(Jogador j);
	
	protected abstract void sortearHabilidadeValor(Jogador jogador, EstrategiaHabilidadePosicaoJogador estrategia, Integer potencial);
	
	public abstract void ajustarPassoDesenvolvimento(Jogador j,
			HabilidadeEstatisticaPercentil habilidadeEstatisticaPercentil,
			Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticaGrupoMap);
	
	protected void sortearEletivas(EstrategiaHabilidadePosicaoJogador estrategia, List<Habilidade> habEspecificas, List<Habilidade> habComuns) {
		List<Integer> posicoesElet = sortearPosicoesHabilidadesEletivas(estrategia.getHabilidadesEspecificasEletivas().size(), estrategia.getNumHabEspEletivas());
		
		for (int i = 0; i < estrategia.getHabilidadesEspecificasEletivas().size(); i++) {
			if (posicoesElet.contains(i)) {
				habEspecificas.add(estrategia.getHabilidadesEspecificasEletivas().get(i));
			} else {
				habComuns.add(estrategia.getHabilidadesEspecificasEletivas().get(i));
			}
		}
	}
	
	protected void sortearHabComunsEletivas(EstrategiaHabilidadePosicaoJogador estrategia, List<Habilidade> habComuns, List<Habilidade> habOutros) {
		//List<Integer> posicoesElet = sortearPosicoesHabilidadesEletivas(estrategia);
		List<Integer> posicoesElet = sortearPosicoesHabilidadesEletivas(estrategia.getHabilidadesComunsEletivas().size(), estrategia.getNumHabComunsEletivas());
		
		for (int i = 0; i < estrategia.getHabilidadesComunsEletivas().size(); i++) {
			if (posicoesElet.contains(i)) {
				habComuns.add(estrategia.getHabilidadesComunsEletivas().get(i));
			} else {
				habOutros.add(estrategia.getHabilidadesComunsEletivas().get(i));
			}
		}
	}
	
	protected List<Integer> sortearPosicoesHabilidadesEletivas(Integer qtdeHabTotal, Integer qtdeHabSortear) {
		//int totalHabEspEle = estrategia.getHabilidadesEspecificasEletivas().size();
		
		List<Integer> posicoes = new ArrayList<Integer>();
		
		int qtde = 0;
		
		Integer sorteado = null; 
		
		//while (qtde < estrategia.getNumHabEspEletivas()) {
		while (qtde < qtdeHabSortear) {
			
			sorteado = R.nextInt(qtdeHabTotal);
			
			if (!posicoes.contains(sorteado)) {
				posicoes.add(sorteado);
				qtde++;
			}

		}
		
		return posicoes;
	}
	
	/*protected List<Integer> sortearPosicoesHabilidadesEletivas(EstrategiaHabilidadePosicaoJogador estrategia) {
		int totalHabEspEle = estrategia.getHabilidadesEspecificasEletivas().size();
		
		List<Integer> posicoes = new ArrayList<Integer>();
		
		int qtde = 0;
		
		Integer sorteado = null; 
		
		while (qtde < estrategia.getNumHabEspEletivas()) {
			
			sorteado = R.nextInt(totalHabEspEle);
			
			if (!posicoes.contains(sorteado)) {
				posicoes.add(sorteado);
				qtde++;
			}

		}
		
		return posicoes;
	}*/
	
	/*protected abstract List<Habilidade> getHabilidadesEspecificas();

	protected abstract List<Habilidade> getHabilidadesEspecificasEletivas();

	protected abstract List<Habilidade> getHabilidadesComum();

	protected abstract List<Habilidade> getHabilidadesOutros();

	protected abstract Integer getNumHabEspEletivas();*/

	//protected abstract Jogador gerarJogador(Clube clube, Integer numero, Boolean titular);
	
	//protected abstract Jogador gerarJogador(Clube clube, Integer numero, Integer idade);
	
	//########	/SUPER CLASSE ABSTRACT	########################
	
	public static JogadorFactory getInstance() {
		return JogadorFactoryImplDesenRegular.getInstance();//TODO
	}

	public Jogador gerarJogador(Clube clube, Posicao posicao, Integer numero) {		
		EstrategiaHabilidadePosicaoJogador estrategia = getEstrategiaPosicaoJogador(posicao);
		return getInstance().gerarJogador(estrategia, clube, posicao, numero);
	}
	
	public Jogador gerarJogador(Clube clube, Posicao posicao, Integer numero, Integer idade) {		
		EstrategiaHabilidadePosicaoJogador estrategia = getEstrategiaPosicaoJogador(posicao);
		return getInstance().gerarJogador(estrategia, clube, posicao, numero, idade);
	}
	
	public static EstrategiaHabilidadePosicaoJogador getEstrategiaPosicaoJogador(Posicao posicao) {
		
		if (Posicao.GOLEIRO.equals(posicao)) {
			return EstrategiaHabilidadeGoleiro.getInstance();
		} else if (Posicao.LATERAL.equals(posicao)) {
			return EstrategiaHabilidadeLateral.getInstance();
		} else if (Posicao.ZAGUEIRO.equals(posicao)) {
			return EstrategiaHabilidadeZagueiro.getInstance();
		} else if (Posicao.VOLANTE.equals(posicao)) {
			return EstrategiaHabilidadeVolante.getInstance();
		} else if (Posicao.MEIA.equals(posicao)) {
			return EstrategiaHabilidadeMeia.getInstance();
		} else if (Posicao.ATACANTE.equals(posicao)) {
			return EstrategiaHabilidadeAtacante.getInstance();
		}
		
		return null;
	}

	protected Jogador gerarJogador(EstrategiaHabilidadePosicaoJogador estrategia, Clube clube, Posicao posicao, Integer numero) {
		Jogador jogador = new Jogador();

		jogador.setNumero(numero);
		jogador.setNome(NomeUtil.sortearNome());
		jogador.setClube(clube);
		jogador.setPosicao(posicao);
		jogador.setIdade(sortearIdade());
		jogador.setAposentado(false);

		sortearHabilidadeValor(jogador, estrategia, clube.getForcaGeral());
		
		return jogador;
	}
	
	protected Jogador gerarJogador(EstrategiaHabilidadePosicaoJogador estrategia, Clube clube, Posicao posicao, Integer numero, Integer idade) {
		Jogador jogador = new Jogador();

		jogador.setNumero(numero);
		jogador.setNome(NomeUtil.sortearNome());
		jogador.setClube(clube);
		jogador.setPosicao(posicao);
		jogador.setIdade(idade);
		jogador.setAposentado(false);

		sortearHabilidadeValor(jogador, estrategia, clube.getForcaGeral());
		
		return jogador;
	}
	
	public static Double calcularForcaGeral(Jogador jogador, EstrategiaHabilidadePosicaoJogador estrategiaHabilidadePosicaoJogador) {
		List<HabilidadeValor> habilidades = new ArrayList<HabilidadeValor>();
		habilidades.addAll(jogador.getHabilidadeValorByHabilidade(estrategiaHabilidadePosicaoJogador.getHabilidadesEspecificas()));
		habilidades.addAll(jogador.getHabilidadeValorByHabilidade(estrategiaHabilidadePosicaoJogador.getHabilidadesEspecificasEletivas()));
		
		Double sumHabValor = 0.0d;
		
		for (HabilidadeValor hv : habilidades) {
			if (hv.isHabilidadeEspecifica()) {
				sumHabValor += hv.getValorTotal();
			} else {
				sumHabValor += (hv.getValorTotal()/PESO_HABILIDADE_COMUM);
			}
		}
		
		return sumHabValor / habilidades.size();
	}
}
