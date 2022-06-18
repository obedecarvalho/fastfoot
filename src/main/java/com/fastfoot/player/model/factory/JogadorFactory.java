package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValor;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.service.util.NomeUtil;

public abstract class JogadorFactory {
	
	//########	SUPER CLASSE ABSTRACT	########################
	
	protected static final Random R = new Random();
	
	protected static final Double STDEV = 3.5; 
	
	protected static final Double PESO_HABILIDADE_COMUM = 0.8;
	
	protected static final Double PESO_HABILIDADE_ESPECIFICO = 1.0;
	
	protected static final Double PESO_HABILIDADE_OUTROS = 0.4;
	
	protected static final Integer VALOR_HABILIDADE_MAX = 100;
	
	protected static final Integer VALOR_HABILIDADE_MIN = 1;
	
	private static final Integer IDADE_MIN = 18;
	private static final Integer IDADE_MAX = 35;
	
	private static final Double POT_DES_PORC_INICIAL = 20d;
	
	//8 anos
	//crescimento acelerado
	private static final Integer FASE_1_IDADE_MIN = 18;
	private static final Integer FASE_1_IDADE_MAX = 25;
	private static final Double FASE_1_POT_DES_PORC = 80d;
	private static final Double FASE_1_POT_DES_PASSO = (FASE_1_POT_DES_PORC - POT_DES_PORC_INICIAL)
			/ (FASE_1_IDADE_MAX - FASE_1_IDADE_MIN + 1);
	
	//7 anos
	//crescimento
	private static final Integer FASE_2_IDADE_MIN = 26;
	private static final Integer FASE_2_IDADE_MAX = 32;
	private static final Double FASE_2_POT_DES_PORC = 100d;
	private static final Double FASE_2_POT_DES_PASSO = (FASE_2_POT_DES_PORC - FASE_1_POT_DES_PORC)
			/ (FASE_2_IDADE_MAX - FASE_2_IDADE_MIN + 1);

	//5 anos
	//decrescimento
	private static final Integer FASE_3_IDADE_MIN = 33;
	private static final Integer FASE_3_IDADE_MAX = 37;
	private static final Double FASE_3_POT_DES_PORC = 75d;
	private static final Double FASE_3_POT_DES_PASSO = (FASE_3_POT_DES_PORC - FASE_2_POT_DES_PORC)
			/ (FASE_3_IDADE_MAX - FASE_3_IDADE_MIN + 1);//Negativo

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
		return Long.valueOf(Math.max(Math.min(Math.round(peso * (R.nextGaussian() * STDEV + media)), VALOR_HABILIDADE_MAX), VALOR_HABILIDADE_MIN)).intValue();
	}
	
	protected void addHabilidade(Jogador jogador, Habilidade habilidade, Integer valor, Integer potencial){
		HabilidadeValor hv = new HabilidadeValor(habilidade, valor, jogador, potencial);
		jogador.addHabilidade(hv);
	}

	protected void addHabilidade(Jogador jogador, Habilidade habilidade, Integer valor, Integer potencial, Boolean especifica){
		HabilidadeValor hv = new HabilidadeValor(habilidade, valor, jogador, potencial, especifica);
		jogador.addHabilidade(hv);
	}
	
	protected void addHabilidade(Jogador jogador, Integer valor, Habilidade habilidade){
		HabilidadeValor hv = new HabilidadeValor(habilidade, valor, jogador, valor);
		jogador.addHabilidade(hv);
	}

	protected Integer sortearIdade() {
		return IDADE_MIN + R.nextInt(IDADE_MAX - IDADE_MIN);
	}

	protected Double getAjusteForca(Integer idade) {
		Double x = 0d;
		
		if (idade >= FASE_1_IDADE_MIN && idade <= FASE_1_IDADE_MAX) {
			x = POT_DES_PORC_INICIAL + FASE_1_POT_DES_PASSO * (idade - FASE_1_IDADE_MIN);
		} else if (idade >= FASE_2_IDADE_MIN && idade <= FASE_2_IDADE_MAX) {
			x = FASE_1_POT_DES_PORC + FASE_2_POT_DES_PASSO * (idade - FASE_2_IDADE_MIN);
		} else if (idade >= FASE_3_IDADE_MIN && idade <= FASE_3_IDADE_MAX) {
			x = FASE_2_POT_DES_PORC + FASE_3_POT_DES_PASSO * (idade - FASE_3_IDADE_MIN);
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
		Integer potencialSorteado = null;
		Double forca = null;
		
		//Comuns
		habComuns.addAll(getHabilidadesComum());
		for (Habilidade h : habComuns) {
			potencialSorteado = gerarValorHabilidadeComum(potencial);
			forca = potencialSorteado * ajusteForca;
			addHabilidade(jogador, h, forca.intValue(), potencialSorteado);
		}
		
		//Especificas
		habEspecificas.addAll(getHabilidadesEspecificas());
		for (Habilidade h : habEspecificas) {
			potencialSorteado = gerarValorHabilidadeEspecifico(potencial);
			forca = potencialSorteado * ajusteForca;
			valorHabilidadesEspecificasPot.add(potencialSorteado);
			valorHabilidadesEspecificas.add(forca.intValue());
			addHabilidade(jogador, h, forca.intValue(), potencialSorteado, true);
		}
		jogador.setForcaGeral(
				(new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		jogador.setForcaGeralPotencial(
				(new Double(valorHabilidadesEspecificasPot.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		
		//Outros
		for (Habilidade h : getHabilidadesOutros()) {
			potencialSorteado = gerarValorHabilidadeOutros(potencial);
			forca = potencialSorteado * ajusteForca;
			addHabilidade(jogador, h, forca.intValue(), potencialSorteado);
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
		
		//System.err.println(posicoes);
		
		return posicoes;
	}
	
	public abstract List<Habilidade> getHabilidadesEspecificas();

	public abstract List<Habilidade> getHabilidadesEspecificasEletivas();

	public abstract List<Habilidade> getHabilidadesComum();

	public abstract List<Habilidade> getHabilidadesOutros();

	public abstract Integer getNumHabEspEletivas();

	public abstract Jogador gerarJogador(Clube clube, Integer numero);
	
	//########	/SUPER CLASSE ABSTRACT	########################

	public Jogador gerarJogador(Clube clube, Posicao posicao, Integer numero) {
		Jogador jogador = null;
		switch (posicao) {
		case GOLEIRO:
			jogador = gerarGoleiro(clube, numero);
			break;
		case ZAGUEIRO:
			jogador = gerarZagueiro(clube, numero);		
			break;
		case LATERAL:
			jogador = gerarLateral(clube, numero);
			break;
		case VOLANTE:
			jogador = gerarVolante(clube, numero);
			break;
		case MEIA_LATERAL:
			jogador = gerarMeiaLateral(clube, numero);
			break;
		case MEIA:
			jogador = gerarMeia(clube, numero);
			break;
		case ATACANTE:
			jogador = gerarAtacante(clube, numero);
			break;
		default:
			break;
		}
		//JogadorCalcularForcaUtil.calcular(jogador);
		return jogador;
	}

	public Jogador gerarGoleiro(Clube clube, Integer numero) {
		Jogador j = gerarGoleiro(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public Jogador gerarGoleiro(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.GOLEIRO);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		List<Integer> valorHabilidadesEspecificas = new ArrayList<Integer>();
		
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.PASSE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FINALIZACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CRUZAMENTO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.ARMACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CABECEIO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.MARCACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.DESARME);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.INTERCEPTACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.VELOCIDADE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.DIBLE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FORCA);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.POSICIONAMENTO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.DOMINIO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.REFLEXO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.JOGO_AEREO);
		
		j.setForcaGeral((new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		
		return j;
	}
	
	public Jogador gerarZagueiro(Clube clube, Integer numero) {
		Jogador j = gerarZagueiro(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public Jogador gerarZagueiro(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.ZAGUEIRO);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		List<Integer> valorHabilidadesEspecificas = new ArrayList<Integer>();
		
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.PASSE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FINALIZACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CRUZAMENTO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.ARMACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.CABECEIO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.MARCACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.DESARME);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.INTERCEPTACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.VELOCIDADE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.DIBLE);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.FORCA);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.POSICIONAMENTO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.DOMINIO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.REFLEXO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.JOGO_AEREO);

		j.setForcaGeral((new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		
		return j;
	}
	
	public Jogador gerarLateral(Clube clube, Integer numero) {
		Jogador j = gerarLateral(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public Jogador gerarLateral(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.LATERAL);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		List<Integer> valorHabilidadesEspecificas = new ArrayList<Integer>();
		
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.PASSE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FINALIZACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CRUZAMENTO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.ARMACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CABECEIO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.MARCACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.DESARME);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.INTERCEPTACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.VELOCIDADE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.DIBLE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FORCA);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.POSICIONAMENTO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.DOMINIO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.REFLEXO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.JOGO_AEREO);

		j.setForcaGeral((new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		
		return j;
	}
	
	public Jogador gerarVolante(Clube clube, Integer numero) {
		Jogador j = gerarVolante(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public Jogador gerarVolante(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.VOLANTE);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		List<Integer> valorHabilidadesEspecificas = new ArrayList<Integer>();
		
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.PASSE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FINALIZACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CRUZAMENTO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.ARMACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CABECEIO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.MARCACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.DESARME);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.INTERCEPTACAO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.VELOCIDADE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.DIBLE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FORCA);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.POSICIONAMENTO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.DOMINIO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.REFLEXO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.JOGO_AEREO);

		j.setForcaGeral((new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		
		return j;
	}
	
	public Jogador gerarMeiaLateral(Clube clube, Integer numero) {
		Jogador j = gerarMeiaLateral(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public Jogador gerarMeiaLateral(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.MEIA_LATERAL);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		List<Integer> valorHabilidadesEspecificas = new ArrayList<Integer>();
		
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.PASSE);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.FINALIZACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.CRUZAMENTO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.ARMACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CABECEIO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.MARCACAO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.DESARME);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.INTERCEPTACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.VELOCIDADE);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.DIBLE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FORCA);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.POSICIONAMENTO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.DOMINIO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.REFLEXO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.JOGO_AEREO);
		
		j.setForcaGeral((new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		
		return j;
	}
	
	public Jogador gerarMeia(Clube clube, Integer numero) {
		Jogador j = gerarMeia(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public Jogador gerarMeia(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.MEIA);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		List<Integer> valorHabilidadesEspecificas = new ArrayList<Integer>();
		
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.PASSE);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.FINALIZACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.CRUZAMENTO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.ARMACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CABECEIO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.MARCACAO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.DESARME);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.INTERCEPTACAO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.VELOCIDADE);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.DIBLE);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.FORCA);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.POSICIONAMENTO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.DOMINIO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.REFLEXO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.JOGO_AEREO);
		
		j.setForcaGeral((new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		
		return j;
	}
	
	public Jogador gerarAtacante(Clube clube, Integer numero) {
		Jogador j = gerarAtacante(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public Jogador gerarAtacante(Integer overhall, Integer numero) {
		Jogador j = new Jogador();
		
		j.setPosicao(Posicao.ATACANTE);
		j.setNumero(numero);
		j.setNome(NomeUtil.getNome());
		List<Integer> valorHabilidadesEspecificas = new ArrayList<Integer>();
		
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.PASSE);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.FINALIZACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.CRUZAMENTO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.ARMACAO);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.CABECEIO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.MARCACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.DESARME);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.INTERCEPTACAO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.VELOCIDADE);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.DIBLE);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.FORCA);
		addHabilidade(j, gerarValorHabilidadeEspecifico(overhall, valorHabilidadesEspecificas), Habilidade.POSICIONAMENTO);
		addHabilidade(j, gerarValorHabilidadeComum(overhall), Habilidade.DOMINIO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.REFLEXO);
		addHabilidade(j, gerarValorHabilidadeOutros(overhall), Habilidade.JOGO_AEREO);
		
		j.setForcaGeral((new Double(valorHabilidadesEspecificas.stream().mapToInt(v -> v).average().getAsDouble())).intValue());
		
		return j;
	}
	
}
