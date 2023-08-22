package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fastfoot.model.Constantes;
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
import com.fastfoot.player.model.ModoDesenvolvimentoJogador;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.service.util.NomeUtil;
import com.fastfoot.service.util.RandomUtil;
import com.fastfoot.service.util.RoletaUtil;

public abstract class JogadorFactory {
	
	private static final Comparator<Jogador> COMPARATOR_POSICAO_FORCA_GERAL;
	
	private static final Comparator<Jogador> COMPARATOR_FORCA_GERAL;
	
	private static final Comparator<Jogador> COMPARATOR_FORCA_GERAL_ENERGIA;
	
	private static final Comparator<Jogador> COMPARATOR_ENERGIA;
	
	static {
		COMPARATOR_FORCA_GERAL = new Comparator<Jogador>() {
			
			@Override
			public int compare(Jogador o1, Jogador o2) {
				return o2.getForcaGeral().compareTo(o1.getForcaGeral());//reverse
			}
		};
		
		COMPARATOR_POSICAO_FORCA_GERAL = new Comparator<Jogador>() {
			
			@Override
			public int compare(Jogador o1, Jogador o2) {
				int compare = o1.getPosicao().compareTo(o2.getPosicao());
				return compare != 0 ? compare : o2.getForcaGeral().compareTo(o1.getForcaGeral());//reverse
			}
		};
		
		COMPARATOR_FORCA_GERAL_ENERGIA = new Comparator<Jogador>() {
			
			@Override
			public int compare(Jogador o1, Jogador o2) {
				Double forcaPonderada1 = Math.pow(o1.getForcaGeral(), Constantes.ROLETA_N_POWER)
						* (o1.getJogadorEnergia().getEnergiaAtual() / 100.0);

				Double forcaPonderada2 = Math.pow(o2.getForcaGeral(), Constantes.ROLETA_N_POWER)
						* (o2.getJogadorEnergia().getEnergiaAtual() / 100.0);
				
				int compare = forcaPonderada2.compareTo(forcaPonderada1);//reverse
				
				if (compare == 0) {
					compare = o1.getIdade().compareTo(o2.getIdade());
				}
				
				return compare;
			}
		};
		
		COMPARATOR_ENERGIA = new Comparator<Jogador>() {
			
			@Override
			public int compare(Jogador o1, Jogador o2) {
				return o1.getJogadorEnergia().getEnergiaAtual()
						.compareTo(o2.getJogadorEnergia().getEnergiaAtual());
			}
		};
	}
	
	//########	SUPER CLASSE ABSTRACT	########################
	
	protected static final Random R = new Random();
	
	protected static final Double STDEV = 3.5; 
	
	protected static final Double PESO_HABILIDADE_COMUM = 0.66666;
	
	protected static final Double PESO_HABILIDADE_ESPECIFICO = 1.0;
	
	protected static final Double PESO_HABILIDADE_OUTROS = 0.33333;
	
	protected static final Double VALOR_HABILIDADE_MAX = 100d;
	
	protected static final Double VALOR_HABILIDADE_MIN = 1d;
	
	public static final Integer IDADE_MIN = 17;
	
	public static final Integer IDADE_MAX = 38;
	
	//public static final Double POT_DES_PORC_INICIAL = 26d;//23d;

	//6 anos
	//crescimento I - 8.0 --7.0
	/*protected static final Integer FASE_1_IDADE_MIN = 17;
	protected static final Integer FASE_1_IDADE_MAX = 22;
	protected static final Double FASE_1_POT_DES_PORC = 74d;//65d;
	protected static final Double FASE_1_POT_DES_PASSO = (FASE_1_POT_DES_PORC - POT_DES_PORC_INICIAL)
			/ (FASE_1_IDADE_MAX - FASE_1_IDADE_MIN + 1);*/
	
	//4 anos
	//crescimento II - 4.0 --5.0
	/*protected static final Integer FASE_2_IDADE_MIN = 23;
	protected static final Integer FASE_2_IDADE_MAX = 26;
	protected static final Double FASE_2_POT_DES_PORC = 90d;//85d;
	protected static final Double FASE_2_POT_DES_PASSO = (FASE_2_POT_DES_PORC - FASE_1_POT_DES_PORC)
			/ (FASE_2_IDADE_MAX - FASE_2_IDADE_MIN + 1);*/
	
	//5 anos
	//crescimento III - 2.0 --3.0
	/*protected static final Integer FASE_3_IDADE_MIN = 27;
	protected static final Integer FASE_3_IDADE_MAX = 31;
	protected static final Double FASE_3_POT_DES_PORC = 100d;
	protected static final Double FASE_3_POT_DES_PASSO = (FASE_3_POT_DES_PORC - FASE_2_POT_DES_PORC)
			/ (FASE_3_IDADE_MAX - FASE_3_IDADE_MIN + 1);*/

	//3 anos
	//decrescimento I - -5.0
	/*protected static final Integer FASE_4_IDADE_MIN = 32;
	protected static final Integer FASE_4_IDADE_MAX = 34;
	protected static final Double FASE_4_POT_DES_PORC = 85d;
	protected static final Double FASE_4_POT_DES_PASSO = (FASE_4_POT_DES_PORC - FASE_3_POT_DES_PORC)
			/ (FASE_4_IDADE_MAX - FASE_4_IDADE_MIN + 1);//Negativo*/
	
	//3 anos
	//decrescimento II - -8.0
	/*protected static final Integer FASE_5_IDADE_MIN = 35;
	protected static final Integer FASE_5_IDADE_MAX = 37;
	protected static final Double FASE_5_POT_DES_PORC = 61d;
	protected static final Double FASE_5_POT_DES_PASSO = (FASE_5_POT_DES_PORC - FASE_4_POT_DES_PORC)
			/ (FASE_5_IDADE_MAX - FASE_5_IDADE_MIN + 1);//Negativo*/
	
	public static final Double QTDE_DESENVOLVIMENTO_ANO_JOGADOR = 5d;

	//17 a 38 anos
	/*public static final List<Double> VALOR_AJUSTE = Arrays.asList(0.23d, 0.30d, 0.37d, 0.44d, 0.51d, 0.58d, 0.65d,
			0.70d, 0.75d, 0.80d, 0.85d, 0.88d, 0.91d, 0.94d, 0.97d, 1.00d, 0.95d, 0.90d, 0.85d, 0.77d, 0.69d, 0.61d);*/
	/*public static final List<Double> VALOR_AJUSTE = Arrays.asList(0.26d, 0.34d, 0.42d, 0.50d, 0.58d, 0.66d, 0.74d,
			0.78d, 0.82d, 0.86d, 0.90d, 0.92d, 0.94d, 0.96d, 0.98d, 1.00d, 0.95d, 0.90d, 0.85d, 0.77d, 0.69d, 0.61d);*/

	protected Double gerarValorHabilidadeEspecifico(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_ESPECIFICO);
	}
	
	protected Double gerarValorHabilidadeComum(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_COMUM);
	}
	
	protected Double gerarValorHabilidadeOutros(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_OUTROS);
	}

	protected Double gerarValorHabilidade(Integer media, Double peso) {
		return Math.max(Math.min(peso * (R.nextGaussian() * STDEV + media), VALOR_HABILIDADE_MAX),
				VALOR_HABILIDADE_MIN);//TODO: usar RandomUtil.getNextGaussianByAvgAndStdDev
	}

	protected void addHabilidade(Jogador jogador, Habilidade habilidade, Integer valor, Double valorDecimal,
			HabilidadeTipo habilidadeTipo, Double potencial, Double passoDesenvolvimento) {
		HabilidadeValor hv = new HabilidadeValor(jogador, habilidade, valor, valorDecimal, habilidadeTipo, potencial,
				passoDesenvolvimento);
		jogador.addHabilidade(hv);
	}

	protected Integer sortearIdade() {
		return RandomUtil.sortearIntervalo(IDADE_MIN, IDADE_MAX);
	}
	
	protected static Double getAjusteForca(ModoDesenvolvimentoJogador modoDesenvolvimentoJogador, Integer idade) {
		return modoDesenvolvimentoJogador.getValorAjuste()[idade - IDADE_MIN];
	}

	/*protected static Double getAjusteForca(Integer idade) {
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
	}*/

	public abstract void ajustarPassoDesenvolvimento(Jogador j);
	
	protected abstract void sortearHabilidadeValor(Jogador jogador, EstrategiaHabilidadePosicaoJogador estrategia, Integer potencial);
	
	public abstract void ajustarPassoDesenvolvimento(Jogador j,
			HabilidadeEstatisticaPercentil habilidadeEstatisticaPercentil,
			Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticaGrupoMap);
	
	protected void sortearEletivas(EstrategiaHabilidadePosicaoJogador estrategia, List<Habilidade> habEspecificas, List<Habilidade> habComuns) {
		List<Integer> posicoesElet = RandomUtil.getRandomDistinctRangeValues(estrategia.getHabilidadesEspecificasEletivas().size(), estrategia.getNumHabEspEletivas());
		
		for (int i = 0; i < estrategia.getHabilidadesEspecificasEletivas().size(); i++) {
			if (posicoesElet.contains(i)) {
				habEspecificas.add(estrategia.getHabilidadesEspecificasEletivas().get(i));
			} else {
				habComuns.add(estrategia.getHabilidadesEspecificasEletivas().get(i));
			}
		}
	}
	
	protected void sortearHabCoringa(EstrategiaHabilidadePosicaoJogador estrategia, List<Habilidade> habEspecificas, List<Habilidade> habComuns, List<Habilidade> habOutros) {
		List<Integer> posicoesEspecificas = RandomUtil.getRandomDistinctRangeValues(estrategia.getHabilidadesCoringa().size(), estrategia.getNumHabCoringaSelecionadoEspecifica());
		List<Integer> posicoesComuns = RandomUtil.getRandomDistinctRangeValuesWithoutValues(estrategia.getHabilidadesCoringa().size(), estrategia.getNumHabCoringaSelecionadoComum(), posicoesEspecificas);
		
		for (int i = 0; i < estrategia.getHabilidadesCoringa().size(); i++) {
			if (posicoesEspecificas.contains(i)) {
				habEspecificas.add(estrategia.getHabilidadesCoringa().get(i));
			} else if (posicoesComuns.contains(i)) {
				habComuns.add(estrategia.getHabilidadesCoringa().get(i));
			} else {
				habOutros.add(estrategia.getHabilidadesCoringa().get(i));
			}
		}

	}
	
	protected void sortearHabComunsEletivas(EstrategiaHabilidadePosicaoJogador estrategia, List<Habilidade> habComuns, List<Habilidade> habOutros) {
		List<Integer> posicoesElet = RandomUtil.getRandomDistinctRangeValues(estrategia.getHabilidadesComunsEletivas().size(), estrategia.getNumHabComunsEletivas());
		
		for (int i = 0; i < estrategia.getHabilidadesComunsEletivas().size(); i++) {
			if (posicoesElet.contains(i)) {
				habComuns.add(estrategia.getHabilidadesComunsEletivas().get(i));
			} else {
				habOutros.add(estrategia.getHabilidadesComunsEletivas().get(i));
			}
		}
	}
	
	//########	/SUPER CLASSE ABSTRACT	########################
	
	public static JogadorFactory getInstance() {
		return JogadorFactoryImplDesenRegular.getInstance();//TODO
	}

	public Jogador gerarJogador(Posicao posicao, Integer potencial) {
		return getInstance().gerarJogador(getEstrategiaPosicaoJogador(posicao), posicao, null, potencial);
	}

	public Jogador gerarJogador(Posicao posicao, Integer idade, Integer potencial) {
		return getInstance().gerarJogador(getEstrategiaPosicaoJogador(posicao), posicao, idade, potencial);
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

	protected Jogador gerarJogador(EstrategiaHabilidadePosicaoJogador estrategia, /*Clube clube,*/ Posicao posicao,
			/*Integer numero,*/ Integer idade, Integer potencial) {
		Jogador jogador = new Jogador();
		
		jogador/*.getJogadorDetalhe()*/ //TODO: ter proporções diferentes
				.setModoDesenvolvimentoJogador(RoletaUtil.sortearPesoUm(ModoDesenvolvimentoJogador.values()));

		//jogador.setNumero(numero);
		jogador.setNome(NomeUtil.sortearNome());
		//jogador.setClube(clube);
		jogador.setPosicao(posicao);
		jogador.setIdade(idade == null ? sortearIdade() : idade);
		jogador.setStatusJogador(StatusJogador.ATIVO);

		sortearHabilidadeValor(jogador, estrategia, potencial);
		
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
	
	public static Double calcularForcaGeral2(Jogador jogador, EstrategiaHabilidadePosicaoJogador estrategiaHabilidadePosicaoJogador) {
		List<HabilidadeValor> habilidades = new ArrayList<HabilidadeValor>();
		habilidades.addAll(jogador.getHabilidadeValorByHabilidade(estrategiaHabilidadePosicaoJogador.getHabilidadesEspecificas()));
		habilidades.addAll(jogador.getHabilidadeValorByHabilidade(estrategiaHabilidadePosicaoJogador.getHabilidadesEspecificasEletivas()));
		
		Double sumHabValor = 0.0d;
		Integer peso = 0;
		
		Integer PESO_ESPECIFICO = 5;
		Integer PESO_COMUM = 1;
		
		for (HabilidadeValor hv : habilidades) {
			if (hv.isHabilidadeEspecifica()) {
				sumHabValor += hv.getValorTotal() * PESO_ESPECIFICO;
				peso += PESO_ESPECIFICO;
			} else {
				sumHabValor += hv.getValorTotal() * PESO_COMUM;
				peso += PESO_COMUM;
			}
		}
		
		return sumHabValor / peso;
	}
	
	public static Comparator<Jogador> getComparatorForcaGeral() {
		return COMPARATOR_FORCA_GERAL;
	}
	
	public static Comparator<Jogador> getComparatorPosicaoForcaGeral() {
		return COMPARATOR_POSICAO_FORCA_GERAL;
	}
	
	public static Comparator<Jogador> getComparatorForcaGeralEnergia() {
		return COMPARATOR_FORCA_GERAL_ENERGIA;
	}
	
	public static Comparator<Jogador> getComparatorEnergia() {
		return COMPARATOR_ENERGIA;
	}
}
