package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValor;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.service.util.JogadorCalcularForcaUtil;
import com.fastfoot.player.service.util.NomeUtil;

public class JogadorFactory {
	
	private static final Random R = new Random();
	
	private static final Double STDEV = 3.5; 
	
	private static final Double PESO_HABILIDADE_COMUM = 0.8;
	
	private static final Double PESO_HABILIDADE_ESPECIFICO = 1.0;
	
	private static final Double PESO_HABILIDADE_OUTROS = 0.4;
	
	private static final Integer VALOR_HABILIDADE_MAX = 100;
	
	private static final Integer VALOR_HABILIDADE_MIN = 10;
	
	public static Jogador gerarJogador(Clube clube, Posicao posicao, Integer numero) {
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

	public static Jogador gerarGoleiro(Clube clube, Integer numero) {
		Jogador j = gerarGoleiro(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarGoleiro(Integer overhall, Integer numero) {
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
	
	public static Jogador gerarZagueiro(Clube clube, Integer numero) {
		Jogador j = gerarZagueiro(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarZagueiro(Integer overhall, Integer numero) {
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
	
	public static Jogador gerarLateral(Clube clube, Integer numero) {
		Jogador j = gerarLateral(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarLateral(Integer overhall, Integer numero) {
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
	
	public static Jogador gerarVolante(Clube clube, Integer numero) {
		Jogador j = gerarVolante(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarVolante(Integer overhall, Integer numero) {
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
	
	public static Jogador gerarMeiaLateral(Clube clube, Integer numero) {
		Jogador j = gerarMeiaLateral(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarMeiaLateral(Integer overhall, Integer numero) {
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
	
	public static Jogador gerarMeia(Clube clube, Integer numero) {
		Jogador j = gerarMeia(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarMeia(Integer overhall, Integer numero) {
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
	
	public static Jogador gerarAtacante(Clube clube, Integer numero) {
		Jogador j = gerarAtacante(clube.getForcaGeral(), numero);
		j.setClube(clube);
		return j;
	}

	public static Jogador gerarAtacante(Integer overhall, Integer numero) {
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

	private static Integer gerarValorHabilidadeEspecifico(Integer media, List<Integer> valorHabilidadesEspecificas) {
		Integer valor = gerarValorHabilidade(media, PESO_HABILIDADE_ESPECIFICO);
		valorHabilidadesEspecificas.add(valor);
		return valor;
	}
	
	private static Integer gerarValorHabilidadeComum(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_COMUM);
	}
	
	private static Integer gerarValorHabilidadeOutros(Integer media) {
		return gerarValorHabilidade(media, PESO_HABILIDADE_OUTROS);
	}

	private static Integer gerarValorHabilidade(Integer media, Double peso) {
		return Long.valueOf(Math.max(Math.min(Math.round(peso * (R.nextGaussian() * STDEV + media)), VALOR_HABILIDADE_MAX), VALOR_HABILIDADE_MIN)).intValue();
	}

	private static void addHabilidade(Jogador jogador, Integer valor, Habilidade habilidade){
		HabilidadeValor hv = new HabilidadeValor(habilidade, valor, jogador, valor);
		jogador.addHabilidade(hv);
	}
}
